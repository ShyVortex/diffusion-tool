from diffusers import BitsandBytesConfig, SD3TransformerPipeline
from diffusers import StableDiffusion3Pipeline
import torch
import GPUtil

def use_full_model(prompt, tags, date):
    pipe = StableDiffusion3Pipeline.from_pretrained("stabilityai/stable-diffusion-3.5-large-turbo", torch_dtype=torch.bfloat16)
    pipe = pipe.to("cuda")

    image = pipe(
        prompt=prompt,
        negative_prompt=tags,
        num_inference_steps=4,
        guidance_scale=0.0,
    ).images[0]
    output_folder = os.path.abspath("result/generated/general/sd3-5")
    output_filename = f"generated_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)

    # Check if the output folder exists, and create it if not, then save the image
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    image.save(output_filepath)

    # Encode the image as a base64 string
    with open(output_filepath, "rb") as image_file:
        encoded_image = base64.b64encode(image_file.read()).decode('utf-8')

    return encoded_image

def use_quantized_model(prompt, tags, date):
    model_id = "stabilityai/stable-diffusion-3.5-large-turbo"

    nf4_config = BitsAndBytesConfig(
        load_in_4bit=True,
        bnb_4bit_quant_type="nf4",
        bnb_4bit_compute_dtype=torch.bfloat16
    )
    model_nf4 = SD3Transformer2DModel.from_pretrained(
        model_id,
        subfolder="transformer",
        quantization_config=nf4_config,
        torch_dtype=torch.bfloat16
    )

    t5_nf4 = T5EncoderModel.from_pretrained("diffusers/t5-nf4", torch_dtype=torch.bfloat16)

    pipeline = StableDiffusion3Pipeline.from_pretrained(
        model_id,
        transformer=model_nf4,
        text_encoder_3=t5_nf4,
        torch_dtype=torch.bfloat16
    )
    pipeline.enable_model_cpu_offload()

    image = pipeline(
        prompt=prompt,
        negative_prompt=tags,
        num_inference_steps=4,
        guidance_scale=0.0,
        max_sequence_length=512,
    ).images[0]

    output_folder = os.path.abspath("result/generated/general/sd3-5")
    output_filename = f"generated_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)

    # Check if the output folder exists, and create it if not, then save the image
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    image.save(output_filepath)

    # Encode the image as a base64 string
    with open(output_filepath, "rb") as image_file:
        encoded_image = base64.b64encode(image_file.read()).decode('utf-8')

    return encoded_image

def get_gpu_memory():
    gpus = GPUtil.getGPUs()
    all_memory = {gpu.id: gpu.memoryFree for gpu in gpus}

    # Get the GPU with the most free memory
    gpu_memory = max(all_memory, key=all_memory.get)

    return gpu_memory

def main():
    # Check if the correct number of command-line arguments is provided
    if len(sys.argv) != 4:
        print("Usage: python generate_sd3-5.py <prompt> <tags> <date>")
        sys.exit(1)

    # Get the prompt and date from the command-line arguments passed from Java
    prompt = sys.argv[1]
    tags = sys.argv[2]
    date = sys.argv[3]

    # Get available video memory and decide which model to use
    vram = get_gpu_memory()
    if vram >= 8192:
        image = use_full_model(
            prompt=prompt,
            tags=tags,
            date=date
        )
    else:
        image = use_quantized_model(
            prompt=prompt,
            tags=tags,
            date=date
        )

    # Print image as string
    print(image)

if __name__ == "__main__":
    main()
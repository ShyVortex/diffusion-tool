import sys
from diffusers import DiffusionPipeline, LCMScheduler
from PIL import Image
from io import BytesIO
import peft
import torch
import os
import base64


def main():
    # Check if the correct number of command-line arguments is provided
    if len(sys.argv) != 4:
        print("Usage: python generate.py <prompt> <tags> <date>")
        sys.exit(1)

    # Get the prompt and date from the command-line arguments passed from Java
    prompt = sys.argv[1]
    tags = sys.argv[2]
    date = sys.argv[3]

    # Model initialization and processing
    model_id = "stabilityai/stable-diffusion-xl-base-1.0"
    lcm_lora_id = "latent-consistency/lcm-lora-sdxl"
    pipe = DiffusionPipeline.from_pretrained(model_id, variant="fp16")
    pipe.scheduler = LCMScheduler.from_config(pipe.scheduler.config)

    # Initialize weights and PEFT
    pipe.load_lora_weights(lcm_lora_id, adapter_name="lora")
    pipe.load_lora_weights("tensors/pixel-art-xl.safetensors", adapter_name="pixel")

    # Load by setting adapters and send to GPU
    pipe.set_adapters(["lora", "pixel"], adapter_weights=[1.0, 1.2])
    pipe.to(device="cuda", dtype=torch.float16)
    torch.cuda.set_per_process_memory_fraction(2.0)

    # Process the prompt and set the output path
    with torch.cuda.amp.autocast():
        image = pipe(
            prompt=prompt,
            negative_prompt=tags,
            num_inference_steps=8,
            guidance_scale=1.5,
        ).images[0]
    output_folder = os.path.abspath("result/generated/pixelart")
    output_filename = f"generated_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)

    # Check if the output folder exists, and create it if not, then save the image
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    image.save(output_filepath)

    # Encode the image as a base64 string
    with open(output_filepath, "rb") as image_file:
        encoded_image = base64.b64encode(image_file.read()).decode('utf-8')

    # Print image as string
    print(encoded_image)


if __name__ == "__main__":
    main()

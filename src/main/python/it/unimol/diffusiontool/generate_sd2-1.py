import sys
from diffusers import DiffusionPipeline, DPMSolverMultistepScheduler
from PIL import Image
from io import BytesIO
import torch
import os
import base64


def main():
    # Check if the correct number of command-line arguments is provided
    if len(sys.argv) != 4:
        print("Usage: python generate_sd2-1.py <prompt> <tags> <date>", file=sys.stderr)
        sys.exit(1)

    # Get the prompt and date from the command-line arguments passed from Java
    prompt = sys.argv[1]
    tags = sys.argv[2]
    date = sys.argv[3]

    # Model initialization and processing
    repo_id = "stabilityai/stable-diffusion-2-1"
    pipe = DiffusionPipeline.from_pretrained(repo_id, torch_dtype=torch.float16, variant="fp16")
    pipe.scheduler = DPMSolverMultistepScheduler.from_config(pipe.scheduler.config)

    # offload components to CPU during inference to save memory
    pipe.enable_model_cpu_offload()

    # Process the prompt and set the output path
    with torch.cuda.amp.autocast():
        image = pipe(prompt=prompt, negative_prompt=tags, num_inference_steps=25).images[0]
    output_folder = os.path.abspath("result/generated/general/sd2-1")
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

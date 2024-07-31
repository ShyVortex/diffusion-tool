import sys
from diffusers import DiffusionPipeline, LCMScheduler
from PIL import Image
from io import BytesIO
import torch
import os
import base64


def main():
    # Check if the correct number of command-line arguments is provided
    if len(sys.argv) != 3:
        print("Usage: python generate_sd2-1.py <prompt> <tags>")
        sys.exit(1)

    # Get the prompt and date from the command-line arguments passed from Java
    prompt = sys.argv[1]
    tags = sys.argv[2]

    # Model initialization and processing
    repo_id = "stabilityai/stable-diffusion-2-1"
    pipe = DiffusionPipeline.from_pretrained(repo_id, torch_dtype=torch.float16, variant="fp16")
    pipe.scheduler = DPMSolverMultistepScheduler.from_config(pipe.scheduler.config)
    pipe = pipe.to("cuda")

    # Process the prompt
    with torch.cuda.amp.autocast():
        image = pipe(prompt=prompt, negative_prompt=tags, num_inference_steps=25).images[0]

    # Encode the image as a base64 string
    with open(image, "rb"):
        encoded_image = base64.b64encode(image).decode('utf-8')

    # Print image as string
    print(encoded_image)


if __name__ == "__main__":
    main()

import sys
from diffusers import StableDiffusion3Pipeline
from PIL import Image
from io import BytesIO
import torch
import os
import base64


def main():
    # Check if the correct number of command-line arguments is provided
    if len(sys.argv) != 3:
        print("Usage: python generate_sd3.py <prompt> <tags>")
        sys.exit(1)

    # Get the prompt and date from the command-line arguments passed from Java
    prompt = sys.argv[1]
    tags = sys.argv[2]

    # Model initialization and processing
    repo_id = "stabilityai/stable-diffusion-3-medium-diffusers"
    pipe = StableDiffusion3Pipeline.from_pretrained(
        repo_id,

        # removes memory-intensive text encoder to decrease memory requirements
        text_encoder_3=None,
        tokenizer_3=None,

        torch_dtype=torch.float16,
    )

    # offload components to CPU during inference to save memory
    pipe.enable_model_cpu_offload()

    # Process the prompt and set the output path
    with torch.cuda.amp.autocast():
        image = pipe(
            prompt=prompt,
            negative_prompt=tags,
            num_inference_steps=25,
            guidance_scale=6.5
        ).images[0]

    # Encode the image as a base64 string
    with open(image, "rb"):
        encoded_image = base64.b64encode(image).decode('utf-8')

    # Print image as string
    print(encoded_image)


if __name__ == "__main__":
    main()

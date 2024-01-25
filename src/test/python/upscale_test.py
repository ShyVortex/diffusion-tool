import sys
import requests
from PIL import Image
from io import BytesIO
from diffusers import DiffusionPipeline, DPMSolverMultistepScheduler, StableDiffusionLatentUpscalePipeline
import torch
import os
import base64


def main():
    if len(sys.argv) != 4:
        print("Usage: python generate_upscale.py <prompt> <tags> <date>")
        sys.exit(1)

    prompt = sys.argv[1]
    tags = sys.argv[2]
    date = sys.argv[3]

    # Load general model
    repo_id = "stabilityai/stable-diffusion-2-1-base"  # you can use 2-1 if you have more VRAM
    pipe = DiffusionPipeline.from_pretrained(repo_id, torch_dtype=torch.float16, variant="fp16")
    pipe.scheduler = DPMSolverMultistepScheduler.from_config(pipe.scheduler.config)
    pipe = pipe.to("cuda")

    # Load upscaling model
    model_id = "stabilityai/sd-x2-latent-upscaler"
    upscaler = StableDiffusionLatentUpscalePipeline.from_pretrained(model_id, torch_dtype=torch.float16)
    upscaler.to("cuda")

    # Encode image to its latents
    generator = torch.manual_seed(33)
    with torch.cuda.amp.autocast():
        low_res_latents = pipe(
            prompt=prompt,
            negative_prompt=tags,
            num_inference_steps=25,
            generator=generator,
            output_type="latent"
        ).images

    # Process upscaling
    upscaled_image = upscaler(
        prompt=prompt,
        image=low_res_latents,
        num_inference_steps=20,
        guidance_scale=0,
        generator=torch.manual_seed(33),
    ).images[0]
    torch.cuda.empty_cache()

    # Encode image to string
    with open(upscaled_image, "rb") as image:
        encoded_image = base64.b64encode(image).decode('utf-8')
    print(encoded_image)


if __name__ == "__main__":
    main()

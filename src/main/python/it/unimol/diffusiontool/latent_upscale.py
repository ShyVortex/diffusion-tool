import sys
import requests
from PIL import Image
from io import BytesIO
from diffusers import StableDiffusionLatentUpscalePipeline, StableDiffusionPipeline
import torch
import os
import base64


def main():
    if len(sys.argv) != 3:
        print("Usage: python latent_upscale.py <base64EncodedImage> <date>")
        sys.exit(1)

    encoded_input_image = sys.argv[1]
    date = sys.argv[2]
    low_res_image = decode_base64_image(encoded_input_image)

    # Load general model
    pipeline = StableDiffusionPipeline.from_pretrained("CompVis/stable-diffusion-v1-4", torch_dtype=torch.float16)
    pipeline.to("cuda")

    # Load upscaling model
    model_id = "stabilityai/sd-x2-latent-upscaler"
    upscaler = StableDiffusionLatentUpscalePipeline.from_pretrained(model_id, torch_dtype=torch.float16)
    upscaler.to("cuda")

    generator = torch.manual_seed(33)

    # Encode image to its latents
    low_res_latents = pipeline(prompt="", image=low_res_image, output_type="latent").images

    # Process upscaling and save the image
    upscaled_image = upscaler(
        prompt="",
        image=low_res_latents,
        num_inference_steps=20,
        guidance_scale=0,
        generator=torch.manual_seed(33),
    ).images[0]
    output_folder = os.path.abspath("result/upscaled")
    output_filename = f"upscaled_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)

    # Check if the output folder exists, and create it if not, then save the image
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    image.save(output_filepath)
    upscaled_image.save(output_filepath)
    torch.cuda.empty_cache()

    with open(output_filepath, "rb") as image_file:
        encoded_image = base64.b64encode(image_file.read()).decode('utf-8')
    print(encoded_image)


def decode_base64_image(base64_string):
    # Extract the base64 data
    img_data = base64.b64decode(base64_string)

    # Create a BytesIO object to treat the data as a file-like object
    img_buffer = BytesIO(img_data)

    # Open the image using PIL
    image = Image.open(img_buffer)

    return image


if __name__ == "__main__":
    main()

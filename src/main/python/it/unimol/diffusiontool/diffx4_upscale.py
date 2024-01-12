import sys
import requests
from PIL import Image
from io import BytesIO
from diffusers import StableDiffusionUpscalePipeline
import torch
import os
import base64


def main():
    if len(sys.argv) != 3:
        print("Usage: python diffx4_upscale.py <base64EncodedImage> <date>")
        sys.exit(1)

    encoded_input_image = sys.argv[1]
    date = sys.argv[2]
    low_res_image = decode_base64_image(encoded_input_image)

    # Load model and scheduler
    model_id = "stabilityai/stable-diffusion-x4-upscaler"
    pipe = StableDiffusionUpscalePipeline.from_pretrained(
        model_id, variant="fp16", torch_dtype=torch.float16
    )
    pipe = pipe.to("cuda")

    # Process upscaling and save the image
    prompt = ""  # @param {type: 'string'}, use if you want better results
    pipe.enable_attention_slicing()  # computes in more steps, saves memory
    with torch.cuda.amp.autocast():
        upscaled_image = pipe(prompt=prompt, image=low_res_image, num_inference_steps=25).images[0]
    output_folder = os.path.abspath("result/upscaled")
    output_filename = f"upscaled_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)
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

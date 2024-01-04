import sys
from diffusers import DiffusionPipeline, DPMSolverMultistepScheduler
import torch
import os


def main():
    # Check if the correct number of command-line arguments is provided
    if len(sys.argv) != 3:
        print("Usage: python generate.py <prompt> <date>")
        sys.exit(1)

    # Get the prompt and date from the command-line arguments passed from Java
    prompt = sys.argv[1]
    date = sys.argv[2]

    # Model initialization and processing
    repo_id = "stabilityai/stable-diffusion-2-base"
    pipe = DiffusionPipeline.from_pretrained(repo_id, torch_dtype=torch.float16, variant="fp16")
    pipe.scheduler = DPMSolverMultistepScheduler.from_config(pipe.scheduler.config)
    pipe = pipe.to("cuda")

    # Process the prompt and save the image
    with torch.cuda.amp.autocast():
        image = pipe(prompt, num_inference_steps=25).images[0]
    output_folder = os.path.abspath("result/generated")
    output_filename = f"generated_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)
    image.save(output_filepath)

    # Print name of the saved image
    print(output_filename)


if __name__ == "__main__":
    main()

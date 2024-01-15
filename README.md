# diffusion-tool
Diffusion Tool is an AI image generator and upscaler created for my third-year Artificial Intelligence university exam, using Java and Python.

# Project Description
At its core, it's a JavaFX application that integrates the Python interpreter and uses it to implement Stable Diffusion pipelines for generative AI plus upscaling 
and BSRGAN's degradation model for the upscaling of any image.  
I initially thought about using the Spring framework to manage user registration, but I wanted everyone to be able to use the program offline, so I opted 
for a local approach instead and the user data is now saved on the working directory.  
It is structured as follow: from the user side, we have the Login and Sign Up pages. Once an user has logged in, they have access to the Home, Profile, Generate
and Upscale pages.  
The last two are the essential part of the project and they act as GUI for the Python scripts.

# Prerequisites
In order to compile and run the software, it is required that you have the following prerequisites:
- Open Java Development Kit (OpenJDK) 17 or above
- Apache Maven (at least version 3.6.3 is recommended)
 
You also MUST install a Python virtual environment in the working directory, inside a folder named 'venv', with the following packages:
 ```shell
 pip install diffusers pillow torch torchvision base64 opencv-python numpy transformers accelerate 
 ```

# System requirements
I will only include consumer-level hardware.  
AI-computing capable hardware that has a GPU with enough VRAM should be capable of running this software.  
**ATTENTION**: currently, AMD GPUs are not supported as the application relies on CUDA, a technology exclusive to NVIDIA.
|                 Minimum                     |                         Recommended                           |
| ------------------------------------------- | ------------------------------------------------------------- |
| `OS` Linux x64, X11 display server          | Linux x64, X11 display server                                 |
| `CPU` Intel Core i5-7500 / AMD Ryzen 5 1600 | Intel Core i7-9700k / AMD Ryzen 5 3600x                       |
| `RAM` 16 GBs                                | 16 GBs                                                        |
| `GPU` NVIDIA GeForce GTX 1660 SUPER         | NVIDIA GeForce RTX 3060                                       |

# Credits
As stated before, this project uses BSRGAN's degradation model for upscaling purposes.  
BSRGAN is a practical degradation model for Deep Blind Image Super-Resolution, developed by [Kai Zhang](https://cszn.github.io/), Jingyun Liang, 
[Luc Van Gool](https://vision.ee.ethz.ch/people-details.OTAyMzM=.TGlzdC8zMjQ4LC0xOTcxNDY1MTc4.html), [Radu Timofte](http://people.ee.ethz.ch/~timofter/),
[Computer Vision Lab](https://vision.ee.ethz.ch/the-institute.html), in ETH Zurich, Switzerland.  
You can check out their repository and find out more here: [BSRGAN](https://github.com/cszn/BSRGAN).    
The project utilizes Stable Diffusion's generative AI pipelines for image generation and upscaling. In particular, it uses:  
+ [stable-diffusion-2-1](https://huggingface.co/stabilityai/stable-diffusion-2-1)
+ [stable-diffusion-2-1-base](https://huggingface.co/stabilityai/stable-diffusion-2-1-base)
+ [sd-x2-latent-upscaler](https://huggingface.co/stabilityai/sd-x2-latent-upscaler)

# License
- This project is distributed under the [GNU General Public License v3.0](https://github.com/ShyVortex/diffusion-tool/blob/master/LICENSE.md).
- Copyright of [@ShyVortex](https://github.com/ShyVortex), 2024.

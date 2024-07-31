<p align="center">
  <img width="180" src="src/main/resources/tool/logo-512.png" alt="diffusion-tool"></img>
  <h1 align="center">diffusion-tool</h1>
  <p align="center">Image generator and upscaler created for my AI university exam
</p>

## Description
At its core, it's a JavaFX application that integrates the Python interpreter and uses it to implement Stable Diffusion pipelines for generative AI plus upscaling 
and BSRGAN's degradation model for the upscaling of any image.  
I initially thought about using the Spring framework to manage user registration, but I wanted everyone to be able to use the program offline, so I opted 
for a local approach instead and the user data is now saved on the working directory.  
It is structured as follow: from the user side, we have the Login and Sign Up pages. Once an user has logged in, they have access to the Home, Profile, Generate
and Upscale pages.  
The last two are the essential part of the project and they act as GUI for the Python scripts.

## Prerequisites
In order to compile and run the software, it is required that you have the following prerequisites:
- Open Java Development Kit (OpenJDK) 17 or above
- Apache Maven (at least version 3.6.3 is recommended)
 
You also MUST install a Python virtual environment in your home directory, inside a folder named 'venv',
with the packages listed in *requirements*.
 ```shell
 cd ~/venv/bin
 source activate
 pip install -r requirements.txt
 ```

## System requirements
I will only include consumer-level hardware.  
AI-computing capable hardware that has a GPU with enough VRAM should be capable of running this software.  
**ATTENTION**: currently, AMD GPUs are not supported as the application relies on CUDA, a technology exclusive to NVIDIA.
|                 Minimum                     |                         Recommended                           |
| ------------------------------------------- | ------------------------------------------------------------- |
| `OS` Linux x64, X11 display server          | Linux x64, X11 display server                                 |
| `CPU` Intel Core i5-7500 / AMD Ryzen 5 1600 | Intel Core i7-9700k / AMD Ryzen 5 3600x                       |
| `RAM` 16 GBs                                | 16 GBs                                                        |
| `GPU` NVIDIA GeForce GTX 1660 SUPER         | NVIDIA GeForce RTX 3060                                       |

## Building
Executable packages can be downloaded from [Releases](https://github.com/ShyVortex/diffusion-tool/releases) or manually built instead.  
You can do that assuming the above prerequisites have already been installed.  
Once you're in the project directory, type the following in a terminal to download the dependencies and compile all the classes:
 ```shell
 mvn clean install
 ```
Then, if you also want a runnable .jar archive, type:
 ```shell
 mvn package
 ```
With these commands, a new folder named 'target' is created containing the compiled project as well as the executable file.

## Unlock Stable Diffusion 3
The newest generative model is currently gated, so first you need to sign up [here](https://huggingface.co/stabilityai/stable-diffusion-3-medium-diffusers).  
Proceed to generate a [token](https://huggingface.co/settings/tokens) under your account settings which you will use to login with:
 ```shell
 huggingface-cli login
 ```
Enter your credentials first, then the token when it's needed.

## Screenshots
### Home
![home-view](https://github.com/user-attachments/assets/50052e5a-c8a4-4eaa-b39f-ae537c81fb9f)
### Image Generation
![generate-view](https://github.com/user-attachments/assets/dc8239d9-faa7-4a88-bb09-7d808763220c)
### Image Upscaling
![upscale-view](https://github.com/user-attachments/assets/db703513-dc09-4344-96c8-1a6c0ce5d246)

## Upscaling Comparison
### Low-res vs. Upscaled
![UpscalingComparison](https://github.com/ShyVortex/diffusion-tool/assets/111277410/0e380dda-36f4-4187-8ff2-9cf287dca06d)
![UpscalingComparison2](https://github.com/ShyVortex/diffusion-tool/assets/111277410/05f0d876-1b9b-4b50-8dba-c558abf815fe)

## Credits
As stated before, this project uses BSRGAN's degradation model for upscaling purposes.  
BSRGAN is a practical degradation model for Deep Blind Image Super-Resolution, developed by [Kai Zhang](https://cszn.github.io/), Jingyun Liang, 
[Luc Van Gool](https://vision.ee.ethz.ch/people-details.OTAyMzM=.TGlzdC8zMjQ4LC0xOTcxNDY1MTc4.html), [Radu Timofte](http://people.ee.ethz.ch/~timofter/),
[Computer Vision Lab](https://vision.ee.ethz.ch/the-institute.html), in ETH Zurich, Switzerland.  
You can check out their repository and find out more here: [BSRGAN](https://github.com/cszn/BSRGAN).    
In order to set up the model, a script made by [TGS963](https://github.com/TGS963) in the public repository of [upscayl](https://github.com/upscayl/upscayl) was particularly helpful.  
I've edited said script to adapt it and make it work on my project, keeping acknowledgments in comments just below library imports.  
The project utilizes Stable Diffusion's generative AI pipelines for image generation and upscaling, in particular:  
+ [stable-diffusion-2-1](https://huggingface.co/stabilityai/stable-diffusion-2-1)
+ [stable-diffusion-2-1-base](https://huggingface.co/stabilityai/stable-diffusion-2-1-base)
+ [stable-diffusion-3-medium](https://huggingface.co/stabilityai/stable-diffusion-3-medium-diffusers)
+ [sd-x2-latent-upscaler](https://huggingface.co/stabilityai/sd-x2-latent-upscaler)
+ [pixel-art-style](https://huggingface.co/kohbanye/pixel-art-style)
+ [pixel-art-xl](https://huggingface.co/nerijs/pixel-art-xl)

## License
- This project is distributed under the [GNU General Public License v3.0](https://github.com/ShyVortex/diffusion-tool/blob/master/LICENSE.md).
- Copyright of [@ShyVortex](https://github.com/ShyVortex), 2024.

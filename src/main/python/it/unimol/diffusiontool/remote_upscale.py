import sys
import requests
import json
from PIL import Image
from io import BytesIO
from re import findall
from time import sleep
import os
import base64


def main():
    if len(sys.argv) != 3:
        print("Usage: python remote_upscale.py <base64EncodedImage> <date>")
        sys.exit(1)

    encoded_input_image = sys.argv[1]
    date = sys.argv[2]

    # Send image to BigJPG AI and upscale
    img_url = upload(encoded_input_image)
    new_file = enlarge(img_url)

    # Set correct output path
    output_folder = os.path.abspath("result/upscaled")
    output_filename = f"upscaled_image_{date}.png"
    output_filepath = os.path.join(output_folder, output_filename)

    # Open upscaled image, save it to the correct location and delete the temporary file
    upscaled_image = Image.open(new_file)
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)
    image.save(output_filepath)
    upscaled_image.save(output_filepath)
    os.remove(new_file)

    # Encode the image back to string
    with open(output_filepath, "rb") as image_file:
        encoded_image = base64.b64encode(image_file.read()).decode('utf-8')
    print(encoded_image)


def upload(file: str):
    api_key = '6d207e02198a847aa98d0a2a901485a5'  # freeimagehost

    payload = {
        'key': api_key,
        'action': 'upload',
        'source': file,
        'format': 'json',
    }

    r = requests.post(
        url='https://freeimage.host/api/1/upload',
        data=payload
    )

    if r.status_code == 200:
        return r.json()['image']['url']


def enlarge(url: str):
    config = {
        'style': 'art',  # `art`(illustration) or `photo`
        'noise': '1',  # -1: none, 0: low, 1: medium, 2: high, 3: highest
        'x2': '2',  # 1: 2x, 2: 4x
        'input': url
    }
    api_key = 'YOUR_BIGJPG_API_KEY'

    # upload image
    r = requests.post(
        url='https://bigjpg.com/api/task/',
        headers={'X-API-KEY': api_key},
        data={'conf': json.dumps(config)}
    )

    print(r.json())
    tid = r.json()['tid']

    # download enlarged image file
    while True:
        img_r = requests.get(
            url='https://bigjpg.com/api/task/%s' % tid,
            # headers={'X-API-KEY': api_key}
        )
        print(img_r.json())
        status = img_r.json()[tid]['status']
        if status == 'error':
            return None
        elif status == 'new':
            sleep(10)
            pass
        elif status == 'success':
            img_url = img_r.json()[tid]['url']
            img = requests.get(url=img_url).content
            filename = findall(r'.*/(.*?)$', url)[0]
            with open(filename, 'wb') as f:
                f.write(img)

            return filename


if __name__ == "__main__":
    main()

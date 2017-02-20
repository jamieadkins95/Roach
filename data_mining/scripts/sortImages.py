#!/usr/bin/python3

import os
import sys

from PIL import Image

MEDIUM = 1093, 1462
LOW = 547, 731

def printUsage():
    print("Please supply the directory of the images as an argument when running this script.")
    print("e.g.")
    print("./sortImages.py ../images")

if len(sys.argv) > 1:
    inputDirectory = sys.argv[1]
else:
    printUsage()
    exit()

# Add a backslash on the end if it doesn't exist.
if inputDirectory[-1] != "/":
    inputDirectory = inputDirectory + "/"

if not os.path.isdir(inputDirectory):
    print(inputDirectory + " is not a valid directory")
    printUsage()
    exit()

output = "../images"

if not os.path.exists(output):
    os.makedirs(output)
    
# Sort all of the cards images into folders named by their card id and variation id.
for filename in os.listdir(inputDirectory):
    if filename.endswith(".png"):
        cardId = filename.split("_")[0]
        cardDirectory = os.path.join(output, cardId)
        if not os.path.exists(cardDirectory):
            os.makedirs(cardDirectory)
            
        variationId = cardId + filename.split(".png")[0][-1] + "0"
        variationDirectory = os.path.join(cardDirectory, variationId)
        if not os.path.exists(variationDirectory):
            os.makedirs(variationDirectory)

        # Have to call it fullsize to support legacy versions.
        highPath = os.path.join(variationDirectory, "fullsize.png")

        print(os.path.join(inputDirectory, filename), "->", highPath)
        os.rename(os.path.join(inputDirectory, filename), highPath)

        # Make lower resolution images.
        try:
            medium = Image.open(highPath)
            medium.thumbnail(MEDIUM, Image.ANTIALIAS)
            medium.save(os.path.join(variationDirectory, "medium.png"), "png")

            low = Image.open(highPath)
            low.thumbnail(LOW, Image.ANTIALIAS)
            low.save(os.path.join(variationDirectory, "low.png"), "png")
        except IOError:
            print("cannot create thumbnail for '%s'" % highPath)
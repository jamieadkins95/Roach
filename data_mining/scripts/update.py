#!/usr/bin/python3
import xml.etree.ElementTree as xml
import sys
import os
import json
import re
import time

from unidecode import unidecode

# Script is used to merge json together. Change for loop as necessary for merging.

def saveJson(filename, cardList):
    filepath = os.path.join("../outputs/" + filename)
    print("Saving %s cards to: %s" % (len(cardList), filepath))
    with open(filepath, "w", encoding="utf-8", newline="\n") as f:
        json.dump(cardList, f, sort_keys=True, indent=2, separators=(',', ': '))

with open(sys.argv[1]) as old_file:    
    original = json.load(old_file)
    
with open(sys.argv[2]) as new_file:    
    new = json.load(new_file)
    
merged = {}
for cardId in original:
    # Change this based on what you need to merge.
    merged[cardId] = original[cardId]
    merged[cardId]['variations'] = new[cardId]['variations']

saveJson("merged.json", merged)





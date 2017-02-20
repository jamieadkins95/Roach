#!/usr/bin/python3
import xml.etree.ElementTree as xml
import sys
import os
import json
import re

from unidecode import unidecode

def saveJson(filename, cardList):
    filepath = os.path.join("../outputs/" + filename)
    print("Saving %s cards to: %s" % (len(cardList), filepath))
    with open(filepath, "w", encoding="utf-8", newline="\n") as f:
        json.dump(cardList, f, sort_keys=True, indent=2, separators=(',', ': '))

def cleanHtml(raw_html):
    cleanr = re.compile('<.*?>')
    cleantext = re.sub(cleanr, '', raw_html)
    return cleantext

def getRawTooltips():
    TOOLTIP_STRINGS_PATH = xml_folder + "tooltip_strings.txt"
    if not os.path.isfile(TOOLTIP_STRINGS_PATH):
        print("Couldn't find tooltip_strings.txt at " + TOOLTIP_STRINGS_PATH)
        exit()

    rawTooltips = {}

    tooltipsFile = open(TOOLTIP_STRINGS_PATH, "r")
    for tooltip in tooltipsFile:
        split = tooltip.split(";")
        tooltipId = split[1].replace("tooltip_","").replace("_description","").replace("\"", "").lstrip("0")

        # Remove any quotation marks, new lines and html tags.
        rawTooltips[tooltipId] = cleanHtml(split[2].replace("\"", "").replace("\n", ""))

    return rawTooltips

def getAbilityData():

    CARD_ABILITIES_PATH = xml_folder + "abilities.xml"
    if not os.path.isfile(CARD_ABILITIES_PATH):
        print("Couldn't find abilities.xml at " + CARD_ABILITIES_PATH)
        exit()

    abilities = {}

    tree = xml.parse(CARD_ABILITIES_PATH)
    root = tree.getroot()

    for ability in root.iter('Ability'):
        abilities[ability.attrib['id']] = ability

    return abilities

def getAbilityValue(abilityId, paramName):
    ability = ABILITIES[abilityId]
    if ability.find(paramName) != None:
        return ability.find(paramName).attrib['V']

def getTooltipData():
    TOOLTIPS_PATH = xml_folder + "tooltips.xml"
    if not os.path.isfile(TOOLTIPS_PATH):
        print("Couldn't find tooltips.xml at " + TOOLTIPS_PATH)
        exit()

    tooltipData = {}

    tree = xml.parse(TOOLTIPS_PATH)
    root = tree.getroot()

    for tooltip in root.iter('CardTooltip'):
        tooltipData[tooltip.attrib['id']] = tooltip

    return tooltipData

def evaluateInfoData(cards):
    # Now that we have the raw strings, we have to get any values that are missing.
    for cardId in cards:
        # Some cards don't have info.
        if cards[cardId]['info'] == None or cards[cardId]['info'] == "":
            continue
            
        # Set info to be the raw tooltip string.
        tooltipId = cards[cardId]['info']
        cards[cardId]['info'] = TOOLTIPS.get(tooltipId)

        result = re.findall(r'.*?\{(.*?)\}.*?', cards[cardId]['info']) # Regex. Get all strings that lie between a '{' and '}'.

        tooltip = TOOLTIP_DATA.get(tooltipId)
        for key in result:
            for variable in tooltip.iter('VariableData'):
                data = variable.find(key)
                if data == None:
                    # This is not the right variable for this key, let's check the next one.
                    continue
                if "crd" in key:
                    # Spawn a specific card.
                    crd = data.attrib['V']
                    if crd != "":
                        cards[cardId]['info'] = cards[cardId]['info'].replace("{" + key + "}", cards[crd]['name'])
                        # We've dealt with this key, move on.
                        continue
                if variable.attrib['key'] == key:
                    # The value is sometimes given immediately here.
                    if data.attrib['V'] != "":
                        cards[cardId]['info'] = cards[cardId]['info'].replace("{" + key + "}", data.attrib['V'])
                    else: # Otherwise we are going to have to look in the ability data to find the value.
                        abilityId = variable.find(key).attrib['abilityId']
                        paramName = variable.find(key).attrib['paramName']
                        cards[cardId]['info'] = cards[cardId]['info'].replace("{" + key + "}", getAbilityValue(abilityId, paramName))

def getCardNames():
    CARD_NAME_PATH = xml_folder + "card_names.txt"
    if not os.path.isfile(CARD_NAME_PATH):
        print("Couldn't find card_names.txt at " + CARD_NAME_PATH)
        exit()

    cardNames = {}

    nameFile = open(CARD_NAME_PATH, "r")
    for line in nameFile:
        split = line.split(";")
        if "_name" in split[1]:
            nameId = split[1].replace("_name", "").replace("\"", "")
            # Remove any quotation marks and new lines.
            cardNames[nameId] = split[2].replace("\"", "").replace("\n", "")

    nameFile.close()
    return cardNames

def getFlavorStrings():
    CARD_NAME_PATH = xml_folder + "card_names.txt"
    if not os.path.isfile(CARD_NAME_PATH):
        print("Couldn't find card_names.txt at " + CARD_NAME_PATH)
        exit()

    flavorStrings = {}

    nameFile = open(CARD_NAME_PATH, "r")
    for line in nameFile:
        split = line.split(";")
        if "_fluff" in split[1]:
            flavorId = split[1].replace("_fluff", "").replace("\"", "")
            # Remove any quotation marks and new lines.
            flavorStrings[flavorId] = split[2].replace("\"", "").replace("\n", "")

    nameFile.close()
    return flavorStrings

def getCardTemplates():
    TEMPLATES_PATH = xml_folder + "templates.xml"
    if not os.path.isfile(TEMPLATES_PATH):
        print("Couldn't find templates.xml at " + TEMPLATES_PATH)
        exit()

    cardTemplates = {}

    tree = xml.parse(TEMPLATES_PATH)
    root = tree.getroot()

    for template in root.iter('CardTemplate'):
        cardTemplates[template.attrib['id']] = template

    return cardTemplates

def createCardJson():
    cards = {}

    for templateId in TEMPLATES:
        template = TEMPLATES[templateId]
        card = {}
        card['ingameId'] = template.attrib['id']
        card['strength'] = int(template.attrib['power'])
        card['type'] = template.attrib['group']
        card['faction'] = template.attrib['factionId'].replace("NorthernKingdom", "Northern Realms")

        key = template.attrib['dbgStr'].lower().replace(" ", "_").replace("'", "")
        # Remove any underscores from the end.
        if key[-1] == "_":
            key = key[:-1]

        card['name'] = CARD_NAMES.get(key)
        card['flavor'] = FLAVOR_STRINGS.get(key)
        
        # False by default, will be set to true if collectible or is a token of a released card.
        card['released'] = False

        card['tooltips'] = {}
        if (template.find('Tooltip') != None):
            # Set to tooltipId for now, we will evaluate after we have looked at every card.
            card['tooltips']['en-US'] = template.find('Tooltip').attrib['key']

        card['positions'] = []
        card['loyalty'] = []
        for flag in template.iter('flag'):
            key = flag.attrib['name']

            if key == "Loyal" or key == "Disloyal":
                card['loyalty'].append(key)

            if key == "Melee" or key == "Ranged" or key == "Siege" or key == "Event":
                card['positions'].append(key)

        card['categories'] = []
        for flag in template.iter('Category'):
            key = flag.attrib['id']
            if key in CATEGORIES:
                card['categories'].append(key.replace("_", " "))

        card['variations'] = {}

        for definition in template.find('CardDefinitions').findall('CardDefinition'):
            variation = {}
            variationId = definition.attrib['id']

            variation['variationId'] = variationId
            variation['availability'] = definition.find('Availability').attrib['V']
            collectible = variation['availability'] == "BaseSet"
            variation['collectible'] = collectible
            
            # If a card is collectible, we know it has been released.
            if collectible:
                card['released'] = True

            variation['rarity'] = definition.find('Rarity').attrib['V']

            variation['craft'] = CRAFT_VALUES[variation['rarity']]
            variation['mill'] = MILL_VALUES[variation['rarity']]

            art = {}
            art['highImage'] = "images/" + card['ingameId'] + "/" + variation['variationId'] + "/" + "fullsize.png"
            art['mediumImage'] = "images/" + card['ingameId'] + "/" + variation['variationId'] + "/" + "medium.png"
            art['lowImage'] = "images/" + card['ingameId'] + "/" + variation['variationId'] + "/" + "low.png"
            art['artist'] = definition.find("UnityLinks").find("StandardArt").attrib['author']
            variation['art'] = art

            card['variations'][variationId] = variation

        cards[card['ingameId']] = card

    return cards

# If a card is a token of a released card, it has also been released.
def evaluateTokens(cards):
    for cardId in cards:
        card = cards[cardId]
        if card['released']:
            card['related'] = []
            for ability in TEMPLATES[cardId].iter('Ability'):
                ability = ABILITIES[ability.attrib['id']]

                # There are several different ways that a template can be referenced.
                for template in ability.iter('templateId'):
                    tokenId = ability.find('templateId').attrib['V']
                    if cards.get(tokenId) != None:
                        cards.get(tokenId)['released'] = True
                        card['related'].append(tokenId)

                for template in ability.iter('TemplatesFromId'):
                    for token in template.iter('id'):
                        tokenId = token.attrib['V']
                        if cards.get(tokenId) != None:
                            cards.get(tokenId)['released'] = True
                            card['related'].append(tokenId)

                for template in ability.iter('TransformTemplate'):
                    tokenId = template.attrib['V']
                    if cards.get(tokenId) != None:
                        cards.get(tokenId)['released'] = True
                        card['related'].append(tokenId)

xml_folder = sys.argv[1]

# Add a backslash on the end if it doesn't exist.
if xml_folder[-1] != "/":
    xml_folder = xml_folder + "/"

if not os.path.isdir(xml_folder):
    print(xml_folder + " is not a valid directory")
    exit()

CRAFT_VALUES = {}
CRAFT_VALUES['Common'] = {"standard": 30, "premium": 200}
CRAFT_VALUES['Rare'] = {"standard": 80, "premium": 400}
CRAFT_VALUES['Epic'] = {"standard": 200, "premium": 800}
CRAFT_VALUES['Legendary'] = {"standard": 800, "premium": 1600}

MILL_VALUES = {}
MILL_VALUES['Common'] = {"standard": 5, "premium": 25}
MILL_VALUES['Rare'] = {"standard": 10, "premium": 50}
MILL_VALUES['Epic'] = {"standard": 50, "premium": 200}
MILL_VALUES['Legendary'] = {"standard": 200, "premium": 800}

CATEGORIES = ["Vampire", "Mage", "Elf", "Potion", "Weather", "Special", "Dyrad", "Breedable",
              "Shapeshifter", "Blue_Stripes", "Wild_Hunt", "Permadeath", "Ambush", "Fleeting", 
              "Vodyanoi", "Witcher", "Relentless", "Dwarf", "Dragon"]

TEMPLATES = getCardTemplates()
ABILITIES = getAbilityData()
TOOLTIPS = getRawTooltips()
TOOLTIP_DATA = getTooltipData()
CARD_NAMES = getCardNames()
FLAVOR_STRINGS = getFlavorStrings()

cardData = createCardJson()

# Requires information about other cards, so needs to be done after we have looked at every card.
evaluateInfoData(cardData)
# We have to do this as well to catch cards like Botchling, that are explicitly named in the Baron's tooltip.
evaluateTokens(cardData)

saveJson("latest.json", cardData)





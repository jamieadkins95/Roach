#!/bin/bash

usage()
{
cat << EOF
usage: $0 options

This script removes the green background from card images.

OPTIONS:
    --inputs          Path to folder containing card images to manipulate.
EOF
}

# Parse user input.
while [[ $# -gt 1 ]]
do
key="$1"

case $key in
    --inputs)
    INPUT_DIRECTORY="$2"
    shift # past argument
    ;;
    *)
    # unknown option
    ;;
esac
shift # past argument or value
done

if [[ ! -d $INPUT_DIRECTORY ]]
        then
        echo "ERROR: A directory ($1) doesn't exist."
        usage
        exit 1
    fi

for f in $(find "$INPUT_DIRECTORY" -name '*.png')
do
  echo "Processing $f file..."
  convert $f -transparent Lime $f
done
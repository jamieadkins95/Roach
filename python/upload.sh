#!/bin/bash

patch=${1?Please include patch name as a parameter.}
destination="https://gwent-9e62a.firebaseio.com/card-data/$1.json"
echo $destination
curl -X PUT -d @latest.json $destination
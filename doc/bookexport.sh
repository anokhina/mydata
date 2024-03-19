#!/bin/bash
topath="/media/sevn/SP PHD U31/DATA/book-store/db"
mongocmd=./mongodb_4_4_14/bin/mongo

mongocol=tagEntity
$mongocmd mongodb://localhost:27017/mydata --quiet --eval "db.$mongocol.find().forEach(printjson);" > "$topath/$mongocol.txt"

mongocol=bookEntity
$mongocmd mongodb://localhost:27017/mydata --quiet --eval "db.$mongocol.find().forEach(printjson);" > "$topath/$mongocol.txt"



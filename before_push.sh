#!/bin/bash

# Hacky - if your .gitignore is not working
# then consider running this before you push
# so we don't waste time deleting things we don't need

# Remove gradle directory and its contents
rm ./.gradle/

# Remove build directory 
rm -rf ./build/

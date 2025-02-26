#!/bin/bash

if grep -q error results.xml; then
    echo "Checkstyle found an error - look at the logs under Print Results for details"
    exit 1
else
    echo "No errors found!"
fi

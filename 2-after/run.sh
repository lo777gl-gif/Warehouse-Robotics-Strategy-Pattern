#!/usr/bin/env bash
# Run the demo without Maven.
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/main/java/*.java
java -cp out Main

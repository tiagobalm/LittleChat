 #!/bin/bash

find -name "*.java" > sources.txt
mkdir out
javac -d ./out -cp ".:./controlsfx-8.40.12.jar:" @sources.txt
cp -a ./src/gui/assets ./out/gui/
cp -a ./src/keys ./out/
rm sources.txt

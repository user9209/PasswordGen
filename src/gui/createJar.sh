#!/bin/bash
javac PasswordGen.java
jar -cvfm ../releases/PasswordGen.jar ./MANIFEST.MF ./PasswordGen*.class licence.txt
rm PasswordGen*.class
exit 0

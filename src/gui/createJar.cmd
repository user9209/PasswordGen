@echo off
mkdir ..\releases
javac PasswordGen.java
jar -cvfm ../releases/PasswordGen.jar ./MANIFEST.MF ./PasswordGen*.class licence.txt
del PasswordGen*.class
pause
exit 0

@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -d out src\main\java\*.java || exit /b 1
java -cp out Main

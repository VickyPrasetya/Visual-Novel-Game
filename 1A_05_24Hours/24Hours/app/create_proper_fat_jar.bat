@echo off
echo Creating proper fat JAR with all dependencies...

REM Buat temporary directory
mkdir temp_jar

REM Copy compiled classes
xcopy /E /I bin temp_jar

REM Extract Gson library ke temp_jar
cd temp_jar
jar xf ..\lib\gson-2.13.1.jar
cd ..

REM Buat manifest
echo Main-Class: game.Main > manifest.txt

REM Create fat JAR
jar cvfm VisualNovelGame.jar manifest.txt -C temp_jar .

REM Clean up
rmdir /S /Q temp_jar
del manifest.txt

echo Proper fat JAR created: VisualNovelGame.jar
echo Note: This JAR still needs JavaFX modules to run
pause

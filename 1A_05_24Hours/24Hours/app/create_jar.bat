@echo off
echo Creating JAR file with manifest...

REM Buat manifest file
echo Main-Class: game.Main > manifest.txt

REM Buat JAR dengan manifest (tanpa JavaFX)
jar cvfm VisualNovelGame.jar manifest.txt -C bin .

REM Hapus manifest temporary
del manifest.txt

echo JAR file created: VisualNovelGame.jar
echo Note: This JAR needs JavaFX modules to run properly
pause
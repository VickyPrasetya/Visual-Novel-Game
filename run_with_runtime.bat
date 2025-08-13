@echo off
echo Running Visual Novel Game with custom runtime...

REM Jalankan JAR dengan custom runtime dan Gson library
custom-runtime\bin\java -cp "VisualNovelGame.jar;custom-runtime\lib\gson-2.13.1.jar" game.Main

pause

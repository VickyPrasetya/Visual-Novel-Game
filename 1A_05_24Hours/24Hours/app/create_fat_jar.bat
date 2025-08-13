@echo off
echo Creating fat JAR with JavaFX...

REM Set JavaFX path (sesuaikan dengan path di komputer kamu)
set JAVAFX_LIB=C:\DevTools\javafx-sdk-24.0.1\lib

REM Buat temporary directory
mkdir temp_jar

REM Copy compiled classes
xcopy /E /I bin temp_jar

REM Extract Gson library ke temp_jar
cd temp_jar
jar xf ..\lib\gson-2.13.1.jar
cd ..

REM Extract JavaFX modules ke temp_jar
cd temp_jar
jar xf "%JAVAFX_LIB%\javafx.base.jar"
jar xf "%JAVAFX_LIB%\javafx.controls.jar"
jar xf "%JAVAFX_LIB%\javafx.fxml.jar"
jar xf "%JAVAFX_LIB%\javafx.graphics.jar"
jar xf "%JAVAFX_LIB%\javafx.media.jar"
jar xf "%JAVAFX_LIB%\javafx.swing.jar"
cd ..

REM Buat manifest
echo Main-Class: game.Main > manifest.txt

REM Create fat JAR
jar cvfm VisualNovelGame.jar manifest.txt -C temp_jar .

REM Clean up
rmdir /S /Q temp_jar
del manifest.txt

echo Fat JAR with JavaFX created: VisualNovelGame.jar
pause

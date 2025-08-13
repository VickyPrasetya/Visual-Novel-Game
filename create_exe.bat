@echo off
echo Creating executable with JPackage...

REM Set paths
set JAVA_HOME=C:\Program Files\Java\jdk-24
set JAVAFX_LIB=C:\DevTools\javafx-sdk-24.0.1\lib

REM Create executable with JPackage (using app-image)
jpackage --input . --name "24Hours" --main-jar VisualNovelGame.jar --main-class game.Main --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing --type app-image

echo App image created in folder: 24Hours
pause

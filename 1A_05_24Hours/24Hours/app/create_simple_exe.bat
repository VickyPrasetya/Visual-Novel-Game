@echo off
echo Creating simple executable...

REM Create batch file that runs the JAR
echo @echo off > 24Hours.bat
echo java --module-path "C:\DevTools\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -jar VisualNovelGame.jar >> 24Hours.bat
echo pause >> 24Hours.bat

echo Simple executable created: 24Hours.bat
pause

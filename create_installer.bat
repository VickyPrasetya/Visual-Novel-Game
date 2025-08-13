@echo off
echo Creating 24Hours Game Installer...

REM Create game folder
mkdir "24Hours Game"

REM Copy JAR file
copy "VisualNovelGame.jar" "24Hours Game\"

REM Copy assets folder
xcopy /E /I "assets" "24Hours Game\assets\"

REM Create launcher batch file
echo @echo off > "24Hours Game\24Hours.bat"
echo title 24Hours Visual Novel Game >> "24Hours Game\24Hours.bat"
echo java --module-path "C:\DevTools\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -jar VisualNovelGame.jar >> "24Hours Game\24Hours.bat"
echo pause >> "24Hours Game\24Hours.bat"

REM Create README
echo 24Hours Visual Novel Game > "24Hours Game\README.txt"
echo. >> "24Hours Game\README.txt"
echo Cara menjalankan: >> "24Hours Game\README.txt"
echo 1. Double-click 24Hours.bat >> "24Hours Game\README.txt"
echo 2. Pastikan Java 24 terinstall >> "24Hours Game\README.txt"
echo 3. Pastikan JavaFX SDK ada di C:\DevTools\javafx-sdk-24.0.1\ >> "24Hours Game\README.txt"

echo Game installer created in folder: "24Hours Game"
pause

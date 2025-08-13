@echo off
echo Compiling Visual Novel Game...
REM Pastikan path JavaFX dan Gson sudah benar
set JAVAFX_LIB=C:\Program Files\Java\javafx-sdk-24.0.1\lib
set GSON_LIB=lib\gson-2.13.1.jar

REM Compile semua file .java di src/ dan subfolder
javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing ^
  -cp "%GSON_LIB%" ^
  -d bin ^
  src/game/*.java src/game/manager/*.java src/game/model/*.java src/game/system/*.java src/game/ui/*.java src/game/util/*.java

echo Done! 
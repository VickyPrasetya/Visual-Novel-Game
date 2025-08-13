@echo off
echo Running Visual Novel Game...

REM Set JavaFX path (sesuaikan dengan path di komputer kamu)
set JAVAFX_LIB=C:\DevTools\javafx-sdk-24.0.1\lib

REM Jalankan JAR dengan JavaFX modules
java --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -jar VisualNovelGame.jar

pause

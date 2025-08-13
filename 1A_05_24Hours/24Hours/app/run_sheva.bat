@echo off
echo Running Visual Novel Game...
java --module-path "C:\Libraries\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.swing,javafx.media -cp "bin;lib/*" game.Main
pause 
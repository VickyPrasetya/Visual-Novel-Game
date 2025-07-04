@echo off
echo Compiling Visual Novel Game...
javac --module-path "C:\DevTools\javafx-sdk-24.0.1\lib" --add-modules javafx.controls,javafx.fxml -cp "lib/*" -d bin src\game\Main.java src\game\model\choiceData.java src\game\model\sceneData.java
if %errorlevel% equ 0 (
    echo Compilation successful!
) else (
    echo Compilation failed!
)
pause 
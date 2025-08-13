@echo off
echo Creating custom Java runtime with JavaFX and Gson...

REM Set paths (sesuaikan dengan path di komputer kamu)
set JAVA_HOME=C:\Program Files\Java\jdk-24
set JAVAFX_LIB=C:\DevTools\javafx-sdk-24.0.1\lib
set GSON_LIB=lib\gson-2.13.1.jar

REM Buat custom runtime dengan JavaFX
jlink --module-path "%JAVA_HOME%\jmods;%JAVAFX_LIB%" ^
      --add-modules java.base,java.desktop,java.logging,java.xml,javafx.controls,javafx.fxml,javafx.media,javafx.swing ^
      --output custom-runtime

REM Copy Gson library ke custom runtime
copy "%GSON_LIB%" custom-runtime\lib\

echo Custom runtime created in folder: custom-runtime
echo Gson library copied to custom-runtime\lib\
pause

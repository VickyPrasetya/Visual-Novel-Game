@echo off
cls
echo ==========================================================
echo           VISUAL NOVEL GAME - BUILD SCRIPT
echo ==========================================================
echo.

REM ==========================================================
REM      PENTING: PASTIKAN PATH INI BENAR 100%
REM ==========================================================
set "JAVAFX_LIB=C:\Libraries\javafx-sdk-24.0.1\lib"
REM ==========================================================

set "GSON_LIB=lib\gson-2.13.1.jar"

echo Path JavaFX di-set ke: "%JAVAFX_LIB%"
if not exist "%JAVAFX_LIB%" (
    echo.
    echo !!!!!!! ERROR: PATH JAVAFX TIDAK DITEMUKAN !!!!!!!
    echo Periksa kembali path di dalam script ini.
    echo Path yang dicari: %JAVAFX_LIB%
    pause
    exit /b 1
)
echo.

REM ----------------------------------------------------------
echo [LANGKAH 1] Membersihkan build lama...
if exist bin (
    echo Menghapus folder 'bin' lama...
    rmdir /s /q bin
)
echo Membuat folder 'bin' yang baru...
mkdir bin
echo.

REM ----------------------------------------------------------
echo [LANGKAH 2] Meng-compile file Java...
javac --module-path "%JAVAFX_LIB%" --add-modules javafx.controls,javafx.media,javafx.swing ^
  -cp "%GSON_LIB%" ^
  -d bin ^
  src/game/*.java src/game/manager/*.java src/game/model/*.java src/game/system/*.java src/game/ui/*.java src/game/util/*.java

if %errorlevel% neq 0 (
    echo.
    echo !!!!!!! ERROR: Kompilasi GAGAL. !!!!!!!
    pause
    exit /b 1
)
echo Kompilasi berhasil.
echo.

REM ----------------------------------------------------------
echo [LANGKAH 3] Menyalin file resource (CSS, Assets, dll.)...
echo Membuat file pengecualian sementara...
echo *.java > exclude_list.txt

echo Menyalin semua file dari 'src' ke 'bin' (kecuali file .java)...
XCOPY src bin /S /E /I /Y /EXCLUDE:exclude_list.txt

del exclude_list.txt
echo.

REM ----------------------------------------------------------
echo [LANGKAH 4] Verifikasi hasil salinan...
echo Memeriksa apakah style.css ada di 'bin\game\ui\'...
if exist "bin\game\ui\style.css" (
    echo   -> SUKSES! File style.css ditemukan di dalam bin.
) else (
    echo   -> GAGAL! File style.css TIDAK ADA di dalam bin.
)
echo.

echo ==========================================================
echo Build selesai. Anda sekarang bisa menjalankan run_abram.bat
echo ==========================================================
echo.
pause
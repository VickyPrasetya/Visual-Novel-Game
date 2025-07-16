@echo off
:: =================================================================
:: FILE COMPILE - Versi FINAL & LENGKAP
:: =================================================================
:: Script ini secara manual menunjuk ke SEMUA folder source code
:: Anda untuk memastikan tidak ada yang terlewat.
:: =================================================================

:: BAGIAN KONFIGURASI (Gunakan tanda kutip jika path ada spasi)
set JAVAFX_LIB="C:\Program Files\Java\javafx-sdk-24.0.1\lib"


:: =================================================================
:: BAGIAN UTAMA - JANGAN DIUBAH
:: =================================================================
echo -----------------------------------------
echo Memulai proses compile (Mode Lengkap)...
echo Menggunakan JavaFX dari: %JAVAFX_LIB%
echo -----------------------------------------

:: Menjalankan perintah javac standar.
:: PERBAIKAN: Menambahkan src\game\ui\*.java dan src\game\util\*.java
javac --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp "..\lib\*" -d ..\bin src\game\*.java src\game\manager\*.java src\game\model\*.java src\game\system\*.java src\game\ui\*.java src\game\util\*.java


:: Cek apakah proses compile berhasil atau gagal
if %errorlevel% neq 0 (
    echo.
    echo **********************************************************
    echo *           TERJADI ERROR SAAT COMPILE!                  *
    echo *  Jika errornya 'package com.google.gson does not exist',*
    echo *  pastikan file 'gson.jar' ada di dalam folder 'lib'.    *
    echo **********************************************************
) else (
    echo.
    echo -----------------------------------------
    echo      PROSES COMPILE BERHASIL!
    echo -----------------------------------------
)


pause
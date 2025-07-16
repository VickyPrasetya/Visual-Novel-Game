@echo off
:: =================================================================
:: FILE UNTUK COMPILE PROYEK VISUAL NOVEL (Versi PowerShell)
:: =================================================================
:: Script ini secara otomatis akan mencari semua file .java di dalam
:: folder proyek dan melakukan compile.
:: =================================================================

:: BAGIAN YANG PERLU ANDA UBAH SESUAI KOMPUTER MASING-MASING
:: Arahkan ini ke folder 'lib' di dalam folder JavaFX SDK Anda.
set JAVAFX_LIB="C:\Program Files\Java\javafx-sdk-24.0.1\lib"


:: =================================================================
:: BAGIAN UTAMA - JANGAN DIUBAH
:: =================================================================
echo -----------------------------------------
echo Memulai proses compile dengan PowerShell...
echo Menggunakan JavaFX dari: %JAVAFX_LIB%
echo Mencari semua file .java secara otomatis...
echo -----------------------------------------

:: Menjalankan perintah javac melalui PowerShell agar bisa menggunakan
:: (Get-ChildItem) untuk mencari semua file .java secara rekursif.
powershell -Command "javac --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml,javafx.media -cp '..\lib\*' -d ..\bin (Get-ChildItem -Recurse -Filter *.java).FullName"


:: Cek apakah proses compile berhasil atau gagal
if %errorlevel% neq 0 (
    echo.
    echo ***********************************
    echo *  TERJADI ERROR SAAT COMPILE!    *
    echo *  Silakan periksa pesan di atas. *
    echo ***********************************
) else (
    echo.
    echo -----------------------------------------
    echo Proses compile berhasil. File .class telah dibuat di folder 'bin'.
    echo -----------------------------------------
)


:: 'pause' akan menahan window terminal agar tidak langsung tertutup.
pause
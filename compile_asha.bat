@echo off
:: =================================================================
:: FILE COMPILE - Versi Stabil (Kembali ke Dasar)
:: =================================================================
:: Script ini tidak menggunakan PowerShell yang rumit.
:: Langsung menjalankan perintah javac standar yang lebih andal.
:: =================================================================

:: BAGIAN YANG PERLU ANDA UBAH SESUAI KOMPUTER MASING-MASING
:: (Gunakan tanda kutip jika path mengandung spasi)
set JAVAFX_LIB="C:\Program Files\Java\javafx-sdk-24.0.1\lib"


:: =================================================================
:: BAGIAN UTAMA - JANGAN DIUBAH
:: =================================================================
echo -----------------------------------------
echo Memulai proses compile (Mode Stabil)...
echo Menggunakan JavaFX dari: %JAVAFX_LIB%
echo -----------------------------------------

:: Menjalankan perintah javac standar.
:: Perintah ini secara eksplisit menunjuk ke semua paket kode Anda.
javac --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp "..\lib\*" -d ..\bin src\game\*.java src\game\model\*.java src\game\manager\*.java src\game\system\*.java


:: Cek apakah proses compile berhasil atau gagal
if %errorlevel% neq 0 (
    echo.
    echo ***********************************
    echo *  TERJADI ERROR SAAT COMPILE!    *
    echo *  Pesan error ada di atas.       *
    echo *  Pastikan Anda sudah me-rename file .java dengan benar. *
    echo ***********************************
) else (
    echo.
    echo -----------------------------------------
    echo Proses compile berhasil.
    echo -----------------------------------------
)


pause
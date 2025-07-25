@echo off
:: =================================================================
:: FILE COMPILE - Versi FINAL dengan Path yang Benar
:: =================================================================
:: Script ini menggunakan path yang benar untuk classpath dan output.
:: =================================================================

:: BAGIAN KONFIGURASI (Gunakan tanda kutip jika path ada spasi)
set JAVAFX_LIB="C:\Program Files\Java\javafx-sdk-24.0.1\lib"


:: =================================================================
:: BAGIAN UTAMA - JANGAN DIUBAH
:: =================================================================
echo -----------------------------------------
echo Memulai proses compile (Mode Path Benar)...
echo Menggunakan JavaFX dari: %JAVAFX_LIB%
echo -----------------------------------------

:: Menjalankan perintah javac standar.
:: PERBAIKAN FINAL: Menghilangkan '..' yang salah dari classpath (-cp) dan direktori output (-d)
javac --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.swing -cp "lib\gson-2.13.1.jar" -d "bin" src\game\*.java src\game\manager\*.java src\game\model\*.java src\game\system\*.java src\game\ui\*.java src\game\util\*.java


:: Cek apakah proses compile berhasil atau gagal
if %errorlevel% neq 0 (
    echo.
    echo **********************************************************
    echo *           TERJADI ERROR SAAT COMPILE!                  *
    echo *           Silakan periksa pesan error di atas.           *
    echo **********************************************************
) else (
    echo.
    echo -----------------------------------------
    echo      PROSES COMPILE BERHASIL!
    echo -----------------------------------------
    echo Anda sekarang bisa menjalankan run_asha.bat
)


pause
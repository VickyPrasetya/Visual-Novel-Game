@echo off
:: =================================================================
:: FILE UNTUK RUN (MENJALANKAN) PROYEK VISUAL NOVEL
:: =================================================================
:: Pastikan Anda sudah menjalankan compile.bat sebelum menjalankan file ini.
:: =================================================================

:: BAGIAN YANG PERLU ANDA UBAH SESUAI KOMPUTER MASING-MASING
:: Arahkan ini ke folder 'lib' di dalam folder JavaFX SDK Anda.
:: (Pastikan path ini SAMA dengan yang ada di file compile_NamaAnda.bat)
set JAVAFX_LIB="C:\Program Files\Java\javafx-sdk-24.0.1\lib"


:: =================================================================
:: BAGIAN UTAMA - JANGAN DIUBAH
:: =================================================================
echo -----------------------------------------
echo Menjalankan aplikasi...
echo Menggunakan JavaFX dari: %JAVAFX_LIB%
echo Classpath: folder 'lib' dan 'bin'
echo Main Class: game.Main
echo -----------------------------------------
echo.

:: Menjalankan aplikasi JavaFX.
:: Perintah ini memberitahu Java di mana menemukan JavaFX, modul apa yang
:: akan digunakan, di mana library (gson) dan kode yang sudah dicompile (bin) berada,
:: dan class mana yang harus dijalankan pertama kali.
java --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml,javafx.media -cp "lib\*;bin" game.Main


echo.
echo -----------------------------------------
echo Aplikasi ditutup.
echo -----------------------------------------

:: 'pause' akan menahan window terminal agar tidak langsung tertutup,
:: sehingga Anda bisa melihat pesan error jika aplikasi gagal dijalankan.
pause
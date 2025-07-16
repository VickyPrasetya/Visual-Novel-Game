package game.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Kelas FileUtil menyediakan utilitas operasi file sederhana seperti copy dan cek exist.
 */
public class FileUtil {
    /**
     * Menyalin file dari path sumber ke path tujuan.
     * @param from Path sumber.
     * @param to Path tujuan.
     * @return true jika berhasil, false jika gagal.
     */
    public static boolean copyFile(String from, String to) {
        try {
            Files.copy(new File(from).toPath(), new File(to).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Mengecek apakah file dengan path tertentu ada.
     * @param path Path file.
     * @return true jika file ada, false jika tidak.
     */
    public static boolean exists(String path) {
        return new File(path).exists();
    }
} 
// File: game/manager/SaveLoadService.java
package game.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.model.GameState;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SaveLoadService {

    private static final String SAVE_DIRECTORY = "saves/";
    private final Gson gson;

    public SaveLoadService() {
        // GsonBuilder untuk format JSON yang lebih rapi (pretty printing)
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        // Buat folder 'saves' jika belum ada
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Gagal membuat direktori saves: " + e.getMessage());
        }
    }

    private String getFilePath(int slot) {
        return SAVE_DIRECTORY + "save_slot_" + slot + ".json";
    }

    /**
     * Menyimpan GameState ke dalam file di slot yang ditentukan.
     */
    public boolean saveGame(int slot, GameState state) {
        String filePath = getFilePath(slot);
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(state, writer);
            System.out.println("Game berhasil disimpan di slot " + slot);
            return true;
        } catch (IOException e) {
            System.err.println("Gagal menyimpan game di slot " + slot + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Memuat GameState dari file di slot yang ditentukan.
     * @return GameState jika berhasil, null jika file tidak ada atau gagal.
     */
    public GameState loadGame(int slot) {
        String filePath = getFilePath(slot);
        if (!new File(filePath).exists()) {
            return null; // File save tidak ada
        }
        
        try (Reader reader = new FileReader(filePath)) {
            GameState state = gson.fromJson(reader, GameState.class);
            System.out.println("Game berhasil dimuat dari slot " + slot);
            return state;
        } catch (Exception e) {
            System.err.println("Gagal memuat game dari slot " + slot + ": " + e.getMessage());
            return null;
        }
    }
}
package ui.visualnovel.game.manager;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.Gson; // Import library untuk membaca JSON
import ui.visualnovel.game.Model.sceneData; // Import kelas SceneData kita

/**
 * Kelas ini mengelola logika inti dari game,
 * seperti memuat cerita dan melacak adegan saat ini.
 */
public class gameManager {
    private Map<String, sceneData> storyData; // Menyimpan semua adegan dari JSON
    private sceneData currentScene; // Menyimpan adegan yang sedang aktif

    public gameManager() {
        loadStory();
        if (storyData != null && !storyData.isEmpty()) {
            // Mulai cerita dari adegan pertama ("prolog_1")
            this.currentScene = storyData.get("prolog_1"); 
        }
    }

    private void loadStory() {
        Gson gson = new Gson();
        // Coba baca file JSON dari folder 'assets'
        try (InputStream is = getClass().getResourceAsStream("/assets/scene1.json")) {
            if (is == null) {
                System.err.println("Error: Tidak bisa menemukan file /assets/scene1.json");
                return;
            }
            Reader reader = new InputStreamReader(is);

            // Parsing JSON menjadi array objek SceneData
            sceneData[] scenes = gson.fromJson(reader, sceneData[].class);
            
            // Ubah array menjadi Map untuk pencarian adegan berdasarkan ID yang lebih cepat
            this.storyData = Arrays.stream(scenes)
                    .collect(Collectors.toMap(scene -> scene.id, Function.identity()));
            
            System.out.println("Cerita berhasil dimuat. Total adegan: " + storyData.size());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Gagal memuat atau mem-parsing file cerita JSON.");
        }
    }

    /**
     * Mengambil data adegan yang sedang aktif saat ini.
     * @return Objek SceneData dari adegan saat ini.
     */
    public sceneData getCurrentScene() {
        return this.currentScene;
    }

    /**
     * Memindahkan game ke adegan berikutnya berdasarkan ID.
     * @param sceneId ID dari adegan yang dituju.
     */
    public void goToScene(String sceneId) {
        if (sceneId != null && storyData.containsKey(sceneId)) {
            this.currentScene = storyData.get(sceneId);
        } else {
            // Jika sceneId null atau tidak ditemukan, berarti cerita tamat
            this.currentScene = null; 
        }
    }
}
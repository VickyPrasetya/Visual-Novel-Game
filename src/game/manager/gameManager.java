package game.manager;

import java.util.Map;

import com.google.gson.Gson; // Import library untuk membaca JSON
import game.model.sceneData; // Import kelas SceneData kita

/**
 * Kelas ini mengelola logika inti dari game, seperti memuat cerita dan melacak
 * adegan saat ini.
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

    // GANTI METODE loadStory() YANG LAMA DENGAN YANG INI:
    private void loadStory() {
        Gson gson = new Gson();
        String filePath = "assets/scene1.json"; // Path relatif dari root project
        System.out.println("--- [METODE BARU] Mencoba membaca file langsung dari: " + filePath);

        try (java.io.Reader reader = new java.io.FileReader(filePath)) {

            System.out.println("--- [METODE BARU] BERHASIL! File ditemukan di " + filePath);

            sceneData[] scenes = gson.fromJson(reader, sceneData[].class);
            this.storyData = java.util.Arrays.stream(scenes)
                    .collect(java.util.stream.Collectors.toMap(scene -> scene.id, java.util.function.Function.identity()));

            System.out.println("--- Cerita berhasil di-parsing. Total adegan: " + storyData.size());

        } catch (java.io.FileNotFoundException e) {
            System.err.println("--- [METODE BARU] FATAL ERROR: File tidak ditemukan di path '" + filePath + "'");
            System.err.println("--- Pastikan kamu menjalankan game dari folder root proyek (VISUAL-NOVEL-GAME).");
            System.err.println("--- Cek lagi apakah nama folder 'assets' dan file 'scene1.json' sudah benar dan tidak ada salah ketik.");
            this.storyData = null; // Pastikan storyData null jika gagal
        } catch (Exception e) {
            System.err.println("--- [METODE BARU] GAGAL mem-parsing file JSON.");
            e.printStackTrace();
            this.storyData = null; // Pastikan storyData null jika gagal
        }
    }

    /**
     * Mengambil data adegan yang sedang aktif saat ini.
     *
     * @return Objek SceneData dari adegan saat ini.
     */
    public sceneData getCurrentScene() {
        return this.currentScene;
    }

    /**
     * Memindahkan game ke adegan berikutnya berdasarkan ID.
     *
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

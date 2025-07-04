package game.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import game.model.SceneData;
import java.io.FileReader;
import game.model.ChoiceData;
import game.model.DialogNode;
import java.util.*;

public class gameManager {

    private SceneData rootScene; // Akar tree
    private SceneData currentScene;
    private int currentDialogIndex;
    private Stack<Integer> dialogStack = new Stack<>(); // Untuk undo dialog
    private Map<String, SceneData> sceneIndex = new HashMap<>(); // Untuk lookup cepat

    public gameManager() {
        loadStory();
        this.currentScene = rootScene;
        this.currentDialogIndex = 0;
        dialogStack.clear();
    }

    public SceneData getCurrentScene() {
        return currentScene;
    }

    // GANTI METODE loadStory() YANG LAMA DENGAN YANG INI:
    private void loadStory() {
        Gson gson = new Gson();
        String filePath = "assets/scene.json"; // Path relatif dari root project
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
    // Saat undoDialog:
    public boolean undoDialog() {
        if (!dialogStack.isEmpty() && dialogStack.peek() >= minUndoIndex) {
            currentDialogIndex = dialogStack.pop();
            return true;
        }
        return false; // Tidak bisa undo lagi
    }

    // Mendapatkan pilihan di scene saat ini
    public List<ChoiceData> getChoices() {
        return currentScene.choices;
    }

    public void goToScene(String sceneId) {
        SceneData next = sceneIndex.get(sceneId);
        if (next != null) {
            currentScene = next;
            currentDialogIndex = 0;
            dialogStack.clear();
            minUndoIndex = 0; // reset batas undo di scene baru
        } else {
            // Jika scene tidak ditemukan, anggap tamat
            currentScene = null;
        }
    }

}

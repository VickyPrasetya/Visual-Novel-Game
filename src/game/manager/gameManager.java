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
    private int minUndoIndex = 0;

    public gameManager() {
        loadStory();
        this.currentScene = rootScene;
        this.currentDialogIndex = 0;
        dialogStack.clear();
    }

    // GANTI METODE loadStory() YANG LAMA DENGAN YANG INI:
    private void loadStory() {
        Gson gson = new Gson();
        String filePath = "assets/scene.json"; // Path relatif dari root project
        System.out.println("--- [METODE BARU] Mencoba membaca file langsung dari: " + filePath);

        try (java.io.Reader reader = new java.io.FileReader(filePath)) {

            System.out.println("--- [METODE BARU] BERHASIL! File ditemukan di " + filePath);

            SceneData[] scenes = gson.fromJson(reader, SceneData[].class);
            if (scenes != null) {
                for (SceneData s : scenes) {
                    sceneIndex.put(s.id, s);
                }
                // Set rootScene sesuai id root di JSON
                rootScene = sceneIndex.get("prolog_scene_1");
            }
        } catch (java.io.FileNotFoundException e) {
            System.err.println("--- [METODE BARU] FATAL ERROR: File tidak ditemukan di path '" + filePath + "'");
            System.err.println("--- Pastikan kamu menjalankan game dari folder root proyek (VISUAL-NOVEL-GAME).");
            System.err.println("--- Cek lagi apakah nama folder 'assets' dan file 'scene.json' sudah benar dan tidak ada salah ketik.");

        } catch (Exception e) {
            System.err.println("--- [METODE BARU] GAGAL mem-parsing file JSON.");
            e.printStackTrace();

        }

        System.out.println("Scene loaded: " + sceneIndex.keySet());
        if (rootScene == null) {
            System.out.println("Root scene not found!");
        } else if (rootScene.dialogs == null || rootScene.dialogs.isEmpty()) {
            System.out.println("Root scene has no dialogs!");
        }

    }

    /**
     * Mengambil data adegan yang sedang aktif saat ini.
     *
     * @return Objek SceneData dari adegan saat ini.
     */
    public SceneData getCurrentScene() {
        return this.currentScene;
    }

    public DialogNode getCurrentDialog() {
        if (currentScene == null || currentScene.dialogs == null || currentDialogIndex >= currentScene.dialogs.size()) {
            return null;
        }
        return currentScene.dialogs.get(currentDialogIndex);
    }

    /**
     * Memindahkan game ke adegan berikutnya berdasarkan ID.
     *
     * @param sceneId ID dari adegan yang dituju.
     */
    // Saat undoDialog:
    public boolean undoDialog() {
        // Tidak bisa undo jika sudah di dialog pertama (index 0)
        if (currentDialogIndex <= 0) {
            return false;
        }
        
        // Tidak bisa undo jika stack kosong atau sudah melewati batas minimum
        if (dialogStack.isEmpty() || dialogStack.peek() < minUndoIndex) {
            return false;
        }
        
        currentDialogIndex = dialogStack.pop();
        return true;
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

    public boolean nextDialog() {
        if (currentScene == null || currentScene.dialogs == null) {
            return false;
        }
        if (currentDialogIndex < currentScene.dialogs.size() - 1) {
            dialogStack.push(currentDialogIndex); // Simpan index dialog saat ini untuk undo
            currentDialogIndex++;
            return true;
        }
        // Jika sudah di dialog terakhir, jangan push ke stack
        return false; // Sudah di dialog terakhir
    }

    public boolean canUndoDialog() {
        // Bisa undo jika:
        // 1. Tidak di dialog pertama (index > 0)
        // 2. Stack tidak kosong
        // 3. Index di stack >= batas minimum
        // 4. Scene memiliki lebih dari 1 dialog (untuk scene dengan 1 dialog saja)
        return currentDialogIndex > 0 && 
               !dialogStack.isEmpty() && 
               dialogStack.peek() >= minUndoIndex &&
               (currentScene.dialogs != null && currentScene.dialogs.size() > 1);
    }
}

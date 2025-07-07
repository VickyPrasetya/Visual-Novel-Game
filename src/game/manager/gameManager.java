package game.manager;

import com.google.gson.Gson;
import game.model.GameState;
import game.model.sceneData;
import game.model.DialogNode;

import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class gameManager {

    private sceneData currentScene;
    private int currentDialogIndex;
    private Stack<Integer> dialogStack = new Stack<>();
    private final Map<String, sceneData> sceneIndex = new HashMap<>();

    /**
     * Constructor akan memuat semua data cerita dan menyiapkan game di scene awal.
     */
    public gameManager() {
        loadStory();
        // Jika data cerita berhasil dimuat, langsung pergi ke scene awal.
        // Ini akan ditimpa jika pemain memuat game dari save file.
        if (!sceneIndex.isEmpty()) {
            goToScene("prolog_scene_1");
        }
    }

    /**
     * Memuat data cerita dari file scene.json.
     */
    private void loadStory() {
        Gson gson = new Gson();
        String filePath = "assets/scene.json";
        System.out.println("Mencoba membaca file dari: " + filePath);

        try (Reader reader = new FileReader(filePath)) {
            sceneData[] scenes = gson.fromJson(reader, sceneData[].class);
            if (scenes != null) {
                for (sceneData s : scenes) {
                    sceneIndex.put(s.id, s);
                }
                System.out.println("Berhasil memuat " + sceneIndex.size() + " scene.");
            }
        } catch (Exception e) {
            System.err.println("FATAL ERROR saat memuat atau mem-parsing story.json.");
            e.printStackTrace();
        }
    }

    /**
     * Mendapatkan scene yang sedang aktif.
     * @return Objek SceneData saat ini.
     */
    public sceneData getCurrentScene() {
        return this.currentScene;
    }

    /**
     * Mendapatkan dialog yang sedang aktif di dalam scene.
     * @return Objek DialogNode saat ini.
     */
    public DialogNode getCurrentDialog() {
        if (currentScene == null || currentScene.dialogs == null || currentDialogIndex >= currentScene.dialogs.size()) {
            return null;
        }
        return currentScene.dialogs.get(currentDialogIndex);
    }

    /**
     * Memindahkan game ke scene lain berdasarkan ID.
     * @param sceneId ID dari scene tujuan.
     */
    public void goToScene(String sceneId) {
        sceneData next = sceneIndex.get(sceneId);
        if (next != null) {
            currentScene = next;
            currentDialogIndex = 0;
            dialogStack.clear(); // Hapus riwayat undo saat pindah scene
        } else {
            System.err.println("Scene dengan ID '" + sceneId + "' tidak ditemukan. Mengakhiri game.");
            currentScene = null; // Anggap tamat jika scene tidak ditemukan
        }
    }

    /**
     * Pindah ke dialog berikutnya dalam scene yang sama.
     * @return true jika berhasil, false jika sudah di dialog terakhir.
     */
    public boolean nextDialog() {
        if (currentScene == null || currentScene.dialogs == null || currentDialogIndex >= currentScene.dialogs.size() - 1) {
            return false; // Sudah di dialog terakhir
        }
        // Simpan index saat ini ke stack untuk fitur undo
        dialogStack.push(currentDialogIndex);
        currentDialogIndex++;
        return true;
    }
    
    /**
     * Kembali ke dialog sebelumnya.
     * @return true jika berhasil, false jika tidak ada riwayat untuk di-undo.
     */
    public boolean undoDialog() {
        if (dialogStack.isEmpty()) {
            return false;
        }
        currentDialogIndex = dialogStack.pop();
        return true;
    }

    /**
     * Memeriksa apakah fitur undo bisa digunakan.
     * @return true jika bisa undo.
     */
    public boolean canUndoDialog() {
        return !dialogStack.isEmpty();
    }
    
    // --- METHOD BARU UNTUK SAVE/LOAD ---

    /**
     * Membuat objek GameState dari kondisi game saat ini untuk disimpan.
     * @return Objek GameState yang siap diserialisasi.
     */
    public GameState createSaveState() {
        if (currentScene == null) {
            return null; // Tidak bisa save jika tidak ada scene aktif
        }
        // Membuat objek baru berisi data progres saat ini
        return new GameState(currentScene.id, currentDialogIndex, dialogStack);
    }

    /**
     * Menerapkan kondisi game dari objek GameState yang dimuat.
     * @param state Objek GameState dari file save.
     */
    public void applyGameState(GameState state) {
        if (state == null || !sceneIndex.containsKey(state.currentSceneId)) {
            System.err.println("Gagal menerapkan game state: state null atau scene ID tidak valid.");
            return;
        }
        
        // Atur ulang semua variabel progres sesuai data dari file save
        this.currentScene = sceneIndex.get(state.currentSceneId);
        this.currentDialogIndex = state.currentDialogIndex;
        
        // Pastikan stack tidak null saat dimuat
        this.dialogStack = (state.dialogStack != null) ? state.dialogStack : new Stack<>();
    }
}
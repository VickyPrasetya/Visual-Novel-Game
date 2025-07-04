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

    private void loadStory() {
        Gson gson = new Gson();
        try (java.io.Reader reader = new java.io.FileReader("assets/scene.json")) {
            SceneData[] scenes = gson.fromJson(reader, SceneData[].class);
            for (SceneData s : scenes) {
                sceneIndex.put(s.id, s);
            }
            // Hubungkan children dari nextScene dan choices
            for (SceneData s : scenes) {
                s.children = new ArrayList<>();
                if (s.nextScene != null) {
                    SceneData child = sceneIndex.get(s.nextScene);
                    if (child != null) {
                        s.children.add(child);
                    }
                }
                if (s.choices != null) {
                    for (ChoiceData c : s.choices) {
                        SceneData child = sceneIndex.get(c.nextScene);
                        if (child != null) {
                            s.children.add(child);
                        }
                    }
                }
                // Tambahkan juga dari dialog yang punya "next" ke id scene lain
                if (s.dialogs != null) {
                    for (DialogNode d : s.dialogs) {
                        if (d.next != null && sceneIndex.containsKey(d.next)) {
                            SceneData child = sceneIndex.get(d.next);
                            if (child != null && !s.children.contains(child)) {
                                s.children.add(child);
                            }
                        }
                    }
                }
            }
            // Set root scene sesuai id di JSON
            rootScene = sceneIndex.get("prolog_scene_1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mendapatkan dialog saat ini (return DialogNode, bukan String)
    public DialogNode getCurrentDialog() {
        if (currentScene.dialogs == null || currentDialogIndex >= currentScene.dialogs.size()) {
            return null;
        }
        return currentScene.dialogs.get(currentDialogIndex);
    }

    private int minUndoIndex = 0; // index minimal undo di scene ini

// Saat memilih pilihan:
    public void choose(int choiceIndex) {
        if (currentScene.children != null && choiceIndex < currentScene.children.size()) {
            currentScene = currentScene.children.get(choiceIndex);
            currentDialogIndex = 0;
            dialogStack.clear();
            minUndoIndex = 0; // reset batas undo di scene baru
        }
    }

    // Lanjut ke dialog berikutnya, handle "next"
    public boolean nextDialog() {
        DialogNode dialog = getCurrentDialog();
        if (dialog == null) {
            return false;
        }

        dialogStack.push(currentDialogIndex);

        if (dialog.next != null) {
            // Cek apakah next adalah id scene
            if (sceneIndex.containsKey(dialog.next)) {
                currentScene = sceneIndex.get(dialog.next);
                currentDialogIndex = 0;
                dialogStack.clear();
                return true;
            } else {
                // Cek apakah next adalah label dialog di scene yang sama
                for (int i = 0; i < currentScene.dialogs.size(); i++) {
                    DialogNode d = currentScene.dialogs.get(i);
                    if (dialog.next.equals(d.label)) {
                        currentDialogIndex = i;
                        return true;
                    }
                }
            }
        } else if (currentDialogIndex < currentScene.dialogs.size() - 1) {
            currentDialogIndex++;
            return true;
        }
        return false;
    }

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

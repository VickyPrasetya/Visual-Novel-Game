package game.model;

import java.util.List;

/**
 * Kelas ini merepresentasikan satu adegan dalam cerita. Strukturnya harus cocok
 * dengan objek di dalam file JSON Anda. Kelas ini merepresentasikan satu adegan
 * dalam cerita. Strukturnya harus cocok dengan objek di dalam file JSON Anda.
 */
public class SceneData {

    public String id;
    public String backgroundImage;
    public String characterImage;
    public List<DialogNode> dialogs; // Ubah dari String dialog ke List<String>
    public List<ChoiceData> choices; // Pilihan ke scene berikutnya
    public String nextScene;
    public List<SceneData> children;
    // Untuk tree
    // public List<sceneData> nextScenes; // Anak-anak (pilihan)
}

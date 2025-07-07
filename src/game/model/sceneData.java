package game.model;

import java.util.List;

/**
 * Kelas ini merepresentasikan satu adegan dalam cerita. Strukturnya harus cocok
 * dengan objek di dalam file JSON Anda. Kelas ini merepresentasikan satu adegan
 * dalam cerita. Strukturnya harus cocok dengan objek di dalam file JSON Anda.
 */
public class sceneData {

    public String id;
    public String backgroundImage;
    public List<CharacterData> characters;
    public List<DialogNode> dialogs; // Ubah dari String dialog ke List<String>
    public List<choiceData> choices; // Pilihan ke scene berikutnya
    public String nextScene;
    public List<sceneData> children;
    public String type;
    public String music;

    public List<CharacterData> getCharacters() {
        return characters;
        // Untuk tree
        // public List<sceneData> nextScenes; // Anak-anak (pilihan)
    }
}

package game.model;

import java.util.List;

/**
 * Kelas SceneData merepresentasikan satu scene dalam game visual novel.
 */
public class SceneData {

    /** ID unik scene */
    public String id;
    /** Path gambar background */
    public String backgroundImage;
    /** Path musik background */
    public String music;
    /** Daftar karakter yang tampil di scene */
    public List<CharacterData> characters;
    /** Daftar dialog dalam scene */
    public List<DialogNode> dialogs; // Ubah dari String dialog ke List<String>
    /** ID scene berikutnya */
    public String nextScene;
    /** Tipe scene (misal: letter) */
    public String type;
    public List<ChoiceData> choices; // Pilihan ke scene berikutnya
    public List<SceneData> children;

    public List<CharacterData> getCharacters() {
        return characters;
        // Untuk tree
        // public List<sceneData> nextScenes; // Anak-anak (pilihan)
    }
}

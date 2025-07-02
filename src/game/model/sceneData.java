package game.model;

import java.util.List;

/**
 * Kelas ini merepresentasikan satu adegan dalam cerita.
 * Strukturnya harus cocok dengan objek di dalam file JSON Anda.
 */
public class sceneData {
    public String id;
    public String dialog;
    public String nextScene;
    public List<choiceData> choices; // Akan bernilai null jika adegan tidak punya pilihan
}
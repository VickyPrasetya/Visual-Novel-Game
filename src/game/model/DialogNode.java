package game.model;

import java.util.*;

/**
 * Kelas DialogNode merepresentasikan satu node dialog dalam scene.
 */
public class DialogNode {

      // Properti untuk dialog biasa
    /** Nama karakter yang berbicara */
    public String character;
    /** Teks dialog */
    public String text;
    /** Pilihan yang tersedia pada dialog ini */
    public List<ChoiceData> choices;
    /** ID dialog berikutnya (opsional) */
    public String next;
}

// File: game/model/GameState.java
package game.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

/**
 * Kelas GameState merepresentasikan state/progres game yang akan disimpan atau dimuat.
 */
public class GameState {
    /** ID scene saat ini */
    public String currentSceneId;
    /** Index dialog saat ini */
    public int currentDialogIndex;
    /** Stack riwayat dialog untuk fitur undo */
    public Stack<Integer> dialogStack;

    // Data tambahan untuk ditampilkan di UI
    public long timestamp;
    public String saveDate;

    // Constructor kosong dibutuhkan oleh Gson
    public GameState() {}

    // Constructor untuk membuat state baru
    public GameState(String currentSceneId, int currentDialogIndex, Stack<Integer> dialogStack) {
        this.currentSceneId = currentSceneId;
        this.currentDialogIndex = currentDialogIndex;
        // Kita perlu membuat salinan stack, bukan hanya referensi
        this.dialogStack = new Stack<>();
        this.dialogStack.addAll(dialogStack);
        
        this.timestamp = System.currentTimeMillis();
        // Format tanggal agar mudah dibaca
        this.saveDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date(this.timestamp));
    }
}
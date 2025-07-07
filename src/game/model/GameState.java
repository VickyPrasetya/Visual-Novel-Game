// File: game/model/GameState.java
package game.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class GameState {
    // Data utama yang perlu disimpan
    public String currentSceneId;
    public int currentDialogIndex;
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
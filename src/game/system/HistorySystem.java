package game.system;

import java.util.ArrayList;
import java.util.List;

/**
 * Kelas HistorySystem bertanggung jawab untuk mencatat riwayat dialog, pilihan, dan scene yang sudah dilalui oleh pemain.
 */
public class HistorySystem {
    private final List<String> historyLog = new ArrayList<>();

    /**
     * Menambahkan entri ke riwayat.
     * @param entry Teks dialog/pilihan/scene yang ingin dicatat.
     */
    public void addHistory(String entry) {
        historyLog.add(entry);
    }

    /**
     * Mengambil seluruh riwayat yang sudah dicatat.
     * @return List riwayat.
     */
    public List<String> getHistory() {
        return new ArrayList<>(historyLog);
    }

    /**
     * Menghapus seluruh riwayat.
     */
    public void clearHistory() {
        historyLog.clear();
    }
} 
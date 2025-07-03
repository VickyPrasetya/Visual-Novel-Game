// File: src/ui/visualnovel/game/Main.java

package game;

import game.manager.gameManager;
import game.model.choiceData;
import game.model.sceneData;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    // Variabel untuk menyimpan komponen utama game
    private gameManager gameManager; // Ini adalah "otak" yang mengelola cerita
    private Label dialogueLabel;     // Ini adalah label untuk menampilkan teks dialog
    private VBox choicesBox;         // Ini adalah wadah untuk tombol-tombol pilihan

    // Metode utama yang akan dieksekusi saat program Java dimulai
    public static void main(String[] args) {
        launch(args); // Perintah ini untuk memulai aplikasi JavaFX
    }

    // Metode ini dipanggil saat aplikasi JavaFX siap dimulai
    @Override
    public void start(Stage primaryStage) {
        // 1. Inisialisasi GameManager
        // Kita membuat objek gameManager baru, yang akan langsung memuat cerita dari scene1.json
        gameManager = new gameManager();

        primaryStage.setTitle("Visual Novel - 24 Hours"); // Judul jendela game

        // 2. Siapkan Layout Utama
        // BorderPane adalah layout yang membagi area menjadi 5 bagian (Top, Bottom, Left, Right, Center)
        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: black;"); // Latar belakang utama jadi hitam

        // 3. Siapkan Kotak Dialog
        // VBox menumpuk elemen secara vertikal. Ini akan jadi tempat dialog dan tombol.
        VBox dialogueContainer = new VBox(15); // Angka 15 adalah jarak antar elemen
        dialogueContainer.setPadding(new Insets(20)); // Jarak dari tepi kotak
        // Latar belakang semi-transparan agar terlihat keren
        dialogueContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-border-color: white; -fx-border-width: 1;");
        dialogueContainer.setAlignment(Pos.CENTER_LEFT);

        // 4. Siapkan Label untuk Teks Dialog
        dialogueLabel = new Label();
        dialogueLabel.setWrapText(true); // Agar teks panjang bisa turun ke baris baru
        dialogueLabel.setFont(Font.font("Arial", 20));
        dialogueLabel.setTextFill(Color.WHITE); // Warna teks putih

        // 5. Siapkan Wadah untuk Tombol Pilihan
        choicesBox = new VBox(10); // Jarak antar tombol 10
        choicesBox.setAlignment(Pos.CENTER_LEFT);

        // Masukkan label dialog dan wadah pilihan ke dalam kotak dialog
        dialogueContainer.getChildren().addAll(dialogueLabel, choicesBox);

        // Letakkan kotak dialog di bagian bawah layout utama
        rootLayout.setBottom(dialogueContainer);

        // 6. Buat Scene dan Tampilkan
        // Scene adalah isi dari sebuah jendela. Ukurannya 1280x720 piksel.
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene); // Pasang scene ke jendela utama (Stage)
        
        // Panggil updateUI() untuk pertama kali agar adegan prolog muncul
        updateUI();

        // Tampilkan jendela game
        primaryStage.show();
    }

    /**
     * Metode ini adalah jantung dari UI. Ia akan memperbarui tampilan
     * setiap kali kita pindah adegan.
     */
    private void updateUI() {
        // Ambil adegan yang sedang aktif dari gameManager
        sceneData currentScene = gameManager.getCurrentScene();

        // Cek jika cerita sudah tamat (currentScene == null)
        if (currentScene == null) {
            dialogueLabel.setText("TAMAT.\nTerima kasih telah bermain.");
            choicesBox.getChildren().clear(); // Hapus semua tombol
            return; // Hentikan fungsi
        }

        // Tampilkan dialog dari adegan saat ini ke label
        dialogueLabel.setText(currentScene.dialog);
        
        // Kosongkan wadah pilihan dari tombol-tombol sebelumnya
        choicesBox.getChildren().clear();

        // Cek apakah adegan ini punya pilihan (choices)
        if (currentScene.choices != null && !currentScene.choices.isEmpty()) {
            // Jika ADA pilihan, buat tombol untuk setiap pilihan
            for (choiceData choice : currentScene.choices) {
                Button choiceButton = new Button(choice.label);
                choiceButton.setStyle("-fx-font-size: 16px;");
                // Saat tombol diklik, pindah ke adegan selanjutnya sesuai pilihan
                choiceButton.setOnAction(_ -> {
                    gameManager.goToScene(choice.nextScene);
                    updateUI(); // Setelah pindah, perbarui lagi UI-nya
                });
                choicesBox.getChildren().add(choiceButton); // Tambahkan tombol ke wadah
            }
        } else {
            // Jika TIDAK ADA pilihan, buat satu tombol "Lanjut"
            // Teks tombol adalah "Tamat" jika ini adalah adegan terakhir
            String buttonText = (currentScene.nextScene != null) ? "Lanjut ->" : "Tamat";
            Button nextButton = new Button(buttonText);
            nextButton.setStyle("-fx-font-size: 16px;");

            // Saat tombol "Lanjut" diklik...
            nextButton.setOnAction(_ -> {
                // Pindah ke adegan selanjutnya
                gameManager.goToScene(currentScene.nextScene);
                updateUI(); // Perbarui lagi UI-nya
            });
            choicesBox.getChildren().add(nextButton); // Tambahkan tombol ke wadah
        }
    }
}
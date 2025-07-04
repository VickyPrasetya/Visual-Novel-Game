// File: /game/Main.java
package game;

import game.manager.gameManager;
import game.model.choiceData;
import game.model.sceneData;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane; // Hanya butuh StackPane sekarang
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private gameManager gameManager;
    private Label dialogueLabel;
    private VBox choicesBox;
    private ImageView backgroundView;
    private ImageView characterView;
    
    // PERUBAHAN: Kita akan pindahkan dialogueContainer ke sini agar bisa diakses di start()
    private VBox dialogueContainer; 

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager = new gameManager();
        primaryStage.setTitle("Visual Novel - 24 Hours");

        // PERUBAHAN: Layout utama sekarang adalah StackPane, bukan BorderPane
        StackPane rootLayout = new StackPane();

        // --- Bagian Gambar (Lapisan Bawah) ---
        // Wadah untuk gambar-gambar. Ini akan berada di lapisan paling bawah dari rootLayout
        StackPane imageContainer = new StackPane();
        
        backgroundView = new ImageView();
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());
        backgroundView.setPreserveRatio(false);

        characterView = new ImageView();
        characterView.setPreserveRatio(true);
        characterView.setFitHeight(600);
        StackPane.setAlignment(characterView, Pos.BOTTOM_CENTER);

        // Masukkan ImageView ke dalam imageContainer
        imageContainer.getChildren().addAll(backgroundView, characterView);
        
        // --- Bagian Dialog (Lapisan Atas) ---
        // Siapkan kotak dialog seperti sebelumnya
        dialogueContainer = new VBox(15);
        dialogueContainer.setPadding(new Insets(20));
        dialogueContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-border-color: white; -fx-border-width: 1;");
        
        // PERUBAHAN: Atur tinggi maksimum agar tidak menutupi seluruh layar
        dialogueContainer.setMaxHeight(250); // Batasi tinggi kotak dialog
        dialogueContainer.setAlignment(Pos.TOP_LEFT); // Dialog mulai dari kiri atas DARI DALAM KOTAK

        dialogueLabel = new Label();
        dialogueLabel.setWrapText(true);
        dialogueLabel.setFont(Font.font("Arial", 20));
        dialogueLabel.setTextFill(Color.WHITE);

        choicesBox = new VBox(10);
        choicesBox.setAlignment(Pos.CENTER_LEFT);

        dialogueContainer.getChildren().addAll(dialogueLabel, choicesBox);

        // --- Gabungkan Semua Lapisan ---
        // PERUBAHAN: Masukkan semua ke dalam rootLayout (StackPane)
        // imageContainer akan jadi lapisan bawah, dialogueContainer akan jadi lapisan atas
        rootLayout.getChildren().addAll(imageContainer, dialogueContainer);
        
        // PERUBAHAN: Atur posisi kotak dialog di bagian bawah layar
        StackPane.setAlignment(dialogueContainer, Pos.BOTTOM_CENTER);
        // Beri sedikit margin agar tidak menempel di tepi bawah
        StackPane.setMargin(dialogueContainer, new Insets(0, 20, 20, 20));

        // --- Buat Scene dan Tampilkan ---
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene); // Pasang scene ke jendela utama (Stage)

        // Panggil updateUI() untuk pertama kali agar adegan prolog muncul
        updateUI();
        primaryStage.show();
    }

    /**
     * Metode ini adalah jantung dari UI. Ia akan memperbarui tampilan setiap
     * kali kita pindah adegan.
     */
    private void updateUI() {
        sceneData currentScene = gameManager.getCurrentScene();

        // PERUBAHAN: Logika untuk menyembunyikan kotak dialog saat tamat
        if (currentScene == null) {
            dialogueContainer.setVisible(false); // Sembunyikan seluruh kotak dialog
            // Tampilkan layar tamat yang lebih bersih
            backgroundView.setImage(null); // Atau gambar credit screen
            characterView.setImage(null);
            
            // Opsional: Tampilkan pesan tamat di tengah layar
            Label endLabel = new Label("TAMAT\nTerima kasih telah bermain.");
            endLabel.setFont(Font.font("Arial", 40));
            endLabel.setTextFill(Color.WHITE);
            endLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");
            
            // Tambahkan label tamat ke root layout
            // Pastikan rootLayout adalah StackPane agar bisa ditambahkan
            if (backgroundView.getParent().getParent() instanceof StackPane) {
                StackPane root = (StackPane) backgroundView.getParent().getParent();
                // Hapus label tamat sebelumnya jika ada
                root.getChildren().removeIf(node -> node instanceof Label && ((Label)node).getText().startsWith("TAMAT"));
                root.getChildren().add(endLabel);
            }
            return;
        }

        // Pastikan kotak dialog terlihat lagi jika game dimulai ulang
        dialogueContainer.setVisible(true);

        updateImage(backgroundView, currentScene.backgroundImage);
        updateImage(characterView, currentScene.characterImage);
        
        dialogueLabel.setText(currentScene.dialog);

        // Kosongkan wadah pilihan dari tombol-tombol sebelumnya
        choicesBox.getChildren().clear();

        if (currentScene.choices != null && !currentScene.choices.isEmpty()) {
            for (choiceData choice : currentScene.choices) {
                Button choiceButton = new Button(choice.label);
                choiceButton.setStyle("-fx-font-size: 16px;");
                choiceButton.setOnAction(_ -> {
                    gameManager.goToScene(choice.nextScene);
                    updateUI();
                });
                choicesBox.getChildren().add(choiceButton);
            }
        } else {
            String buttonText = (currentScene.nextScene != null) ? "Lanjut ->" : "Tamat";
            Button nextButton = new Button(buttonText);
            nextButton.setStyle("-fx-font-size: 16px;");
            nextButton.setOnAction(_ -> {
                gameManager.goToScene(currentScene.nextScene);
                updateUI();
            });
            choicesBox.getChildren().add(nextButton); // Tambahkan tombol ke wadah
        }
    }

     // Metode updateImage tetap sama persis, tidak perlu diubah
    private void updateImage(ImageView view, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                Image image = new Image(imageFile.toURI().toString());
                view.setImage(image);
            } catch (Exception e) {
                System.err.println("Gagal memuat gambar: " + imagePath);
                view.setImage(null);
            }
        } else {
            view.setImage(null);
        }
    }
    
}

 

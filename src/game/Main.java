// File: /game/Main.java
package game;

import game.manager.gameManager;
import game.model.choiceData;
import game.model.sceneData;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane; // Import untuk scrolling surat
import javafx.scene.effect.DropShadow; // Import untuk efek bayangan
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    private gameManager gameManager;
    private Stage primaryStage; // Simpan stage untuk referensi ukuran

    // Komponen UI
    private StackPane rootLayout;
    private ImageView backgroundView;
    private ImageView characterView;
    
    // UI Dialog Bawah
    private VBox dialogueUIGroup;
    private StackPane nameBox;
    private Label nameLabel;
    private VBox dialogueContainer;
    private Label dialogueLabel;
    private Label nextIndicator;
    private VBox choicesBox;

    // --- PENAMBAHAN FITUR BARU ---
    private VBox centeredChoicesContainer; // Wadah untuk tombol pilihan di tengah
    private StackPane letterContainer;     // Wadah untuk tampilan surat

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Simpan stage
        gameManager = new gameManager();
        primaryStage.setTitle("Visual Novel - 24 Hours");

        rootLayout = new StackPane();

        // --- LAPISAN 1: GAMBAR ---
        StackPane imageContainer = new StackPane();
        backgroundView = new ImageView();
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());
        backgroundView.setPreserveRatio(false);
        characterView = new ImageView();
        characterView.setPreserveRatio(true);
        characterView.setFitHeight(600);
        StackPane.setAlignment(characterView, Pos.BOTTOM_CENTER);
        imageContainer.getChildren().addAll(backgroundView, characterView);

        // --- LAPISAN 2: UI DIALOG BAWAH (Struktur yang sudah benar) ---
        dialogueUIGroup = new VBox();
        dialogueUIGroup.setAlignment(Pos.BOTTOM_LEFT);
        dialogueUIGroup.setPadding(new Insets(0, 20, 20, 20));

        nameLabel = new Label();
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        nameLabel.setTextFill(Color.web("#4e342e"));
        nameBox = new StackPane(nameLabel);
        nameBox.setStyle("-fx-background-color: #ffb7c5; -fx-background-radius: 5 5 0 0; -fx-border-color: white; -fx-border-width: 2; -fx-border-radius: 5 5 0 0;");
        nameBox.setPadding(new Insets(5, 20, 5, 20));
        nameBox.setAlignment(Pos.CENTER);
        nameBox.setMaxWidth(VBox.USE_PREF_SIZE);
        VBox.setMargin(nameBox, new Insets(0, 0, 0, 40)); 
        nameBox.setVisible(false);

        StackPane dialogueSystemStack = new StackPane();
        dialogueContainer = new VBox(15);
        dialogueContainer.setPadding(new Insets(20));
        dialogueContainer.setStyle("-fx-background-color: rgba(255, 183, 197, 0.8); -fx-border-color: white; -fx-border-width: 2;");
        dialogueContainer.setMinHeight(180);

        dialogueLabel = new Label();
        dialogueLabel.setWrapText(true);
        dialogueLabel.setFont(Font.font("Arial", 24));
        dialogueLabel.setTextFill(Color.web("#4e342e"));
        dialogueLabel.setLineSpacing(5);

        choicesBox = new VBox(10);
        choicesBox.setAlignment(Pos.CENTER_LEFT);
        dialogueContainer.getChildren().addAll(dialogueLabel, choicesBox);

        nextIndicator = new Label("▼");
        nextIndicator.setFont(Font.font("Arial", 24));
        nextIndicator.setTextFill(Color.web("#4e342e"));
        nextIndicator.setPadding(new Insets(0, 15, 15, 0));
        nextIndicator.setVisible(false);

        dialogueSystemStack.getChildren().addAll(dialogueContainer, nextIndicator);
        StackPane.setAlignment(nextIndicator, Pos.BOTTOM_RIGHT);
        dialogueUIGroup.getChildren().addAll(nameBox, dialogueSystemStack);
        
        // --- PENAMBAHAN FITUR BARU: Wadah untuk Pilihan & Surat ---
        // 1. Wadah untuk pilihan di tengah
        centeredChoicesContainer = new VBox(20);
        centeredChoicesContainer.setAlignment(Pos.CENTER);
        centeredChoicesContainer.setPadding(new Insets(20));
        centeredChoicesContainer.setVisible(false); // Sembunyikan di awal

        // 2. Wadah untuk surat
        letterContainer = new StackPane();
        letterContainer.setStyle("-fx-background-color: rgba(255, 255, 240, 0.95);");
        letterContainer.setPadding(new Insets(50));
        letterContainer.setVisible(false); // Sembunyikan di awal
        
        // --- GABUNGKAN SEMUA LAPISAN ---
        rootLayout.getChildren().addAll(imageContainer, dialogueUIGroup, centeredChoicesContainer, letterContainer);
        StackPane.setAlignment(dialogueUIGroup, Pos.BOTTOM_CENTER);

        // --- BUAT SCENE ---
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);

        updateUI();
        primaryStage.show();
    }

    private void updateUI() {
        // --- Reset semua UI ke kondisi awal ---
        dialogueUIGroup.setVisible(true);
        nameBox.setVisible(false);
        nextIndicator.setVisible(false);
        dialogueUIGroup.setCursor(Cursor.DEFAULT);
        dialogueUIGroup.setOnMouseClicked(null);
        centeredChoicesContainer.setVisible(false);
        letterContainer.setVisible(false);

        sceneData currentScene = gameManager.getCurrentScene();

        if (currentScene == null) { /* Logika akhir game... */ return; }
        
        updateImage(backgroundView, currentScene.backgroundImage);
        updateImage(characterView, currentScene.characterImage);
        
        // --- PENAMBAHAN FITUR BARU: Logika untuk menampilkan UI yang berbeda ---

        // KASUS 1: Adegan Surat
        if ("epilog_12".equals(currentScene.id)) {
            showLetter(currentScene);
            return;
        }

        // KASUS 2: Adegan dengan Pilihan
        if (currentScene.choices != null && !currentScene.choices.isEmpty()) {
            showChoices(currentScene);
            return;
        }
        
        // KASUS 3: Dialog Normal (Default)
        showNormalDialogue(currentScene);
    }

    // --- PENAMBAHAN FITUR BARU: Metode terpisah untuk setiap jenis UI ---

    private void showNormalDialogue(sceneData currentScene) {
        dialogueLabel.setText(currentScene.dialog);
        choicesBox.getChildren().clear(); // Pastikan kosong
        
        if (currentScene.characterName != null && !currentScene.characterName.isEmpty()) {
            nameLabel.setText(currentScene.characterName);
            nameBox.setVisible(true);
        }

        nextIndicator.setVisible(true);
        dialogueUIGroup.setCursor(Cursor.HAND);
        dialogueUIGroup.setOnMouseClicked(event -> {
            gameManager.goToScene(currentScene.nextScene);
            updateUI();
        });
    }

    private void showChoices(sceneData currentScene) {
        // Tampilkan kotak dialog bawah untuk konteks
        dialogueLabel.setText(currentScene.dialog);
        choicesBox.getChildren().clear();
        if (currentScene.characterName != null && !currentScene.characterName.isEmpty()) {
            nameLabel.setText(currentScene.characterName);
            nameBox.setVisible(true);
        }
        
        // Sembunyikan tombol lanjut bawah
        nextIndicator.setVisible(false);

        // Tampilkan tombol pilihan di tengah
        centeredChoicesContainer.getChildren().clear();
        centeredChoicesContainer.setVisible(true);

        for (choiceData choice : currentScene.choices) {
            Button choiceButton = new Button(choice.label);
            choiceButton.setMaxWidth(primaryStage.getWidth() * 0.7); // Batasi lebar
            choiceButton.setWrapText(true);
            choiceButton.setFont(Font.font("Arial", FontWeight.BOLD, 22));
            choiceButton.setTextFill(Color.WHITE);
            choiceButton.setPadding(new Insets(15, 30, 15, 30));
            // Style yang lebih gelap seperti contoh
            choiceButton.setStyle("-fx-background-color: rgba(20, 20, 40, 0.85); -fx-border-color: #a0a0ff; -fx-border-width: 3;");
            choiceButton.setEffect(new DropShadow(10, Color.BLACK));
            
            choiceButton.setOnMouseEntered(e -> choiceButton.setStyle("-fx-background-color: rgba(80, 80, 120, 0.85); -fx-border-color: #a0a0ff; -fx-border-width: 3;"));
            choiceButton.setOnMouseExited(e -> choiceButton.setStyle("-fx-background-color: rgba(20, 20, 40, 0.85); -fx-border-color: #a0a0ff; -fx-border-width: 3;"));

            choiceButton.setOnAction(_ -> {
                gameManager.goToScene(choice.nextScene);
                updateUI();
            });
            centeredChoicesContainer.getChildren().add(choiceButton);
        }
    }
    
    private void showLetter(sceneData currentScene) {
        dialogueUIGroup.setVisible(false); // Sembunyikan UI dialog bawah
        
        Label letterTextLabel = new Label(currentScene.dialog);
        letterTextLabel.setWrapText(true);
        letterTextLabel.setFont(Font.font("Courier New", 22));
        letterTextLabel.setTextFill(Color.BLACK);
        
        // Gunakan ScrollPane jika teks sangat panjang
        ScrollPane scrollPane = new ScrollPane(letterTextLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        letterContainer.getChildren().setAll(scrollPane); // Ganti isi letterContainer
        letterContainer.setVisible(true);

        // Aksi klik untuk melanjutkan
        letterContainer.setCursor(Cursor.HAND);
        letterContainer.setOnMouseClicked(e -> {
            gameManager.goToScene(currentScene.nextScene);
            updateUI();
        });
    }
    private void updateImage(ImageView view, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    view.setImage(image);
                } else {
                    System.err.println("File gambar tidak ditemukan: " + imagePath);
                    view.setImage(null);
                }
            } catch (Exception e) {
                System.err.println("Gagal memuat gambar: " + imagePath);
                e.printStackTrace();
                view.setImage(null);
            }
        } else {
            view.setImage(null);
        }
    }
}
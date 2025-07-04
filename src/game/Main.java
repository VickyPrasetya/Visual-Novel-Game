// File: /game/Main.java
package game;

import game.manager.gameManager;
import game.model.ChoiceData;
import game.model.SceneData;
import game.model.DialogNode;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region; // <-- IMPORT BARU YANG DIBUTUHKAN
import javafx.scene.layout.StackPane;
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

    private VBox dialogueContainer;
    private Label nextIndicator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager = new gameManager();
        primaryStage.setTitle("Visual Novel - 24 Hours");

        StackPane rootLayout = new StackPane();

        // --- LAPISAN 1: GAMBAR (BACKGROUND & KARAKTER) ---
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

        // --- LAPISAN 2: UI (KOTAK DIALOG & INDIKATOR) ---
        StackPane uiOverlay = new StackPane();
        uiOverlay.setPadding(new Insets(0, 20, 20, 20));

        // PERBAIKAN KRUSIAL: Mencegah wadah UI mengembang memenuhi layar.
        // Baris ini menyuruh uiOverlay untuk "menjadi sekecil mungkin sesuai isinya".
        uiOverlay.setMaxSize(1600, 200); // Ukuran maksimum yang sesuai untuk UI

        dialogueContainer = new VBox(15);
        dialogueContainer.setPadding(new Insets(20));
        dialogueContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.3); -fx-border-color: white; -fx-border-width: 1;");
        dialogueContainer.setMaxHeight(200);
        dialogueContainer.setAlignment(Pos.TOP_LEFT);

        dialogueLabel = new Label();
        dialogueLabel.setWrapText(true);
        dialogueLabel.setFont(Font.font("Arial", 20));
        dialogueLabel.setTextFill(Color.WHITE);

        choicesBox = new VBox(10);
        choicesBox.setAlignment(Pos.CENTER_LEFT);

        dialogueContainer.getChildren().addAll(dialogueLabel, choicesBox);

        nextIndicator = new Label("▼");
        nextIndicator.setFont(Font.font("Arial", 24));
        nextIndicator.setTextFill(Color.WHITE);
        nextIndicator.setVisible(false);

        uiOverlay.getChildren().addAll(dialogueContainer, nextIndicator);
        StackPane.setAlignment(nextIndicator, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(nextIndicator, new Insets(0, 25, 15, 0));

        // --- GABUNGKAN SEMUA LAPISAN KE LAYOUT UTAMA ---
        rootLayout.getChildren().addAll(imageContainer, uiOverlay);

        // Sekarang baris ini akan bekerja karena uiOverlay sudah berukuran kecil
        StackPane.setAlignment(uiOverlay, Pos.BOTTOM_CENTER);

        // --- BUAT SCENE DAN TAMPILKAN ---
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);

        updateUI();
        primaryStage.show();
    }

    // Metode updateUI() dan updateImage() tidak perlu diubah.
    private void updateUI() {
        dialogueContainer.setOnMouseClicked(null);
        dialogueContainer.setCursor(Cursor.DEFAULT);
        nextIndicator.setVisible(false);

        sceneData currentScene = gameManager.getCurrentScene();

        if (currentScene == null) {
            if (dialogueContainer.getParent() != null) {
                dialogueContainer.getParent().setVisible(false);
            }
            backgroundView.setImage(null);
            characterView.setImage(null);

            Label endLabel = new Label("TAMAT\nTerima kasih telah bermain.");
            endLabel.setFont(Font.font("Arial", 40));
            endLabel.setTextFill(Color.WHITE);
            endLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");

            if (backgroundView.getParent() instanceof StackPane) {
                StackPane root = (StackPane) backgroundView.getParent();
                root.getChildren().removeIf(node -> node instanceof Label && ((Label) node).getText().startsWith("TAMAT"));
                root.getChildren().add(endLabel);
            }
            return;
        }

        if (dialogueContainer.getParent() != null) {
            dialogueContainer.getParent().setVisible(true);
        }

        updateImage(backgroundView, currentScene.backgroundImage);
        updateImage(characterView, currentScene.characterImage);

        dialogueLabel.setText(currentScene.dialog);

        // Kosongkan wadah pilihan dari tombol-tombol sebelumnya
        choicesBox.getChildren().clear();
        DialogNode currentDialog = gameManager.getCurrentDialog();
        if (currentDialog.choices != null && !currentDialog.choices.isEmpty()) {
            for (ChoiceData choice : currentDialog.choices) {
                Button choiceButton = new Button(choice.text);
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

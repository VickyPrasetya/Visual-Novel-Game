package ui.visualnovel.game;

import ui.visualnovel.game.manager.gameManager;
import ui.visualnovel.game.Model.choiceData;
import ui.visualnovel.game.Model.sceneData;

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
    // ... (Salin semua sisa kode dari jawaban saya sebelumnya di sini) ...
    // Kode dari private GameManager gameManager; sampai akhir.
    private gameManager gameManager;
    private Label dialogueLabel;
    private VBox choicesBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        gameManager = new gameManager();

        primaryStage.setTitle("24 Hours - Visual Novel");

        BorderPane rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: black;");

        VBox dialogueContainer = new VBox(15);
        dialogueContainer.setPadding(new Insets(20));
        dialogueContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");
        dialogueContainer.setAlignment(Pos.CENTER_LEFT);

        dialogueLabel = new Label();
        dialogueLabel.setWrapText(true);
        dialogueLabel.setFont(Font.font("Arial", 18));
        dialogueLabel.setTextFill(Color.WHITE);

        choicesBox = new VBox(10);
        choicesBox.setAlignment(Pos.CENTER_LEFT);

        dialogueContainer.getChildren().addAll(dialogueLabel, choicesBox);
        rootLayout.setBottom(dialogueContainer);

        // Ubah nama file JSON dari scene1.json ke scenes.json atau sebaliknya
        // agar sesuai dengan kode di GameManager
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);
        
        updateUI();
        primaryStage.show();
    }

    private void updateUI() {
        sceneData currentScene = gameManager.getCurrentScene();

        if (currentScene == null) {
            dialogueLabel.setText("TAMAT.\nTerima kasih telah bermain.");
            choicesBox.getChildren().clear();
            return;
        }

        dialogueLabel.setText(currentScene.dialog);
        choicesBox.getChildren().clear();

        if (currentScene.choices != null && !currentScene.choices.isEmpty()) {
            for (choiceData choice : currentScene.choices) {
                Button choiceButton = new Button(choice.label);
                choiceButton.setOnAction(e -> {
                    gameManager.goToScene(choice.nextScene);
                    updateUI();
                });
                choicesBox.getChildren().add(choiceButton);
            }
        } else {
            Button nextButton = new Button(currentScene.nextScene != null ? "Lanjut ->" : "Tamat");
            nextButton.setOnAction(e -> {
                gameManager.goToScene(currentScene.nextScene);
                updateUI();
            });
            choicesBox.getChildren().add(nextButton);
        }
    }
}
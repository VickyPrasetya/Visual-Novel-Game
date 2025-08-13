package game.system;

import javafx.scene.text.TextAlignment;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import game.ui.Style;
import game.GameAppCallback;

public class CreditsSystem {

    public StackPane showCreditsScreen(GameAppCallback callback) {
        
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #111;");

        // --- BAGIAN ATAS (TOP): JUDUL ---
        Label title = new Label("Credits");
        title.setFont(Font.font(Style.MAIN_FONT_BOLD, FontWeight.BOLD, Style.TITLE_FONT_SIZE));
        title.setTextFill(Color.WHITE);
        
        layout.setTop(title);
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(60, 0, 40, 0));

        // --- BAGIAN TENGAH (CENTER): KONTEN YANG BISA DI-SCROLL ---
        Label creditsText = new Label(
            "Game Visual Novel 24 Hours\n\n" +
            "Programmer:\nKelompok 5\n\n" +
            "Art:\nhttps://noranekogames.itch.io/\n" +
            "https://potat0master.itch.io/\n" +
            "https://kawaiisayian.itch.io\n" +
            "https://leonardo.ai/\n\n" +
            "Music:\nhttps://freetouse.com/music/category/timelaps...\n\n" +
            "Special Thanks:\nBpk.Suhendra\n" +
            "Ibu.Raden Rara Kartika Kusuma Winahyu\n" +
            "Kak.Afni Tazkiyatul Misky\n\n" +
            "Terima kasih telah bermain!"
        );
        creditsText.setFont(Font.font("Times New Roman", 24)); 
        creditsText.setTextFill(Color.LIGHTGRAY);
        creditsText.setWrapText(true);
        creditsText.setTextAlignment(TextAlignment.CENTER);

        // FIX: Bungkus label dengan StackPane untuk memusatkannya secara horizontal
        StackPane textContainer = new StackPane(creditsText);

        // Masukkan container ke dalam ScrollPane
        ScrollPane scrollPane = new ScrollPane(textContainer);
        scrollPane.setFitToWidth(true); 
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        scrollPane.getStyleClass().add("hidden-scrollbar");

        layout.setCenter(scrollPane);
        BorderPane.setMargin(scrollPane, new Insets(0, 80, 0, 80));

        // --- BAGIAN BAWAH (BOTTOM): TOMBOL BACK ---
        Button backButton = new Button("Back");
        Style.styleMainMenuButton(backButton);
        backButton.setOnAction(e -> callback.showMainMenu());

        layout.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(40, 0, 60, 0));

        // Bungkus dengan StackPane untuk dikembalikan
        StackPane root = new StackPane(layout);
        return root;
    }
}
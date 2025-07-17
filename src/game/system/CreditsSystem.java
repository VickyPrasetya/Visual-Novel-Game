package game.system;

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
        StackPane layout = new StackPane();
        layout.setStyle("-fx-background-color: #111;");
        Label title = new Label("Credits");
        title.setFont(Font.font(Style.MAIN_FONT_BOLD, FontWeight.BOLD, Style.TITLE_FONT_SIZE));
        title.setTextFill(Color.WHITE);
        Label creditsText = new Label(
            "Game Visual Novel 24 Hours\n\nProgrammer: \nKelompok 5\nArt:\nhttps://noranekogames.itch.io/ \nhttps://potat0master.itch.io/ \nhttps://kawaiisayian.itch.io \nhttps://leonardo.ai/\n Music:\nhttps://freetouse.com/music/category/timelapse \nSpecial Thanks:\nBpk.Suhendra\nIbu.Raden Rara Kartika Kusuma Winahyu\nKak.Afni Tazkiyatul Misky\nTerima kasih telah bermain!"
        );
        creditsText.setFont(Font.font(Style.MAIN_FONT, 24));
        creditsText.setTextFill(Color.LIGHTGRAY);
        creditsText.setAlignment(Pos.CENTER);
        creditsText.setWrapText(true);
        Button backButton = new Button("Back");
        Style.styleMainMenuButton(backButton);
        backButton.setOnAction(e -> callback.showMainMenu());
        VBox content = new VBox(40, title, creditsText, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(60));
        layout.getChildren().add(content);
        return layout;
    }
} 
package game.system;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import game.ui.Style;
import game.GameAppCallback;
import javafx.scene.control.Label;

/**
 * Kelas MenuSystem bertanggung jawab untuk mengelola logika menu utama dan in-game menu.
 */
public class MenuSystem {
    private final GameAppCallback callback;
    public MenuSystem(GameAppCallback callback) {
        this.callback = callback;
    }

    /**
     * Menampilkan menu utama dengan background blur dan style modern.
     * @return StackPane menu utama.
     */
    public StackPane showMainMenu() {
        StackPane menuLayout = new StackPane();
        // Gunakan background blur dari Main
        ImageView menuBg = Style.createBlurredBackground(Style.MENU_BACKGROUND_PATH);
        Text gameTitle = new Text("24 𝓗𝓸𝓾𝓻𝓼");
        gameTitle.setFont(Font.font(Style.MAIN_FONT_BOLD, FontWeight.BOLD, Style.TITLE_FONT_SIZE));
        gameTitle.setFill(Color.WHITE);
        gameTitle.setStroke(Color.BLACK);
        gameTitle.setStrokeWidth(2);
        Button startButton = new Button("Start");
        Button loadButton = new Button("Load Game");
        Button settingsButton = new Button("Settings");
        Button creditsButton = new Button("Credits");
        Button exitButton = new Button("Quit");
        Style.styleMainMenuButton(startButton);
        Style.styleMainMenuButton(loadButton);
        Style.styleMainMenuButton(settingsButton);
        Style.styleMainMenuButton(creditsButton);
        Style.styleMainMenuButton(exitButton);
        startButton.setOnAction(e -> ((game.Main)callback).startNewGame());
        loadButton.setOnAction(e -> callback.onRequestLoad(false));
        settingsButton.setOnAction(e -> callback.showSettingsScreen());
        creditsButton.setOnAction(e -> callback.showCreditsScreen());
        exitButton.setOnAction(e -> callback.exitGame());
        VBox menuButtons = new VBox(20, startButton, loadButton, settingsButton, creditsButton, exitButton);
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setMaxWidth(350);
        VBox titleAndButtons = new VBox(50, gameTitle, menuButtons);
        titleAndButtons.setAlignment(Pos.CENTER);
        menuLayout.getChildren().addAll(menuBg, titleAndButtons);
        return menuLayout;
    }

    /**
     * Menyembunyikan menu yang sedang aktif.
     */
    public void hideMenu() {}
} 
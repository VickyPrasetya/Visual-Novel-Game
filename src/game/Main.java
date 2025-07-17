package game;

import game.manager.gameManager;
import game.manager.SaveLoadService;
import game.model.CharacterData;
import game.model.DialogNode;
import game.model.GameState;
import game.model.choiceData;
import game.model.sceneData;
import game.system.ScreenshotSystem;
import game.ui.Style;
import game.util.FileUtil;
import game.system.HistorySystem;
import game.system.TransitionSystem;
import game.system.MenuSystem;
import game.system.SettingsSystem;
import game.system.SaveLoadSystem;
import game.ui.GameUIScreen;
import game.system.AudioSystem;
import game.system.CreditsSystem;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javax.imageio.ImageIO;
import javafx.stage.Screen;
import java.awt.Rectangle;
import java.awt.AWTException;
import java.awt.Robot;
import javafx.embed.swing.SwingFXUtils;

/**
 * Kelas Main adalah entry point utama aplikasi Visual Novel 24 Hours.
 * Bertanggung jawab untuk inisialisasi UI, navigasi layar, dan integrasi sistem modular.
 */
public class Main extends Application implements GameAppCallback {

    private Stage primaryStage;
    private StackPane rootLayout;
    private MenuSystem menuSystem;
    private SettingsSystem settingsSystem;
    private SaveLoadSystem saveLoadSystem;
    private SaveLoadService saveLoadService;
    private gameManager gameManager;
    private GameUIScreen gameUIScreen;
    private HistorySystem historySystem = new HistorySystem();
    private TransitionSystem transitionSystem = new TransitionSystem();
    private static final String DEFAULT_MUSIC_PATH = "assets/music/Music Page Menu.mp3";
    private CreditsSystem creditsSystem;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visual Novel - 24 Hours");
        rootLayout = new StackPane();
        menuSystem = new MenuSystem(this);
        settingsSystem = new SettingsSystem();
        saveLoadSystem = new SaveLoadSystem();
        saveLoadService = new SaveLoadService();
        creditsSystem = new CreditsSystem();
        gameManager = new gameManager();
        gameUIScreen = new GameUIScreen(gameManager, historySystem, transitionSystem, new GameUIScreen.GameUICallback() {
            @Override public void onRequestMenu() { showMainMenu(); }
            @Override public void onRequestSave(boolean fromInGameMenu) { showSaveLoadScreen(true, fromInGameMenu); }
            @Override public void onRequestLoad(boolean fromInGameMenu) { showSaveLoadScreen(false, fromInGameMenu); }
            @Override public void onRequestSettings() { showSettingsScreen(); }
            @Override public void onRequestCredits() { showCreditsScreen(); }
            @Override public void onRequestExit() { exitGame(); }
        });
        showMainMenu();
  

// Lalu set scene-nya
Scene scene = new Scene(gameUIScreen.getGamePane(), 1280, 720);
primaryStage.setScene(scene);
        primaryStage.show();
        AudioSystem.getInstance().playMusic(DEFAULT_MUSIC_PATH, true);
    }

    // --- Callback untuk transisi antar layar ---
    public void showMainMenu() {
        rootLayout.getChildren().setAll(menuSystem.showMainMenu());
        AudioSystem.getInstance().playMusic(DEFAULT_MUSIC_PATH, true);
    }
    public void showGameScreen() {
        rootLayout.getChildren().setAll(gameUIScreen.getGamePane());
        // Optionally: play scene music here if needed
    }
    public void showSettingsScreen() {
        Runnable onBack = null;
        if (rootLayout.getChildren().contains(gameUIScreen.getGamePane())) {
            onBack = () -> {
                rootLayout.getChildren().setAll(gameUIScreen.getGamePane());
                gameUIScreen.showInGameMenuOverlay();
            };
        }
        rootLayout.getChildren().setAll(settingsSystem.showSettingsUI(primaryStage, this, onBack));
    }
    public void showSaveLoadScreen(boolean isSave, boolean fromInGameMenu) {
        Runnable onBack = () -> {
            if (fromInGameMenu) {
                rootLayout.getChildren().setAll(gameUIScreen.getGamePane());
                gameUIScreen.showInGameMenuOverlay();
            } else {
                showMainMenu();
            }
        };
        if (isSave) {
            rootLayout.getChildren().setAll(saveLoadSystem.showSaveUI(this, gameManager, saveLoadService, gameUIScreen.getGamePane(), onBack));
        } else {
            rootLayout.getChildren().setAll(saveLoadSystem.showLoadUI(this, gameManager, saveLoadService, onBack));
        }
    }
    public void showCreditsScreen() {
        rootLayout.getChildren().setAll(creditsSystem.showCreditsScreen(this));
    }
    public void exitGame() { primaryStage.close(); }

    public void showGameScreenAndUpdate() {
        showGameScreen();
        gameUIScreen.updateUI();
    }

    public void startNewGame() {
        gameManager = new gameManager();
        gameUIScreen = new GameUIScreen(gameManager, historySystem, transitionSystem, new GameUIScreen.GameUICallback() {
            @Override public void onRequestMenu() { showMainMenu(); }
            @Override public void onRequestSave(boolean fromInGameMenu) { showSaveLoadScreen(true, fromInGameMenu); }
            @Override public void onRequestLoad(boolean fromInGameMenu) { showSaveLoadScreen(false, fromInGameMenu); }
            @Override public void onRequestSettings() { showSettingsScreen(); }
            @Override public void onRequestCredits() { showCreditsScreen(); }
            @Override public void onRequestExit() { exitGame(); }
        });
        showGameScreen();
        gameUIScreen.updateUI();
    }

    public GameUIScreen getGameUIScreen() {
        return gameUIScreen;
    }

    @Override
    public void onRequestSave(boolean fromInGameMenu) { showSaveLoadScreen(true, fromInGameMenu); }
    @Override
    public void onRequestLoad(boolean fromInGameMenu) { showSaveLoadScreen(false, fromInGameMenu); }
}

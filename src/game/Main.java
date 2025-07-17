package game;

// Import yang benar dengan casing yang benar
import game.manager.GameManager;
import game.manager.SaveLoadService;
import game.system.CreditsSystem;
import game.system.HistorySystem;
import game.system.MenuSystem;
import game.system.SaveLoadSystem;
import game.system.SettingsSystem;
import game.system.TransitionSystem;
import game.ui.GameUIScreen;

// Import JavaFX
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Kelas Main versi modular yang bersih.
 * Bertanggung jawab untuk inisialisasi dan navigasi antar sistem.
 */
public class Main extends Application implements GameAppCallback {

    private Stage primaryStage;
    private StackPane rootLayout;
    
    // Deklarasi semua sistem dan manajer
    private MenuSystem menuSystem;
    private SettingsSystem settingsSystem;
    private SaveLoadSystem saveLoadSystem;
    private HistorySystem historySystem;
    private TransitionSystem transitionSystem;
    private CreditsSystem creditsSystem;
    private SaveLoadService saveLoadService;
    private GameManager gameManager;
    private GameUIScreen gameUIScreen;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visual Novel - 24 Hours");
        
        // Inisialisasi semua komponen
        rootLayout = new StackPane();
        saveLoadService = new SaveLoadService();
        historySystem = new HistorySystem();
        transitionSystem = new TransitionSystem();
        menuSystem = new MenuSystem(this);
        settingsSystem = new SettingsSystem();
        saveLoadSystem = new SaveLoadSystem();
        creditsSystem = new CreditsSystem();

        // Tampilkan menu utama saat aplikasi dimulai
        showMainMenu();
        
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- Implementasi dari GameAppCallback untuk Navigasi ---

    @Override
    public void showMainMenu() {
        rootLayout.getChildren().setAll(menuSystem.showMainMenu());
    }

    @Override
    public void startNewGame() {
        // Buat instance baru untuk game baru
        gameManager = new GameManager(); // Menggunakan 'G' besar
        gameUIScreen = new GameUIScreen(gameManager, historySystem, transitionSystem, this);
        showGameScreen();
        gameUIScreen.updateUI();
    }
    
    @Override
    public void showGameScreen() {
        if (gameUIScreen != null) {
            rootLayout.getChildren().setAll(gameUIScreen.getGamePane());
        }
    }

    @Override
    public void showSettingsScreen() {
        Runnable onBack = null;
        if (gameUIScreen != null && rootLayout.getChildren().contains(gameUIScreen.getGamePane())) {
            onBack = () -> {
                rootLayout.getChildren().setAll(gameUIScreen.getGamePane());
                gameUIScreen.showInGameMenuOverlay();
            };
        }
        rootLayout.getChildren().setAll(settingsSystem.showSettingsUI(primaryStage, this, onBack));
    }

    @Override
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

    @Override
    public void showCreditsScreen() {
        rootLayout.getChildren().setAll(creditsSystem.showCreditsScreen(this));
    }

    @Override
    public void exitGame() {
        primaryStage.close();
    }

    @Override
    public void showGameScreenAndUpdate() {
        showGameScreen();
        if (gameUIScreen != null) {
            gameUIScreen.updateUI();
        }
    }

    // --- Implementasi dari GameUICallback (didelegasikan dari Main) ---

    @Override
    public void onRequestMenu() { showMainMenu(); }
    @Override
    public void onRequestSave(boolean fromInGameMenu) { showSaveLoadScreen(true, fromInGameMenu); }
    @Override
    public void onRequestLoad(boolean fromInGameMenu) { showSaveLoadScreen(false, fromInGameMenu); }
    @Override
    public void onRequestSettings() { showSettingsScreen(); }
    @Override
    public void onRequestCredits() { showCreditsScreen(); }
    @Override
    public void onRequestExit() { exitGame(); }
    
    @Override
    public GameUIScreen getGameUIScreen() {
        return gameUIScreen;
    }
}
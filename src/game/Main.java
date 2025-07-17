package game;

import game.manager.gameManager;
import game.model.GameState;
import game.manager.SaveLoadService;
import game.system.AudioSystem;
import game.system.CreditsSystem;
import game.system.HistorySystem;
import game.system.MenuSystem;
import game.system.SaveLoadSystem;
import game.system.SettingsSystem;
import game.system.TransitionSystem;
import game.ui.GameUIScreen;
import game.ui.GameUIScreen.GameUICallback;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Kelas Main adalah entry point dan "sutradara" utama aplikasi.
 * Bertanggung jawab untuk inisialisasi semua sistem, manajemen layar,
 * dan menjadi jembatan komunikasi antar modul.
 */
public class Main extends Application implements GameAppCallback {

    private Stage primaryStage;
    private StackPane rootLayout; // Panggung utama untuk semua layar

    // Deklarasi semua sistem/aktor modular
    private MenuSystem menuSystem;
    private SettingsSystem settingsSystem;
    private SaveLoadSystem saveLoadSystem;
    private SaveLoadService saveLoadService;
    private gameManager gameManager;
    private GameUIScreen gameUIScreen;
    private HistorySystem historySystem;
    private TransitionSystem transitionSystem;
    private CreditsSystem creditsSystem;

    private static final String DEFAULT_MUSIC_PATH = "assets/music/Music Page Menu.mp3";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visual Novel - 24 Hours");

        // 1. Siapkan panggung utama
        rootLayout = new StackPane();
        rootLayout.setStyle("-fx-background-color: black;");

        // 2. Inisialisasi semua sistem (para aktor)
        initializeSystems();
        
        // 3. Tampilkan layar pertama (Main Menu)
        showMainMenu();

        // 4. Atur Scene dan tampilkan Stage
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Putar musik default
        AudioSystem.getInstance().playMusic(DEFAULT_MUSIC_PATH, true);
    }

    /**
     * Menginisialisasi semua kelas sistem dan UI.
     */
    private void initializeSystems() {
        // Inisialisasi sistem non-UI
        historySystem = new HistorySystem();
        transitionSystem = new TransitionSystem();
        saveLoadService = new SaveLoadService();
        gameManager = new gameManager();

        // Inisialisasi sistem UI (layar)
        // Kita berikan 'this' sebagai callback agar mereka bisa "melapor" kembali ke Main
        menuSystem = new MenuSystem(this);
        settingsSystem = new SettingsSystem();
        saveLoadSystem = new SaveLoadSystem();
        creditsSystem = new CreditsSystem();
        
        // GameUIScreen butuh lebih banyak dependensi
        gameUIScreen = new GameUIScreen(gameManager, historySystem, transitionSystem, new GameUICallback() {
            @Override public void onRequestMenu() { showMainMenu(); }
            @Override public void onRequestSave(boolean fromInGameMenu) { showSaveLoadScreen(true, fromInGameMenu); }
            @Override public void onRequestLoad(boolean fromInGameMenu) { showSaveLoadScreen(false, fromInGameMenu); }
            @Override public void onRequestSettings() { showSettingsScreen(); }
            @Override public void onRequestCredits() { showCreditsScreen(); } // Jika ada
            @Override public void onRequestExit() { exitGame(); }
        });
    }

    /**
     * Method helper untuk mengganti layar yang sedang aktif di panggung utama.
     * @param screen Node (layar) yang ingin ditampilkan.
     */
    private void switchScreen(Node screen) {
        rootLayout.getChildren().setAll(screen);
    }

    // --- Implementasi Callback & Navigasi Layar ---

    @Override
    public void showMainMenu() {
        switchScreen(menuSystem.showMainMenu());
        AudioSystem.getInstance().playMusic(DEFAULT_MUSIC_PATH, true);
    }

    @Override
    public void showGameScreen() {
        switchScreen(gameUIScreen.getGamePane());
        // Musik akan di-handle oleh updateUI di dalam GameUIScreen berdasarkan data scene
    }
    
   

    @Override
    public void showSettingsScreen() {
        // Tentukan apa yang harus dilakukan saat tombol "Back" di settings ditekan
        Runnable onBackAction;
        // Jika layar sebelumnya adalah Game, kembali ke Game dengan overlay menu
        if (rootLayout.getChildren().contains(gameUIScreen.getGamePane())) {
            onBackAction = () -> {
                showGameScreen();
                gameUIScreen.showInGameMenuOverlay();
            };
        } else { // Jika tidak, kembali ke Main Menu
            onBackAction = this::showMainMenu;
        }
        
        switchScreen(settingsSystem.showSettingsUI(primaryStage, this, onBackAction));
    }

    @Override
    public void showSaveLoadScreen(boolean isSave, boolean fromInGameMenu) {
        // Tentukan apa yang harus dilakukan saat tombol "Back" di save/load ditekan
        Runnable onBackAction = () -> {
            if (fromInGameMenu) {
                showGameScreen();
                gameUIScreen.showInGameMenuOverlay();
            } else {
                showMainMenu();
            }
        };

        if (isSave) {
            // Kita butuh screenshot game screen, jadi kita panggil getGamePane()
            Node gamePaneForScreenshot = gameUIScreen.getGamePane();
            switchScreen(saveLoadSystem.showSaveUI(this, gameManager, saveLoadService, gamePaneForScreenshot, onBackAction));
        } else {
            switchScreen(saveLoadSystem.showLoadUI(this, gameManager, saveLoadService, onBackAction));
        }
    }

    @Override
    public void showCreditsScreen() {
        switchScreen(creditsSystem.showCreditsScreen(this));
    }
    
  

    @Override
    public void exitGame() {
        primaryStage.close();
    }
    @Override
    public void onRequestLoad(boolean fromInGameMenu) {
    showSaveLoadScreen(false, fromInGameMenu);
}

@Override
public void onRequestSave(boolean fromInGameMenu) {
    showSaveLoadScreen(true, fromInGameMenu);
}
@Override
public void startNewGame() {
    if (gameManager == null) {
        gameManager = new gameManager();
    }
    gameManager.startNewGame(); // Panggil method di GameManager untuk reset
    showGameScreen();
    gameUIScreen.updateUI(); // Tampilkan UI untuk scene pertama
}

@Override
public void loadGame(GameState gameState) {
    if (gameManager == null) {
        gameManager = new gameManager();
    }
    gameManager.applyGameState(gameState);
    showGameScreen();
    gameUIScreen.updateUI(); // Tampilkan UI untuk state yang di-load
}
}
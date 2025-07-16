package game;

public interface GameAppCallback {
    void showMainMenu();
    void showGameScreen();
    void showSettingsScreen();
    void showSaveLoadScreen(boolean isSave, boolean fromInGameMenu);
    void showCreditsScreen();
    void exitGame();
    void onRequestSave(boolean fromInGameMenu);
    void onRequestLoad(boolean fromInGameMenu);
} 
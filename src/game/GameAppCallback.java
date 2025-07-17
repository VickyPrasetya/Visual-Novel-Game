package game;

import game.model.GameState;

public interface GameAppCallback {
    void showMainMenu();
    void showGameScreen();

    void showSettingsScreen();
    void startNewGame();
    void showSaveLoadScreen(boolean isSave, boolean fromInGameMenu);
    void showCreditsScreen();

    void exitGame();
    void loadGame(GameState gameState);
    void onRequestSave(boolean fromInGameMenu);
    void onRequestLoad(boolean fromInGameMenu);
} 
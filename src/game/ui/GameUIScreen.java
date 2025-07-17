package game.ui;

import game.manager.gameManager;
import game.model.sceneData;
import game.model.DialogNode;
import game.model.choiceData;
import game.system.HistorySystem;
import game.system.TransitionSystem;
import javafx.scene.layout.Priority;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import java.io.File;
import java.util.function.Consumer;
import javafx.scene.control.Tooltip;
import game.system.AudioSystem;
import javafx.scene.layout.Region;

public class GameUIScreen {

    // --- DEPENDENSI & CALLBACK ---
    private final gameManager gameManager;
    private final HistorySystem historySystem;
    private final TransitionSystem transitionSystem;
    private final GameUICallback callback;

    // --- NODE UI UTAMA ---
    private final StackPane rootPane;
    private final ImageView backgroundView;
    private final HBox characterContainer;
    private final ImageView leftCharacterView;
    private final ImageView rightCharacterView;
    private final VBox dialogueUIGroup;
    private final VBox centeredChoicesContainer;
    private final StackPane letterContainer;
    private StackPane inGameMenuOverlay;

    // --- ELEMEN DIALOG ---
    private final StackPane nameBox;
    private final Text nameLabel;
    private final Text dialogueLabel;
    private final VBox choicesBox;
    private final Label nextIndicator;

    // --- ELEMEN TOMBOL ---
    private final Button undoButton;
    private final Button textSpeedButton;
    private final Button skipButton;
    private final HBox dialogBoxButtonBar;
    private final Button menuButton;
    private final Button muteButton;
    private final Button historyButton;

    // --- STATE & EFEK ---
    private Timeline typewriterTimeline;
    private String fullDialogText = "";
    private boolean isTypewriterRunning = false;
    private int textSpeedMode = 1; // 0=lambat, 1=normal, 2=cepat
    // Tambahan: referensi ke SettingsSystem untuk sinkronisasi dua arah
    private game.system.SettingsSystem settingsSystem;

    public void setSettingsSystem(game.system.SettingsSystem settingsSystem) {
        this.settingsSystem = settingsSystem;
    }
    private boolean isInGameMenuVisible = false;
    private VBox historyOverlay;
    private boolean isHistoryVisible = false;

    // --- KONSTANTA ---
    private static final String[] TEXT_SPEED_LABELS = {"🐢", "⏩", "⚡"};
    private static final String MAIN_FONT = "Segoe UI";
    private static final String MAIN_FONT_BOLD = "Segoe UI Semibold";
    private static final int DIALOG_FONT_SIZE = 25;
    private static final int NAME_FONT_SIZE = 22;

    public GameUIScreen(gameManager gameManager, HistorySystem historySystem, TransitionSystem transitionSystem, GameUICallback callback) {
        this.gameManager = gameManager;
        this.historySystem = historySystem;
        this.transitionSystem = transitionSystem;
        this.callback = callback;

        // 1. Inisialisasi Root Pane
        rootPane = new StackPane();
        rootPane.setMinWidth(320);
        rootPane.setMinHeight(240);
        rootPane.setStyle("-fx-background-color: black;");

        // 2. Setup Background & Character Views
        backgroundView = new ImageView();
        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundView.fitHeightProperty().bind(rootPane.heightProperty());

        leftCharacterView = new ImageView();
        leftCharacterView.setPreserveRatio(true);
        leftCharacterView.fitHeightProperty().bind(rootPane.heightProperty().multiply(0.9));

        rightCharacterView = new ImageView();
        rightCharacterView.setPreserveRatio(true);
        rightCharacterView.fitHeightProperty().bind(rootPane.heightProperty().multiply(0.9));

        characterContainer = new HBox(leftCharacterView, rightCharacterView);
        characterContainer.setAlignment(Pos.BOTTOM_CENTER);
        characterContainer.setSpacing(100);
        characterContainer.setPadding(new Insets(0, 0, 20, 0));
        characterContainer.setPickOnBounds(false);

        // ==================================================================
        // [FIX] URUTAN PEMBUATAN KOMPONEN DIALOG SUDAH DIPERBAIKI DI SINI
        // ==================================================================
        // LANGKAH A: BUAT BAGIAN DALAM KOTAK DIALOG (dialogueContainer)
        final VBox dialogueContainer;
        {
            dialogueLabel = new Text();
            dialogueLabel.setFont(Font.font(MAIN_FONT, FontWeight.BOLD, DIALOG_FONT_SIZE));
            dialogueLabel.setFill(Color.WHITE);
            dialogueLabel.setStroke(Color.BLACK);
            dialogueLabel.setStrokeWidth(0.5);

            TextFlow dialogueFlow = new TextFlow(dialogueLabel);
            dialogueFlow.setLineSpacing(5);

            ScrollPane scroll = new ScrollPane(dialogueFlow);
            scroll.setFitToWidth(true);
            scroll.setFitToHeight(true);
            scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            VBox.setVgrow(scroll, Priority.ALWAYS);

            choicesBox = new VBox(10);
            choicesBox.setAlignment(Pos.CENTER);

            dialogueContainer = new VBox(15, scroll, choicesBox);
            dialogueContainer.setPadding(new Insets(20, 40, 60, 40));
            dialogueContainer.setStyle("-fx-background-color: rgba(255, 183, 197, 0.9); -fx-border-radius:10; -fx-border-color: white; -fx-border-width: 1; -fx-background-radius: 10;");
            dialogueContainer.minHeightProperty().bind(rootPane.heightProperty().multiply(0.25));
            dialogueContainer.minWidthProperty().bind(rootPane.widthProperty().multiply(0.95));
        }

        // LANGKAH B: BUAT TOMBOL-TOMBOL DIALOG (dialogBoxButtonBar)
        {
            historyButton = new Button("\uD83D\uDCDC History");
            undoButton = new Button("⟲ Undo");
            textSpeedButton = new Button(TEXT_SPEED_LABELS[textSpeedMode]);
            skipButton = new Button("Skip");
            Style.styleBottomBarButton(historyButton);
            Style.styleBottomBarButton(undoButton);
            Style.styleBottomBarButton(textSpeedButton);
            Style.styleBottomBarButton(skipButton);

            dialogBoxButtonBar = new HBox(8, historyButton, undoButton, textSpeedButton, skipButton);
            dialogBoxButtonBar.setAlignment(Pos.BOTTOM_CENTER);
            dialogBoxButtonBar.setPadding(new Insets(0, 0, 5, 0));
        }

        // LANGKAH C: BUAT KOTAK NAMA (nameBox)
        {
            nameLabel = new Text();
            nameLabel.setFont(Font.font(MAIN_FONT_BOLD, FontWeight.BOLD, NAME_FONT_SIZE));
            nameLabel.setFill(Color.WHITE);
            nameLabel.setStroke(Color.BLACK);
            nameLabel.setStrokeWidth(0.5);

            nameBox = new StackPane(new TextFlow(nameLabel));
            nameBox.setStyle("-fx-background-color: #ffb7c5; -fx-background-radius: 15 15 0 0; -fx-border-color: white; -fx-border-width: 1; -fx-border-radius: 15 15 0 0;");
            nameBox.setPadding(new Insets(5, 20, 5, 20));
            nameBox.setAlignment(Pos.CENTER_LEFT);
            nameBox.setMaxWidth(VBox.USE_PREF_SIZE);
            nameBox.setVisible(false);
        }

        // LANGKAH D: GABUNGKAN SEMUA BAGIAN DIALOG MENJADI SATU (dialogueUIGroup)
        {
            nextIndicator = new Label("▼");
            nextIndicator.setFont(Font.font(MAIN_FONT, 24));
            nextIndicator.setTextFill(Color.web("#4e342e"));

            // Buat StackPane HANYA SATU KALI di sini untuk menumpuk kotak dialog, tombol, dan indikator
            StackPane dialogueSystemStack = new StackPane(dialogueContainer, dialogBoxButtonBar, nextIndicator);
            StackPane.setAlignment(dialogBoxButtonBar, Pos.BOTTOM_RIGHT);
            StackPane.setAlignment(nextIndicator, Pos.BOTTOM_RIGHT);
            StackPane.setMargin(nextIndicator, new Insets(0, 25, 10, 0));

            // Buat wrapper untuk nameBox agar bisa di-offset
            HBox nameBoxWrapper = new HBox(nameBox);
            nameBoxWrapper.setPadding(new Insets(0, 0, -2, 40));
            nameBoxWrapper.setPickOnBounds(false);

            // Gabungkan namebox wrapper dan tumpukan dialog
            dialogueUIGroup = new VBox(nameBoxWrapper, dialogueSystemStack);
            dialogueUIGroup.setAlignment(Pos.BOTTOM_CENTER);
            dialogueUIGroup.setPickOnBounds(false);
            dialogueUIGroup.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.95));
            dialogueUIGroup.setMaxWidth(1200);
        }

        // ==================================================================
        // AKHIR DARI PERBAIKAN URUTAN
        // ==================================================================
        // 4. Setup Centered Choices Container
        centeredChoicesContainer = new VBox(15);
        centeredChoicesContainer.setAlignment(Pos.CENTER);
        centeredChoicesContainer.setPickOnBounds(false);
        centeredChoicesContainer.setVisible(false);

        // 5. Setup Letter Container
        letterContainer = new StackPane();
        letterContainer.setVisible(false);

        // 6. Setup Top-Left Buttons (Menu, Mute)
        menuButton = new Button("☰");
        muteButton = new Button(AudioSystem.getInstance().isMuted() ? "\uD83D\uDD07" : "\uD83D\uDD0A");
        Style.styleIconButton(menuButton);
        menuButton.setPrefSize(50, 50);
        Style.styleIconButton(muteButton);
        muteButton.setPrefSize(50, 50);
        muteButton.setFont(Font.font("Segoe UI Emoji", 22));

        HBox topLeftButtonBar = new HBox(10, menuButton, muteButton);
        topLeftButtonBar.setAlignment(Pos.TOP_LEFT);
        topLeftButtonBar.setPadding(new Insets(20));

        // 7. Assemble All Layers into Root Pane
        rootPane.getChildren().addAll(
                backgroundView,
                characterContainer,
                topLeftButtonBar,
                dialogueUIGroup,
                centeredChoicesContainer,
                letterContainer
        );
        StackPane.setAlignment(dialogueUIGroup, Pos.BOTTOM_CENTER);
        rootPane.setPadding(new Insets(15));

        // 8. Setup Event Handlers & Actions
        setupEventHandlers();

        // 9. Initial UI State
        updateUI();
    }

    // METHOD UNTUK MENDAPATKAN NODE UTAMA
    public Node getGamePane() {
        return rootPane;
    }

    // METHOD UNTUK MENUNJUKKAN MENU OVERLAY (DIPANGGIL DARI LUAR)
    public void showInGameMenuOverlay() {
        if (isInGameMenuVisible) {
            return;
        }
        if (inGameMenuOverlay == null) {
            inGameMenuOverlay = createInGameMenuOverlay();
        }
        if (!rootPane.getChildren().contains(inGameMenuOverlay)) {
            rootPane.getChildren().add(inGameMenuOverlay);
        }
        isInGameMenuVisible = true;
    }

    // --- LOGIKA UTAMA UNTUK UPDATE UI ---
    public void updateUI() {
        if (gameManager == null) {
            return;
        }
        sceneData currentScene = gameManager.getCurrentScene();
        if (currentScene == null) {
            return;
        }
           if (currentScene.music != null) {
        if (currentScene.music.trim().isEmpty()) {
            AudioSystem.getInstance().stopMusic();
        } 
        else {    
            AudioSystem.getInstance().playMusic(currentScene.music, true);
        }
    }
        // Tambahan: clear history jika sudah sampai ending
        if ("ending_screen".equals(currentScene.id)) {
            historySystem.clearHistory();
        }
        // Tambahan: clear history jika baru mulai game (scene pertama)
        if ("prolog_scene_1".equals(currentScene.id) && historySystem.getHistory().size() > 0) {
            historySystem.clearHistory();
        }
        updateImage(backgroundView, currentScene.backgroundImage, "assets/bg/black.jpg");
        updateCharacters(currentScene);
        dialogueUIGroup.setVisible(false);
        centeredChoicesContainer.setVisible(false);
        letterContainer.setVisible(false);
        if ("letter".equals(currentScene.type)) {
            showLetter(currentScene);
        } else {
            DialogNode currentDialog = gameManager.getCurrentDialog();
            if (currentDialog != null) {
                if (currentDialog.choices != null && !currentDialog.choices.isEmpty()) {
                    showChoices(currentDialog);
                } else {
                    // Tambahan: hanya simpan history jika bukan scene pertama
                    if (!"prolog_scene_1".equals(currentScene.id)) {
                        showNormalDialogue(currentScene, currentDialog);
                    } else {
                        // Tampilkan dialog tanpa menyimpan ke history
                        dialogueUIGroup.setVisible(true);
                        rootPane.setCursor(Cursor.HAND);
                        if (currentDialog.character != null && !currentDialog.character.isEmpty()) {
                            nameLabel.setText(currentDialog.character);
                            nameBox.setVisible(true);
                        } else {
                            nameBox.setVisible(false);
                        }
                        if (typewriterTimeline != null) typewriterTimeline.stop();
                        fullDialogText = currentDialog.text;
                        dialogueLabel.setText(fullDialogText);
                        isTypewriterRunning = false;
                        dialogBoxButtonBar.setVisible(true);
                        undoButton.setDisable(!gameManager.canUndoDialog());
                    }
                }
            }
        }
    }

    // --- PRIVATE HELPER METHODS ---
    private void setupEventHandlers() {
        rootPane.setOnMouseClicked(event -> {
            if (isInGameMenuVisible || isHistoryVisible || letterContainer.isVisible()) {
                return;
            }
            if (event.getTarget() instanceof Button || (event.getTarget() instanceof Node && ((Node) event.getTarget()).getParent() instanceof Button)) {
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                handlePrimaryClick();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                handleSecondaryClick();
            }
        });

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.H) {
                toggleHistoryOverlay();
            }
        });

        menuButton.setOnAction(e -> toggleInGameMenuOverlay());
        muteButton.setOnAction(e -> {
            boolean newMute = !AudioSystem.getInstance().isMuted();
            AudioSystem.getInstance().setMute(newMute);
            muteButton.setText(newMute ? "\uD83D\uDD07" : "\uD83D\uDD0A");
        });
        historyButton.setOnAction(e -> toggleHistoryOverlay());
        undoButton.setOnAction(e -> handleSecondaryClick());
        skipButton.setOnAction(e -> {
            if (gameManager != null) {
                while (true) {
                    DialogNode currentDialog = gameManager.getCurrentDialog();
                    if (currentDialog == null || (currentDialog.choices != null && !currentDialog.choices.isEmpty())) {
                        break;
                    }
                    if (!gameManager.nextDialog()) {
                        break;
                    }
                }
                updateUI();
            }
        });
        textSpeedButton.setOnAction(e -> {
            textSpeedMode = (textSpeedMode + 1) % 3;
            textSpeedButton.setText(TEXT_SPEED_LABELS[textSpeedMode]);
            // Sinkronkan ke SettingsSystem jika tersedia
            if (settingsSystem != null) {
                settingsSystem.setTextSpeed(textSpeedMode);
                settingsSystem.updateTextSpeedSlider(textSpeedMode);
                settingsSystem.notifyTextSpeedChanged(textSpeedMode);
            }
        });
    }

    private void handlePrimaryClick() {
        if (isTypewriterRunning) {
            if (typewriterTimeline != null) {
                typewriterTimeline.stop();
            }
            dialogueLabel.setText(fullDialogText);
            isTypewriterRunning = false;
            return;
        }
        boolean moved = gameManager.nextDialog();
        if (!moved) {
            sceneData currentScene = gameManager.getCurrentScene();
            DialogNode currentDialog = gameManager.getCurrentDialog();
            if (currentScene != null && currentDialog != null) {
                String nextSceneId = (currentDialog.next != null) ? currentDialog.next : currentScene.nextScene;
                if (nextSceneId != null) {
                    historySystem.addHistory("[Pindah ke scene: " + nextSceneId + "]");
                    transitionSystem.fadeTransition(backgroundView, 400);
                }
                gameManager.goToScene(nextSceneId);
            }
        }
        updateUI();
    }

    private void handleSecondaryClick() {
        if (gameManager.undoDialog()) {
            updateUI();
        }
    }

    private void showNormalDialogue(sceneData currentScene, DialogNode currentDialog) {
        dialogueUIGroup.setVisible(true);
        rootPane.setCursor(Cursor.HAND);

        if (currentDialog.character != null && !currentDialog.character.isEmpty()) {
            nameLabel.setText(currentDialog.character);
            nameBox.setVisible(true);
        } else {
            nameBox.setVisible(false);
        }

        if (typewriterTimeline != null) {
            typewriterTimeline.stop();
        }
        fullDialogText = currentDialog.text;
        dialogueLabel.setText("");
        isTypewriterRunning = true;
        int speed = switch (textSpeedMode) {
            case 0 ->
                60;
            case 2 ->
                10;
            default ->
                30;
        };
        typewriterTimeline = new Timeline();
        for (int i = 0; i <= fullDialogText.length(); i++) {
            final int idx = i;
            typewriterTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * speed), e -> {
                dialogueLabel.setText(fullDialogText.substring(0, idx));
                if (idx == fullDialogText.length()) {
                    isTypewriterRunning = false;
                }
            }));
        }
        typewriterTimeline.play();

        String historyEntry = (currentDialog.character != null ? currentDialog.character + ": " : "") + currentDialog.text;
        historySystem.addHistory(historyEntry);

        dialogBoxButtonBar.setVisible(true);
        undoButton.setDisable(!gameManager.canUndoDialog());
    }

    private void showChoices(DialogNode currentDialog) {
        centeredChoicesContainer.setVisible(true);
        centeredChoicesContainer.getChildren().clear();
        rootPane.setCursor(Cursor.DEFAULT);

        for (choiceData choice : currentDialog.choices) {
            Button choiceButton = new Button(choice.text);
            Style.styleChoiceButtonLarge(choiceButton);
            choiceButton.setOnAction(e -> {
                historySystem.addHistory("[Pilihan] " + choice.text);
                if ("main_menu".equals(choice.nextScene)) {
                    callback.onRequestMenu();
                    return;
                }
                if ("exit_game".equals(choice.nextScene)) {
                    callback.onRequestExit();
                    return;
                }
                if (choice.nextScene != null && !choice.nextScene.isEmpty()) {
                    transitionSystem.fadeTransition(backgroundView, 400);
                    gameManager.goToScene(choice.nextScene);
                    updateUI();
                } else {
                    System.err.println("ERROR: nextScene untuk choice '" + choice.text + "' null atau kosong!");
                }
            });
            centeredChoicesContainer.getChildren().add(choiceButton);
        }
    }

    private void showLetter(sceneData currentScene) {
        letterContainer.setVisible(true);
        letterContainer.getChildren().clear();
        rootPane.setCursor(Cursor.DEFAULT);

        DialogNode letterDialog = gameManager.getCurrentDialog();
        if (letterDialog == null) {
            return;
        }

        Label letterTextLabel = new Label(letterDialog.text);
        letterTextLabel.setWrapText(true);
        letterTextLabel.setFont(Font.font("Courier New", 22));
        letterTextLabel.setTextFill(Color.BLACK);
        letterTextLabel.setPadding(new Insets(50));

        ScrollPane scrollPane = new ScrollPane(letterTextLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        StackPane paper = new StackPane(scrollPane);
        paper.setStyle("-fx-background-color: #faf0e6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);");
        paper.maxWidthProperty().bind(rootPane.widthProperty().multiply(0.8));
        paper.maxHeightProperty().bind(rootPane.heightProperty().multiply(0.8));

        letterContainer.getChildren().add(paper);

        letterContainer.setOnMouseClicked(e -> {
            if (currentScene.nextScene != null) {
                gameManager.goToScene(currentScene.nextScene);
                updateUI();
            }
        });
    }

    private void updateCharacters(sceneData currentScene) {
        leftCharacterView.setImage(null);
        rightCharacterView.setImage(null);
        if (currentScene.characters != null) {
            for (var character : currentScene.characters) {
                ImageView viewToUpdate = "left".equalsIgnoreCase(character.position) ? leftCharacterView : rightCharacterView;
                updateImage(viewToUpdate, character.sprite, null);
            }
        }
    }

    private void updateImage(ImageView view, String imagePath, String fallbackPath) {
        try {
            String finalPath = imagePath;
            if (finalPath == null || finalPath.isEmpty()) {
                finalPath = fallbackPath;
            }
            if (finalPath != null && !finalPath.isEmpty()) {
                File imageFile = new File(finalPath);
                if (imageFile.exists()) {
                    view.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    view.setImage(null);
                }
            } else {
                view.setImage(null);
            }
        } catch (Exception e) {
            view.setImage(null);
        }
    }

    // --- OVERLAY LOGIC ---
    private void toggleInGameMenuOverlay() {
        if (isInGameMenuVisible) {
            hideInGameMenuOverlay();
        } else {
            showInGameMenuOverlay();
        }
    }

    private void hideInGameMenuOverlay() {
        if (isInGameMenuVisible && inGameMenuOverlay != null) {
            rootPane.getChildren().remove(inGameMenuOverlay);
            isInGameMenuVisible = false;
        }
    }

    private StackPane createInGameMenuOverlay() {
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(40));

        Button resumeBtn = new Button("Resume");
        Button saveBtn = new Button("Save");
        Button loadBtn = new Button("Load");
        Button settingsBtn = new Button("Settings");
        Button mainMenuBtn = new Button("Main Menu");
        Button quitBtn = new Button("Quit");

        Style.styleMainMenuButton(resumeBtn);
        Style.styleMainMenuButton(saveBtn);
        Style.styleMainMenuButton(loadBtn);
        Style.styleMainMenuButton(settingsBtn);
        Style.styleMainMenuButton(mainMenuBtn);
        Style.styleMainMenuButton(quitBtn);

        resumeBtn.setOnAction(e -> hideInGameMenuOverlay());
        saveBtn.setOnAction(e -> {
            hideInGameMenuOverlay();
            callback.onRequestSave(true);
        });
        loadBtn.setOnAction(e -> {
            hideInGameMenuOverlay();
            callback.onRequestLoad(true);
        });
        settingsBtn.setOnAction(e -> {
            hideInGameMenuOverlay();
            callback.onRequestSettings();
        });
        mainMenuBtn.setOnAction(e -> showConfirmDialog("Kembali ke Main Menu?", "Progres yang belum disimpan akan hilang.", () -> {
            hideInGameMenuOverlay();
            callback.onRequestMenu();
        }));
        quitBtn.setOnAction(e -> showConfirmDialog("Keluar dari Game?", "Progres yang belum disimpan akan hilang.", () -> callback.onRequestExit()));

        menuBox.getChildren().addAll(resumeBtn, saveBtn, loadBtn, settingsBtn, mainMenuBtn, quitBtn);

        StackPane overlay = new StackPane(menuBox);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        overlay.setOnMouseClicked(e -> e.consume());
        return overlay;
    }

    private void toggleHistoryOverlay() {
        if (isHistoryVisible) {
            rootPane.getChildren().remove(historyOverlay);
            isHistoryVisible = false;
        } else {
            if (historyOverlay == null) {
                historyOverlay = createHistoryOverlay();
            }
            VBox logBox = (VBox) ((ScrollPane) historyOverlay.getChildren().get(1)).getContent();
            logBox.getChildren().clear();
            for (String entry : historySystem.getHistory()) {
                Label l = new Label(entry);
                l.setFont(Font.font(MAIN_FONT, 18));
                l.setTextFill(Color.LIGHTGRAY);
                l.setWrapText(true);
                logBox.getChildren().add(l);
            }
            rootPane.getChildren().add(historyOverlay);
            isHistoryVisible = true;
        }
    }

    private VBox createHistoryOverlay() {
        VBox overlay = new VBox(10);
        Style.styleHistoryOverlay(overlay);
        overlay.setOnMouseClicked(e -> e.consume());

        Label title = new Label("Riwayat Dialog");
        title.setFont(Font.font(MAIN_FONT_BOLD, FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        ScrollPane scroll = new ScrollPane();
        scroll.setContent(new VBox(8));
        scroll.setFitToWidth(true);

        Button closeBtn = new Button("Tutup");
        Style.styleMainMenuButton(closeBtn);
        closeBtn.setOnAction(e -> toggleHistoryOverlay());

        overlay.getChildren().addAll(title, scroll, closeBtn);
        return overlay;
    }

    private void showConfirmDialog(String titleText, String message, Runnable onConfirm) {
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));

        Label title = new Label(titleText);
        title.setFont(Font.font(MAIN_FONT_BOLD, FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Label msg = new Label(message);
        msg.setFont(Font.font(MAIN_FONT, 20));
        msg.setTextFill(Color.LIGHTGRAY);

        Button cancelBtn = new Button("Batal");
        Button confirmBtn = new Button("Ya, Lanjutkan");
        Style.styleMainMenuButton(cancelBtn);
        Style.styleMainMenuButton(confirmBtn);

        HBox buttonContainer = new HBox(20, cancelBtn, confirmBtn);
        buttonContainer.setAlignment(Pos.CENTER);
        box.getChildren().addAll(title, msg, buttonContainer);

        StackPane confirmOverlay = new StackPane(box);
        confirmOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.9);");

        cancelBtn.setOnAction(e -> rootPane.getChildren().remove(confirmOverlay));
        confirmBtn.setOnAction(e -> {
            rootPane.getChildren().remove(confirmOverlay);
            onConfirm.run();
        });

        rootPane.getChildren().add(confirmOverlay);
    }

    // Callback interface untuk komunikasi ke Main.java
    public interface GameUICallback {

        void onRequestMenu();

        void onRequestSave(boolean fromInGameMenu);

        void onRequestLoad(boolean fromInGameMenu);

        void onRequestSettings();

        void onRequestCredits();

        void onRequestExit();
    }

    // Tambahan: Sinkronisasi dari SettingsSystem
    public void setTextSpeedMode(int mode) {
        this.textSpeedMode = mode;
        textSpeedButton.setText(TEXT_SPEED_LABELS[mode]);
    }

    public int getTextSpeedMode() {
        return this.textSpeedMode;
    }

    public void updateMuteIcon() {
        muteButton.setText(AudioSystem.getInstance().isMuted() ? "\uD83D\uDD07" : "\uD83D\uDD0A");
    }
}

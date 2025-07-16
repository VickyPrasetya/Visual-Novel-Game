package game.ui;

import game.manager.GameManager;
import game.model.SceneData;
import game.model.DialogNode;
import game.model.ChoiceData;
import game.system.HistorySystem;
import game.system.TransitionSystem;
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

/**
 * GameUIScreen: Modular class for all dialog/gameplay UI logic.
 */
public class GameUIScreen {
    private final GameManager gameManager;
    private final HistorySystem historySystem;
    private final TransitionSystem transitionSystem;
    private final GameUICallback callback;

    // UI fields
    private final StackPane rootPane;
    private final ImageView backgroundView;
    private final HBox characterContainer;
    private final ImageView leftCharacterView;
    private final ImageView rightCharacterView;
    private final VBox dialogueUIGroup;
    private final StackPane nameBox;
    private final Text nameLabel;
    private final Text dialogueLabel;
    private final VBox choicesBox;
    private final Label nextIndicator;
    private final VBox centeredChoicesContainer;
    private final StackPane letterContainer;
    private final Button undoButton;
    private final Button textSpeedButton;
    private final Button skipButton;
    private final HBox dialogBoxButtonBar;
    private VBox historyOverlay;
    private boolean isHistoryVisible = false;

    // Typewriter effect
    private Timeline typewriterTimeline;
    private String fullDialogText = "";
    private boolean isTypewriterRunning = false;
    private int textSpeedMode = 1;
    private static final String[] TEXT_SPEED_LABELS = {"🐢", "⏩", "⚡"};

    // Font constants
    private static final String MAIN_FONT = "Segoe UI";
    private static final String MAIN_FONT_BOLD = "Segoe UI Semibold";
    private static final int DIALOG_FONT_SIZE = 25;
    private static final int NAME_FONT_SIZE = 22;
    private static final int LETTER_FONT_SIZE = 22;
    private static final String CIRCLE_BUTTON_STYLE = "-fx-font-size: 22px; -fx-background-radius: 25; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 0; -fx-border-color: white; -fx-border-width: 2px;";

    private final Button menuButton;
    private final Button muteButton;
    private final HBox topLeftButtonBar;
    private StackPane inGameMenuOverlay;
    private boolean isInGameMenuVisible = false;

    public GameUIScreen(GameManager gameManager, HistorySystem historySystem, TransitionSystem transitionSystem, GameUICallback callback) {
        this.gameManager = gameManager;
        this.historySystem = historySystem;
        this.transitionSystem = transitionSystem;
        this.callback = callback;
        // Build UI
        rootPane = new StackPane();
        backgroundView = new ImageView();
        backgroundView.setPreserveRatio(false);
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> backgroundView.setFitWidth(newVal.doubleValue()));
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> backgroundView.setFitHeight(newVal.doubleValue()));
        leftCharacterView = new ImageView();
        leftCharacterView.setPreserveRatio(true);
        leftCharacterView.setFitHeight(650);
        rightCharacterView = new ImageView();
        rightCharacterView.setPreserveRatio(true);
        rightCharacterView.setFitHeight(650);
        characterContainer = new HBox(leftCharacterView, rightCharacterView);
        characterContainer.setAlignment(Pos.BOTTOM_CENTER);
        characterContainer.setSpacing(100);
        characterContainer.setPadding(new Insets(0, 0, 20, 0));
        characterContainer.setPickOnBounds(false);
        // Dialogue UI
        dialogueUIGroup = new VBox();
        dialogueUIGroup.setAlignment(Pos.BOTTOM_CENTER);
        dialogueUIGroup.setPadding(new Insets(80, 30, 30, 30));
        dialogueUIGroup.setPickOnBounds(true);
        dialogueUIGroup.maxWidthProperty().bind(rootPane.widthProperty().subtract(80));
        dialogueUIGroup.prefWidthProperty().bind(rootPane.widthProperty().subtract(80));
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> dialogueUIGroup.setPrefHeight(newVal.doubleValue() * 0.35));
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
        HBox nameBoxWrapper = new HBox(nameBox);
        nameBoxWrapper.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(nameBox, new Insets(0, 0, -5, 40));
        nameBoxWrapper.setPickOnBounds(false);
        nameBox.setVisible(false);
        StackPane dialogueSystemStack = new StackPane();
        VBox dialogueContainer = new VBox(15);
        dialogueContainer.setPadding(new Insets(20, 40, 60, 40));
        dialogueContainer.setStyle("-fx-background-color: rgba(255, 183, 197, 0.9); -fx-border-radius:10; -fx-border-color: white; -fx-border-width: 1; -fx-background-radius: 10;");
        dialogueContainer.setMinHeight(180);
        dialogueContainer.setPickOnBounds(true);
        dialogueContainer.maxWidthProperty().bind(rootPane.widthProperty().subtract(120));
        dialogueContainer.prefWidthProperty().bind(rootPane.widthProperty().subtract(120));
        dialogueLabel = new Text();
        dialogueLabel.setFont(Font.font(MAIN_FONT, FontWeight.BOLD, DIALOG_FONT_SIZE));
        dialogueLabel.setFill(Color.WHITE);
        dialogueLabel.setStroke(Color.BLACK);
        dialogueLabel.setStrokeWidth(0.5);
        TextFlow dialogueFlow = new TextFlow(dialogueLabel);
        dialogueFlow.setMaxWidth(900);
        dialogueFlow.setLineSpacing(5);
        choicesBox = new VBox(10);
        choicesBox.setAlignment(Pos.CENTER);
        dialogueContainer.getChildren().addAll(dialogueFlow, choicesBox);
        nextIndicator = new Label("▼");
        nextIndicator.setFont(Font.font(MAIN_FONT, 24));
        nextIndicator.setTextFill(Color.web("#4e342e"));
        dialogueSystemStack.getChildren().addAll(dialogueContainer, nextIndicator);
        StackPane.setAlignment(nextIndicator, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(nextIndicator, new Insets(0, 25, 10, 0));
        dialogueUIGroup.getChildren().addAll(nameBoxWrapper, dialogueSystemStack);
        // dialogueUIGroup.setMaxWidth(1000);
        // dialogueUIGroup.setPrefWidth(1000);
        StackPane.setAlignment(dialogueUIGroup, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueUIGroup, new Insets(0, 30, 30, 30));
        // Choices & Letter
        centeredChoicesContainer = new VBox(15);
        centeredChoicesContainer.setAlignment(Pos.CENTER);
        centeredChoicesContainer.setStyle("-fx-background-color: rgba(255, 0, 0, 0.3);");
        centeredChoicesContainer.setVisible(false);
        centeredChoicesContainer.setOnMouseClicked(event -> event.consume());
        StackPane.setAlignment(centeredChoicesContainer, Pos.CENTER);
        centeredChoicesContainer.setPadding(new Insets(20));
        letterContainer = new StackPane();
        letterContainer.setStyle("-fx-background-color: rgba(0, 255, 0, 0.3);");
        letterContainer.setVisible(false);
        StackPane.setAlignment(letterContainer, Pos.CENTER);
        StackPane.setMargin(letterContainer, new Insets(20));
        // Undo Button
        undoButton = new Button("⟲ Undo");
        undoButton.setOpacity(0.7);
        undoButton.setStyle("-fx-font-size: 18px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 4 10;");
        undoButton.setOnAction(e -> {
            if (gameManager != null && gameManager.undoDialog()) {
                updateUI();
            }
        });
        VBox.setMargin(undoButton, new Insets(10, 0, 0, 10));
        undoButton.setAlignment(Pos.BOTTOM_LEFT);
        // Text Speed Button
        textSpeedButton = new Button(TEXT_SPEED_LABELS[textSpeedMode]);
        textSpeedButton.setOnAction(e -> {
            textSpeedMode = (textSpeedMode + 1) % 3;
            textSpeedButton.setText(TEXT_SPEED_LABELS[textSpeedMode]);
        });
        // Skip Button
        skipButton = new Button("Skip");
        skipButton.setOnAction(e -> {
            if (gameManager != null) {
                while (true) {
                    DialogNode currentDialog = gameManager.getCurrentDialog();
                    if (currentDialog == null) break;
                    if (currentDialog.choices != null && !currentDialog.choices.isEmpty()) break;
                    boolean moved = gameManager.nextDialog();
                    if (!moved) break;
                }
                updateUI();
            }
        });
        Button historyButton = new Button("\uD83D\uDCDC History");
        historyButton.setTooltip(new Tooltip("Lihat Riwayat Dialog (H)"));
        Style.styleMainMenuButton(historyButton);
        historyButton.setOnAction(e -> toggleHistoryOverlay());
        dialogBoxButtonBar = new HBox(8, historyButton, undoButton, textSpeedButton, skipButton);
        dialogBoxButtonBar.setAlignment(Pos.BOTTOM_RIGHT);
        dialogBoxButtonBar.setPickOnBounds(false);
        dialogBoxButtonBar.setMouseTransparent(false);
        dialogBoxButtonBar.setStyle("-fx-background-color: transparent;");
        dialogueSystemStack.getChildren().add(dialogBoxButtonBar);
        StackPane.setAlignment(dialogBoxButtonBar, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(dialogBoxButtonBar, new Insets(0, 80, 10, 0));
        // Menu (hamburger) button
        menuButton = new Button("\u2630");
        menuButton.setTooltip(new Tooltip("Menu"));
        Style.styleMainMenuButton(menuButton);
        menuButton.setPrefSize(50, 50);
        menuButton.setOnAction(e -> toggleInGameMenuOverlay());
        // Mute button
        muteButton = new Button(AudioSystem.getInstance().isMuted() ? "\uD83D\uDD07" : "\uD83D\uDD0A");
        muteButton.setTooltip(new Tooltip("Mute/Unmute Music"));
        Style.styleMainMenuButton(muteButton);
        muteButton.setPrefSize(50, 50);
        muteButton.setOnAction(e -> {
            boolean newMute = !AudioSystem.getInstance().isMuted();
            AudioSystem.getInstance().setMute(newMute);
            muteButton.setText(newMute ? "\uD83D\uDD07" : "\uD83D\uDD0A");
        });
        // Top left bar
        topLeftButtonBar = new HBox(10, menuButton, muteButton);
        topLeftButtonBar.setAlignment(Pos.TOP_LEFT);
        topLeftButtonBar.setPadding(new Insets(20, 0, 0, 20));
        topLeftButtonBar.setMaxWidth(120);
        topLeftButtonBar.setPrefWidth(120);
        topLeftButtonBar.setMouseTransparent(false);
        StackPane.setAlignment(topLeftButtonBar, Pos.TOP_LEFT);
        StackPane.setMargin(topLeftButtonBar, new Insets(20, 0, 0, 20));
        topLeftButtonBar.setStyle("");
        topLeftButtonBar.maxWidthProperty().set(120);
        topLeftButtonBar.prefWidthProperty().set(120);
        // DEBUG: Add semi-transparent green background to dialogueUIGroup
        dialogueUIGroup.setStyle("-fx-background-color: rgba(0,255,0,0.1);"); // DEBUG: semi-transparent green
        dialogueUIGroup.setMouseTransparent(false);
        // Paksa font icon agar Unicode muncul
        menuButton.setFont(Font.font("Segoe UI Symbol", 28));
        muteButton.setFont(Font.font("Segoe UI Emoji", 28));
        // Root pane
        rootPane.getChildren().addAll(backgroundView, characterContainer, dialogueUIGroup, centeredChoicesContainer, letterContainer, topLeftButtonBar);
        // Initial UI update
        updateUI();
        // Tambahkan handler keyboard di rootPane:
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.H) {
                toggleHistoryOverlay();
            }
        });
        // Style all bottom bar buttons consistently
        Style.styleBottomBarButton(undoButton);
        Style.styleBottomBarButton(textSpeedButton);
        Style.styleBottomBarButton(skipButton);
        Style.styleBottomBarButton(historyButton);
    }

    public StackPane getGamePane() {
        return rootPane;
    }

    public void updateUI() {
        if (gameManager == null) return;
        SceneData currentScene = gameManager.getCurrentScene();
        if (currentScene == null) return;
        updateImage(backgroundView, currentScene.backgroundImage);
        leftCharacterView.setImage(null);
        rightCharacterView.setImage(null);
        characterContainer.getChildren().clear();
        if (currentScene.characters != null && !currentScene.characters.isEmpty()) {
            boolean hasLeft = false;
            boolean hasRight = false;
            for (var character : currentScene.characters) {
                if ("left".equalsIgnoreCase(character.position)) {
                    updateImage(leftCharacterView, character.sprite);
                    hasLeft = true;
                } else {
                    updateImage(rightCharacterView, character.sprite);
                    hasRight = true;
                }
            }
            if (hasLeft) characterContainer.getChildren().add(leftCharacterView);
            if (hasRight) characterContainer.getChildren().add(rightCharacterView);
        }
        if ("letter".equals(currentScene.type)) {
            showLetter(currentScene);
            undoButton.setDisable(true);
            undoButton.setVisible(false);
            dialogBoxButtonBar.setVisible(false);
            return;
        }
        DialogNode currentDialog = gameManager.getCurrentDialog();
        if (currentDialog != null) {
            if (currentDialog.choices != null && !currentDialog.choices.isEmpty()) {
                showChoices(currentScene, currentDialog);
                dialogBoxButtonBar.setVisible(false);
            } else {
                showNormalDialogue(currentScene, currentDialog);
                dialogBoxButtonBar.setVisible(true);
            }
        }
        boolean canUndo = gameManager.canUndoDialog();
        undoButton.setDisable(!canUndo);
        undoButton.setVisible(canUndo && dialogBoxButtonBar.isVisible());
    }

    private void showNormalDialogue(SceneData currentScene, DialogNode currentDialog) {
        if (typewriterTimeline != null) typewriterTimeline.stop();
        fullDialogText = currentDialog.text;
        dialogueLabel.setText("");
        isTypewriterRunning = true;
        int speed = switch (textSpeedMode) {
            case 0 -> 60;
            case 2 -> 10;
            default -> 30;
        };
        typewriterTimeline = new Timeline();
        for (int i = 0; i <= fullDialogText.length(); i++) {
            final int idx = i;
            typewriterTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * speed), e -> {
                dialogueLabel.setText(fullDialogText.substring(0, idx));
                if (idx == fullDialogText.length()) isTypewriterRunning = false;
            }));
        }
        typewriterTimeline.play();
        if (currentDialog.character != null && !currentDialog.character.isEmpty()) {
            nameLabel.setText(currentDialog.character);
            nameBox.setVisible(true);
        }
        nextIndicator.setVisible(true);
        dialogueUIGroup.setCursor(Cursor.HAND);
        String historyEntry = (currentDialog.character != null ? currentDialog.character + ": " : "") + currentDialog.text;
        historySystem.addHistory(historyEntry);
        dialogueUIGroup.setOnMouseClicked(event -> {
            if (isTypewriterRunning) {
                if (typewriterTimeline != null) typewriterTimeline.stop();
                dialogueLabel.setText(fullDialogText);
                isTypewriterRunning = false;
                return;
            }
            if (event.getButton() == MouseButton.PRIMARY) {
                boolean movedToNextDialog = gameManager.nextDialog();
                if (!movedToNextDialog) {
                    String nextSceneId = (currentDialog.next != null) ? currentDialog.next : currentScene.nextScene;
                    if (nextSceneId != null) {
                        historySystem.addHistory("[Pindah ke scene: " + nextSceneId + "]");
                        transitionSystem.fadeTransition(backgroundView, 400);
                    }
                    gameManager.goToScene(nextSceneId);
                }
                updateUI();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if (gameManager.undoDialog()) updateUI();
            }
        });
        centeredChoicesContainer.setVisible(false);
        dialogueUIGroup.setVisible(true);
        letterContainer.setVisible(false);
    }

    private void showChoices(SceneData currentScene, DialogNode currentDialog) {
        dialogueUIGroup.setVisible(false);
        centeredChoicesContainer.setVisible(true);
        centeredChoicesContainer.getChildren().clear();
        undoButton.setDisable(true);
        undoButton.setVisible(false);
        for (ChoiceData choice : currentDialog.choices) {
            Button choiceButton = new Button(choice.text);
            Style.styleChoiceButtonLarge(choiceButton);
            choiceButton.setOnAction(_ -> {
                System.out.println("Choice clicked: " + choice.text + " -> " + choice.nextScene);
                if (choice.nextScene == null || choice.nextScene.isEmpty()) {
                    System.err.println("ERROR: nextScene untuk choice '" + choice.text + "' null atau kosong!");
                    return;
                }
                if (choice.nextScene.equals("main_menu")) {
                    callback.onRequestMenu();
                    return;
                }
                if (choice.nextScene.equals("exit_game")) {
                    callback.onRequestExit();
                    return;
                }
                historySystem.addHistory("[Pilihan] " + choice.text);
                transitionSystem.fadeTransition(backgroundView, 400);
                gameManager.goToScene(choice.nextScene);
                undoButton.setVisible(false);
                updateUI();
            });
            centeredChoicesContainer.getChildren().add(choiceButton);
        }
        letterContainer.setVisible(false);
    }

    private void showLetter(SceneData currentScene) {
        dialogueUIGroup.setVisible(false);
        letterContainer.setVisible(true);
        letterContainer.getChildren().clear();
        StackPane paper = new StackPane();
        paper.setStyle("-fx-background-color: #faf0e6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);");
        paper.maxWidthProperty().bind(rootPane.widthProperty().subtract(200));
        DialogNode letterDialog = gameManager.getCurrentDialog();
        if (letterDialog == null) return;
        Label letterTextLabel = new Label(letterDialog.text);
        letterTextLabel.setWrapText(true);
        letterTextLabel.setFont(Font.font("Courier New", LETTER_FONT_SIZE));
        letterTextLabel.setTextFill(Color.BLACK);
        letterTextLabel.setPadding(new Insets(50));
        ScrollPane scrollPane = new ScrollPane(letterTextLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        paper.getChildren().add(scrollPane);
        letterContainer.getChildren().add(paper);

        // --- LOGIKA: klik aktif jika scroll sudah di bawah ATAU tidak perlu scroll ---
        Runnable enableClick = () -> {
            letterContainer.setCursor(Cursor.HAND);
            letterContainer.setOnMouseClicked(e -> {
                if (currentScene.nextScene == null || currentScene.nextScene.isEmpty()) {
                    System.out.println("Tamat! Tidak ada scene berikutnya.");
                    return;
                }
                gameManager.goToScene(currentScene.nextScene);
                letterContainer.setVisible(false);
                updateUI();
            });
        };

        Runnable disableClick = () -> {
            letterContainer.setCursor(Cursor.DEFAULT);
            letterContainer.setOnMouseClicked(null);
        };

        // Cek apakah scrollPane perlu discroll
        scrollPane.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> {
            double contentHeight = letterTextLabel.getHeight();
            double viewportHeight = newVal.getHeight();
            if (contentHeight <= viewportHeight + 1) {
                // Tidak perlu scroll, klik langsung aktif
                enableClick.run();
            } else {
                // Perlu scroll, klik aktif hanya jika sudah di bawah
                disableClick.run();
                scrollPane.vvalueProperty().addListener((o, ov, nv) -> {
                    if (nv.doubleValue() >= 0.99) {
                        enableClick.run();
                    } else {
                        disableClick.run();
                    }
                });
            }
        });
    }

    private void updateImage(ImageView view, String imagePath) {
        updateImage(view, imagePath, "assets/bg/black.jpg");
    }
    private void updateImage(ImageView view, String imagePath, String fallbackPath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    view.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    File fallbackFile = new File(fallbackPath);
                    if (fallbackFile.exists()) {
                        view.setImage(new Image(fallbackFile.toURI().toString()));
                    } else {
                        view.setImage(null);
                    }
                }
            } catch (Exception e) {
                view.setImage(null);
            }
        } else {
            File fallbackFile = new File(fallbackPath);
            if (fallbackFile.exists()) {
                view.setImage(new Image(fallbackFile.toURI().toString()));
            } else {
                view.setImage(null);
            }
        }
    }

    private void toggleHistoryOverlay() {
        if (isHistoryVisible) {
            rootPane.getChildren().remove(historyOverlay);
            isHistoryVisible = false;
        } else {
            showHistoryOverlay();
        }
    }

    private void showHistoryOverlay() {
        if (historyOverlay == null) {
            historyOverlay = new VBox(10);
            Style.styleHistoryOverlay(historyOverlay);
            historyOverlay.setPadding(new Insets(30));
            historyOverlay.maxWidthProperty().bind(rootPane.widthProperty().subtract(200));
            historyOverlay.setMaxHeight(500);
            historyOverlay.setAlignment(Pos.TOP_LEFT);
        }
        historyOverlay.getChildren().clear();
        Label title = new Label("Riwayat Dialog");
        title.setFont(Font.font(Style.MAIN_FONT_BOLD, FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        Button closeBtn = new Button("Tutup");
        Style.styleMainMenuButton(closeBtn);
        closeBtn.setOnAction(e -> toggleHistoryOverlay());
        ScrollPane scroll = new ScrollPane();
        VBox logBox = new VBox(8);
        logBox.setPadding(new Insets(10));
        for (String entry : historySystem.getHistory()) {
            Label l = new Label(entry);
            l.setFont(Font.font(Style.MAIN_FONT, 18));
            l.setTextFill(Color.LIGHTGRAY);
            logBox.getChildren().add(l);
        }
        scroll.setContent(logBox);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(400);
        historyOverlay.getChildren().addAll(title, scroll, closeBtn);
        rootPane.getChildren().add(historyOverlay);
        isHistoryVisible = true;
    }

    private void toggleInGameMenuOverlay() {
        if (isInGameMenuVisible) {
            rootPane.getChildren().remove(inGameMenuOverlay);
            isInGameMenuVisible = false;
        } else {
            showInGameMenuOverlay();
        }
    }

    public void showInGameMenuOverlay() {
        // Only show overlay if not already visible
        if (isInGameMenuVisible) return;
        if (inGameMenuOverlay == null) {
            inGameMenuOverlay = new StackPane();
            inGameMenuOverlay.setStyle("-fx-background-color: rgba(255,0,0,0.3); -fx-background-radius: 18; -fx-border-color: #fff; -fx-border-width: 2px; -fx-border-radius: 18;"); // DEBUG: semi-transparent red
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
            saveBtn.setOnAction(e -> { hideInGameMenuOverlay(); callback.onRequestSave(true); });
            loadBtn.setOnAction(e -> { hideInGameMenuOverlay(); callback.onRequestLoad(true); });
            settingsBtn.setOnAction(e -> { hideInGameMenuOverlay(); callback.onRequestSettings(); });
            mainMenuBtn.setOnAction(e -> showConfirmDialog(
                "Kembali ke Main Menu?",
                "Apakah anda yakin ingin kembali ke Main Menu? Progres anda tidak akan tersimpan kecuali anda sudah menyimpannya.",
                () -> { hideInGameMenuOverlay(); callback.onRequestMenu(); }
            ));
            quitBtn.setOnAction(e -> showConfirmDialog(
                "Keluar dari Game?",
                "Apakah anda yakin ingin keluar? Progres anda tidak akan tersimpan kecuali anda sudah menyimpannya.",
                () -> { System.exit(0); }
            ));
            menuBox.getChildren().addAll(resumeBtn, saveBtn, loadBtn, settingsBtn, mainMenuBtn, quitBtn);
            inGameMenuOverlay.getChildren().add(menuBox);
        }
        if (!rootPane.getChildren().contains(inGameMenuOverlay)) {
            rootPane.getChildren().add(inGameMenuOverlay);
        }
        isInGameMenuVisible = true;
    }

    private void hideInGameMenuOverlay() {
        if (isInGameMenuVisible && inGameMenuOverlay != null) {
            rootPane.getChildren().remove(inGameMenuOverlay);
            isInGameMenuVisible = false;
        }
    }

    private void showConfirmDialog(String titleText, String message, Runnable onConfirm) {
        StackPane confirmOverlay = new StackPane();
        confirmOverlay.setStyle("-fx-background-color: rgba(30,30,30,0.97); -fx-background-radius: 18; -fx-border-color: #fff; -fx-border-width: 2px; -fx-border-radius: 18;");
        VBox box = new VBox(20);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(40));
        Label title = new Label(titleText);
        title.setFont(Font.font(Style.MAIN_FONT_BOLD, FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);
        Label msg = new Label(message);
        msg.setFont(Font.font(Style.MAIN_FONT, 20));
        msg.setTextFill(Color.LIGHTGRAY);
        Button cancelBtn = new Button("Batal");
        Button confirmBtn = new Button("Keluar");
        Style.styleMainMenuButton(cancelBtn);
        Style.styleMainMenuButton(confirmBtn);
        cancelBtn.setOnAction(e -> rootPane.getChildren().remove(confirmOverlay));
        confirmBtn.setOnAction(e -> { rootPane.getChildren().remove(confirmOverlay); onConfirm.run(); });
        box.getChildren().addAll(title, msg, new HBox(20, cancelBtn, confirmBtn));
        confirmOverlay.getChildren().add(box);
        StackPane.setAlignment(confirmOverlay, Pos.CENTER);
        StackPane.setAlignment(box, Pos.CENTER);
        rootPane.getChildren().add(confirmOverlay);
    }

    // Callback interface for navigation/events
    public interface GameUICallback {
        void onRequestMenu();
        void onRequestSave(boolean fromInGameMenu);
        void onRequestLoad(boolean fromInGameMenu);
        void onRequestSettings();
        void onRequestCredits();
        void onRequestExit();
    }
} 
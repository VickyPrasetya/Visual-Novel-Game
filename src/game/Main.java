package game;

import game.manager.SaveLoadService;
import game.manager.gameManager;
import game.model.CharacterData;
import game.model.DialogNode;
import game.model.GameState;
import game.model.choiceData;
import game.model.sceneData;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class Main extends Application {

    // --- DEKLARASI VARIABEL INSTANCE ---
    private Stage primaryStage;
    private StackPane rootLayout;
    private MediaPlayer mediaPlayer;
    private String currentMusicPath = null;
    private boolean isMusicOn = true;

    // --- LAYANAN & MODE ---
    private SaveLoadService saveLoadService;
    private enum SaveLoadMode { SAVE, LOAD }

    // --- Kontainer untuk setiap layar ---
    private StackPane mainMenuContainer;
    private StackPane gameContainer;
    private StackPane settingsContainer;
    private StackPane saveLoadContainer;

    // --- Variabel untuk Layar Game ---
    private gameManager gameManager;
    private ImageView backgroundView;
    private ImageView leftCharacterView;
    private ImageView rightCharacterView;
    private HBox characterContainer;
    private Button undoButton;
    private VBox dialogueUIGroup;
    private StackPane nameBox;
    private Text nameLabel;
    private Text dialogueLabel;
    private VBox choicesBox;
    private Label nextIndicator;
    private VBox centeredChoicesContainer;
    private StackPane letterContainer;
    private VBox inGameMenu;
    private boolean justUndone = false;
    private Button musicToggleButton;
    private VBox dialogueContainer;

    private static final String DEFAULT_MUSIC_PATH = "assets/music/Music Page Menu.mp3";
    private static final String MENU_BACKGROUND_PATH = "assets/bg/TamanBelakang/Park.jpg";

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Visual Novel - 24 Hours");

        saveLoadService = new SaveLoadService();
        rootLayout = new StackPane();

        mainMenuContainer = createMainMenu();
        gameContainer = createGameUI();
        settingsContainer = createSettingsUI();
        saveLoadContainer = createSaveLoadUI();

        rootLayout.getChildren().addAll(gameContainer, settingsContainer, saveLoadContainer, mainMenuContainer);
        showScreen(mainMenuContainer);

        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
        playMusic(DEFAULT_MUSIC_PATH);
    }

    private void showScreen(Pane screen) {
        mainMenuContainer.setVisible(screen == mainMenuContainer);
        gameContainer.setVisible(screen == gameContainer);
        settingsContainer.setVisible(screen == settingsContainer);
        saveLoadContainer.setVisible(screen == saveLoadContainer);
    }

    private StackPane createMainMenu() {
        StackPane menuLayout = new StackPane();
        ImageView menuBg = createBlurredBackground(MENU_BACKGROUND_PATH);
        Text gameTitle = new Text("24 Hours");
        gameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 90));
        gameTitle.setFill(Color.WHITE);
        gameTitle.setStroke(Color.BLACK);
        gameTitle.setStrokeWidth(2);
        Button startButton = new Button("Start");
        Button loadButton = new Button("Load Game");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Quit");
        styleMainMenuButton(startButton);
        styleMainMenuButton(loadButton);
        styleMainMenuButton(settingsButton);
        styleMainMenuButton(exitButton);
        startButton.setOnAction(e -> {
            gameManager = new gameManager();
            if (gameManager.getCurrentScene() != null) {
                showScreen(gameContainer);
                updateUI();
            } else {
                System.err.println("Gagal memulai game, root scene tidak ditemukan!");
            }
        });
        loadButton.setOnAction(e -> openSaveLoadScreen(SaveLoadMode.LOAD));
        settingsButton.setOnAction(e -> showScreen(settingsContainer));
        exitButton.setOnAction(e -> primaryStage.close());
        VBox menuButtons = new VBox(20, startButton, loadButton, settingsButton, exitButton);
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setMaxWidth(350);
        VBox titleAndButtons = new VBox(50, gameTitle, menuButtons);
        titleAndButtons.setAlignment(Pos.CENTER);
        menuLayout.getChildren().addAll(menuBg, titleAndButtons);
        return menuLayout;
    }

    private StackPane createGameUI() {
        StackPane layout = new StackPane();
        StackPane imageContainer = new StackPane();
        backgroundView = new ImageView();
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());
        backgroundView.setPreserveRatio(false);
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
        imageContainer.getChildren().addAll(backgroundView, characterContainer);
        dialogueUIGroup = new VBox();
        dialogueUIGroup.setAlignment(Pos.BOTTOM_CENTER);
        dialogueUIGroup.setPadding(new Insets(0, 20, 20, 20));
        dialogueUIGroup.setPickOnBounds(false);
        nameLabel = new Text();
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
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
        dialogueContainer = new VBox(15);
        dialogueContainer.setPadding(new Insets(20));
        dialogueContainer.setStyle("-fx-background-color: rgba(255, 183, 197, 0.9); -fx-border-radius:10; -fx-border-color: white; -fx-border-width: 1; -fx-background-radius: 10;");
        dialogueContainer.setMinHeight(180);
        dialogueLabel = new Text();
        dialogueLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 25));
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
        nextIndicator.setFont(Font.font("Arial", 24));
        nextIndicator.setTextFill(Color.web("#ffffff"));
        dialogueSystemStack.getChildren().addAll(dialogueContainer, nextIndicator);
        StackPane.setAlignment(nextIndicator, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(nextIndicator, new Insets(0, 25, 10, 0));
        dialogueUIGroup.getChildren().addAll(nameBoxWrapper, dialogueSystemStack);
        centeredChoicesContainer = new VBox(15);
        centeredChoicesContainer.setAlignment(Pos.CENTER);
        centeredChoicesContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
        centeredChoicesContainer.setVisible(false);
        centeredChoicesContainer.setOnMouseClicked(event -> event.consume());
        letterContainer = new StackPane();
        letterContainer.setVisible(false);
        undoButton = new Button("⟲ Undo");
        undoButton.setOpacity(0.7);
        undoButton.setStyle("-fx-font-size: 18px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 4 10;");
        undoButton.setTooltip(new Tooltip("Undo (Klik Kanan)"));
        undoButton.setOnAction(e -> {
            if (gameManager != null && gameManager.undoDialog()) {
                justUndone = true;
                updateUI();
            }
        });
        musicToggleButton = new Button("\uD83D\uDD0A");
        musicToggleButton.setStyle("-fx-font-size: 22px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 4 10;");
        musicToggleButton.setOnAction(e -> toggleMusic());
        musicToggleButton.setTooltip(new Tooltip("Toggle Music"));
        Button hamburgerButton = new Button("\u2630");
        hamburgerButton.setStyle("-fx-font-size: 22px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 4 10;");
        hamburgerButton.setTooltip(new Tooltip("Menu"));
        hamburgerButton.setOnAction(e -> {
            if (inGameMenu != null) {
                inGameMenu.setVisible(!inGameMenu.isVisible());
            }
        });
        HBox topLeftButtons = new HBox(8, hamburgerButton, musicToggleButton, undoButton);
        topLeftButtons.setAlignment(Pos.TOP_LEFT);
        topLeftButtons.setPadding(new Insets(10, 0, 0, 10));
        topLeftButtons.setPickOnBounds(false);
        layout.getChildren().addAll(imageContainer, dialogueUIGroup, centeredChoicesContainer, letterContainer, topLeftButtons);
        initializeInGameMenu(layout);
        return layout;
    }

    private StackPane createSettingsUI() {
        StackPane settingsLayout = new StackPane();
        ImageView settingsBg = createBlurredBackground(MENU_BACKGROUND_PATH);
        Label title = new Label("Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        title.setTextFill(Color.WHITE);
        Label placeholder = new Label("Settings options (volume, resolution, etc.) will be here.");
        placeholder.setFont(Font.font("Arial", 20));
        placeholder.setTextFill(Color.WHITE);
        Button backButton = new Button("Back");
        styleMainMenuButton(backButton);
        backButton.setOnAction(e -> showScreen(mainMenuContainer));
        VBox content = new VBox(30, title, placeholder, backButton);
        content.setAlignment(Pos.CENTER);
        settingsLayout.getChildren().addAll(settingsBg, content);
        return settingsLayout;
    }

    private StackPane createSaveLoadUI() {
        StackPane layout = new StackPane();
        ImageView bg = createBlurredBackground(MENU_BACKGROUND_PATH);

        Label titleLabel = new Label();
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        titleLabel.setTextFill(Color.WHITE);

        GridPane slotsGrid = new GridPane();
        slotsGrid.setHgap(20);
        slotsGrid.setVgap(20);
        slotsGrid.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back");
        styleSubMenuButton(backButton);
        backButton.setPrefWidth(250);
        backButton.setOnAction(e -> {
            if (gameContainer.isVisible() && !mainMenuContainer.isVisible()) {
                showScreen(gameContainer);
                if(inGameMenu != null) inGameMenu.setVisible(true);
            } else {
                showScreen(mainMenuContainer);
            }
        });
        
        VBox content = new VBox(30, titleLabel, slotsGrid, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        VBox.setMargin(backButton, new Insets(20, 0, 0, 0));

        layout.getChildren().addAll(bg, content);
        return layout;
    }

    private void openSaveLoadScreen(SaveLoadMode mode) {
        if(inGameMenu != null) inGameMenu.setVisible(false);

        VBox content = (VBox) saveLoadContainer.getChildren().get(1);
        Label titleLabel = (Label) content.getChildren().get(0);
        GridPane slotsGrid = (GridPane) content.getChildren().get(1);

        titleLabel.setText(mode == SaveLoadMode.SAVE ? "Save Game" : "Load Game");
        slotsGrid.getChildren().clear();

        for (int i = 1; i <= 6; i++) {
            final int slotNumber = i;
            GameState state = saveLoadService.loadGame(slotNumber);

            VBox slotBox = new VBox(-5);
            slotBox.setAlignment(Pos.CENTER_LEFT);
            slotBox.setPadding(new Insets(15));
            slotBox.setPrefSize(350, 100);
            
            String baseStyle = "-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: white; -fx-border-width: 1px; -fx-background-radius: 5; -fx-border-radius: 5;";
            String hoverStyle = baseStyle + "-fx-background-color: rgba(255, 255, 255, 0.2);";
            slotBox.setStyle(baseStyle);
            
            Label slotLabel = new Label("SLOT " + slotNumber);
            slotLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            slotLabel.setTextFill(Color.WHITE);

            Label dateLabel = new Label(state != null ? state.saveDate : "Kosong");
            dateLabel.setFont(Font.font("Arial", 16));
            dateLabel.setTextFill(state != null ? Color.LIGHTGREEN : Color.GRAY);

            slotBox.getChildren().addAll(slotLabel, dateLabel);

            if(state != null || mode == SaveLoadMode.SAVE) {
                slotBox.setCursor(Cursor.HAND);
                slotBox.setOnMouseEntered(e -> slotBox.setStyle(hoverStyle));
                slotBox.setOnMouseExited(e -> slotBox.setStyle(baseStyle));
                slotBox.setOnMouseClicked(e -> {
                    if (mode == SaveLoadMode.SAVE) {
                        GameState currentState = gameManager.createSaveState();
                        if (currentState != null && saveLoadService.saveGame(slotNumber, currentState)) {
                           openSaveLoadScreen(SaveLoadMode.SAVE);
                        }
                    } else {
                        if (state != null) {
                            if (gameManager == null) gameManager = new gameManager();
                            gameManager.applyGameState(state);
                            showScreen(gameContainer);
                            updateUI();
                        }
                    }
                });
            } else {
                 slotBox.setCursor(Cursor.DEFAULT);
            }
            slotsGrid.add(slotBox, (i - 1) % 2, (i - 1) / 2);
        }
        showScreen(saveLoadContainer);
    }
    
    // --- FIX: METHOD-METHOD YANG HILANG DITAMBAHKAN KEMBALI ---
    
    private void updateUI() {
        if (gameManager == null) return;
        if (inGameMenu != null && inGameMenu.isVisible()) {
            inGameMenu.setVisible(false);
        }
        sceneData currentScene = gameManager.getCurrentScene();
        if (currentScene == null) {
            dialogueUIGroup.setVisible(false);
            Label endLabel = new Label("TAMAT\nTerima kasih telah bermain.");
            endLabel.setFont(Font.font("Arial", 40));
            endLabel.setTextFill(Color.WHITE);
            endLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");
            if (gameContainer.getChildren().stream().noneMatch(node -> node instanceof Label && ((Label)node).getText().startsWith("TAMAT"))) {
                gameContainer.getChildren().add(endLabel);
            }
            playMusic(DEFAULT_MUSIC_PATH);
            return;
        }
        playMusic(currentScene.music);
        dialogueUIGroup.setVisible(true);
        centeredChoicesContainer.setVisible(false);
        letterContainer.setVisible(false);
        choicesBox.getChildren().clear();
        nextIndicator.setVisible(false);
        nameBox.setVisible(false);
        dialogueUIGroup.setOnMouseClicked(null);
        dialogueUIGroup.setCursor(Cursor.DEFAULT);
        updateImage(backgroundView, currentScene.backgroundImage);
        leftCharacterView.setImage(null);
        rightCharacterView.setImage(null);
        characterContainer.getChildren().clear();
        if (currentScene.characters != null && !currentScene.characters.isEmpty()) {
             boolean hasLeft = false;
             boolean hasRight = false;
             for (CharacterData character : currentScene.characters) {
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
            return;
        }
        DialogNode currentDialog = gameManager.getCurrentDialog();
        if (currentDialog != null) {
            if (currentDialog.choices != null && !currentDialog.choices.isEmpty()) {
                showChoices(currentScene, currentDialog);
            } else {
                showNormalDialogue(currentScene, currentDialog);
            }
        } else if (currentScene.nextScene != null){
            gameManager.goToScene(currentScene.nextScene);
            updateUI();
        } else {
             // Jika tidak ada dialog dan tidak ada nextScene, anggap tamat
             updateUI(); // Akan memanggil blok currentScene == null di atas
        }
        undoButton.setDisable(!gameManager.canUndoDialog());
    }

    private void showNormalDialogue(sceneData currentScene, DialogNode currentDialog) {
        dialogueLabel.setText(currentDialog.text);
        if (currentDialog.character != null && !currentDialog.character.isEmpty()) {
            nameLabel.setText(currentDialog.character);
            nameBox.setVisible(true);
        }
        nextIndicator.setVisible(true);
        dialogueUIGroup.setCursor(Cursor.HAND);
        dialogueUIGroup.setOnMouseClicked(event -> {
            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                if (justUndone) {
                    justUndone = false;
                    return;
                }
                boolean movedToNextDialog = gameManager.nextDialog();
                if (!movedToNextDialog) {
                    String nextSceneId = (currentDialog.next != null) ? currentDialog.next : currentScene.nextScene;
                    gameManager.goToScene(nextSceneId);
                }
                updateUI();
            } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                if (gameManager.undoDialog()) {
                    justUndone = true;
                    updateUI();
                }
            }
        });
    }

    private void showChoices(sceneData currentScene, DialogNode currentDialog) {
        dialogueUIGroup.setVisible(false);
        centeredChoicesContainer.setVisible(true);
        centeredChoicesContainer.getChildren().clear();
        undoButton.setDisable(true);
        for (choiceData choice : currentDialog.choices) {
            Button choiceButton = new Button(choice.text);
            choiceButton.setMaxWidth(450);
            choiceButton.setWrapText(true);
            choiceButton.setStyle("-fx-background-color: white; -fx-text-fill: #593b59; -fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-padding: 10 20; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 10; -fx-cursor: hand; -fx-border-radius: 10;");
            choiceButton.setOnMouseEntered(e -> choiceButton.setStyle("-fx-background-color: #ffddf4; -fx-text-fill: #593b59; -fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-padding: 10 20; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;"));
            choiceButton.setOnMouseExited(e -> choiceButton.setStyle("-fx-background-color: white; -fx-text-fill: #593b59; -fx-font-family: 'Verdana'; -fx-font-size: 20px; -fx-padding: 10 20; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;"));
            choiceButton.setOnAction(_ -> {
                gameManager.goToScene(choice.nextScene);
                updateUI();
            });
            centeredChoicesContainer.getChildren().add(choiceButton);
        }
    }
    
    private void showLetter(sceneData currentScene) {
        dialogueUIGroup.setVisible(false); 
        letterContainer.setVisible(true);
        letterContainer.getChildren().clear();
        StackPane paper = new StackPane();
        paper.setStyle("-fx-background-color: #faf0e6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 5);");
        paper.setMaxWidth(800);
        paper.setMaxHeight(600);
        DialogNode letterDialog = gameManager.getCurrentDialog();
        if (letterDialog == null) return;
        Label letterTextLabel = new Label(letterDialog.text);
        letterTextLabel.setWrapText(true);
        letterTextLabel.setFont(Font.font("Courier New", 22));
        letterTextLabel.setTextFill(Color.BLACK);
        letterTextLabel.setPadding(new Insets(50));
        ScrollPane scrollPane = new ScrollPane(letterTextLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        paper.getChildren().add(scrollPane);
        letterContainer.getChildren().add(paper);
        letterContainer.setCursor(Cursor.HAND);
        letterContainer.setOnMouseClicked(e -> {
            gameManager.goToScene(currentScene.nextScene);
            updateUI();
        });
    }

    private void updateImage(ImageView view, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    view.setImage(new Image(imageFile.toURI().toString()));
                } else {
                    System.err.println("File gambar tidak ditemukan: " + imagePath);
                    view.setImage(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                view.setImage(null);
            }
        } else {
            view.setImage(null);
        }
    }
    
    private void playMusic(String musicPath) {
        String pathToPlay = (musicPath != null && !musicPath.isEmpty()) ? musicPath : DEFAULT_MUSIC_PATH;
        if (pathToPlay.equals(currentMusicPath)) return;
        try {
            Media media = new Media(new File(pathToPlay).toURI().toString());
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(isMusicOn ? 0.5 : 0.0);
            mediaPlayer.setOnError(() -> System.err.println("MediaPlayer error: " + mediaPlayer.getError()));
            mediaPlayer.play();
            currentMusicPath = pathToPlay;
        } catch (Exception e) {
            System.err.println("Gagal memutar musik: " + pathToPlay);
            currentMusicPath = null;
        }
    }
    
    private void toggleMusic() {
        isMusicOn = !isMusicOn;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(isMusicOn ? 0.5 : 0.0);
        }
        if (musicToggleButton != null) {
            musicToggleButton.setText(isMusicOn ? "\uD83D\uDD0A" : "\uD83D\uDD07");
        }
    }

    private void initializeInGameMenu(StackPane parent) {
        inGameMenu = new VBox(15);
        inGameMenu.setAlignment(Pos.CENTER);
        inGameMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75);");
        inGameMenu.setVisible(false);
        Label menuTitle = new Label("Menu");
        menuTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        menuTitle.setTextFill(Color.WHITE);
        Button resumeButton = new Button("Lanjutkan");
        Button saveButton = new Button("Simpan Game");
        Button backToMainMenuButton = new Button("Kembali ke Menu Utama");
        styleSubMenuButton(resumeButton);
        styleSubMenuButton(saveButton);
        styleSubMenuButton(backToMainMenuButton);
        resumeButton.setOnAction(e -> inGameMenu.setVisible(false));
        saveButton.setOnAction(e -> openSaveLoadScreen(SaveLoadMode.SAVE));
        backToMainMenuButton.setOnAction(e -> {
            showScreen(mainMenuContainer);
            playMusic(DEFAULT_MUSIC_PATH);
        });
        inGameMenu.getChildren().addAll(menuTitle, resumeButton, saveButton, backToMainMenuButton);
        parent.getChildren().add(inGameMenu);
    }
    
    private void styleMainMenuButton(Button button) {
        button.setPrefWidth(300);
        button.setPrefHeight(50);
        String baseStyle = "-fx-font-size: 22px; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 5; -fx-border-radius: 5;";
        String hoverStyle = "-fx-font-size: 22px; -fx-background-color: rgba(255, 255, 255, 0.3); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 5; -fx-border-radius: 5;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
    
    private void styleSubMenuButton(Button button) {
        button.setPrefWidth(250);
        String baseStyle = "-fx-font-size: 16px; -fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;";
        String hoverStyle = "-fx-font-size: 16px; -fx-background-color: #666; -fx-text-fill: white; -fx-background-radius: 5;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    private ImageView createBlurredBackground(String path) {
        ImageView background = new ImageView();
        try {
            background.setImage(new Image(new File(path).toURI().toString()));
            background.setEffect(new GaussianBlur(10));
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar latar: " + path);
            background.setStyle("-fx-background-color: black;");
        }
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        background.fitHeightProperty().bind(primaryStage.heightProperty());
        background.setPreserveRatio(false);
        return background;
    }

        private Stage primaryStage;
    private StackPane rootLayout;
    private MenuSystem menuSystem;
    private SettingsSystem settingsSystem;
    private SaveLoadSystem saveLoadSystem;
    private SaveLoadService saveLoadService;
    private GameManager gameManager;
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
        gameManager = new GameManager();
        gameUIScreen = new GameUIScreen(gameManager, historySystem, transitionSystem, new GameUIScreen.GameUICallback() {
            @Override public void onRequestMenu() { showMainMenu(); }
            @Override public void onRequestSave(boolean fromInGameMenu) { showSaveLoadScreen(true, fromInGameMenu); }
            @Override public void onRequestLoad(boolean fromInGameMenu) { showSaveLoadScreen(false, fromInGameMenu); }
            @Override public void onRequestSettings() { showSettingsScreen(); }
            @Override public void onRequestCredits() { showCreditsScreen(); }
            @Override public void onRequestExit() { exitGame(); }
        });
        showMainMenu();
        Scene scene = new Scene(rootLayout, 1280, 720);
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
        gameManager = new GameManager();
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
// File: game/Main.java
package game;

import game.manager.gameManager;
import game.model.CharacterData;
import game.model.choiceData;
import game.model.sceneData;
import game.model.DialogNode;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

    // --- BAGIAN YANG HILANG DAN KINI DITAMBAHKAN KEMBALI ---
    // Deklarasi variabel instance
    private gameManager gameManager;
    private Stage primaryStage;
    private StackPane rootLayout;
    private ImageView backgroundView;

    // Wadah untuk karakter kiri dan kanan
    private ImageView leftCharacterView;
    private ImageView rightCharacterView;
    private HBox characterContainer;

    // Semua elemen UI yang perlu diakses di seluruh kelas
    private Button undoButton;
    private VBox dialogueUIGroup;
    private VBox buttonGroup; // Group untuk tombol-tombol menu
    private StackPane nameBox;
    private Text nameLabel;
    private javafx.scene.text.Text dialogueLabel;
    private VBox choicesBox; // Meskipun diinisialisasi lokal, lebih aman dideklarasikan di sini
    private VBox dialogueContainer; // Variabel yang menyebabkan error utama
    private Label nextIndicator;
    private VBox centeredChoicesContainer;
    private StackPane letterContainer;
    private MediaPlayer mediaPlayer;
    private String currentMusicPath = null;
    private boolean isMusicOn = true;
    private VBox createInGameMenu; // Untuk menu dalam game jika diperlukan
    private Button musicToggleButton;
    private Button hamburgerButton;
    // --- AKHIR BAGIAN YANG DIPERBAIKI ---

    private static final String DEFAULT_MUSIC_PATH = "assets/music/Music Page Menu.mp3";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        gameManager = new gameManager();
        primaryStage.setTitle("Visual Novel - 24 Hours");

        rootLayout = new StackPane();

        // --- LAPISAN 1: GAMBAR ---
        StackPane imageContainer = new StackPane();
        backgroundView = new ImageView();
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());
        backgroundView.setPreserveRatio(false);

        // Inisialisasi ImageView untuk setiap posisi
        leftCharacterView = new ImageView();
        leftCharacterView.setPreserveRatio(true);
        leftCharacterView.setFitHeight(650);

        rightCharacterView = new ImageView();
        rightCharacterView.setPreserveRatio(true);
        rightCharacterView.setFitHeight(650);

        // Buat HBox untuk menampung karakter
        characterContainer = new HBox(leftCharacterView, rightCharacterView);
        characterContainer.setAlignment(Pos.BOTTOM_CENTER);
        characterContainer.setSpacing(100);
        characterContainer.setPadding(new Insets(0, 0, 20, 0));
        characterContainer.setPickOnBounds(false);

        imageContainer.getChildren().addAll(backgroundView, characterContainer);

        // --- LAPISAN 2: UI DIALOG BAWAH (Sekarang akan berfungsi) ---
        dialogueUIGroup = new VBox();
        dialogueUIGroup.setAlignment(Pos.BOTTOM_CENTER);
        dialogueUIGroup.setPadding(new Insets(0, 20, 20, 20));
        dialogueUIGroup.setPickOnBounds(false);

        nameLabel = new javafx.scene.text.Text();
        nameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        nameLabel.setFill(Color.WHITE);
        nameLabel.setStroke(Color.BLACK);
        nameLabel.setStrokeWidth(0.5);
        TextFlow nameFlow = new TextFlow(nameLabel);
        nameBox = new StackPane(nameFlow);
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

        dialogueLabel = new javafx.scene.text.Text();
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
        nextIndicator.setTextFill(Color.web("#4e342e"));

        // --- BUAT BUTTON GROUP ---
        buttonGroup = new VBox(10);
        buttonGroup.setAlignment(Pos.BOTTOM_LEFT);
        buttonGroup.setPadding(new Insets(0, 0, 30, 30)); // Padding yang sama dengan dialogueUIGroup

        undoButton = new Button("⟲ Undo");
        undoButton.setOpacity(0.5);
        undoButton.setStyle("-fx-font-size: 14px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 8 15;");
        undoButton.setOnAction(e -> {
            if (gameManager.undoDialog()) {
                justUndone = true;
                updateUI();
            }
        });
        undoButton.setTooltip(new Tooltip("Undo"));
        // Mencegah event bubbling ke parent - gunakan setOnMousePressed dan setOnMouseReleased
        undoButton.setOnMousePressed(e -> e.consume());
        undoButton.setOnMouseReleased(e -> e.consume());
        undoButton.setOnMouseClicked(e -> e.consume());

        // Tambahkan undo button ke button group
        buttonGroup.getChildren().add(undoButton);

        // Contoh: Tombol menu lain (bisa ditambahkan nanti)
        // Button saveButton = new Button("💾 Save");
        // saveButton.setStyle("-fx-font-size: 14px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 8 15;");
        // buttonGroup.getChildren().add(saveButton);
        dialogueSystemStack.getChildren().addAll(dialogueContainer, nextIndicator);
        StackPane.setAlignment(nextIndicator, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(nextIndicator, new Insets(0, 25, 10, 0));

        dialogueUIGroup.getChildren().addAll(nameBoxWrapper, dialogueSystemStack);

        // --- LAPISAN 3 & 4 ---
        centeredChoicesContainer = new VBox(15);
        centeredChoicesContainer.setAlignment(Pos.CENTER);
        centeredChoicesContainer.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");
        centeredChoicesContainer.setVisible(false);
        centeredChoicesContainer.setOnMouseClicked(event -> event.consume());

        letterContainer = new StackPane();
        letterContainer.setVisible(false);

        // Tambah tombol music ON/OFF dengan icon
        musicToggleButton = new Button("\uD83D\uDD0A"); // 🔊
        musicToggleButton.setStyle("-fx-font-size: 22px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 4 10 4 10;");
        musicToggleButton.setOnAction(e -> toggleMusic());
        musicToggleButton.setTooltip(new Tooltip("Toggle Music"));

        // Tambah tombol hamburger
        hamburgerButton = new Button("\u2630"); // ☰
        hamburgerButton.setStyle("-fx-font-size: 22px; -fx-background-radius: 20; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 4 10 4 10;");
        hamburgerButton.setTooltip(new Tooltip("Menu"));
        hamburgerButton.setOnAction(e -> createInGameMenu().setVisible(!createInGameMenu().isVisible()));

        // Buat HBox untuk kedua tombol
        HBox topLeftButtons = new HBox(8, hamburgerButton, musicToggleButton);
        topLeftButtons.setAlignment(Pos.TOP_LEFT);
        topLeftButtons.setPadding(new Insets(10, 0, 0, 10));
        topLeftButtons.setPickOnBounds(false);

        // --- GABUNGKAN SEMUA LAPISAN KE ROOT ---
        rootLayout.getChildren().addAll(imageContainer, dialogueUIGroup, centeredChoicesContainer, letterContainer);
        // Tambahkan tombol ke rootLayout PALING AKHIR agar selalu di atas
        rootLayout.getChildren().add(topLeftButtons);
        StackPane.setAlignment(topLeftButtons, Pos.TOP_LEFT);
        StackPane.setMargin(topLeftButtons, new Insets(10, 0, 0, 10));

        // --- BUAT SCENE ---
        Scene scene = new Scene(rootLayout, 1280, 720);
        primaryStage.setScene(scene);

        updateUI();
        primaryStage.show();
    }

    private void updateUI() {
        // Reset state
        dialogueUIGroup.setVisible(true);
        buttonGroup.setVisible(true);
        centeredChoicesContainer.setVisible(false);
        letterContainer.setVisible(false);
        choicesBox.getChildren().clear();
        nextIndicator.setVisible(false);
        nameBox.setVisible(false);
        // JANGAN reset event handler di sini, biarkan di-set di showNormalDialogue
        dialogueUIGroup.setCursor(Cursor.DEFAULT);

        sceneData currentScene = gameManager.getCurrentScene();
        if (currentScene == null) {
            dialogueUIGroup.setVisible(false);
            Label endLabel = new Label("TAMAT\nTerima kasih telah bermain.");
            endLabel.setFont(Font.font("Arial", 40));
            endLabel.setTextFill(Color.WHITE);
            endLabel.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-padding: 20;");
            if (rootLayout.getChildren().stream().noneMatch(node -> node instanceof Label && ((Label) node).getText().startsWith("TAMAT"))) {
                rootLayout.getChildren().add(endLabel);
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                currentMusicPath = null;
            }
            return;
        }

        // --- LOGIKA PLAY MUSIC ---
        String musicPath = (currentScene.music != null && !currentScene.music.isEmpty()) ? currentScene.music : DEFAULT_MUSIC_PATH;
        if (musicPath != null && !musicPath.isEmpty()) {
            try {
                // Hanya ganti musik jika path-nya BERBEDA
                if (!musicPath.equals(currentMusicPath)) {
                    System.out.println("Mencari musik di path: " + new File(musicPath).getAbsolutePath()); 
            
                    Media media = new Media(new File(musicPath).toURI().toString());
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.dispose();
                    }
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Looping
                    mediaPlayer.setVolume(isMusicOn ? 1.0 : 0.0);
                    mediaPlayer.setOnError(() -> {
                        System.err.println("MediaPlayer error: " + mediaPlayer.getError());
                    });
                    mediaPlayer.play();
                    currentMusicPath = musicPath;
                } else {
                    // Jika path sama, pastikan volume sesuai status tombol
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(isMusicOn ? 1.0 : 0.0);
                    }
                }
                // Selalu update icon tombol sesuai status
                musicToggleButton.setText(isMusicOn ? "\uD83D\uDD0A" : "\uD83D\uDD07");
            } catch (Exception e) {
                System.err.println("Gagal memutar musik: " + musicPath);
                e.printStackTrace();
            }
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                currentMusicPath = null;
            }
        }

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
                } else if ("right".equalsIgnoreCase(character.position)) {
                    updateImage(rightCharacterView, character.sprite);
                    hasRight = true;
                } else { // Asumsi default atau "center"
                    updateImage(rightCharacterView, character.sprite);
                    hasRight = true;
                }
            }
            if (hasLeft) {
                characterContainer.getChildren().add(leftCharacterView);
            }
            if (hasRight) {
                characterContainer.getChildren().add(rightCharacterView);
            }

            if (currentScene.characters.size() == 1) {
                characterContainer.setAlignment(Pos.BOTTOM_CENTER);
            } else {
                characterContainer.setAlignment(Pos.BOTTOM_CENTER);
            }
        }

        if ("letter".equals(currentScene.type)) {
            showLetter(currentScene);
            undoButton.setDisable(true);
            return;
        }

        DialogNode currentDialog = gameManager.getCurrentDialog();
        System.out.println("Current dialog: " + (currentDialog != null ? "ADA" : "NULL"));
        if (currentDialog != null) {
            System.out.println("Dialog choices: " + (currentDialog.choices != null ? currentDialog.choices.size() : "NULL"));
            if (currentDialog.choices != null && !currentDialog.choices.isEmpty()) {
                System.out.println("Menampilkan choices...");
                showChoices(currentScene, currentDialog);
            } else {
                System.out.println("Menampilkan normal dialogue...");
                showNormalDialogue(currentScene, currentDialog);
            }
        } else {
            System.out.println("Dialog null, pindah ke scene berikutnya...");
            gameManager.goToScene(currentScene.nextScene);
            updateUI();
            return;
        }

        // Sembunyikan tombol undo jika tidak bisa undo, bukan hanya disable
        if (gameManager.canUndoDialog()) {
            undoButton.setVisible(true);
            undoButton.setDisable(false);
        } else {
            undoButton.setVisible(false);
            undoButton.setDisable(true);
        }

        // Button group tetap visible, hanya tombol individual yang di-hide
        // Ini memungkinkan tombol lain tetap terlihat meskipun undo button di-hide
        buttonGroup.setVisible(true);

        // Debug: pastikan event handler aktif
        System.out.println("Scene " + currentScene.id + " - Dialog " + gameManager.getCurrentDialogIndex()
                + " - Can undo: " + gameManager.canUndoDialog()
                + " - Event handler aktif: " + (dialogueUIGroup.getOnMouseClicked() != null)
                + " - Undo button visible: " + undoButton.isVisible()
                + " - Button group visible: " + buttonGroup.isVisible());
    }

    private void showNormalDialogue(sceneData currentScene, DialogNode currentDialog) {
        dialogueLabel.setText(currentDialog.text);
        if (currentDialog.character != null && !currentDialog.character.isEmpty()) {
            nameLabel.setText(currentDialog.character);
            nameBox.setVisible(true);
        }

        nextIndicator.setVisible(true);
        dialogueUIGroup.setCursor(Cursor.HAND);

        // Debug: pastikan event handler di-set
        System.out.println("Setting event handler untuk dialog box...");

        dialogueUIGroup.setOnMouseClicked(event -> {
            System.out.println("Dialog box diklik! Button: " + event.getButton());

            if (event.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                if (justUndone) {
                    System.out.println("Klik kiri setelah undo, hanya reset flag, tidak next!");
                    justUndone = false;
                    // Setelah undo, klik kiri pertama hanya reset flag, tidak next
                    return;
                }
                // Hanya blokir next jika sudah di dialog terakhir dan tidak bisa undo
                if (gameManager.getCurrentDialogIndex() >= gameManager.getCurrentScene().dialogs.size() - 1 && !gameManager.canUndoDialog()) {
                    // Sudah di dialog terakhir, tidak bisa next lagi
                    System.out.println("Sudah di dialog terakhir, tidak bisa next");
                    return;
                }
                System.out.println("Mencoba next dialog...");
                boolean movedToNextDialog = gameManager.nextDialog();
                if (!movedToNextDialog) {
                    String nextSceneId = (currentDialog.next != null) ? currentDialog.next : currentScene.nextScene;
                    System.out.println("Pindah ke scene: " + nextSceneId);
                    gameManager.goToScene(nextSceneId);
                }
                updateUI();
            } else if (event.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                if (gameManager.canUndoDialog()) {
                    if (gameManager.undoDialog()) {
                        justUndone = true;
                        updateUI();
                    }
                }
            }
        });

        System.out.println("Event handler berhasil di-set untuk dialog box");
    }

    private void showChoices(sceneData currentScene, DialogNode currentDialog) {
        dialogueUIGroup.setVisible(false);
        centeredChoicesContainer.setVisible(true);
        centeredChoicesContainer.getChildren().clear();

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
        scrollPane.setFitToHeight(false);
        scrollPane.setPrefViewportHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        paper.setMaxHeight(600);
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
                    view.setStyle(""); // Reset style jika ada gambar
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

    private void toggleMusic() {
        isMusicOn = !isMusicOn;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(isMusicOn ? 1.0 : 0.0);
        }
        musicToggleButton.setText(isMusicOn ? "\uD83D\uDD0A" : "\uD83D\uDD07"); // 🔊/🔇

    }
<<<<<<< HEAD
}
=======

    private VBox createInGameMenu() {
        VBox menuLayout = new VBox(15);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.75); -fx-background-radius: 10;");
        menuLayout.setPadding(new Insets(30));
        menuLayout.setMaxSize(300, 350);

        Label menuTitle = new Label("Menu");
        menuTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        menuTitle.setTextFill(Color.WHITE);

        Button saveButton = new Button("Simpan Game");
        Button loadButton = new Button("Muat Game");
        Button settingsButton = new Button("Pengaturan");
        Button quitButton = new Button("Keluar dari Game");

        styleMenuButton(saveButton);
        styleMenuButton(loadButton);
        styleMenuButton(settingsButton);
        styleMenuButton(quitButton);

        saveButton.setOnAction(e -> System.out.println("Logika Simpan Game..."));
        loadButton.setOnAction(e -> System.out.println("Logika Muat Game..."));
        settingsButton.setOnAction(e -> System.out.println("Logika Pengaturan..."));
        quitButton.setOnAction(e -> primaryStage.close());

        Button closeButton = new Button("Lanjutkan");
        closeButton.setStyle("-fx-font-size: 14px; -fx-background-color: #ffb7c5; -fx-text-fill: white;");
        closeButton.setOnAction(e -> inGameMenu.setVisible(false));

        menuLayout.getChildren().addAll(menuTitle, saveButton, loadButton, settingsButton, quitButton, new Label(), closeButton);
        menuLayout.setVisible(false);
        return menuLayout;
    }

    private void styleMenuButton(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);
        button.setFont(Font.font("Verdana", FontWeight.NORMAL, 16));
        button.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-background-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;"));
    }


}
>>>>>>> 6e48dfe09914b420c77e25ee453d1bbb16acbc81

package game.system;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import game.ui.Style;
import game.GameAppCallback;
import game.util.FileUtil;
import game.model.GameState;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Cursor;
import game.manager.GameManager;
import game.manager.SaveLoadService;
import java.io.File;

/**
 * Kelas SaveLoadSystem bertanggung jawab untuk mengelola logika UI dan proses save/load game (interaksi user dan tampilan).
 */
public class SaveLoadSystem {
    /**
     * Menampilkan UI save game.
     * @return Pane UI save game.
     */
    public StackPane showSaveUI(GameAppCallback callback, GameManager gameManager, SaveLoadService saveLoadService, Pane gamePane, Runnable onBack) {
        return buildSaveLoadUI(callback, gameManager, saveLoadService, true, gamePane, onBack);
    }

    /**
     * Menampilkan UI load game.
     * @return Pane UI load game.
     */
    public StackPane showLoadUI(GameAppCallback callback, GameManager gameManager, SaveLoadService saveLoadService, Runnable onBack) {
        return buildSaveLoadUI(callback, gameManager, saveLoadService, false, null, onBack);
    }

    private StackPane buildSaveLoadUI(GameAppCallback callback, GameManager gameManager, SaveLoadService saveLoadService, boolean isSave, Pane gamePane, Runnable onBack) {
        StackPane layout = new StackPane();
        layout.setStyle("-fx-background-color: #222;");
        Label titleLabel = new Label(isSave ? "Simpan Game" : "Muat Game");
        titleLabel.setFont(Font.font(Style.MAIN_FONT, FontWeight.BOLD, Style.TITLE_FONT_SIZE));
        titleLabel.setTextFill(Color.WHITE);
        GridPane slotsGrid = new GridPane();
        slotsGrid.setHgap(20);
        slotsGrid.setVgap(20);
        slotsGrid.setAlignment(Pos.CENTER);
        int slotPerPage = 6;
        int totalSlot = 30;
        int[] currentPage = {0};
        HBox paginationBox = new HBox(20);
        paginationBox.setAlignment(Pos.CENTER);
        Button prevButton = new Button("\u2190 Previous");
        Button nextButton = new Button("Next \u2192");
        Label pageLabel = new Label();
        pageLabel.setFont(Font.font("Segoe UI", 16));
        pageLabel.setTextFill(Color.WHITE);
        Style.styleSubMenuButton(prevButton);
        Style.styleSubMenuButton(nextButton);
        paginationBox.getChildren().addAll(prevButton, pageLabel, nextButton);
        final Runnable[] refreshUI = new Runnable[1];
        refreshUI[0] = new Runnable() {
            @Override
            public void run() {
                slotsGrid.getChildren().clear();
                int startSlot = currentPage[0] * slotPerPage + 1;
                int endSlot = Math.min(startSlot + slotPerPage - 1, totalSlot);
                for (int i = startSlot; i <= endSlot; i++) {
                    final int slotNumber = i;
                    GameState state = saveLoadService.loadGame(slotNumber);
                    VBox slotBox = new VBox(5);
                    slotBox.setAlignment(Pos.CENTER_LEFT);
                    slotBox.setPadding(new Insets(15));
                    // slotBox.setPrefSize(350, 120);
                    String baseStyle = "-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: white; -fx-border-width: 1px; -fx-background-radius: 5; -fx-border-radius: 5; -fx-cursor: hand;";
                    String hoverStyle = baseStyle + "-fx-background-color: rgba(255, 255, 255, 0.2);";
                    slotBox.setStyle(baseStyle);
                    ImageView coverImage = new ImageView();
                    coverImage.setFitWidth(80);
                    coverImage.setFitHeight(60);
                    coverImage.setPreserveRatio(true);
                    coverImage.setStyle("-fx-background-color: #333; -fx-background-radius: 3;");
                    String coverPath = (slotNumber == -1) ? "saveCover/cover_temp.png" : "saveCover/cover_slot_" + slotNumber + ".png";
                    if (FileUtil.exists(coverPath)) {
                        try {
                            coverImage.setImage(new Image(new java.io.File(coverPath).toURI().toString()));
                        } catch (Exception e) {
                            System.err.println("Gagal memuat cover: " + coverPath);
                        }
                    }
                    Label slotLabel = new Label("SLOT " + slotNumber);
                    slotLabel.setFont(Font.font(Style.MAIN_FONT, FontWeight.BOLD, 20));
                    slotLabel.setTextFill(Color.WHITE);
                    Label dateLabel = new Label(state != null ? state.saveDate : "Kosong");
                    dateLabel.setFont(Font.font("Segoe UI", 16));
                    dateLabel.setTextFill(state != null ? Color.LIGHTGREEN : Color.GRAY);
                    HBox slotContent = new HBox(10, coverImage, new VBox(5, slotLabel, dateLabel));
                    slotContent.setAlignment(Pos.CENTER_LEFT);
                    slotBox.getChildren().add(slotContent);
                    if (state != null || isSave) {
                        slotBox.setCursor(Cursor.HAND);
                        slotBox.setOnMouseEntered(e -> slotBox.setStyle(hoverStyle));
                        slotBox.setOnMouseExited(e -> slotBox.setStyle(baseStyle));
                        slotBox.setOnMouseClicked(e -> {
                            if (isSave) {
                                // Ambil screenshot dari gamePane dan simpan ke cover slot
                                try {
                                    File coverDir = new File("saveCover");
                                    if (!coverDir.exists()) coverDir.mkdirs();
                                    if (gamePane != null) {
                                        game.system.ScreenshotSystem.savePaneScreenshot(gamePane, "saveCover/cover_slot_" + slotNumber + ".png");
                                    }
                                } catch (Exception ex) {
                                    System.err.println("Gagal menyimpan screenshot cover: " + ex.getMessage());
                                }
                                GameState currentState = gameManager.createSaveState();
                                if (currentState != null && saveLoadService.saveGame(slotNumber, currentState)) {
                                    System.out.println("Game berhasil disimpan di slot " + slotNumber + " (file: saves/save_slot_" + slotNumber + ".json)");
                                    // Auto-refresh tampilan slot setelah save
                                    refreshUI[0].run();
                                } else {
                                    System.err.println("Gagal menyimpan game di slot " + slotNumber);
                                }
                            } else {
                                if (state != null) {
                                    System.out.println("Mencoba load slot " + slotNumber + " (file: saves/save_slot_" + slotNumber + ".json)");
                                    gameManager.applyGameState(state);
                                    System.out.println("Game berhasil dimuat dari slot " + slotNumber);
                                    callback.showGameScreen();
                                    ((game.Main)callback).getGameUIScreen().updateUI();
                                } else {
                                    System.err.println("Slot " + slotNumber + " kosong atau gagal load.");
                                }
                            }
                        });
                    } else {
                        slotBox.setCursor(Cursor.DEFAULT);
                    }
                    int gridIndex = i - startSlot;
                    slotsGrid.add(slotBox, gridIndex % 2, gridIndex / 2);
                }
                int totalPages = (totalSlot + slotPerPage - 1) / slotPerPage;
                pageLabel.setText("Page " + (currentPage[0] + 1) + " of " + totalPages);
                prevButton.setDisable(currentPage[0] == 0);
                nextButton.setDisable(currentPage[0] >= totalPages - 1);
            }
        };
        prevButton.setOnAction(e -> {
            if (currentPage[0] > 0) {
                currentPage[0]--;
                refreshUI[0].run();
            }
        });
        nextButton.setOnAction(e -> {
            int totalPages = (totalSlot + slotPerPage - 1) / slotPerPage;
            if (currentPage[0] < totalPages - 1) {
                currentPage[0]++;
                refreshUI[0].run();
            }
        });
        Button backButton = new Button("Back");
        Style.styleSubMenuButton(backButton);
        // backButton.setPrefWidth(250);
        backButton.setOnAction(e -> {
            System.out.println("[DEBUG] Back button clicked in Save/Load. onBack is " + (onBack != null));
            if (onBack != null) {
                onBack.run();
            }
        });
        VBox content = new VBox(30, titleLabel, slotsGrid, paginationBox, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        VBox.setMargin(backButton, new Insets(20, 0, 0, 0));
        layout.getChildren().addAll(content);
        refreshUI[0].run();
        return layout;
    }

    /**
     * Menyembunyikan UI save/load.
     */
    public void hideSaveLoadUI() {}
} 
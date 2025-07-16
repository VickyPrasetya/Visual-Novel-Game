package game.system;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import game.ui.Style;
import game.GameAppCallback;
import javafx.scene.layout.VBox;
import game.system.AudioSystem;

/**
 * Kelas SettingsSystem bertanggung jawab untuk menyimpan dan memuat preferensi user (volume, mute, resolusi, fullscreen, text speed).
 */
public class SettingsSystem {
    private double volume = 0.5;
    private boolean muted = false;
    private String resolution = "1280x720";
    private boolean fullscreen = false;
    private int textSpeed = 1;

    /**
     * Menyimpan preferensi user ke file/config.
     */
    public void saveSettings() {}

    /**
     * Memuat preferensi user dari file/config.
     */
    public void loadSettings() {}

    public StackPane showSettingsUI(Stage primaryStage, GameAppCallback callback, Runnable onBack) {
        StackPane settingsLayout = new StackPane();
        settingsLayout.setStyle("-fx-background-color: #222;");
        Label title = new Label("Settings");
        title.setFont(Font.font(Style.MAIN_FONT, FontWeight.BOLD, Style.TITLE_FONT_SIZE));
        title.setTextFill(Color.WHITE);
        // Volume
        Label volumeLabel = new Label("Volume Musik");
        volumeLabel.setFont(Font.font(Style.MAIN_FONT, 20));
        volumeLabel.setTextFill(Color.WHITE);
        double initialVolume = AudioSystem.getInstance().getVolume();
        Slider volumeSlider = new Slider(0, 1, initialVolume);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.5);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setBlockIncrement(0.01);
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            setVolume(newVal.doubleValue());
            AudioSystem.getInstance().setVolume(newVal.doubleValue());
            if (!isMuted()) AudioSystem.getInstance().setMute(false);
        });
        // Mute
        boolean initialMute = AudioSystem.getInstance().isMuted();
        CheckBox muteCheck = new CheckBox("Mute");
        muteCheck.setSelected(initialMute);
        muteCheck.setFont(Font.font(Style.MAIN_FONT, 18));
        muteCheck.setTextFill(Color.WHITE);
        muteCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            setMuted(newVal);
            AudioSystem.getInstance().setMute(newVal);
        });
        // Resolusi
        Label resLabel = new Label("Resolusi");
        resLabel.setFont(Font.font(Style.MAIN_FONT, 20));
        resLabel.setTextFill(Color.WHITE);
        ComboBox<String> resCombo = new ComboBox<>();
        String[] RESOLUTIONS = {"1280x720", "1600x900", "1920x1080"};
        resCombo.getItems().addAll(RESOLUTIONS);
        int currentResolutionIndex = 0;
        for (int i = 0; i < RESOLUTIONS.length; i++) {
            if (RESOLUTIONS[i].equals(getResolution())) {
                currentResolutionIndex = i;
                break;
            }
        }
        resCombo.getSelectionModel().select(currentResolutionIndex);
        resCombo.setOnAction(e -> {
            setResolution(resCombo.getValue());
            String[] wh = getResolution().split("x");
            int w = Integer.parseInt(wh[0]);
            int h = Integer.parseInt(wh[1]);
            primaryStage.setWidth(w);
            primaryStage.setHeight(h);
        });
        // Fullscreen
        CheckBox fullscreenCheck = new CheckBox("Fullscreen");
        fullscreenCheck.setSelected(isFullscreen());
        fullscreenCheck.setFont(Font.font(Style.MAIN_FONT, 18));
        fullscreenCheck.setTextFill(Color.WHITE);
        fullscreenCheck.selectedProperty().addListener((obs, oldVal, newVal) -> {
            setFullscreen(newVal);
            primaryStage.setFullScreen(newVal);
        });
        // Text Speed
        Label speedLabel = new Label("Text Speed");
        speedLabel.setFont(Font.font(Style.MAIN_FONT, 20));
        speedLabel.setTextFill(Color.WHITE);
        Slider speedSlider = new Slider(0, 2, getTextSpeed());
        speedSlider.setMajorTickUnit(1);
        speedSlider.setMinorTickCount(0);
        speedSlider.setSnapToTicks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setLabelFormatter(new javafx.util.StringConverter<Double>() {
            @Override public String toString(Double n) {
                if (n < 0.5) return "Lambat";
                if (n < 1.5) return "Normal";
                return "Cepat";
            }
            @Override public Double fromString(String s) { return 1.0; }
        });
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            setTextSpeed(newVal.intValue());
            // Implement text speed callback if needed
        });
        // Back button
        Button backButton = new Button("Back");
        Style.styleMainMenuButton(backButton);
        backButton.setOnAction(e -> {
            System.out.println("[DEBUG] Back button clicked in Settings. onBack is " + (onBack != null));
            if (onBack != null) {
                onBack.run();
            } else {
                callback.showMainMenu();
            }
        });
        VBox content = new VBox(25, title, volumeLabel, volumeSlider, muteCheck, resLabel, resCombo, fullscreenCheck, speedLabel, speedSlider, backButton);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        settingsLayout.getChildren().addAll(content);
        return settingsLayout;
    }

    // Getter dan setter untuk setiap preferensi
    public double getVolume() { return volume; }
    public void setVolume(double volume) { this.volume = volume; }
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
    public String getResolution() { return resolution; }
    public void setResolution(String resolution) { this.resolution = resolution; }
    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }
    public int getTextSpeed() { return textSpeed; }
    public void setTextSpeed(int textSpeed) { this.textSpeed = textSpeed; }
} 
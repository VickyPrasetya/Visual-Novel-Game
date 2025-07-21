package game.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Kelas Style menyediakan utilitas styling untuk komponen UI seperti Button.
 */
public class Style {
    /**
     * Styling untuk tombol utama (main menu).
     * @param button Button yang akan distyling.
     */
    public static void styleMainMenuButton(Button button) {
        button.setPrefWidth(300);
        button.setPrefHeight(50);
        button.setWrapText(true);
        String baseStyle = "-fx-font-size: 22px; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 5; -fx-border-radius: 5;";
        String hoverStyle = "-fx-font-size: 22px; -fx-background-color: rgba(255, 255, 255, 0.3); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 5; -fx-border-radius: 5;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public static void styleIconButton(Button button) {
        button.setPrefWidth(300);
        button.setPrefHeight(50);
        button.setWrapText(true);
        String baseStyle = "-fx-font-size: 16px; -fx-background-color: rgba(0, 0, 0, 0.5); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 5; -fx-border-radius: 5;";
        String hoverStyle = "-fx-font-size: 16px; -fx-background-color: rgba(255, 255, 255, 0.3); -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 2px; -fx-background-radius: 5; -fx-border-radius: 5;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
    /**
     * Styling untuk tombol submenu.
     * @param button Button yang akan distyling.
     */
    public static void styleSubMenuButton(Button button) {
        String baseStyle = "-fx-font-family: 'Segoe UI Symbol'; -fx-font-size: 16px; -fx-background-color: #444; -fx-text-fill: white; -fx-background-radius: 5;";
        String hoverStyle = "-fx-font-family: 'Segoe UI Symbol'; -fx-font-size: 16px; -fx-background-color: #666; -fx-text-fill: white; -fx-background-radius: 5;";
        button.setStyle(baseStyle);
        button.setPrefSize(50, 50);
        button.setWrapText(true);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
    /**
     * Styling untuk tombol pilihan (choices).
     * @param button Button yang akan distyling.
     */
    public static void styleChoiceButton(Button button) {
        button.setMaxWidth(450);
        button.setWrapText(true);
        String baseStyle = "-fx-background-color: white; -fx-text-fill: #593b59; -fx-font-family: 'Segoe UI'; -fx-font-size: 20px; -fx-padding: 10 20; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 10; -fx-cursor: hand; -fx-border-radius: 10;";
        String hoverStyle = "-fx-background-color: #ffddf4; -fx-text-fill: #593b59; -fx-font-family: 'Segoe UI'; -fx-font-size: 20px; -fx-padding: 10 20; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 10; -fx-border-radius: 10;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
    /**
     * Styling untuk tombol bottom bar (undo, history, text speed, skip).
     */
    public static void styleBottomBarButton(Button button) {
        button.setPrefWidth(110);
        button.setPrefHeight(44);
        button.setWrapText(true);
        String baseStyle = "-fx-font-size: 18px; -fx-background-color: #222; -fx-text-fill: white; -fx-border-color: #fff; -fx-border-width: 2px; -fx-background-radius: 20; -fx-border-radius: 20;";
        String hoverStyle = "-fx-font-size: 18px; -fx-background-color: #444; -fx-text-fill: white; -fx-border-color: #fff; -fx-border-width: 2px; -fx-background-radius: 20; -fx-border-radius: 20;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }
    /**
     * Styling untuk overlay history agar background gelap dan teks terbaca.
     */
    public static void styleHistoryOverlay(javafx.scene.layout.Region overlay) {
        overlay.setStyle("-fx-background-color: rgba(20,20,20,0.92); -fx-background-radius: 18; -fx-border-color: #fff; -fx-border-width: 2px; -fx-border-radius: 18;");
    }
    /**
     * Styling untuk tombol pilihan besar (choice) agar lebih mudah dibaca.
     */
    public static void styleChoiceButtonLarge(Button button) {
        button.setMaxWidth(600);
        button.setPrefHeight(60);
        button.setWrapText(true);
        String baseStyle = "-fx-background-color: white; -fx-text-fill: #593b59; -fx-font-family: 'Segoe UI'; -fx-font-size: 24px; -fx-padding: 16 32; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 12; -fx-cursor: hand; -fx-border-radius: 12;";
        String hoverStyle = "-fx-background-color: #ffddf4; -fx-text-fill: #593b59; -fx-font-family: 'Segoe UI'; -fx-font-size: 24px; -fx-padding: 16 32; -fx-border-color: #593b59; -fx-border-width: 2; -fx-background-radius: 12; -fx-border-radius: 12;";
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public static final String MAIN_FONT = "Segoe UI";
    public static final String MAIN_FONT_BOLD = "Segoe UI Semibold";
    public static final int TITLE_FONT_SIZE = 90;
    public static final int SUBTITLE_FONT_SIZE = 50;
    public static final int LABEL_FONT_SIZE = 20;
    public static final int BUTTON_FONT_SIZE = 22;
    public static final int DIALOG_FONT_SIZE = 25;
    public static final int NAME_FONT_SIZE = 22;
    public static final int LETTER_FONT_SIZE = 22;
    public static final String CIRCLE_BUTTON_STYLE = "-fx-font-size: 22px; -fx-background-radius: 25; -fx-background-color: #222; -fx-text-fill: white; -fx-padding: 0; -fx-border-color: white; -fx-border-width: 2px;";
    public static final String MENU_BACKGROUND_PATH = "assets/bg/TamanBelakang/Park.jpg";

    public static ImageView createBlurredBackground(String path) {
        ImageView background = new ImageView();
        try {
            background.setImage(new javafx.scene.image.Image(new java.io.File(path).toURI().toString()));
            background.setEffect(new javafx.scene.effect.GaussianBlur(10));
        } catch (Exception e) {
            background.setStyle("-fx-background-color: black;");
        }
        background.setPreserveRatio(false);
        return background;
    }
} 
package game.system;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import javafx.scene.layout.Pane;
import java.io.File;
import javafx.scene.Node;

/**
 * Kelas ScreenshotSystem menyediakan utilitas untuk mengambil screenshot Pane dan menyimpannya ke file PNG.
 */
public class ScreenshotSystem {
    /**
     * Menyimpan screenshot dari Pane ke file PNG.
     * @param pane Pane yang akan di-screenshot.
     * @param path Path file output PNG.
     */
    public static void savePaneScreenshot(Node node, String path) {
        try {
            WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
            File file = new File(path);
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 
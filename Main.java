
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("24 Hours - Visual Novel");

        // Untuk sementara tampilkan MainMenuUI jika sudah tersedia
        StackPane root = new StackPane();
        root.getChildren().add(new Label("Hello Visual Novel"));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

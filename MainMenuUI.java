
public class MainMenuUI extends VBox {

    public MainMenuUI() {
        Label title = new Label("24 HOURS");
        Button start = new Button("Start");
        this.getChildren().addAll(title, start);
    }
}

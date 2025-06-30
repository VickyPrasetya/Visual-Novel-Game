
public class VNScene {

    private String dialog;
    private List<Choice> choices;

    public VNScene(String dialog) {
        this.dialog = dialog;
        this.choices = new ArrayList<>();
    }

    // Add setter/getter
}

public class Choice {

    private String label;
    private String nextScene;

    public Choice(String label, String nextScene) {
        this.label = label;
        this.nextScene = nextScene;
    }
}

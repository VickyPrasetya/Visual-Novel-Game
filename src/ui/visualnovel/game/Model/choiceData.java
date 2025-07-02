package ui.visualnovel.game.Model;

public class choiceData {
    private String text;
    private String nextScene;

    public choiceData(String text, String nextScene) {
        this.text = text;
        this.nextScene = nextScene;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNextScene() {
        return nextScene;
    }

    public void setNextScene(String nextScene) {
        this.nextScene = nextScene;
    }
}

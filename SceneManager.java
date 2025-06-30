
public class SceneManager {

    private Map<String, VNScene> sceneMap = new HashMap<>();
    private String currentSceneId;

    public void loadScenes() {
        // TODO: Baca dari JSON nanti
        sceneMap.put("scene1", new VNScene("Zia duduk di taman kampus..."));
    }

    public VNScene getCurrentScene() {
        return sceneMap.get(currentSceneId);
    }

    public void goTo(String nextSceneId) {
        currentSceneId = nextSceneId;
    }
}

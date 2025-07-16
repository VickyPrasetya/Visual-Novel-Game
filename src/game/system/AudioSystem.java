package game.system;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class AudioSystem {
    private static AudioSystem instance;
    private MediaPlayer mediaPlayer;
    private double volume = 0.5;
    private boolean muted = false;
    private String currentMusicPath = null;

    private AudioSystem() {}

    public static synchronized AudioSystem getInstance() {
        if (instance == null) instance = new AudioSystem();
        return instance;
    }

    public synchronized void playMusic(String path, boolean loop) {
        if (path == null || path.isEmpty()) return;
        if (path.equals(currentMusicPath) && mediaPlayer != null) {
            if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) mediaPlayer.play();
            return;
        }
        stopMusic();
        try {
            Media media = new Media(new File(path).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
            mediaPlayer.setVolume(muted ? 0.0 : volume);
            mediaPlayer.setOnError(() -> System.err.println("Audio error: " + mediaPlayer.getError()));
            mediaPlayer.play();
            currentMusicPath = path;
        } catch (Exception e) {
            System.err.println("Gagal memutar musik: " + path);
            currentMusicPath = null;
        }
    }

    public synchronized void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            currentMusicPath = null;
        }
    }

    public synchronized void setVolume(double volume) {
        this.volume = volume;
        if (!muted && mediaPlayer != null) mediaPlayer.setVolume(volume);
    }

    public synchronized void setMute(boolean mute) {
        this.muted = mute;
        if (mediaPlayer != null) mediaPlayer.setVolume(mute ? 0.0 : volume);
    }

    public synchronized boolean isMuted() {
        return muted;
    }

    public synchronized double getVolume() {
        return volume;
    }
} 
package View.Utils;

import java.io.*;
import javax.sound.sampled.*;

public class MusicPlayer {
    private static Clip currentClip;
    private static boolean isPlaying = false;
    
    public static void playMusic(String musicPath) {
        if (isPlaying) {
            return;
        }
        
        try {
            if (currentClip != null) {
                currentClip.stop();
                currentClip.close();
            }
            
            InputStream audioSrc = MusicPlayer.class.getResourceAsStream(musicPath);
            if (audioSrc == null) {
                File file = new File(musicPath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                currentClip = AudioSystem.getClip();
                currentClip.open(audioStream);
            } else {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(audioSrc));
                currentClip = AudioSystem.getClip();
                currentClip.open(audioStream);
            }
            
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
            isPlaying = true;
        } catch (Exception e) {
            System.out.println("Error al reproducir música: " + e.getMessage());
        }
    }
    
    public static void stopMusic() {
        if (currentClip != null) {
            currentClip.stop();
            currentClip.close();
            isPlaying = false;
        }
    }
    
    public static void changeMusic(String newMusicPath) {
        stopMusic();
        playMusic(newMusicPath);
    }
}
package client.resources;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sound {
	
    public static Map<String, Clip> soundCache = new HashMap<>();

    public static void initialize() {
    		loadSound("buttonclickon.wav");
    		loadSound("buttonclickoff.wav");
        
    }

    public static Clip getSound(String name) {
        return soundCache.get(name);
    }

    public static Clip loadSound(String fileName) {
    		
        if (soundCache.containsKey(fileName)) {
            return soundCache.get(fileName);
        }
        try {
        		
          
            final InputStream soundInputStream = Sound.class.getResourceAsStream("audio/" + fileName);
            final AudioInputStream sound = AudioSystem.getAudioInputStream(new BufferedInputStream(soundInputStream));
            final Clip clip = AudioSystem.getClip();
            clip.open(sound);
            
            soundCache.put(fileName.split("\\.")[0], clip);
            return clip;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}

package Sound;

import Exceptions.InvalidAudioException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Sound {
    public Clip music;
    List<Clip> soundEffects = new ArrayList<Clip>();

    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/music/menu_music.wav");      // menu
        soundURL[1] = getClass().getResource("/sounds/pistol.wav");
        soundURL[2] = getClass().getResource("/sounds/out_of_ammo_sound.wav");
        soundURL[3] = getClass().getResource("/sounds/ammo_pickup_sound.wav");
        soundURL[4] = getClass().getResource("/sounds/reload.wav");
        soundURL[5] = getClass().getResource("/sounds/zombie_sound.wav");
        soundURL[6] = getClass().getResource("/sounds/helicopter-fly-over-03.wav");
        soundURL[7] = getClass().getResource("/sounds/zombie_death_sound.wav");
        soundURL[8] = getClass().getResource("/sounds/medkit_sound.wav");
        soundURL[9] = getClass().getResource("/sounds/menu_sound.wav");
        soundURL[10] = getClass().getResource("/sounds/level_passed.wav");
        soundURL[11] = getClass().getResource("/sounds/level_lost.wav");
        soundURL[12] = getClass().getResource("/sounds/vaccine_collect.wav");

        // music URLs
        soundURL[14] = getClass().getResource("/music/level1_music.wav");   // level 1
        soundURL[15] = getClass().getResource("/music/rage.wav");           // level 2
        soundURL[16] = getClass().getResource("/music/frantic.wav");        // level 3
        soundURL[17] = getClass().getResource("/music/hellfire.wav");       // level 4
        soundURL[18] = getClass().getResource("/music/metal-dark-matter.wav");      // boss fight
        soundURL[19] = getClass().getResource("/music/victory.wav");        // victory
    }

    public void setFile(int i) throws InvalidAudioException {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new InvalidAudioException();
        }
    }

    public void playSoundEffect(int i) throws InvalidAudioException {
        removeInactiveSE();

        setFile(i);
        soundEffects.add(clip);
        clip.start();
    }

    public void playMusic(int i) throws InvalidAudioException {
        setFile(i);
        music = clip;
        music.loop(Clip.LOOP_CONTINUOUSLY);
        music.start();
    }

    // stops music
    public void stopMusic() {
        if (music != null) {
            music.close();
            music = null;
        }
    }

    public void removeInactiveSE() {
        for (int i = soundEffects.size()-1; i >= 0; i--) {
            Clip c = soundEffects.get(i);
            if (!c.isActive()) {
                c.close();
                soundEffects.remove(i);
            }
        }
    }
}

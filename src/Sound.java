/**
 * Created by Cody on 9/16/2015.
 */
import java.applet.*;
import java.io.File;
import java.net.*;
import javax.sound.sampled.*;
public class Sound { // Holds one audio file
    private Clip sound; // Sound player
    private URL soundPath; // Sound path
    private FloatControl gainControl; // The gain control (basically volume)

    Sound(URL soundPath, float gain) {
        this.soundPath = soundPath;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundPath);
            sound = AudioSystem.getClip();
            sound.open(audioInputStream);
            gainControl = (FloatControl)sound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);
        } catch (Exception e) {
            System.out.println("The sound from file " + String.valueOf(soundPath) + "was not found!");
            System.out.println(e);
        } // Satisfy the catch
    }

    public void openSound() {
        if (!sound.isOpen()) {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundPath);
                sound = AudioSystem.getClip();
                sound.open(audioInputStream);
            } catch (Exception e) {
                System.out.println("The sound from file " + String.valueOf(soundPath) + "was not found!");
                System.out.println(e);
            }
        }
    }

    public void closeSound(){
        sound.close();
    }

    public void loopSound(){
        sound.loop(Clip.LOOP_CONTINUOUSLY); // Loop
    }

    public void stopSound() {
        sound.stop(); // Stop and set the play position at the beginning
        sound.flush();
        sound.setFramePosition(0);
    }

    public void playSound(){
        sound.start(); // Play only once
    }

    public void pauseSound(){
        sound.stop();
    }

    public void changeGain(float gain){
        gainControl.setValue(gain);
    }


    public void restartSound(){
        sound.stop();
        sound.flush();
        sound.setFramePosition(0);
        sound.start();
    }

    public void dispose(){
        sound.close();
        sound = null;
    }

}


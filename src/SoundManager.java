import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/16/2015.
 */
public class SoundManager {

    public static SoundManager manager;
    public static Map<String, Sound> playList;

    private SoundManager(){
        playList = new HashMap<>();
    }

    public static SoundManager getInstance(){
        if (manager == null) {
            manager = new SoundManager();
        }
        return manager;
    }

    public static void add(String name, Sound sound){
        playList.put(name, sound);
    }

    public static void remove(String name){
        playList.get(name).dispose();
        playList.remove(name);
    }

    public static void playSound(String name) {
        playList.get(name).playSound();
    }

    public static void loopSound(String name) {
        playList.get(name).loopSound();
    }

    public static void stopSound(String name){
        playList.get(name).stopSound();
    }

    public static void restartSound(String name){
        playList.get(name).restartSound();
    }

    public static void pauseSound(String name){
        playList.get(name).pauseSound();
    }

    public static void openSound(String name){
        playList.get(name).openSound();
    }

    public static void changeGain(String name, float gain){
        playList.get(name).changeGain(gain);
    }

    public static void closeSound(String name){
        playList.get(name).closeSound();
    }



}

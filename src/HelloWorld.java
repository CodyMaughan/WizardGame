/**
 * Created by Cody on 9/9/2015.
 */

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JButton;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HelloWorld {
    public static void main(String[] args) {

        System.out.println("Hello World!" + "This is a test.");
        Map<String, String> dictionary = new HashMap<>();
        dictionary.put("1", "one");
        dictionary.put("2", "two");
        System.out.println(dictionary.get("1"));
        System.out.println(dictionary.get("2"));
        System.out.println(dictionary.get("3"));

        String fonts[] =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        //for ( int i = 0; i < fonts.length; i++ )
        //{
        //    System.out.println(fonts[i]);
        //}
        AudioFileFormat.Type[] list = AudioSystem.getAudioFileTypes();
        for (int i = 0; i < list.length; i++) {
            System.out.println(list[i]);
        }
    }
}

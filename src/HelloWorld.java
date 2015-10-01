/**
 * Created by Cody on 9/9/2015.
 */

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.swing.JButton;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        List<String> test = new ArrayList<>();
        test.add("A");
        test.add("B");
        test.add("C");
        System.out.println(test.get(0));
        System.out.println(test.get(1));
        test.remove(0);
        System.out.println(test.get(0));
        System.out.println(test.get(1));
    }
}

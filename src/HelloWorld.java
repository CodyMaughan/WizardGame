/**
 * Created by Cody on 9/9/2015.
 */

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
        String filePath = "\\C:\\Users\\Cody\\IdeaProjects\\WizardGame\\WizardGame\\src\\resources\\tmxfiles\\rotationTests.tmx";
        System.out.println(filePath);
        //ReadXMLFile.readTMXFile(filePath);
        System.out.println(Math.pow(2, 31));
        System.out.println((long)Math.pow(2, 31));
        long gid = 2684354578L;
        gid = gid - (long)Math.pow(2, 31);
        System.out.println(gid);
    }
}

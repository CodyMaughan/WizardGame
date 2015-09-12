import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cody on 9/11/2015.
 */
public class KeyMapper {

    private static KeyMapper instance = null;

    private static Map<Integer, String> textMap;

    private KeyMapper() {
        textMap = new HashMap<>();
        textMap.put(KeyEvent.VK_0, "0");
        textMap.put(KeyEvent.VK_1, "1");
        textMap.put(KeyEvent.VK_2, "2");
        textMap.put(KeyEvent.VK_3, "3");
        textMap.put(KeyEvent.VK_4, "4");
        textMap.put(KeyEvent.VK_5, "5");
        textMap.put(KeyEvent.VK_6, "6");
        textMap.put(KeyEvent.VK_7, "7");
        textMap.put(KeyEvent.VK_8, "8");
        textMap.put(KeyEvent.VK_9, "9");
        textMap.put(KeyEvent.VK_A, "A");
        textMap.put(KeyEvent.VK_B, "B");
        textMap.put(KeyEvent.VK_C, "C");
        textMap.put(KeyEvent.VK_D, "D");
        textMap.put(KeyEvent.VK_E, "E");
        textMap.put(KeyEvent.VK_F, "F");
        textMap.put(KeyEvent.VK_G, "G");
        textMap.put(KeyEvent.VK_H, "H");
        textMap.put(KeyEvent.VK_I, "I");
        textMap.put(KeyEvent.VK_J, "J");
        textMap.put(KeyEvent.VK_K, "K");
        textMap.put(KeyEvent.VK_L, "L");
        textMap.put(KeyEvent.VK_M, "M");
        textMap.put(KeyEvent.VK_N, "N");
        textMap.put(KeyEvent.VK_O, "O");
        textMap.put(KeyEvent.VK_P, "P");
        textMap.put(KeyEvent.VK_Q, "Q");
        textMap.put(KeyEvent.VK_R, "R");
        textMap.put(KeyEvent.VK_S, "S");
        textMap.put(KeyEvent.VK_T, "T");
        textMap.put(KeyEvent.VK_U, "U");
        textMap.put(KeyEvent.VK_V, "V");
        textMap.put(KeyEvent.VK_W, "W");
        textMap.put(KeyEvent.VK_X, "X");
        textMap.put(KeyEvent.VK_Y, "Y");
        textMap.put(KeyEvent.VK_Z, "Z");
        textMap.put(KeyEvent.VK_PERIOD, ".");
        textMap.put(KeyEvent.VK_AT, "@");
        textMap.put(KeyEvent.VK_BACK_SLASH, "'\'");
        textMap.put(KeyEvent.VK_CIRCUMFLEX, "^");
        textMap.put(KeyEvent.VK_CLOSE_BRACKET, "]");
        textMap.put(KeyEvent.VK_COLON, ":");
        textMap.put(KeyEvent.VK_COMMA, ",");
        textMap.put(KeyEvent.VK_DOLLAR, "$");
        textMap.put(KeyEvent.VK_EXCLAMATION_MARK, "!");
        textMap.put(KeyEvent.VK_LEFT_PARENTHESIS, "(");
        textMap.put(KeyEvent.VK_MINUS, "-");
        textMap.put(KeyEvent.VK_NUMBER_SIGN, "#");
        textMap.put(KeyEvent.VK_OPEN_BRACKET, "[");
        textMap.put(KeyEvent.VK_NUMPAD0, "0");
        textMap.put(KeyEvent.VK_NUMPAD1, "1");
        textMap.put(KeyEvent.VK_NUMPAD2, "2");
        textMap.put(KeyEvent.VK_NUMPAD3, "3");
        textMap.put(KeyEvent.VK_NUMPAD4, "4");
        textMap.put(KeyEvent.VK_NUMPAD5, "5");
        textMap.put(KeyEvent.VK_NUMPAD6, "6");
        textMap.put(KeyEvent.VK_NUMPAD7, "7");
        textMap.put(KeyEvent.VK_NUMPAD8, "8");
        textMap.put(KeyEvent.VK_NUMPAD9, "9");
        textMap.put(KeyEvent.VK_PLUS, "+");
        textMap.put(KeyEvent.VK_RIGHT_PARENTHESIS, ")");
        textMap.put(KeyEvent.VK_SEMICOLON, ";");
        textMap.put(KeyEvent.VK_SLASH, "/");
        textMap.put(KeyEvent.VK_SPACE, " ");
        textMap.put(KeyEvent.VK_UNDERSCORE, "_");

    }

    public static KeyMapper getInstance() {
        if (instance == null) {
            instance = new KeyMapper();
        }
        return instance;
    }

    public static String getText(int key) {
        return textMap.get(key);
    }

    public static Map<Integer,String> getTextMap() {
        return textMap;
    }

}

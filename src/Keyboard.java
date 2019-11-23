import java.awt.event.KeyEvent;
import java.security.Key;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Keyboard {
    private ArrayList<KeyEvent> keyEvents;
    private HashMap<Integer, Integer> keyMapping;
    private Object mutex = new Object();
    private boolean[] keys = new boolean[16];

    public Keyboard() {
        keyMapping = getDefaultMapping();
    }

    public Keyboard(HashMap<Integer, Integer> keyMapping) {
        this.keyMapping = keyMapping;;
    }

    public void setKeyMapping(HashMap<Integer, Integer> mapping) {
        keyMapping = mapping;
    }

    boolean isKeyPressed(int key) {
        if(key < 16) {
            return keys[key];
        } else {
            return false;
        }
    }

    Optional<Integer> getKeyPressed() {
        for(int i=0; i < 16; ++i) {
            if(keys[i]) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if(keyMapping.containsKey(key))
            keys[keyMapping.get(key)] = true;

    }

    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if(keyMapping.containsKey(key))
            keys[keyMapping.get(key)] = false;

    }

    public static HashMap<Integer, Integer> getDefaultMapping() {
        return new HashMap<Integer, Integer>() {
            {
                put(KeyEvent.VK_1, 1);
                put(KeyEvent.VK_2, 2);
                put(KeyEvent.VK_3, 3);
                put(KeyEvent.VK_Q, 4);
                put(KeyEvent.VK_W, 5);
                put(KeyEvent.VK_E, 6);
                put(KeyEvent.VK_A, 7);
                put(KeyEvent.VK_S, 8);
                put(KeyEvent.VK_D, 9);
                put(KeyEvent.VK_Z, 10);
                put(KeyEvent.VK_X, 0);
                put(KeyEvent.VK_C, 11);
                put(KeyEvent.VK_4, 12);
                put(KeyEvent.VK_R, 13);
                put(KeyEvent.VK_F, 14);
                put(KeyEvent.VK_V, 15);
            }
        };
    }
}

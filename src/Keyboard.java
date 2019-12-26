import java.awt.event.KeyEvent;
import java.security.Key;
import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * class handling keyboard events
 */
public class Keyboard {
    private ArrayList<KeyEvent> keyEvents;
    private HashMap<Integer, Integer> keyMapping;
    private Object mutex = new Object();
    private boolean[] keys = new boolean[16];

    /**
     * default constructor
     */
    public Keyboard() {
        keyMapping = getDefaultMapping();
    }

    /**
     * constructor with custom key mappings
     * @param keyMapping new key mappings
     */
    public Keyboard(HashMap<Integer, Integer> keyMapping) {
        this.keyMapping = keyMapping;;
    }

    /**
     * set key mappings
     * @param mapping new key mappings
     */
    public void setKeyMapping(HashMap<Integer, Integer> mapping) {
        keyMapping = mapping;
    }

    /**
     * check whether specified key is pressed
     * @param key key code to check against
     * @return true if key is pressed, false otherwise
     */
    boolean isKeyPressed(int key) {
        if(key < 16) {
            return keys[key];
        } else {
            return false;
        }
    }

    /**
     * get key code being pressed or empty when none is pressed
     * @return pressed key code or empty
     */
    Optional<Integer> getKeyPressed() {
        for(int i=0; i < 16; ++i) {
            if(keys[i]) {
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    /**
     * called by key event dispatcher sets key
     * @param keyEvent key pressed event from keyboard
     */
    public void keyPressed(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if(keyMapping.containsKey(key))
            keys[keyMapping.get(key)] = true;

    }

    /**
     * called by key dispatcher resets key
     * @param keyEvent key event from keyboard
     */
    public void keyReleased(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        if(keyMapping.containsKey(key))
            keys[keyMapping.get(key)] = false;

    }

    /**
     * return the default mapping
     * @return hashmap mapping keys on real keyboard to CHIP-8's hex keyboard
     */
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

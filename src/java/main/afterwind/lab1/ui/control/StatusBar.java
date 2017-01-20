package afterwind.lab1.ui.control;

import javafx.event.EventTarget;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple Label that retains information of what message should show
 * based on what Object(EventTarget) it is given
 */
public class StatusBar extends Label {

    public final Map<EventTarget, String> messages = new HashMap<>();

    public StatusBar() {
        this.setTextFill(Color.BLACK);
    }

    /**
     * Sets the message based on the target
     */
    public void setMessage(EventTarget n) {
        if (messages.containsKey(n)) {
            setText(messages.get(n));
        }
    }

    /**
     * Adds a message for the respective target
     */
    public void addMessage(EventTarget t, String m) {
        messages.put(t, m);
    }
}

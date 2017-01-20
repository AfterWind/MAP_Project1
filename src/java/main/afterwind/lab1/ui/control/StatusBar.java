package afterwind.lab1.ui.control;

import javafx.event.EventTarget;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class StatusBar extends Label {

    public final Map<EventTarget, String> messages = new HashMap<>();

    public StatusBar() {
        this.setTextFill(Color.BLACK);
    }

    public void setMessage(EventTarget n) {
        if (messages.containsKey(n)) {
            setText(messages.get(n));
        }
    }

    public void addMessage(EventTarget t, String m) {
        messages.put(t, m);
    }
}

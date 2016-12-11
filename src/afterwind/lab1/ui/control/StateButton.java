package afterwind.lab1.ui.control;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StateButton<T> extends Button {

    private final List<T> states = new ArrayList<>();
    private Iterator<T> current = states.iterator();
    private T currentState;

    public StateButton() {
        super();
    }

    public void addState(T state) {
        states.add(state);
        current = states.iterator();
        changeState();
    }

    public void changeState() {
        if (current.hasNext()) {
            currentState = current.next();
        } else {
            current = states.iterator();
            currentState = current.next();
        }
        setText(currentState.toString());
    }

    public T getState() {
        return currentState;
    }
}

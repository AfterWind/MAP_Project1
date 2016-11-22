package afterwind.lab1.ui;

import afterwind.lab1.old_controller.AbstractController;
import afterwind.lab1.entity.IIdentifiable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class ControllerScene<T extends IIdentifiable<Integer>> extends Scene {
    private final AbstractController<T> controller;
    private final Group root;

    public ControllerScene(Group group, AbstractController<T> controller) {
        super(group, 800, 600, Color.AQUA);
        this.root = group;
        this.controller = controller;
//        root.getChildren().add(new CandidateView(controller));
    }

}

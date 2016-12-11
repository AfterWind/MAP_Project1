package afterwind.lab1.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface XMLSerializer<T> {

    Node serialize(Document doc, T e);

    T deserialize(Document doc, Node node);

}

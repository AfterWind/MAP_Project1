package afterwind.lab1.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Serializer for XML files
 * @param <T> serialized entity
 */
public interface XMLSerializer<T> {

    /**
     * Converts an entity to a Node that can be written directly to XML
     */
    Node serialize(Document doc, T e);

    /**
     * Converts a Node to an entity that can be used inside an application
     */
    T deserialize(Document doc, Node node);

}

package afterwind.lab1.entity;

/**
 * Represents how a certain obejct should be serialized in text form
 * @param <T>
 */
public interface ISerializer<T> {
    /**
     * Deserialize a string into a T object
     * @param s input string
     * @return the object
     */
    T deserialize(String s);

    /**
     * Serializes a T object into a string
     * @param e the object
     * @return serialized data
     */
    String serialize(T e);
}

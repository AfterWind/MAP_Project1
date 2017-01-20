package afterwind.lab1.entity;

/**
 * An interface for identifiable objects using a UNIQUE key of type T
 * @param <T> the type of the key
 */
public interface IIdentifiable<T> {

    /**
     * @return identificatorul unic al entitatii
     */
    T getId();
}

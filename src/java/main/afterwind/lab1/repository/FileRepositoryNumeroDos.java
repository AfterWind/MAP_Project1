package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.entity.ISerializer;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;

import java.io.*;
import java.util.List;

/**
 * Represents a Repository that writes/reads data from a file
 * @param <T> data
 * @param <K> key
 */
public class FileRepositoryNumeroDos<T extends IIdentifiable<K>, K> extends PaginatedRepository<T, K> {

    private final String filename;

    public FileRepositoryNumeroDos(IValidator<T> validator, String file, int entitiesPerPage) {
        super(validator, entitiesPerPage);
        this.filename = file;
        read();
    }

    /**
     * Reads all the lines from a file
     */
    protected void read() {
        try(ObjectInputStream br = new ObjectInputStream(new FileInputStream(filename))) {
            T[] data = (T[]) br.readObject();
            for (T e : data) {
                add(e);
            }
        } catch (EOFException e2) {
            System.out.println("File is empty, no data to read");
        } catch (FileNotFoundException e1) {
            System.err.println("File '" + filename + "' not found!");
        } catch (IOException | ValidationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all the data to the file
     */
    protected void write() {
        try (ObjectOutputStream bw = new ObjectOutputStream(new FileOutputStream(filename))){
            bw.writeObject(data.toArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(T e) throws ValidationException {
        super.add(e);
        write();
    }

    @Override
    public void update(K k, T data) {
        super.update(k, data);
        write();
    }

    @Override
    public void remove(T e) {
        super.remove(e);
        write();
    }
}
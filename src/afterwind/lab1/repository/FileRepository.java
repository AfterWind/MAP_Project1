package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.entity.ISerializer;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;

import java.io.*;

/**
 * Represents a Repository that writes/reads data from a file
 * @param <T>
 * @param <K>
 */
public class FileRepository<T extends IIdentifiable<K>, K> extends Repository<T, K> {

    private final ISerializer<T> serializer;
    private final String filename;

    public FileRepository(IValidator<T> validator, ISerializer<T> serializer, String file) {
        super(validator);
        this.serializer = serializer;
        this.filename = file;
        read();
    }

    /**
     * Reads all the lines from a file
     */
    protected void read() {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            super.add(serializer.deserialize(br.readLine()));
        } catch (IOException | ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all the data to the file
     */
    protected void write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
            for (T e : data) {
                bw.write(serializer.serialize(e));
                bw.newLine();
            }
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
    public void remove(T e) {
        super.remove(e);
        write();
    }
}

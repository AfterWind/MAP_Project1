package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.entity.ISerializer;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;

import java.io.*;

/**
 * Represents a Repository that writes/reads data from a file
 * @param <T> data
 * @param <K> key
 */
public class FileRepository<T extends IIdentifiable<K>, K> extends PaginatedRepository<T, K> {

    private final ISerializer<T> serializer;
    private final String filename;

    public FileRepository(IValidator<T> validator, ISerializer<T> serializer, String file, int entitiesPerPage) {
        super(validator, entitiesPerPage);
        this.serializer = serializer;
        this.filename = file;
        read();
    }

    /**
     * Reads all the lines from a file
     */
    protected void read() {
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                if (!"".equals(line)) {
                    T t = serializer.deserialize(line);
                    if (t == null) {
                        System.err.println("Linia: '" + line + "' este invalida, se ignora!");
                    } else {
                        super.add(t);
                    }
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e1) {
            System.err.println("File '" + filename + "' not found!");
        } catch (IOException | ValidationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all the data to the file
     */
    protected void write() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
            for (T e : getData()) {
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

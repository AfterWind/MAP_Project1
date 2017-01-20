package afterwind.lab1.config;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;

/**
 * Simple JSON configuration file
 */
public class Config {

    public static final String path = "res/config.json";

    public static String datasourceType = "fileXML";
    public static String datasourcePath = "res/data/";
    public static String reportsPath = "res/reports/";

    /**
     * Initializes data from the config and creates it if needed
     */
    public static void init() {
        try {
            read();
        } catch (IOException e) {
            System.err.println("Config file ("+ path +") corrupted... closing");
            throw new RuntimeException(e);
        }
    }

    private static void read() throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            throw new IOException("Failed to create directories");
        }
        if (file.exists()) {
            JsonReader reader = new JsonReader(new FileReader(file));
            reader.beginObject();
            while (reader.peek() != JsonToken.END_OBJECT) {
                switch (reader.nextName()) {
                    case "datasourceType":
                        datasourceType = reader.nextString();
                        break;
                    case "datasourcePath":
                        datasourcePath = reader.nextString();
                        break;
                    case "reportsPath":
                        reportsPath = reader.nextString();
                        break;
                    default:
                        reader.nextString();
                        break;
                }
            }
            reader.endObject();
            reader.close();
        } else {
            JsonWriter writer = new JsonWriter(new FileWriter(file));
            writer.setIndent("    ");
            writer.beginObject();
            writer.name("COMMENT").value("The type of datasource. Can be any of the following: fileXML, fileText, sqlite");
            writer.name("datasourceType").value(datasourceType);
            writer.name("COMMENT").value("The folder to the datasource.");
            writer.name("datasourcePath").value(datasourcePath);
            writer.name("COMMENT").value("The folder where the reports are saved.");
            writer.name("reportsPath").value(reportsPath);
            writer.endObject();
            writer.close();
        }
        check();
    }

    private static void check() {
        File file = new File(datasourcePath);
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Failed to create directories for the data");
        }
        file = new File(reportsPath);
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Failed to create directories for the data");
        }
    }

}

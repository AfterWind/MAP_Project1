package afterwind.lab1.repository;

import afterwind.lab1.entity.IIdentifiable;
import afterwind.lab1.entity.XMLSerializer;
import afterwind.lab1.exception.ValidationException;
import afterwind.lab1.validator.IValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class XMLRepository<T extends IIdentifiable<K>, K> extends PaginatedRepository<T, K> {

    private final XMLSerializer<T> serializer;
    private final String filename;

    public XMLRepository(IValidator<T> validator, XMLSerializer<T> serializer, String file, int entitiesPerPage) {
        super(validator, entitiesPerPage);
        this.serializer = serializer;
        this.filename = file;
        read();
    }

    /**
     * Reads all the lines from a file
     */
    protected void read() {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " not found... Creating");
            write();
            return;
        }
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new FileInputStream(filename));
            NodeList elements = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                if (elements.item(i) instanceof Element) {
                    T t = serializer.deserialize(doc, elements.item(i));
                    if (t == null) {
                        System.err.println("Nodul: '" + elements.item(i) + "' este invalid, se ignora!");
                    } else {
                        super.add(t);
                    }
                }
            }
        } catch (FileNotFoundException e1) {
            System.err.println("File '" + filename + "' not found!");
        } catch (IOException | ValidationException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes all the data to the file
     */
    protected void write() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("list");
            doc.appendChild(root);
            for (T t : getData()) {
                root.appendChild(serializer.serialize(doc, t));
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));
            transformer.transform(source, result);

        } catch (TransformerException | ParserConfigurationException e) {
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

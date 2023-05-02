import com.fasterxml.jackson.databind.ObjectMapper;
import documentGenerator.word;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class jsonHandler {

    public static void writeOnJson(Map data) {

        ObjectMapper mapper = new ObjectMapper();

        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File("src/main/java/facturas/data.json"), data);
            System.out.println("Data written to data.json");
            word.writeOnWord();
        } catch (IOException | InvalidFormatException ex) {
            ex.printStackTrace();
        }
    }

}

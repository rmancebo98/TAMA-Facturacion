package documentGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import util.notifications;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class jsonHandler {

    public static void writeOnFinalJson(Map data) {

        ObjectMapper mapper = new ObjectMapper();

        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File("src/main/resources/json/factura.json"), data);
            word.writeOnWord();
            notifications.showPopUpNotification("Factura creada", "Operación exitosa");
        } catch (IOException | InvalidFormatException ex) {
            notifications.showErrorNotification("Error guardando informacion");
            ex.printStackTrace();
        }
    }

}

package documentGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.core;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import util.ncf;
import util.notifications;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class jsonHandler {

    public static void writeOnFinalJson(Map data) {

        ObjectMapper mapper = new ObjectMapper();

        // Write the modified map back to the JSON file
        int result =
                JOptionPane.showConfirmDialog(null, "Esta seguro que desea generar la factura?"
                        , "Generar factura?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            try {
                mapper.writeValue(new File(core.sourceFolder + "/json/factura.json"), data);
                word.writeOnWord();
                notifications.showPopUpNotification("Factura creada", "Operación exitosa");
                core.NCFTxt.setText(ncf.getLastNCF());
            } catch (IOException | InvalidFormatException ex) {
                notifications.showErrorNotification("Error guardando informacion");
                ex.printStackTrace();
            }
        }

    }

}

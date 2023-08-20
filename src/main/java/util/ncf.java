package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ncf {

    static String NCF = "B0100000001";

    public static String getLastNCF() {
        // Get a value from the JSON object by its key
        Map<String, Map<String, String>> receiptJson = readReceiptJson();
        Map<String, String> generalInfo = receiptJson.get("INFORMACION GENERAL");
        String currentNFC = generalInfo.get("Numero de Comprobante Fiscal");
        if (currentNFC.equals("")) {
            return NCF;
        } else {
            String numericPart = currentNFC.substring(3);

            // Incrementar el valor numérico en 1
            long incrementedValue = Long.parseLong(numericPart) + 1;

            // Combinar el prefijo "B01" con el valor numérico incrementado
            NCF = "B01" + String.format("%09d", incrementedValue);

            return NCF;
        }
    }

    public static Map<String, Map<String, String>> readReceiptJson() {
        ObjectMapper mapper = new ObjectMapper();
        Map data = null;

        try {
            data = mapper.readValue(new File(core.sourceFolder + "/json/factura.json"), Map.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error generando documento");
        }
        return data;
    }
}

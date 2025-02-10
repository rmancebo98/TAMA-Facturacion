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
        Map<String, Map<String, String>> receiptJson = readJson("/json/factura.json",
                "Error generando documento");
        Map<String, String> generalInfo = receiptJson.get("INFORMACION GENERAL");
        String currentNFC = generalInfo.get("Numero de Comprobante Fiscal");
        if (currentNFC.equals("")) {
            return NCF;
        } else {
            String numericPart = currentNFC.substring(3);

            // Incrementar el valor numérico en 1
            long incrementedValue = Long.parseLong(numericPart) + 1;

            // Combinar el prefijo "B01" con el valor numérico incrementado
            NCF = "B01" + String.format("%08d", incrementedValue);

            return NCF;
        }
    }

    public static String getMaxNCF() {
        Map<String, Map<String, String>> receiptJson = readJson("/json/config.json", "Error leyendo configuraciones");
        Map<String, String> generalInfo = receiptJson.get("NCF");
        String currentNFC = generalInfo.get("maxNCF");
        if (currentNFC.equals("")) {
            return NCF;
        }
        String numericPart = currentNFC.substring(3);

        // Extract numeric part
        long formattedNumber = Long.parseLong(numericPart);

        // Combinar el prefijo "B01" con el valor numérico
        NCF = "B01" + String.format("%08d", formattedNumber);

        return NCF;

    }

    public static Map<String, Map<String, String>> readJson(String path, String errorMsg) {
        ObjectMapper mapper = new ObjectMapper();
        Map data = null;

        try {
            data = mapper.readValue(new File(core.sourceFolder + path), Map.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification(errorMsg);
        }
        return data;
    }

    public static Boolean ncfCloseToMax() {
        long max = Long.parseLong(getMaxNCF().substring(3));
        long current = Long.parseLong(getLastNCF().substring(3)) - 1;
        return max - current <= 5;
    }
}

package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ncf {

    static ObjectMapper mapper = new ObjectMapper();
    static String NCF = "B0100000001";

    public static String getLastNCF() {
        try {
            JsonNode root = mapper.readTree(new File("src/main/java/facturas/factura.json"));
            // Get a value from the JSON object by its key
            String currentNFC = root.get("Numero de Comprobante Fiscal").asText();
            if (currentNFC.equals("")) {
                return NCF;
            }
            String numericPart = currentNFC.substring(3);

            // Incrementar el valor numérico en 1
            long incrementedValue = Long.parseLong(numericPart) + 1;

            // Combinar el prefijo "B01" con el valor numérico incrementado
            NCF = "B01" + String.format("%09d", incrementedValue);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return NCF;
    }
}

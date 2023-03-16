package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ncf {

    static ObjectMapper mapper = new ObjectMapper();
    static int NCF = 0;

    public static int getLastNCF() {

        try {
            JsonNode root = mapper.readTree(new File("src/main/java/facturas/data.json"));
            // Get a value from the JSON object by its key
            NCF = root.get("Numero de Comprobante Fiscal").asInt() + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NCF;
    }
}

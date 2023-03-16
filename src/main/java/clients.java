import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class clients {

    private static JFrame addFrame;
    private static final String clientsPath = "src/main/java/clients/clients.json";

    public static void createClientWindow() {
        addFrame = new JFrame("Agregar cliente");
        JPanel addPanel = new JPanel();
        JButton addClient;

        addPanel.add(new JLabel("Nombre del cliente :"));
        JTextField nameTxt = new JTextField(25);
        addPanel.add(nameTxt);

        addPanel.add(new JLabel("RNC:"));
        JTextField rncTxt = new JTextField(25);
        addPanel.add(rncTxt);

        addClient = new JButton("Crear cliente");

        addClient.addActionListener(e -> {
            addValuesOnJson(nameTxt.getText(), rncTxt.getText());
        });

        addPanel.add(addClient);

        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(300, 175));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static void showFrame() {
        createClientWindow();
        addFrame.setVisible(true);
    }

    public static Map<String, String> readClientsJson() {
        ObjectMapper mapper = new ObjectMapper();
        Map data = null;

        try {
            data = mapper.readValue(new File(clientsPath), Map.class);
        } catch (IOException ex) {
            // If the file doesn't exist, create a new empty map
            data = new HashMap<String, String>();
        }
        return data;
    }

    public static void addValuesOnJson(String client, String RNC) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = readClientsJson();

        // Add the string to the map
        data.put(client.toUpperCase(), RNC);

        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File(clientsPath), data);
            System.out.println("Data written to clients.json");
            notifications.showPopUpNotification("Cliente añadido","Operación exitosa");
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showPopUpNotification("Error añadiendo cliente","Hubo un error");
        }
    }

    public static Set<String> getAllKeysFromJson() {
        // Collect all keys into a set and return it
        return readClientsJson().keySet();
    }

    public static String getClientRNC(String client) {
        String actualRnc;
        try {
            actualRnc = readClientsJson().get(client);
        } catch (NullPointerException nE) {
            actualRnc = "";
        }
        return actualRnc;
    }
}

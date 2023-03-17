import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class clients {

    private static JFrame addFrame;
    private static JFrame deleteFrame;
    private static final String clientsPath = "src/main/java/clients/clients.json";

    public static void createClientWindow() {
        addFrame = new JFrame("Agregar cliente");
        JPanel addPanel = new JPanel();
        JButton addClient;
        JButton deleteClient;

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

        deleteClient = new JButton("Ir a eliminar cliente");
        deleteClient.addActionListener(e -> showAddClientFrame());

        addPanel.add(addClient);
        addPanel.add(deleteClient);
        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(300, 175));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static void createDeleteClientWindow() {
        deleteFrame = new JFrame("Eliminar cliente");
        JPanel deletePanel = new JPanel();
        JButton deleteClient;

        deleteFrame.add(new JLabel("Clientes:"));
        String[] options = clients.getAllKeysFromJson().toArray(new String[0]);
        JComboBox<String> clientsDropDown = new JComboBox<>(options);

        deleteClient = new JButton("Eliminar cliente");

//        deleteClient.addActionListener(e -> addValuesOnJson(nameTxt.getText(), rncTxt.getText()));
        //TODO: show the clients in a dropdown list and add an action listener to delete the delected one from clients.json
        deletePanel.add(clientsDropDown);
        deletePanel.add(deleteClient);

        deleteFrame.add(deletePanel);
        deleteFrame.setPreferredSize(new Dimension(300, 175));
        deleteFrame.pack();
        deleteFrame.setLocationRelativeTo(null);
    }

    public static void showAddClientFrame() {
        createDeleteClientWindow();
        deleteFrame.setVisible(true);
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
        Map<String, String> clientData = readClientsJson();

        client = client.toUpperCase();

        // Add the string to the map
        clientData.put(client, RNC);

        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File(clientsPath), clientData);
            System.out.println("Data written to clients.json");

            if (checkIfClientExist(client)) {
                notifications.showPopUpNotification("El cliente ya existe, actualizando RNC", "Actualizando cliente");
            } else {
                notifications.showPopUpNotification("Cliente añadido", "Operación exitosa");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo cliente");
        }
    }

    public static void deleteValuesOnJson(String client) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> clientData = readClientsJson();

        client = client.toUpperCase();
    }

    public static boolean checkIfClientExist(String client) {
        return readClientsJson().get(client) != null && !Objects.equals(readClientsJson().get(client), "");
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

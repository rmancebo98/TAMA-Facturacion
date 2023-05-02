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
    static JComboBox<String> clientsDropDown;

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
        formatter.setNumericOnly(rncTxt);

        addClient = new JButton("Crear cliente");

        addClient.addActionListener(e -> {
            addValuesOnJson(nameTxt.getText(), rncTxt.getText());
            updateClientsDropDown();
            core.updateClientsDropdown();
        });

        addPanel.add(new JLabel("Clientes:"));
        String[] options = clients.getAllKeysFromJson().toArray(new String[0]);
        clientsDropDown = new JComboBox<>(options);


        deleteClient = new JButton("Eliminar cliente");
        deleteClient.setBackground(Color.RED);

        deleteClient.addActionListener(e -> {
            deleteValuesOnJson(clientsDropDown.getSelectedItem().toString());
            updateClientsDropDown();
            core.updateClientsDropdown();
        });

        addPanel.add(addClient);
        addPanel.add(clientsDropDown);
        addPanel.add(deleteClient);
        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(300, 300));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static void updateClientsDropDown() {
        clientsDropDown.setModel(new DefaultComboBoxModel<>(clients.getAllKeysFromJson().toArray(new String[0])));
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
            if (checkIfClientExist(client)) {
                notifications.showPopUpNotification("El cliente ya existe, actualizando RNC", "Actualizando cliente");
            } else {
                notifications.showPopUpNotification("Cliente añadido", "Operación exitosa");
            }
            mapper.writeValue(new File(clientsPath), clientData);
            System.out.println("Client " + client + " added to clients.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo cliente");
        }

    }

    public static void deleteValuesOnJson(String client) {
        ImageIcon icon = new ImageIcon("src/main/resources/icons/alert.png");
        int result =
                JOptionPane.showConfirmDialog(null, "Esta seguro que desea borrar el cliente " + client + "?", "Borrar cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, icon);

        if (result == JOptionPane.OK_OPTION) {
            // Read the JSON file into a map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> clientData = readClientsJson();

            client = client.toUpperCase();

            // Add the string to the map
            clientData.remove(client);

            // Write the modified map back to the JSON file
            try {
                mapper.writeValue(new File(clientsPath), clientData);
                System.out.println("Client " + client + " deleted from clients.json");
                notifications.showPopUpNotification("Cliente " + client + " eliminado", "Cliente eliminado");
            } catch (IOException ex) {
                ex.printStackTrace();
                notifications.showErrorNotification("Error eliminando cliente");
            }
        }
    }

    public static boolean checkIfClientExist(String client) {
        String clientFromJson = readClientsJson().get(client);
        return clientFromJson != null && clientFromJson != "" && !clientFromJson.isEmpty();
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

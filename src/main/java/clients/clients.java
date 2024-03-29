package clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import util.formatter;
import util.notifications;
import core.core;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class clients {

    private static JFrame addFrame;
    private static final String clientsPath = core.sourceFolder + "/json/clients.json";
    static JComboBox<String> clientsDropDown;

    public static void createClientWindow() {
        addFrame = new JFrame("Agregar cliente");
        JPanel addPanel = new JPanel();
        JButton addClient;
        JButton deleteClient;
        JTextField rncTxt = new JTextField(25);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.add(new JLabel("Nombre del cliente :"));
        String[] options = clients.getAllKeysFromJson().toArray(new String[0]);
        clientsDropDown = new JComboBox<>(options);
        clientsDropDown.setSelectedIndex(-1);
        clientsDropDown.setEditable(true);
        clientsDropDown.addActionListener(e -> {
            if (clientsDropDown.getSelectedIndex() != -1) {
                rncTxt.setText(readClientsJson().get(clientsDropDown.getSelectedItem()).get("RNC"));
            }
        });
        namePanel.add(clientsDropDown);
        addPanel.add(namePanel);

        JPanel deleteClientPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel rncPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rncPanel.add(new JLabel("RNC:"));
        rncPanel.add(rncTxt);
        formatter.setNumericOnly(rncTxt);
        addPanel.add(rncPanel);



        JPanel createClientPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addClient = new JButton("Crear cliente");

        addClient.addActionListener(e -> {
            addValuesOnJson((String) clientsDropDown.getSelectedItem(), rncTxt.getText());
            updateClientsDropDown();
            core.updateClientsDropdown();
            try {
                clientsDropDown.setSelectedIndex(options.length);
            } catch (Exception ignore) {}
        });

        addPanel.add(createClientPanel);


        deleteClient = new JButton("Eliminar cliente");
        deleteClient.setBackground(Color.RED);

        deleteClient.addActionListener(e -> {
            deleteValuesOnJson(clientsDropDown.getSelectedItem().toString());
            updateClientsDropDown();
            core.updateClientsDropdown();
            clientsDropDown.setSelectedIndex(-1);
        });

        deleteClientPanel.add(addClient);
        deleteClientPanel.add(deleteClient);

        addPanel.add(deleteClientPanel);
        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(600, 300));
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

    public static Map<String, Map<String, String>> readClientsJson() {
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
        Map<String, Map<String, String>> clientData = readClientsJson();
        Map<String, String> clientInfo = new HashMap<>();

        client = client.toUpperCase();

        // Add the string to the map
        clientInfo.put("RNC", RNC);
//        clientInfo.put("Dirección", address);

        clientData.put(client, clientInfo);
        // Write the modified map back to the JSON file
        try {
            if (checkIfClientExist(client)) {
                notifications.showInformativeNotification("El cliente ya existe, actualizando RNC", "Actualizando cliente");
            } else {
                notifications.showInformativeNotification("Cliente añadido", "Operación exitosa");
            }
            mapper.writeValue(new File(clientsPath), clientData);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo cliente");
        }

    }

    public static void deleteValuesOnJson(String client) {
        int result =
                JOptionPane.showConfirmDialog(null, "Esta seguro que desea borrar el cliente " + client + "?", "Borrar cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Read the JSON file into a map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Map<String, String>> clientData = readClientsJson();

            client = client.toUpperCase();

            // Add the string to the map
            clientData.remove(client);

            // Write the modified map back to the JSON file
            try {
                mapper.writeValue(new File(clientsPath), clientData);
            } catch (IOException ex) {
                ex.printStackTrace();
                notifications.showErrorNotification("Error eliminando cliente");
            }
        }
    }

    public static boolean checkIfClientExist(String client) {
        try {
            Map<String, String> clientFromJson = readClientsJson().get(client);
            return clientFromJson != null && !clientFromJson.isEmpty();
        } catch (NullPointerException ignore) {
            return false;
        }
    }

    public static Set<String> getAllKeysFromJson() {
        // Collect all keys into a set and return it
        return readClientsJson().keySet();
    }

    public static String getClientRNC(String client) {
        String actualRnc;
        try {
            actualRnc = readClientsJson().get(client).get("RNC");
        } catch (NullPointerException nE) {
            actualRnc = "";
        }
        return actualRnc;
    }

    public static String getClientAddress(String client) {
        String actualAddress;
        try {
            actualAddress = readClientsJson().get(client).get("Dirección");
        } catch (NullPointerException ignore) {
            actualAddress = "";
        }
        return actualAddress;
    }
}

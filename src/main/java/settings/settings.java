package settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.core;
import fees.fees;
import util.formatter;
import util.ncf;
import util.notifications;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class settings {

    private static JFrame addFrame;
    private static final String configPath = core.sourceFolder + "/json/config.json";
    static JTextField maximumRNCTxt = new JTextField(11);

    public static void createSettingsWindow() {
        addFrame = new JFrame("Configuración");
        JPanel settingsPanel = new JPanel();
        JButton addRNCLimit;

        settingsPanel.add(new JLabel("RNC disponible hasta:"));
        try {
            maximumRNCTxt = new JTextField(String.valueOf(ncf.getMaxNCF()), 11);
        } catch (NullPointerException ignore) {
            maximumRNCTxt = new JTextField("B0100000001");
        }

        addRNCLimit = new JButton("Actualizar");

        addRNCLimit.addActionListener(e -> {
            addValuesOnJson("maxNCF", getMaxTxtValue());
            notifications.showInformativeNotification("Maximo actualizado", "NCF Maximo actualizado");
        });

        settingsPanel.add(maximumRNCTxt);
        settingsPanel.add(addRNCLimit);
        addFrame.add(settingsPanel);
        addFrame.setPreferredSize(new Dimension(400, 200));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static String getMaxTxtValue() {
        return maximumRNCTxt.getText();
    }

    public static void addValuesOnJson(String setting, String value) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String,  Map<String, String>> configData = new HashMap<>();
        Map<String, String> newSettings = new HashMap<>();
        newSettings.put(setting, value);
        configData.put("NCF", newSettings);
        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File(configPath), configData);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo setting");
        }
    }

    public static Map<String, String> readConfigJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map data = null;

        try {
            data = objectMapper.readValue(new File(configPath), TreeMap.class);
        } catch (IOException ex) {
            // If the file doesn't exist, create a new empty map
            data = new HashMap<String, String>();
        }
        return data;
    }


    public static void showFrame() {
        createSettingsWindow();
        addFrame.setVisible(true);
    }
}

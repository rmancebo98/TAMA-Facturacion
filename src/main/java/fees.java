import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class fees {

    private static JFrame feesFrame;
    private static final String feesPath = "src/main/java/fees/fees.json";
    static JComboBox<String> feesDropDown;

    public static void createFeesWindow() {
        feesFrame = new JFrame("Agregar Honorarios");
        JPanel feesPanel = new JPanel();
        // Set the feesPanel layout to BoxLayout
        feesPanel.setLayout(new BoxLayout(feesPanel, BoxLayout.Y_AXIS));

        // Add the components to the feesPanel
        feesPanel.add(Box.createVerticalGlue()); // Adds glue at the top to center the components vertically

        JButton addFee;
        JButton deleteFee;

        feesPanel.add(new JLabel("Honorario:"));
        JTextField feeTxt = new JTextField(25);
        feesPanel.add(feeTxt);

        feesPanel.add(new JLabel("Monto:"));
        JTextField amountTxt = new JTextField(25);
        feesPanel.add(amountTxt);

        addFee = new JButton("Agregar honorario");

        addFee.addActionListener(e -> {
            addValuesOnJson(feeTxt.getText(), amountTxt.getText());
            updateFeesDropDown();
        });
        feesPanel.add(addFee);

        String[] options = fees.getAllKeysFromJson().toArray(new String[0]);
        feesDropDown = new JComboBox<>(options);
        feesPanel.add(feesDropDown);

        deleteFee = new JButton("Eliminar Honorario");
        deleteFee.setBackground(Color.RED);
        feesPanel.add(deleteFee);

        feesFrame.add(feesPanel);
        feesFrame.setPreferredSize(new Dimension(300, 205));
        feesFrame.pack();
        feesFrame.setLocationRelativeTo(null);
    }

    private static void updateFeesDropDown() {
        feesDropDown.setModel(new DefaultComboBoxModel<>(getAllKeysFromJson().toArray(new String[0])));
    }

    public static void showFeesFrame() {
        createFeesWindow();
        feesFrame.setVisible(true);
    }

    private static Map<String, String> readFeesJson() {
        ObjectMapper mapper = new ObjectMapper();
        Map data = null;

        try {
            data = mapper.readValue(new File(feesPath), Map.class);
        } catch (IOException ex) {
            // If the file doesn't exist, create a new empty map
            data = new HashMap<String, String>();
        }
        return data;
    }

    private static void addValuesOnJson(String fee, String amount) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> clientData = readFeesJson();

        fee = fee.toUpperCase();

        // Add the string to the map
        clientData.put(fee, amount);
        // Write the modified map back to the JSON file
        try {
            if (checkIfFeeExist(fee)) {
                notifications.showPopUpNotification("El honorario ya existe, actualizando monto", "Actualizando honorario");
            } else {
                notifications.showPopUpNotification("Honorario añadido", "Operación exitosa");
            }
            mapper.writeValue(new File(feesPath), clientData);
            System.out.println("Fee " + fee + " added to fees.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo honorario");
        }

    }

    private static void deleteValuesOnJson(String fee) {
        ImageIcon icon = new ImageIcon("src/main/resources/icons/alert.png");
        int result = JOptionPane.showConfirmDialog(null, "Esta seguro que desea borrar el honorario " + fee + "?", "Borrar cliente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, icon);

        if (result == JOptionPane.OK_OPTION) {
            // Read the JSON file into a map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> feesData = readFeesJson();

            fee = fee.toUpperCase();

            // Add the string to the map
            feesData.remove(fee);

            // Write the modified map back to the JSON file
            try {
                mapper.writeValue(new File(feesPath), feesData);
                System.out.println("Fee " + fee + " deleted from fee.json");
                notifications.showPopUpNotification("Honorario " + fee + " eliminado", "Operación Exitosa");
            } catch (IOException ex) {
                ex.printStackTrace();
                notifications.showErrorNotification("Error eliminando honorario");
            }
        }
    }

    private static boolean checkIfFeeExist(String fee) {
        String clientFromJson = readFeesJson().get(fee);
        return clientFromJson != null && clientFromJson != "" && !clientFromJson.isEmpty();
    }

    private static Set<String> getAllKeysFromJson() {
        // Collect all keys into a set and return it
        return readFeesJson().keySet();
    }
}

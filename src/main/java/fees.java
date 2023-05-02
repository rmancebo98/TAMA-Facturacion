import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class fees {

    private static JFrame addFrame;
    private static final String feesPath = "src/main/java/fees/fees.json";
    static JComboBox<String> feesDropDown;

    public static void createFeeWindow() {
        addFrame = new JFrame("Agregar honorario");
        JPanel addPanel = new JPanel();
        JButton addFee;
        JButton deleteFee;

        addPanel.add(new JLabel("Nombre del honorario:"));
        JTextField nameTxt = new JTextField(25);
        addPanel.add(nameTxt);

        addPanel.add(new JLabel("Monto:"));
        JTextField amountTxt = new JTextField(25);
        addPanel.add(amountTxt);
        formatter.formatFieldAsNumber(amountTxt);

        addFee = new JButton("Agregar honorario");

        addFee.addActionListener(e -> {
            addValuesOnJson(nameTxt.getText(), amountTxt.getText());
            updateFeesDropDown();
        });

        addPanel.add(new JLabel("Honorarios:"));
        String[] options = fees.getAllKeysFromJson().toArray(new String[0]);
        feesDropDown = new JComboBox<>(options);


        deleteFee = new JButton("Eliminar honorario");
        deleteFee.setBackground(Color.RED);

        deleteFee.addActionListener(e -> {
            deleteValuesOnJson(feesDropDown.getSelectedItem().toString());
            updateFeesDropDown();
        });

        addPanel.add(addFee);
        addPanel.add(feesDropDown);
        addPanel.add(deleteFee);
        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(300, 300));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static void updateFeesDropDown() {
        feesDropDown.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
    }

    public static void showFrame() {
        createFeeWindow();
        addFrame.setVisible(true);
    }

    public static Map<String, String> readFeesJson() {
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

    public static String getFeeAmount(String fee) {
        String feeAmount;
        try {
            feeAmount = readFeesJson().get(fee);
        } catch (NullPointerException nE) {
            feeAmount = "";
        }
        return feeAmount;
    }

    public static void addValuesOnJson(String fee, String amount) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> feeData = readFeesJson();

        fee = fee.toUpperCase();

        // Add the string to the map
        feeData.put(fee, amount);
        // Write the modified map back to the JSON file
        try {
            if (checkIfFeeExist(fee)) {
                notifications.showPopUpNotification("El honorario ya existe, actualizando el monto", "Actualizando honorario");
            } else {
                notifications.showPopUpNotification("Honorario añadido", "Operación exitosa");
            }
            mapper.writeValue(new File(feesPath), feeData);
            System.out.println("Fee " + fee + " added to fees.json");
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo honorario");
        }

    }

    public static void deleteValuesOnJson(String fee) {
        ImageIcon icon = new ImageIcon("src/main/resources/icons/alert.png");
        int result =
                JOptionPane.showConfirmDialog(null, "Esta seguro que desea borrar el honorario " + fee + "?", "Borrar honorario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, icon);

        if (result == JOptionPane.OK_OPTION) {
            // Read the JSON file into a map
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> feeData = readFeesJson();

            fee = fee.toUpperCase();

            // Add the string to the map
            feeData.remove(fee);

            // Write the modified map back to the JSON file
            try {
                mapper.writeValue(new File(feesPath), feeData);
                System.out.println("Fee " + fee + " deleted from fees.json");
                notifications.showPopUpNotification("Honorario " + fee + " eliminado", "Honorario eliminado");
            } catch (IOException ex) {
                ex.printStackTrace();
                notifications.showErrorNotification("Error eliminando honorario");
            }
        }
    }

    public static boolean checkIfFeeExist(String fee) {
        String feeFromJson = readFeesJson().get(fee);
        return feeFromJson != null && feeFromJson != "" && !feeFromJson.isEmpty();
    }

    public static Set<String> getAllKeysFromJson() {
        // Collect all keys into a set and return it
        return readFeesJson().keySet();
    }

}

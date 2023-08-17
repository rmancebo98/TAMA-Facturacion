package fees;

import com.fasterxml.jackson.databind.ObjectMapper;
import util.date;
import util.format;
import util.formatter;
import util.notifications;
import core.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class fees {

    private static JFrame addFrame;
    private static final String feesPath = "src/main/resources/json/fees.json";
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
            updateFeesDropDownInCore();
        });

        addPanel.add(new JLabel("Honorarios:"));
        String[] options = fees.getAllKeysFromJson().toArray(new String[0]);
        feesDropDown = new JComboBox<>(options);


        deleteFee = new JButton("Eliminar honorario");
        deleteFee.setBackground(Color.RED);

        deleteFee.addActionListener(e -> {
            deleteValuesOnJson(feesDropDown.getSelectedItem().toString());
            updateFeesDropDown();
            updateFeesDropDownInCore();
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
            feeAmount = "$0";
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
            System.out.println("Fee " + fee + " added to fees.fees.json");
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
                System.out.println("Fee " + fee + " deleted from fees.fees.json");
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

    public static void formatFeesTxtInCore() {
        core.firstFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.firstFeeTxt.setText(format.formatAsMoney(core.firstFeeTxt.getText()));
            }
        });
        core.secondFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.secondFeeTxt.setText(format.formatAsMoney(core.secondFeeTxt.getText()));
            }
        });
        core.thirdFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.thirdFeeTxt.setText(format.formatAsMoney(core.thirdFeeTxt.getText()));
            }
        });
        core.forthFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.forthFeeTxt.setText(format.formatAsMoney(core.forthFeeTxt.getText()));
            }
        });
        core.fifthFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.fifthFeeTxt.setText(format.formatAsMoney(core.fifthFeeTxt.getText()));
            }
        });
    }

    public static JTextField fillFeeValueInCore(JComboBox<String> comboBox) {
        JTextField txtField;
        if (comboBox.getSelectedItem() == null) {
            txtField = new JTextField("$0.00", 12);
        } else {
            txtField = new JTextField(fees.getFeeAmount(comboBox.getSelectedItem().toString()), 12);
        }

        return txtField;
    }

    public static void fillFeesTxtInCore() {
        core.firstFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.firstFeeComboBox.getSelectedItem() != null) {
                core.firstFeeTxt.setText(fees.getFeeAmount(core.firstFeeComboBox.getSelectedItem().toString()));
            } else if (core.firstFeeComboBox.getSelectedItem() == null) {
                core.firstFeeTxt.setText("");
            }
        });
        core.secondFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.secondFeeComboBox.getSelectedItem() != null) {
                core.secondFeeTxt.setText(fees.getFeeAmount(core.secondFeeComboBox.getSelectedItem().toString()));
            } else if (core.secondFeeComboBox.getSelectedItem() == null) {
                core.secondFeeTxt.setText("");
            }
        });
        core.thirdFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.thirdFeeComboBox.getSelectedItem() != null) {
                core.thirdFeeTxt.setText(fees.getFeeAmount(core.thirdFeeComboBox.getSelectedItem().toString()));
            } else if (core.thirdFeeComboBox.getSelectedItem() == null) {
                core.thirdFeeTxt.setText("");
            }
        });
        core.forthFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.forthFeeComboBox.getSelectedItem() != null) {
                core.forthFeeTxt.setText(fees.getFeeAmount(core.forthFeeComboBox.getSelectedItem().toString()));
            } else if (core.forthFeeComboBox.getSelectedItem() == null) {
                core.forthFeeTxt.setText("");
            }
        });
        core.fifthFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.fifthFeeComboBox.getSelectedItem() != null) {
                core.fifthFeeTxt.setText(fees.getFeeAmount(core.fifthFeeComboBox.getSelectedItem().toString()));
            } else if (core.fifthFeeComboBox.getSelectedItem() == null) {
                core.fifthFeeTxt.setText("");
            }
        });
    }

    public static void fillFeesDateInCore() {

        core.secondFeeComboBox.addActionListener(e -> {
            //Update date value based on selection of feeComboBox
            if (core.secondFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.secondFeeComboBox.getSelectedItem() != null && core.secondFeeDateTxt.getText().equals("")) {
                core.secondFeeDateTxt.setText(date.today());
            } else if (core.secondFeeComboBox.getSelectedItem().equals("")) {
                core.secondFeeComboBox.setSelectedIndex(-1);
                core.secondFeeDateTxt.setText("");
            }

        });
        core.thirdFeeComboBox.addActionListener(e -> {
            if (core.thirdFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.thirdFeeComboBox.getSelectedItem() != null && core.thirdFeeDateTxt.getText().equals("")) {
                core.thirdFeeDateTxt.setText(date.today());
            } else if (core.thirdFeeComboBox.getSelectedItem().equals("")) {
                core.thirdFeeComboBox.setSelectedIndex(-1);
                core.thirdFeeDateTxt.setText("");
            }
        });
        core.forthFeeComboBox.addActionListener(e -> {
            if (core.forthFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.forthFeeComboBox.getSelectedItem() != null && core.forthFeeDateTxt.getText().equals("")) {
                core.forthFeeDateTxt.setText(date.today());
            } else if (core.forthFeeComboBox.getSelectedItem().equals("")) {
                core.forthFeeComboBox.setSelectedIndex(-1);
                core.forthFeeDateTxt.setText("");
            }
        });
        core.fifthFeeComboBox.addActionListener(e -> {
            if (core.fifthFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.fifthFeeComboBox.getSelectedItem() != null && core.fifthFeeDateTxt.getText().equals("")) {
                core.fifthFeeDateTxt.setText(date.today());
            } else if (core.fifthFeeComboBox.getSelectedItem().equals("")) {
                core.fifthFeeComboBox.setSelectedIndex(-1);
                core.fifthFeeDateTxt.setText("");
            }
        });
    }

    public static void updateFeesDropDownInCore() {
        int firstFeeSelectedIndex = core.firstFeeComboBox.getSelectedIndex();
        core.firstFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.firstFeeComboBox.setSelectedIndex(firstFeeSelectedIndex);

        int secondFeeSelectedIndex = core.secondFeeComboBox.getSelectedIndex();
        core.secondFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.secondFeeComboBox.setSelectedIndex(secondFeeSelectedIndex);

        int thirdFeeSelectedIndex = core.thirdFeeComboBox.getSelectedIndex();
        core.thirdFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.thirdFeeComboBox.setSelectedIndex(thirdFeeSelectedIndex);

        int forthFeeSelectedIndex = core.forthFeeComboBox.getSelectedIndex();
        core.forthFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.forthFeeComboBox.setSelectedIndex(forthFeeSelectedIndex);

        int fifthFeeSelectedIndex = core.fifthFeeComboBox.getSelectedIndex();
        core.fifthFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.fifthFeeComboBox.setSelectedIndex(fifthFeeSelectedIndex);
    }

}

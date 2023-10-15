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
    private static final String feesPath = core.sourceFolder + "/json/fees.json";
    static JComboBox<String> feesDropDown;

    public static void createFeeWindow() {
        addFrame = new JFrame("Agregar honorario");
        JPanel addPanel = new JPanel();
        JButton addFee;
        JButton deleteFee;
        JTextField amountTxt = new JTextField(25);
        addFee = new JButton("Agregar honorario");

        addPanel.add(new JLabel("Nombre del honorario:"));
        String[] options = fees.getAllKeysFromJson().toArray(new String[0]);
        feesDropDown = new JComboBox<>(options);
        feesDropDown.setSelectedIndex(-1);
        feesDropDown.setEditable(true);
        addPanel.add(feesDropDown);
        feesDropDown.addActionListener(e -> {
            if (feesDropDown.getSelectedIndex() != -1) {
                amountTxt.setText(getFeeAmount((String) feesDropDown.getSelectedItem()));
            }
        });

        addPanel.add(new JLabel("Monto:"));
        addPanel.add(amountTxt);
        formatter.setNumericOnly(amountTxt);
        formatter.formatFieldAsNumber(amountTxt);

        addFee.addActionListener(e -> {
            addValuesOnJson((String) feesDropDown.getSelectedItem(), amountTxt.getText());
            updateFeesDropDown();
            updateFeesDropDownInCore();
            try {
                feesDropDown.setSelectedIndex(options.length);
            } catch (Exception ignore) {
            }
        });

        deleteFee = new JButton("Eliminar honorario");
        deleteFee.setBackground(Color.RED);

        deleteFee.addActionListener(e -> {
            deleteValuesOnJson(feesDropDown.getSelectedItem().toString());
            updateFeesDropDown();
            updateFeesDropDownInCore();
            feesDropDown.setSelectedIndex(-1);
        });

        addPanel.add(addFee);
        addPanel.add(deleteFee);
        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(400, 300));
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
        } catch (NullPointerException ignore) {
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
                notifications.showInformativeNotification("El honorario ya existe, actualizando el monto", "Actualizando honorario");
            } else {
                notifications.showInformativeNotification("Honorario añadido", "Operación exitosa");
            }
            mapper.writeValue(new File(feesPath), feeData);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error añadiendo honorario");
        }

    }

    public static void deleteValuesOnJson(String fee) {
        int result =
                JOptionPane.showConfirmDialog(null, "Esta seguro que desea borrar el honorario " + fee + "?", "Borrar honorario", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

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
                notifications.showInformativeNotification("Honorario " + fee
                        + " eliminado", "Honorario eliminado");
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
        core.sixthFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.sixthFeeTxt.setText(format.formatAsMoney(core.sixthFeeTxt.getText()));
            }
        });
        core.seventhFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.seventhFeeTxt.setText(format.formatAsMoney(core.seventhFeeTxt.getText()));
            }
        });
        core.eighthFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.eighthFeeTxt.setText(format.formatAsMoney(core.eighthFeeTxt.getText()));
            }
        });
        core.ninethFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.ninethFeeTxt.setText(format.formatAsMoney(core.ninethFeeTxt.getText()));
            }
        });
        core.tenthFeeTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                core.tenthFeeTxt.setText(format.formatAsMoney(core.tenthFeeTxt.getText()));
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
        core.sixthFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.sixthFeeComboBox.getSelectedItem() != null) {
                core.sixthFeeTxt.setText(fees.getFeeAmount(core.sixthFeeComboBox.getSelectedItem().toString()));
            } else if (core.sixthFeeComboBox.getSelectedItem() == null) {
                core.sixthFeeTxt.setText("");
            }
        });
        core.seventhFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.seventhFeeComboBox.getSelectedItem() != null) {
                core.seventhFeeTxt.setText(fees.getFeeAmount(core.seventhFeeComboBox.getSelectedItem().toString()));
            } else if (core.seventhFeeComboBox.getSelectedItem() == null) {
                core.seventhFeeTxt.setText("");
            }
        });
        core.eighthFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.eighthFeeComboBox.getSelectedItem() != null) {
                core.eighthFeeTxt.setText(fees.getFeeAmount(core.eighthFeeComboBox.getSelectedItem().toString()));
            } else if (core.eighthFeeComboBox.getSelectedItem() == null) {
                core.eighthFeeTxt.setText("");
            }
        });
        core.ninethFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.ninethFeeComboBox.getSelectedItem() != null) {
                core.ninethFeeTxt.setText(fees.getFeeAmount(core.ninethFeeComboBox.getSelectedItem().toString()));
            } else if (core.ninethFeeComboBox.getSelectedItem() == null) {
                core.ninethFeeTxt.setText("");
            }
        });
        core.tenthFeeComboBox.addActionListener(e -> {
            //Update fee value based on selection of feeComboBox
            if (core.tenthFeeComboBox.getSelectedItem() != null) {
                core.tenthFeeTxt.setText(fees.getFeeAmount(core.tenthFeeComboBox.getSelectedItem().toString()));
            } else if (core.tenthFeeComboBox.getSelectedItem() == null) {
                core.tenthFeeTxt.setText("");
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
        core.sixthFeeComboBox.addActionListener(e -> {
            if (core.sixthFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.sixthFeeComboBox.getSelectedItem() != null && core.sixthFeeDateTxt.getText().equals("")) {
                core.sixthFeeDateTxt.setText(date.today());
            } else if (core.sixthFeeComboBox.getSelectedItem().equals("")) {
                core.sixthFeeComboBox.setSelectedIndex(-1);
                core.sixthFeeDateTxt.setText("");
            }
        });
        core.seventhFeeComboBox.addActionListener(e -> {
            if (core.seventhFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.seventhFeeComboBox.getSelectedItem() != null && core.seventhFeeDateTxt.getText().equals("")) {
                core.seventhFeeDateTxt.setText(date.today());
            } else if (core.seventhFeeComboBox.getSelectedItem().equals("")) {
                core.seventhFeeComboBox.setSelectedIndex(-1);
                core.seventhFeeDateTxt.setText("");
            }
        });
        core.eighthFeeComboBox.addActionListener(e -> {
            if (core.eighthFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.eighthFeeComboBox.getSelectedItem() != null && core.eighthFeeDateTxt.getText().equals("")) {
                core.eighthFeeDateTxt.setText(date.today());
            } else if (core.eighthFeeComboBox.getSelectedItem().equals("")) {
                core.eighthFeeComboBox.setSelectedIndex(-1);
                core.eighthFeeDateTxt.setText("");
            }
        });
        core.ninethFeeComboBox.addActionListener(e -> {
            if (core.ninethFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.ninethFeeComboBox.getSelectedItem() != null && core.ninethFeeDateTxt.getText().equals("")) {
                core.ninethFeeDateTxt.setText(date.today());
            } else if (core.ninethFeeComboBox.getSelectedItem().equals("")) {
                core.ninethFeeComboBox.setSelectedIndex(-1);
                core.ninethFeeDateTxt.setText("");
            }
        });
        core.tenthFeeComboBox.addActionListener(e -> {
            if (core.tenthFeeComboBox.getSelectedItem() == null) {
                return;
            }
            if (core.tenthFeeComboBox.getSelectedItem() != null && core.tenthFeeDateTxt.getText().equals("")) {
                core.tenthFeeDateTxt.setText(date.today());
            } else if (core.tenthFeeComboBox.getSelectedItem().equals("")) {
                core.tenthFeeComboBox.setSelectedIndex(-1);
                core.tenthFeeDateTxt.setText("");
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

        int sixthFeeSelectedIndex = core.sixthFeeComboBox.getSelectedIndex();
        core.sixthFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.sixthFeeComboBox.setSelectedIndex(sixthFeeSelectedIndex);

        int seventhFeeSelectedIndex = core.sixthFeeComboBox.getSelectedIndex();
        core.sixthFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.sixthFeeComboBox.setSelectedIndex(seventhFeeSelectedIndex);

        int eighthFeeSelectedIndex = core.eighthFeeComboBox.getSelectedIndex();
        core.eighthFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.eighthFeeComboBox.setSelectedIndex(eighthFeeSelectedIndex);

        int ninethFeeSelectedIndex = core.ninethFeeComboBox.getSelectedIndex();
        core.ninethFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.ninethFeeComboBox.setSelectedIndex(ninethFeeSelectedIndex);

        int tenthFeeSelectedIndex = core.tenthFeeComboBox.getSelectedIndex();
        core.tenthFeeComboBox.setModel(new DefaultComboBoxModel<>(fees.getAllKeysFromJson().toArray(new String[0])));
        core.tenthFeeComboBox.setSelectedIndex(tenthFeeSelectedIndex);
    }

}

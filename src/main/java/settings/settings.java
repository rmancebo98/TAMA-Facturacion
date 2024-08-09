package settings;

import core.core;
import fees.fees;
import util.formatter;
import util.ncf;

import javax.swing.*;
import java.awt.*;

public class settings {

    private static JFrame addFrame;
    private static final String feesPath = core.sourceFolder + "/json/fees.json";
    static JComboBox<String> feesDropDown;

    public static void createSettingsWindow() {
        addFrame = new JFrame("Configuración");
        JPanel settingsPanel = new JPanel();
        JButton addRNCLimit;
        JTextField maximumRNCTxt;

        settingsPanel.add(new JLabel("RNC disponible hasta:"));
        try {
            maximumRNCTxt = new JTextField(String.valueOf(ncf.getLastNCF()), 11);
        } catch (NullPointerException ignore) {
            maximumRNCTxt = new JTextField("B0100000001");
        }

        addRNCLimit = new JButton("Actualizar");

        settingsPanel.add(maximumRNCTxt);
        settingsPanel.add(addRNCLimit);
        addFrame.add(settingsPanel);
        addFrame.setPreferredSize(new Dimension(400, 200));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static void showFrame() {
        createSettingsWindow();
        addFrame.setVisible(true);
    }
}

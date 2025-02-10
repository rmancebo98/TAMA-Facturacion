package core;

import clients.clients;
import com.fasterxml.jackson.databind.ObjectMapper;
import documentGenerator.jsonHandler;
import fees.fees;
import settings.settings;
import util.date;
import util.formatter;
import util.ncf;
import util.notifications;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class core extends JFrame implements ActionListener {
    @Serial
    private static final long serialVersionUID = 3L;

    JPanel panel;
    static JComboBox<String> clientsDropDown;
    JButton generateButtom;
    JTextField clientRNC;
    JTextField insuredName;
    JTextField clientAddress;
    JTextField caseNumber;
    public static JComboBox<String> firstFeeComboBox;
    public static JTextField firstFeeTxt;
    JTextField firstFeeDateTxt;
    public static JComboBox<String> secondFeeComboBox;
    public static JTextField secondFeeTxt;
    public static JTextField secondFeeDateTxt;
    public static JComboBox<String> thirdFeeComboBox;
    public static JTextField thirdFeeTxt;
    public static JTextField thirdFeeDateTxt;
    public static JComboBox<String> forthFeeComboBox;
    public static JTextField forthFeeTxt;
    public static JTextField forthFeeDateTxt;
    public static JComboBox<String> fifthFeeComboBox;
    public static JTextField fifthFeeTxt;
    public static JTextField fifthFeeDateTxt;
    public static JComboBox<String> sixthFeeComboBox;
    public static JTextField sixthFeeTxt;
    public static JTextField sixthFeeDateTxt;
    public static JComboBox<String> seventhFeeComboBox;
    public static JTextField seventhFeeTxt;
    public static JTextField seventhFeeDateTxt;
    public static JComboBox<String> eighthFeeComboBox;
    public static JTextField eighthFeeTxt;
    public static JTextField eighthFeeDateTxt;
    public static JComboBox<String> ninethFeeComboBox;
    public static JTextField ninethFeeTxt;
    public static JTextField ninethFeeDateTxt;
    public static JComboBox<String> tenthFeeComboBox;
    public static JTextField tenthFeeTxt;
    public static JTextField tenthFeeDateTxt;
    public static JComboBox<String> eleventhFeeComboBox;
    public static JTextField eleventhFeeTxt;
    public static JTextField eleventhFeeDateTxt;
    JTextField RNCTxt;
    public static JTextField NCFTxt;
    JTextField dateTxt;
    JTextField expirationDateTxt;
    JButton addClientButton;
    JButton addFeesButton;
    JButton settingsButton;
    public static String sourceFolder;

    public static void main(String[] args) {
        sourceFolder = args[0];
        new core();
    }

    public core() {
        super("Tavárez y Mancebo - Sistema de facturación");
        createReceiptWindow();
    }

    public void createReceiptWindow() {

        // Create the GUI components
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        createHeaderPanel();
        createClientPanel();
        createCasePanel();
        createFirstFeePanel();
        createSecondFeePanel();
        createThirdFeePanel();
        createForthFeePanel();
        createFifthFeePanel();
        createSixthFeePanel();
        createSeventhFeePanel();
        createEighthFeePanel();
        createNinethFeePanel();
        createTenthFeePanel();
        createEleventhFeePanel();
        panel.add(new TextFieldPopup());

        // Add the panel to the frame and display the GUI
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        createButtonsPanel();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        fees.formatFeesTxtInCore();
        fees.fillFeesTxtInCore();
        fees.fillFeesDateInCore();
    }

    public static void updateClientsDropdown() {
        clientsDropDown.setModel(new DefaultComboBoxModel<>(clients.getAllKeysFromJson().toArray(new String[0])));
    }

    private void createHeaderPanel() {
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        datePanel.add(new JLabel("Fecha de facturación"));
        dateTxt = new JTextField(date.today());
        expirationDateTxt = new JTextField(date.getLastDayOfYear());
        datePanel.add(dateTxt);
        datePanel.add(new JLabel("Fecha de expiración"));
        datePanel.add(expirationDateTxt);
        mainPanel.add(datePanel);

        JPanel rncPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rncPanel.add(new JLabel("RNC"));
        RNCTxt = new JTextField("131731651", 11);
        formatter.setNumericOnly(RNCTxt);
        rncPanel.add(RNCTxt);

        rncPanel.add(new JLabel("Numero de comprobante fiscal"));
        try {
            NCFTxt = new JTextField(String.valueOf(ncf.getLastNCF()), 11);
        } catch (NullPointerException ignore) {
            NCFTxt = new JTextField("B0100000001");
        }
        formatter.setNumericOnly(NCFTxt);
        rncPanel.add(NCFTxt);

        if(ncf.ncfCloseToMax()) {
            rncPanel.add(new JLabel("Comprobantes cerca de expirar!")).setForeground(Color.RED);
        }

        // Add the button panel to the main panel
        mainPanel.add(rncPanel);
        panel.add(mainPanel);

    }

    private void createClientPanel() {
        JPanel clientsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        // Add the clients.clients dropdown
        clientsPanel.add(new JLabel("Clientes:"));
        String[] options = clients.getAllKeysFromJson().toArray(new String[0]);
        clientsDropDown = new JComboBox<>(options);
        Dimension dropdownSize = new Dimension(200, 25); // Set your desired width and height
        clientsDropDown.setPreferredSize(dropdownSize);
        clientsPanel.add(clientsDropDown);

        clientsDropDown.addActionListener(e -> {
            //Update RNC and address based on selection of clientsDropDown
            clientRNC.setText(clients.getClientRNC(clientsDropDown.getSelectedItem().toString()));
            clientAddress.setText(clients.getClientAddress(clientsDropDown.getSelectedItem().toString()));
        });

        clientsPanel.add(new JLabel("RNC del cliente:"));
        if (clientsDropDown.getSelectedItem() == null) {
            clientRNC = new JTextField("0", 12);
        } else {
            clientRNC = new JTextField(clients.getClientRNC(clientsDropDown.getSelectedItem().toString()), 12);
        }
        clientsPanel.add(clientRNC);

        clientsPanel.add(new JLabel("Direccion del cliente: "));
        clientAddress = new JTextField(100);
        if (clientsDropDown.getSelectedItem() == null) {
            clientAddress = new JTextField("", 100);
        } else {
            clientAddress = new JTextField(clients.getClientAddress(clientsDropDown.getSelectedItem().toString()), 100);
        }

        clientsPanel.add(clientAddress);
        panel.add(clientsPanel);

    }

    private void createCasePanel() {
        JPanel casePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        casePanel.add(new JLabel("Nombre del asegurado:"));
        insuredName = new JTextField("", 12);
        casePanel.add(insuredName);

        casePanel.add(new JLabel("Numero de expediente: "));
        caseNumber = new JTextField(20);

        casePanel.add(caseNumber);
        panel.add(casePanel);
    }

    private void createFirstFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));


        firstFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        firstFeeComboBox.setEditable(true);
        feesPanel.add(firstFeeComboBox);
        firstFeeTxt = fees.fillFeeValueInCore(firstFeeComboBox);
        feesPanel.add(firstFeeTxt);
        formatter.setNumericOnly(firstFeeTxt);
        JLabel firstFeeDateLbl = new JLabel("Fecha del importe");
        feesPanel.add(firstFeeDateLbl);
        firstFeeDateTxt = new JTextField(date.today(), 10);
        feesPanel.add(firstFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createSecondFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        secondFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        secondFeeComboBox.setSelectedIndex(-1);
        secondFeeComboBox.setEditable(true);
        feesPanel.add(secondFeeComboBox);
        secondFeeTxt = fees.fillFeeValueInCore(secondFeeComboBox);
        feesPanel.add(secondFeeTxt);
        formatter.setNumericOnly(secondFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        secondFeeDateTxt = new JTextField(10);
        feesPanel.add(secondFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createThirdFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        thirdFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        thirdFeeComboBox.setSelectedIndex(-1);
        thirdFeeComboBox.setEditable(true);
        feesPanel.add(thirdFeeComboBox);
        thirdFeeTxt = fees.fillFeeValueInCore(thirdFeeComboBox);
        feesPanel.add(thirdFeeTxt);
        formatter.setNumericOnly(thirdFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        thirdFeeDateTxt = new JTextField(10);
        feesPanel.add(thirdFeeDateTxt);

        panel.add(feesPanel);
    }


    private void createForthFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        forthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        forthFeeComboBox.setSelectedIndex(-1);
        forthFeeComboBox.setEditable(true);
        feesPanel.add(forthFeeComboBox);
        forthFeeTxt = fees.fillFeeValueInCore(forthFeeComboBox);
        feesPanel.add(forthFeeTxt);
        formatter.setNumericOnly(forthFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        forthFeeDateTxt = new JTextField(10);
        feesPanel.add(forthFeeDateTxt);

        panel.add(feesPanel);
    }


    private void createFifthFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        fifthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        fifthFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(fifthFeeComboBox);
        fifthFeeComboBox.setEditable(true);
        fifthFeeTxt = fees.fillFeeValueInCore(fifthFeeComboBox);
        feesPanel.add(fifthFeeTxt);
        formatter.setNumericOnly(fifthFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        fifthFeeDateTxt = new JTextField(10);
        feesPanel.add(fifthFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createSixthFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        sixthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        sixthFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(sixthFeeComboBox);
        sixthFeeComboBox.setEditable(true);
        sixthFeeTxt = fees.fillFeeValueInCore(sixthFeeComboBox);
        feesPanel.add(sixthFeeTxt);
        formatter.setNumericOnly(sixthFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        sixthFeeDateTxt = new JTextField(10);
        feesPanel.add(sixthFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createSeventhFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        seventhFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        seventhFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(seventhFeeComboBox);
        seventhFeeComboBox.setEditable(true);
        seventhFeeTxt = fees.fillFeeValueInCore(seventhFeeComboBox);
        feesPanel.add(seventhFeeTxt);
        formatter.setNumericOnly(seventhFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        seventhFeeDateTxt = new JTextField(10);
        feesPanel.add(seventhFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createEighthFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        eighthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        eighthFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(eighthFeeComboBox);
        eighthFeeComboBox.setEditable(true);
        eighthFeeTxt = fees.fillFeeValueInCore(eighthFeeComboBox);
        feesPanel.add(eighthFeeTxt);
        formatter.setNumericOnly(eighthFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        eighthFeeDateTxt = new JTextField(10);
        feesPanel.add(eighthFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createNinethFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        ninethFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        ninethFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(ninethFeeComboBox);
        ninethFeeComboBox.setEditable(true);
        ninethFeeTxt = fees.fillFeeValueInCore(ninethFeeComboBox);
        feesPanel.add(ninethFeeTxt);
        formatter.setNumericOnly(ninethFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        ninethFeeDateTxt = new JTextField(10);
        feesPanel.add(ninethFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createTenthFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        tenthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        tenthFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(tenthFeeComboBox);
        tenthFeeComboBox.setEditable(true);
        tenthFeeTxt = fees.fillFeeValueInCore(tenthFeeComboBox);
        feesPanel.add(tenthFeeTxt);
        formatter.setNumericOnly(tenthFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        tenthFeeDateTxt = new JTextField(10);
        feesPanel.add(tenthFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createEleventhFeePanel() {
        JPanel feesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        feesPanel.add(new JLabel("Concepto a facturar:"));

        eleventhFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        eleventhFeeComboBox.setSelectedIndex(-1);
        feesPanel.add(eleventhFeeComboBox);
        eleventhFeeComboBox.setEditable(true);
        eleventhFeeTxt = fees.fillFeeValueInCore(eleventhFeeComboBox);
        feesPanel.add(eleventhFeeTxt);
        formatter.setNumericOnly(eleventhFeeTxt);
        feesPanel.add(new JLabel("Fecha del importe"));
        eleventhFeeDateTxt = new JTextField(10);
        feesPanel.add(eleventhFeeDateTxt);

        panel.add(feesPanel);
    }

    private void createButtonsPanel() {
        generateButtom = new JButton("Generar factura");
        generateButtom.addActionListener(this);

        addClientButton = new JButton("Agregar o eliminar cliente");
        addClientButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            clients.showFrame();
        });

        addFeesButton = new JButton("Agregar o eliminar honorario");
        addFeesButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            fees.showFrame();
        });

        settingsButton = new JButton("Configuración");
        settingsButton.addActionListener(e -> {
            settings.showFrame();
        });

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(generateButtom);
        buttonPanel.add(addClientButton);
        buttonPanel.add(addFeesButton);
        buttonPanel.add(settingsButton);

        // Add the button panel to the main panel
        panel.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new HashMap<>();
        Map<String, Map<String, String>> receiptInfoMap = new HashMap<>();
        Map<String, Map<String, String>> firstFeeMap = new HashMap<>();
        Map<String, Map<String, String>> secondFeeMap = new HashMap<>();
        Map<String, Map<String, String>> thirdFeeMap = new HashMap<>();
        Map<String, Map<String, String>> forthFeeMap = new HashMap<>();
        Map<String, Map<String, String>> fifthFeeMap = new HashMap<>();
        Map<String, Map<String, String>> sixthFeeMap = new HashMap<>();
        Map<String, Map<String, String>> seventhFeeMap = new HashMap<>();
        Map<String, Map<String, String>> eighthFeeMap = new HashMap<>();
        Map<String, Map<String, String>> ninethFeeMap = new HashMap<>();
        Map<String, Map<String, String>> tenthFeeMap = new HashMap<>();
        Map<String, Map<String, String>> eleventhFeeMap = new HashMap<>();
        Map<Object, Object> finalData = new HashMap<>();


        // Add the client data into the map
        data.put("Fecha", dateTxt.getText());
        data.put("Fecha de expiracion", expirationDateTxt.getText());
        data.put("RNC", RNCTxt.getText());
        data.put("Numero de Comprobante Fiscal", NCFTxt.getText());
        data.put("Cliente", clientsDropDown.getSelectedItem().toString());
        data.put("RNC del Cliente", clientRNC.getText());
        data.put("Nombre del asegurado", insuredName.getText());
        data.put("Numero de expediente", caseNumber.getText());
        receiptInfoMap.put("INFORMACION GENERAL", data);

        //Add the fees.fees data into the map

        try {
            if (firstFeeComboBox.getSelectedItem().toString().equals("")) {
                notifications.showInformativeNotification("Por favor agrege un honorario a facturar"
                        , "Agregue honorario");
            } else {
                Map<String, String> firstFeeData = new HashMap<>();
                firstFeeData.put("Razon: ", firstFeeComboBox.getSelectedItem().toString());
                firstFeeData.put("Monto: ", firstFeeTxt.getText());
                firstFeeData.put("Fecha: ", firstFeeDateTxt.getText());
                firstFeeMap.put("PRIMER HONORARIO:", firstFeeData);
            }
        } catch (NullPointerException ignore) {
            notifications.showInformativeNotification("Por favor agrege un honorario a facturar"
                    , "Agregue honorario");
        }


        try {
            if (!secondFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> secondFeeData = new HashMap<>();
                secondFeeData.put("Razon: ", secondFeeComboBox.getSelectedItem().toString());
                secondFeeData.put("Monto: ", secondFeeTxt.getText());
                secondFeeData.put("Fecha: ", secondFeeDateTxt.getText());
                secondFeeMap.put("SEGUNDO HONORARIO:", secondFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!thirdFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> thirdFeeData = new HashMap<>();
                thirdFeeData.put("Razon: ", thirdFeeComboBox.getSelectedItem().toString());
                thirdFeeData.put("Monto: ", thirdFeeTxt.getText());
                thirdFeeData.put("Fecha: ", thirdFeeDateTxt.getText());
                thirdFeeMap.put("TERCER HONORARIO:", thirdFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!forthFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> forthFeeData = new HashMap<>();
                forthFeeData.put("Razon: ", forthFeeComboBox.getSelectedItem().toString());
                forthFeeData.put("Monto: ", forthFeeTxt.getText());
                forthFeeData.put("Fecha: ", forthFeeDateTxt.getText());
                forthFeeMap.put("CUARTO HONORARIO:", forthFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!fifthFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> fifthFeeData = new HashMap<>();
                fifthFeeData.put("Razon: ", fifthFeeComboBox.getSelectedItem().toString());
                fifthFeeData.put("Monto: ", fifthFeeTxt.getText());
                fifthFeeData.put("Fecha: ", fifthFeeDateTxt.getText());
                fifthFeeMap.put("QUINTO HONORARIO:", fifthFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!sixthFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> sixthFeeData = new HashMap<>();
                sixthFeeData.put("Razon: ", sixthFeeComboBox.getSelectedItem().toString());
                sixthFeeData.put("Monto: ", sixthFeeTxt.getText());
                sixthFeeData.put("Fecha: ", sixthFeeDateTxt.getText());
                sixthFeeMap.put("SEXTO HONORARIO:", sixthFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!seventhFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> seventhFeeData = new HashMap<>();
                seventhFeeData.put("Razon: ", seventhFeeComboBox.getSelectedItem().toString());
                seventhFeeData.put("Monto: ", seventhFeeTxt.getText());
                seventhFeeData.put("Fecha: ", seventhFeeDateTxt.getText());
                seventhFeeMap.put("SEPTIMO HONORARIO:", seventhFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!eighthFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> eighthFeeData = new HashMap<>();
                eighthFeeData.put("Razon: ", eighthFeeComboBox.getSelectedItem().toString());
                eighthFeeData.put("Monto: ", eighthFeeTxt.getText());
                eighthFeeData.put("Fecha: ", eighthFeeDateTxt.getText());
                eighthFeeMap.put("OCTAVO HONORARIO:", eighthFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!ninethFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> ninethFeeData = new HashMap<>();
                ninethFeeData.put("Razon: ", ninethFeeComboBox.getSelectedItem().toString());
                ninethFeeData.put("Monto: ", ninethFeeTxt.getText());
                ninethFeeData.put("Fecha: ", ninethFeeDateTxt.getText());
                ninethFeeMap.put("NOVENO HONORARIO:", ninethFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!tenthFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> tenthFeeData = new HashMap<>();
                tenthFeeData.put("Razon: ", tenthFeeComboBox.getSelectedItem().toString());
                tenthFeeData.put("Monto: ", tenthFeeTxt.getText());
                tenthFeeData.put("Fecha: ", tenthFeeDateTxt.getText());
                tenthFeeMap.put("DECIMO HONORARIO:", tenthFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        try {
            if (!eleventhFeeComboBox.getSelectedItem().toString().equals("")) {
                Map<String, String> eleventhFeeData = new HashMap<>();
                eleventhFeeData.put("Razon: ", eleventhFeeComboBox.getSelectedItem().toString());
                eleventhFeeData.put("Monto: ", eleventhFeeTxt.getText());
                eleventhFeeData.put("Fecha: ", eleventhFeeDateTxt.getText());
                eleventhFeeMap.put("ONCEAVO HONORARIO:", eleventhFeeData);
            }
        } catch (NullPointerException ignore) {
        }

        finalData.putAll(receiptInfoMap);

        if (!firstFeeMap.isEmpty()) {
            finalData.putAll(firstFeeMap);
        }

        if (!secondFeeMap.isEmpty()) {
            finalData.putAll(secondFeeMap);
        }

        if (!thirdFeeMap.isEmpty()) {
            finalData.putAll(thirdFeeMap);
        }

        if (!forthFeeMap.isEmpty()) {
            finalData.putAll(forthFeeMap);
        }

        if (!fifthFeeMap.isEmpty()) {
            finalData.putAll(fifthFeeMap);
        }

        if (!sixthFeeMap.isEmpty()) {
            finalData.putAll(sixthFeeMap);
        }

        if (!seventhFeeMap.isEmpty()) {
            finalData.putAll(seventhFeeMap);
        }

        if (!eighthFeeMap.isEmpty()) {
            finalData.putAll(eighthFeeMap);
        }

        if (!ninethFeeMap.isEmpty()) {
            finalData.putAll(ninethFeeMap);
        }

        if (!tenthFeeMap.isEmpty()) {
            finalData.putAll(tenthFeeMap);
        }

        if (!eleventhFeeMap.isEmpty()) {
            finalData.putAll(eleventhFeeMap);
        }

        jsonHandler.writeOnFinalJson(finalData);
    }

    public class TextFieldPopup extends JPanel {
        public TextFieldPopup() {
            JPopupMenu menu = new JPopupMenu();
            Action cut = new DefaultEditorKit.CutAction();
            cut.putValue(Action.NAME, "Cut");
            cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
            menu.add(cut);

            Action copy = new DefaultEditorKit.CopyAction();
            copy.putValue(Action.NAME, "Copy");
            copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
            menu.add(copy);

            Action paste = new DefaultEditorKit.PasteAction();
            paste.putValue(Action.NAME, "Paste");
            paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
            menu.add(paste);

            dateTxt.setComponentPopupMenu(menu);
            expirationDateTxt.setComponentPopupMenu(menu);
            RNCTxt.setComponentPopupMenu(menu);
            NCFTxt.setComponentPopupMenu(menu);
            clientRNC.setComponentPopupMenu(menu);
            insuredName.setComponentPopupMenu(menu);
            clientAddress.setComponentPopupMenu(menu);
            caseNumber.setComponentPopupMenu(menu);

            firstFeeTxt.setComponentPopupMenu(menu);
            firstFeeDateTxt.setComponentPopupMenu(menu);
            secondFeeTxt.setComponentPopupMenu(menu);
            secondFeeDateTxt.setComponentPopupMenu(menu);
            thirdFeeTxt.setComponentPopupMenu(menu);
            thirdFeeDateTxt.setComponentPopupMenu(menu);
            forthFeeTxt.setComponentPopupMenu(menu);
            forthFeeDateTxt.setComponentPopupMenu(menu);
            fifthFeeTxt.setComponentPopupMenu(menu);
            fifthFeeDateTxt.setComponentPopupMenu(menu);
            sixthFeeTxt.setComponentPopupMenu(menu);
            sixthFeeDateTxt.setComponentPopupMenu(menu);
            seventhFeeTxt.setComponentPopupMenu(menu);
            seventhFeeDateTxt.setComponentPopupMenu(menu);
            eighthFeeTxt.setComponentPopupMenu(menu);
            eighthFeeDateTxt.setComponentPopupMenu(menu);
            ninethFeeTxt.setComponentPopupMenu(menu);
            ninethFeeDateTxt.setComponentPopupMenu(menu);
            tenthFeeTxt.setComponentPopupMenu(menu);
            tenthFeeDateTxt.setComponentPopupMenu(menu);
            eleventhFeeTxt.setComponentPopupMenu(menu);
            eleventhFeeDateTxt.setComponentPopupMenu(menu);
        }

    }

}

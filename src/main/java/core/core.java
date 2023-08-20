package core;

import clients.clients;
import com.fasterxml.jackson.databind.ObjectMapper;
import documentGenerator.jsonHandler;
import fees.fees;
import util.date;
import util.formatter;
import util.ncf;
import util.notifications;

import javax.swing.*;
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
    JTextField RNCTxt;
    JTextField NCFTxt;
    JTextField dateTxt;
    JTextField expirationDateTxt;
    JButton addClientButton;
    JButton addFeesButton;
    public static String sourceFolder;

    public static void main(String[] args) {
        sourceFolder = args[0];
        new core();
    }

    public core() {
        super("Tavarez y Mancebo - Sistema de facturación");
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
        feesPanel.add(new JLabel("Fecha del importe"));
        fifthFeeDateTxt = new JTextField(10);
        feesPanel.add(fifthFeeDateTxt);

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

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(generateButtom);
        buttonPanel.add(addClientButton);
        buttonPanel.add(addFeesButton);

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
                System.out.println(fifthFeeMap);
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

        jsonHandler.writeOnFinalJson(finalData);
        NCFTxt.setText(ncf.getLastNCF());
    }
}

package core;

import clients.clients;
import com.fasterxml.jackson.databind.ObjectMapper;
import documentGenerator.jsonHandler;
import fees.fees;
import util.date;
import util.formatter;
import util.ncf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class core extends JFrame implements ActionListener {
    @Serial
    private static final long serialVersionUID = 2L;

    static JComboBox<String> clientsDropDown;
    JButton generateButtom;
    JTextField clientRNC;
    public static JComboBox<String> firstFeeComboBox;
    public static JTextField firstFeeTxt;
    public static JComboBox<String> secondFeeComboBox;
    public static JTextField secondFeeTxt;
    public static JComboBox<String> thirdFeeComboBox;
    public static JTextField thirdFeeTxt;
    public static JComboBox<String> forthFeeComboBox;
    public static JTextField forthFeeTxt;
    public static JComboBox<String> fifthFeeComboBox;
    public static JTextField fifthFeeTxt;
    JTextField RNCTxt;
    JTextField NCFTxt;
    JTextField dateTxt;
    JButton addClientButton;
    JButton addFeesButton;

    public core() {
        super("Tavarez y Mancebo - Sistema de facturación");
        createReceiptWindow();
    }


    public void createReceiptWindow() {

        // Create the GUI components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Fecha de facturación"));
        dateTxt = new JTextField(date.today());
        panel.add(dateTxt);

        panel.add(new JLabel("RNC"));
        RNCTxt = new JTextField("131731651", 11);
        panel.add(RNCTxt);
        formatter.setNumericOnly(RNCTxt);

        panel.add(new JLabel("Numero de comprobante fiscal"));
        NCFTxt = new JTextField(String.valueOf(ncf.getLastNCF()), 11);
        panel.add(NCFTxt);
        formatter.setNumericOnly(NCFTxt);

        // Add the clients.clients dropdown
        panel.add(new JLabel("Clientes:"));
        String[] options = clients.getAllKeysFromJson().toArray(new String[0]);
        clientsDropDown = new JComboBox<>(options);
        panel.add(clientsDropDown);

        clientsDropDown.addActionListener(e -> {
            //Update RNC based on selection of clientsDropDown
            clientRNC.setText(clients.getClientRNC(clientsDropDown.getSelectedItem().toString()));
        });

        panel.add(new JLabel("RNC del cliente:"));
        if (clientsDropDown.getSelectedItem() == null) {
            clientRNC = new JTextField("0", 12);
        } else {
            clientRNC = new JTextField(clients.getClientRNC(clientsDropDown.getSelectedItem().toString()), 12);
        }
        panel.add(clientRNC);
        formatter.setNumericOnly(clientRNC);

        panel.add(new JLabel("Conceptos a facturar:"));
        firstFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        firstFeeComboBox.setEditable(true);
        panel.add(firstFeeComboBox);
        firstFeeTxt = fees.fillFeeValueInCore(firstFeeComboBox);
        panel.add(firstFeeTxt);

        secondFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        secondFeeComboBox.setSelectedIndex(-1);
        secondFeeComboBox.setEditable(true);
        panel.add(secondFeeComboBox);
        secondFeeTxt = fees.fillFeeValueInCore(secondFeeComboBox);
        panel.add(secondFeeTxt);

        thirdFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        thirdFeeComboBox.setSelectedIndex(-1);
        thirdFeeComboBox.setEditable(true);
        panel.add(thirdFeeComboBox);
        thirdFeeTxt = fees.fillFeeValueInCore(thirdFeeComboBox);
        panel.add(thirdFeeTxt);

        forthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        forthFeeComboBox.setSelectedIndex(-1);
        forthFeeComboBox.setEditable(true);
        panel.add(forthFeeComboBox);
        forthFeeTxt = fees.fillFeeValueInCore(forthFeeComboBox);
        panel.add(forthFeeTxt);

        fifthFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        fifthFeeComboBox.setSelectedIndex(-1);
        panel.add(fifthFeeComboBox);
        fifthFeeComboBox.setEditable(true);
        fifthFeeTxt = fees.fillFeeValueInCore(fifthFeeComboBox);
        panel.add(fifthFeeTxt);

        generateButtom = new JButton("Generar factura");
        generateButtom.addActionListener(this);
        panel.add(generateButtom);


        addClientButton = new JButton("Agregar o eliminar cliente");
        addClientButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            clients.showFrame();
        });

        panel.add(addClientButton);

        addFeesButton = new JButton("Agregar o eliminar honorario");
        addFeesButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            fees.showFrame();
        });

        panel.add(addFeesButton);

        // Add the panel to the frame and display the GUI
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        fees.formatFeesTxtInCore();
        fees.fillFeesTxtInCore();
    }

    public static void main(String[] args) {
        new core();
    }

    public static void updateClientsDropdown() {
        clientsDropDown.setModel(new DefaultComboBoxModel<>(clients.getAllKeysFromJson().toArray(new String[0])));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = null;
        try {
            data = mapper.readValue(new File("src/main/java/facturas/clientData.json"), Map.class);
        } catch (IOException ex) {
            // If the file doesn't exist, create a new empty map
            data = new HashMap<String, String>();
        }

        // Add the client data into the map
        data.put("Fecha", dateTxt.getText());
        data.put("RNC", RNCTxt.getText());
        data.put("Numero de Comprobante Fiscal", NCFTxt.getText());
        data.put("Cliente", clientsDropDown.getSelectedItem().toString());
        data.put("RNC del Cliente", clientRNC.getText());

        //Add the fees.fees data into the map
        data.put(firstFeeComboBox.getSelectedItem().toString(), firstFeeTxt.getText());

        jsonHandler.writeOnJson(data);
    }
}

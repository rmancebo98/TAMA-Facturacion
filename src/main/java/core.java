import com.fasterxml.jackson.databind.ObjectMapper;
import util.date;
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
    JComboBox<String> firstFeeComboBox;
    JTextField firstFeeTxt;
    JComboBox<String> secondFeeComboBox;
    JTextField secondFeeTxt;
    JComboBox<String> thirdFeeComboBox;
    JTextField thirdFeeTxt;
    JTextField RNCTxt;
    JTextField NCFTxt;
    JTextField dateTxt;
    JButton addClientButton;
    JButton addFeesButton;

    public core() {
        super("Tavarez y Mancebo - Sistema de facturación");
        createReceipWindow();
    }


    public void createReceipWindow() {

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

        // Add the clients dropdown
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
        panel.add(firstFeeComboBox);
        firstFeeTxt = fillFeeValue(firstFeeComboBox);
        panel.add(firstFeeTxt);
        firstFeeComboBox.addActionListener(e -> {
            //Update RNC based on selection of clientsDropDown
            firstFeeTxt.setText(fees.getFeeAmount(firstFeeComboBox.getSelectedItem().toString()));
        });

        secondFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        panel.add(secondFeeComboBox);
        secondFeeTxt = fillFeeValue(secondFeeComboBox);
        panel.add(secondFeeTxt);
        secondFeeComboBox.addActionListener(e -> {
            //Update RNC based on selection of clientsDropDown
            secondFeeTxt.setText(fees.getFeeAmount(secondFeeComboBox.getSelectedItem().toString()));
        });

        thirdFeeComboBox = new JComboBox<>(fees.getAllKeysFromJson().toArray(new String[0]));
        panel.add(thirdFeeComboBox);
        thirdFeeTxt = fillFeeValue(thirdFeeComboBox);
        panel.add(thirdFeeTxt);
        thirdFeeComboBox.addActionListener(e -> {
            //Update RNC based on selection of clientsDropDown
            thirdFeeTxt.setText(fees.getFeeAmount(thirdFeeComboBox.getSelectedItem().toString()));
        });

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
    }

    public static void main(String[] args) {
        new core();
    }

    public static void updateClientsDropdown() {
        clientsDropDown.setModel(new DefaultComboBoxModel<>(clients.getAllKeysFromJson().toArray(new String[0])));
    }

    public static void updateFeesDropDown(){

    }

    public JTextField fillFeeValue(JComboBox<String> comboBox) {
        JTextField txtField;
        if (comboBox.getSelectedItem() == null) {
            txtField = new JTextField("0", 12);
        } else {
            txtField = new JTextField(fees.getFeeAmount(comboBox.getSelectedItem().toString()), 12);
        }

        return txtField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = null;
        try {
            data = mapper.readValue(new File("src/main/java/facturas/data.json"), Map.class);
        } catch (IOException ex) {
            // If the file doesn't exist, create a new empty map
            data = new HashMap<String, String>();
        }

        // Add the string to the map
        data.put("Fecha", dateTxt.getText());
        data.put("RNC", RNCTxt.getText());
        data.put("Numero de Comprobante Fiscal", NCFTxt.getText());
        data.put("Cliente", clientsDropDown.getSelectedItem().toString());
        data.put("RNC del Cliente", clientRNC.getText());
//        data.put("Monto", moneyField.getText());

        jsonHandler.writeOnJson(data);
    }
}

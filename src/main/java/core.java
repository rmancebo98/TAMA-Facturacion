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

    JComboBox<String> clientsDropDown;
    JButton generateButtom;
    JTextField clientRNC;
    JTextField moneyField;
    JTextField RNCTxt;
    JTextField NCFTxt;
    JTextField dateTxt;
    JButton addClientButton;
    JButton addFeeButton;

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

        panel.add(new JLabel("Numero de comprobante fiscal"));
        NCFTxt = new JTextField(String.valueOf(ncf.getLastNCF()), 11);
        panel.add(NCFTxt);

        // Add the clients dropdown
        panel.add(new JLabel("Clientes:"));
        String[] options = clients.getAllKeysFromJson().toArray(new String[0]);
        clientsDropDown = new JComboBox<>(options);
        panel.add(clientsDropDown);

        // Add an event listener to the clientsDropDown object
        clientsDropDown.addActionListener(e -> {
            //Update RNC based on selection of clientsDropDown
            clientRNC.setText(clients.getClientRNC(clientsDropDown.getSelectedItem().toString()));
            // Update the dropdown with the updated options array
            clientsDropDown.setModel(new DefaultComboBoxModel<>(clients.getAllKeysFromJson().toArray(new String[0])));
        });

        panel.add(new JLabel("RNC del cliente:"));
        clientRNC = new JTextField(clients.getClientRNC(clientsDropDown.getSelectedItem().toString()), 12);
        panel.add(clientRNC);

        panel.add(new JLabel("Monto a facturar:"));
        moneyField = new JTextField(7);
        panel.add(moneyField);

        generateButtom = new JButton("Generar factura");
        generateButtom.addActionListener(this);
        panel.add(generateButtom);


        addClientButton = new JButton("Agregar o eliminar cliente");
        addClientButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            clients.showClientsFrame();
        });
        panel.add(addClientButton);

        addFeeButton = new JButton("Agregar o eliminar honorario");
        addFeeButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            fees.showFeesFrame();
        });

        panel.add(addFeeButton);

        // Add the panel to the frame and display the GUI
        add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new core();
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

        // Get the string entered in the text field

        // Add the string to the map
        data.put("Fecha", dateTxt.getText());
        data.put("Monto", moneyField.getText());
        data.put("RNC", RNCTxt.getText());
        data.put("Numero de Comprobante Fiscal", NCFTxt.getText());
        data.put("Cliente", clientsDropDown.getSelectedItem().toString());

        jsonHandler.writeOnJson(data);
    }
}

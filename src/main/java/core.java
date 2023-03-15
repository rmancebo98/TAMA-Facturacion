import com.fasterxml.jackson.databind.ObjectMapper;
import documentGenerator.word;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import util.date;
import util.ncf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class core extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField clientTxt;
    private JButton generateButtom;
    private JTextField moneyField;
    private JTextField RNCTxt;
    private JTextField NCFTxt;
    private JTextField dateTxt;
    private JButton addButton;


    public core() {
        super("Tavarez y Mancebo - Sistema de Facturación");

        // Create the GUI components
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Fecha de facturación"));
        dateTxt = new JTextField(date.today());
        panel.add(dateTxt);

        panel.add(new JLabel("RNC"));
        RNCTxt = new JTextField("131731651",11);
        panel.add(RNCTxt);

        panel.add(new JLabel("Numero de comprobante fiscal"));
        NCFTxt = new JTextField(String.valueOf(ncf.getLastNCF()),11);
        panel.add(NCFTxt);

        panel.add(new JLabel("Cliente:"));
        clientTxt = new JTextField(25);
        panel.add(clientTxt);

        panel.add(new JLabel("Monto a facturar:"));
        moneyField = new JTextField(7);
        panel.add(moneyField);

        generateButtom = new JButton("Generar factura");
        generateButtom.addActionListener(this);
        panel.add(generateButtom);



        addButton = new JButton("Add");
        addButton.addActionListener(e -> {
            // Show the new window when the button is clicked
            addClient.showFrame();
        });
        panel.add(addButton);


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
        data.put("fecha", dateTxt.getText());
        data.put("Monto", moneyField.getText());
        data.put("RNC", RNCTxt.getText());
        data.put("Numero de Comprobante Fiscal", NCFTxt.getText());
        data.put("Cliente", clientTxt.getText());

        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File("src/main/java/facturas/data.json"), data);
            System.out.println("Data written to data.json");
            word.writeOnWord();
        } catch (IOException | InvalidFormatException ex) {
            ex.printStackTrace();
        }
    }
}

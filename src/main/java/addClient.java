import com.fasterxml.jackson.databind.ObjectMapper;
import documentGenerator.word;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class addClient {

    private static JFrame addFrame;

    public static void createClientWindow(){
        addFrame = new JFrame("Agregar cliente");
        JPanel addPanel = new JPanel();
        JButton addClient;

        addPanel.add(new JLabel("Nombre del cliente :"));
        JTextField nameTxt = new JTextField(25);
        addPanel.add(nameTxt);

        addPanel.add(new JLabel("RNC:"));
        JTextField rncTxt = new JTextField(25);
        addPanel.add(rncTxt);

        addClient = new JButton("Crear cliente");

        addClient.addActionListener(e -> {
            addValuesOnJson(nameTxt.getText(), rncTxt.getText());
        });
        addPanel.add(addClient);

        addFrame.add(addPanel);
        addFrame.setPreferredSize(new Dimension(300, 175));
        addFrame.pack();
        addFrame.setLocationRelativeTo(null);
    }

    public static void showFrame(){
        createClientWindow ();
        addFrame.setVisible(true);
    }

    public static void addValuesOnJson(String cliente, String RNC){
        // Read the JSON file into a map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = null;
        try {
            data = mapper.readValue(new File("src/main/java/facturas/clients.json"), Map.class);
        } catch (IOException ex) {
            // If the file doesn't exist, create a new empty map
            data = new HashMap<String, String>();
        }

        // Get the string entered in the text field

        // Add the string to the map
        data.put("fecha", cliente);
        data.put("Monto", RNC);

        // Write the modified map back to the JSON file
        try {
            mapper.writeValue(new File("src/main/java/facturas/clients.json"), data);
            System.out.println("Data written to clients.json");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

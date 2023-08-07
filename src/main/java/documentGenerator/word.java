package documentGenerator;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import util.notifications;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class word {

    public static Map<String, Map<String, String>> readReceiptJson() {
        ObjectMapper mapper = new ObjectMapper();
        Map data = null;

        try {
            data = mapper.readValue(new File("src/main/java/facturas/factura.json"), Map.class);
        } catch (IOException ex) {
            notifications.showErrorNotification("Error generando documento");
        }
        return data;
    }

    public static void writeOnWord() throws IOException, InvalidFormatException {

        Map<String, Map<String, String>> receiptJson = readReceiptJson();
        Map<String, String> generalInfo = receiptJson.get("INFORMACION GENERAL");

        // Create a new document
        XWPFDocument document =
                new XWPFDocument(new FileInputStream("src/main/resources/templates/template_receipt.docx"));

        // Iterate over paragraphs in the document and replace text
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
                for (int i = 0; i < run.getCTR().sizeOfTArray(); i++) {
                    String text = run.getText(i);
                    // Replace customer name

                    if (text != null && text.contains("[RNC_OFC]")) {
                        text = text.replace("[RNC_OFC]", generalInfo.get("RNC")); // Replace with actual customer name
                        run.setText(text, i);
                    } else if (text != null && text.contains("[FECHA_FACTURACION]")) {
                        text = text.replace("[FECHA_FACTURACION]", generalInfo.get("Fecha")); // Replace with actual customer name
                        run.setText(text, i);
                    } else if (text != null && text.contains("[NCF]")) {
                        text = text.replace("[NCF]", generalInfo.get("Numero de Comprobante Fiscal")); // Replace with actual customer name
                        run.setText(text, i);
                    } else if (text != null && text.contains("[FECHA_VENCI]")) {
                        text = text.replace("[FECHA_VENCI]", "TODO"); // Replace with actual customer name
                        run.setText(text, i);
                    } else if (text != null && text.contains("[RNC_CLIENT]")) {
                        text = text.replace("[RNC_CLIENT]", generalInfo.get("RNC del Cliente")); // Replace with actual customer name
                        run.setText(text, i);
                    } else if (text != null && text.contains("[CLIENT_NAME]")) {
                        text = text.replace("[CLIENT_NAME]", generalInfo.get("Cliente")); // Replace with actual customer name
                        run.setText(text, i);
                    }else if (text != null && text.contains("[ASEGURADO]")) {
                        text = text.replace("[ASEGURADO]", "TODO"); // Replace with actual customer name
                        run.setText(text, i);
                    }else if (text != null && text.contains("[NUMERO_EXPEDIEN]")) {
                        text = text.replace("[NUMERO_EXPEDIEN]", "TODO"); // Replace with actual customer name
                        run.setText(text, i);
                    }
                }

            }
        }

        fillFeesTable(document);


        // Save the document
        try {
            FileOutputStream out = new FileOutputStream("src/main/java/facturas/document.docx");
            document.write(out);
            out.close();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            notifications.showErrorNotification("Error generando documento");
        }
    }

    public static void fillFeesTable(XWPFDocument document){
        XWPFTable table = document.getTables().get(0);

        //fill first fee
        // Access the first row of the table (assuming there is at least one row)
        XWPFTableRow row = table.getRow(1);

        // Access the first cell in the row (assuming there is at least one cell)
        XWPFTableCell cell = row.getCell(0);

        // Write new text to the cell
        cell.setText("New content for A1");
    }

}

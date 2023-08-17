package documentGenerator;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import util.formatter;
import util.maths;
import util.notifications;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class word {

    private static ArrayList<Double> amounts = new ArrayList<>();
    private static ArrayList<Double> ITBIS = new ArrayList<>();
    private static double plainTotal;
    private static double mapfreRetention;

    public static void addAmount(double amount) {
        amounts.add(amount);
    }

    public static void addITBIS(double ITBISFromAmount) {
        ITBIS.add(ITBISFromAmount);
    }

    public static Map<String, Map<String, String>> readReceiptJson() {
        ObjectMapper mapper = new ObjectMapper();
        Map data = null;

        try {
            data = mapper.readValue(new File("src/main/resources/json/factura.json"), Map.class);
        } catch (IOException ex) {
            ex.printStackTrace();
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

                    if (text != null && text.contains("[RNC_OFC]")) {
                        text = text.replace("[RNC_OFC]", generalInfo.get("RNC"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[FECHA_FACTURACION]")) {
                        text = text.replace("[FECHA_FACTURACION]", generalInfo.get("Fecha"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[NCF]")) {
                        text = text.replace("[NCF]", generalInfo.get("Numero de Comprobante Fiscal"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[FECHA_VENCI]")) {
                        text = text.replace("[FECHA_VENCI]", generalInfo.get("Fecha de expiracion"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[RNC_CLIENT]")) {
                        text = text.replace("[RNC_CLIENT]", generalInfo.get("RNC del Cliente"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[CLIENT_NAME          ]")) {
                        text = text.replace("[CLIENT_NAME          ]",String.format("%-23s",generalInfo.get("Cliente")));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[ASEGURADO]")) {
                        text = text.replace("[ASEGURADO]", generalInfo.get("Nombre del asegurado"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[NUMERO_EXPEDIEN]")) {
                        text = text.replace("[NUMERO_EXPEDIEN]", generalInfo.get("Numero de expediente"));
                        run.setText(text, i);
                    } else if (text != null && text.contains("[SIGNATURE]")) {
                        text = text.replace("[SIGNATURE]", generalInfo.get("Cliente"));
                        run.setText(text, i);
                    }
                }

            }
        }

        fillFeesTable(document);

        // Prompt the user to choose a file location and name
        JFileChooser fileChooser = new JFileChooser();
        int userChoice = fileChooser.showSaveDialog(null);

        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();

            // Add the .docx extension if not provided by the user
            if (!filePath.toLowerCase().endsWith(".docx")) {
                filePath += ".docx";
            }

            // Save the document
            try {
                FileOutputStream out = new FileOutputStream(filePath);
                document.write(out);
                out.close();
                document.close();
            } catch (Exception e) {
                e.printStackTrace();
                notifications.showErrorNotification("Error generando documento");
            }
        }
    }

    public static void fillFeesTable(XWPFDocument document) {
        //Clean amounts and ITBIS arrays
        amounts.clear();
        ITBIS.clear();
        Map<String, String> firstFee = readReceiptJson().get("PRIMER HONORARIO:");
        Map<String, String> secondFee = readReceiptJson().get("SEGUNDO HONORARIO:");
        Map<String, String> thirdFee = readReceiptJson().get("TERCER HONORARIO:");
        Map<String, String> forthFee = readReceiptJson().get("CUARTO HONORARIO:");
        Map<String, String> fifthFee = readReceiptJson().get("QUINTO HONORARIO:");
        XWPFTable table = document.getTables().get(0);

        //fill first fee
        // Fill concept
        table.getRow(1).getCell(0).setText(firstFee.get("Razon: "));
        //Fill date
        table.getRow(1).getCell(1).setText(firstFee.get("Fecha: "));
        //Fill amount
        table.getRow(1).getCell(2).setText(firstFee.get("Monto: "));
        //Fill ITBIS
        table.getRow(1).getCell(3).setText(maths.getITBIS(firstFee.get("Monto: ")));
        //Fill amount with ITBIS
        table.getRow(1).getCell(4).setText(maths.getAmountWithITBIS(firstFee.get("Monto: ")));


        //fill second fee
        if (secondFee != null) {
            // Fill concept
            table.getRow(2).getCell(0).setText(secondFee.get("Razon: "));
            //Fill date
            table.getRow(2).getCell(1).setText(secondFee.get("Fecha: "));
            //Fill amount
            table.getRow(2).getCell(2).setText(secondFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(2).getCell(3).setText(maths.getITBIS(secondFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(2).getCell(4).setText(maths.getAmountWithITBIS(secondFee.get("Monto: ")));
        }

        //fill third fee
        if (thirdFee != null) {
            // Fill concept
            table.getRow(3).getCell(0).setText(thirdFee.get("Razon: "));
            //Fill date
            table.getRow(3).getCell(1).setText(thirdFee.get("Fecha: "));
            //Fill amount
            table.getRow(3).getCell(2).setText(thirdFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(3).getCell(3).setText(maths.getITBIS(thirdFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(3).getCell(4).setText(maths.getAmountWithITBIS(thirdFee.get("Monto: ")));
        }

        //fill forth fee
        if (forthFee != null) {
            // Fill concept
            table.getRow(4).getCell(0).setText(forthFee.get("Razon: "));
            //Fill date
            table.getRow(4).getCell(1).setText(forthFee.get("Fecha: "));
            //Fill amount
            table.getRow(4).getCell(2).setText(forthFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(4).getCell(3).setText(maths.getITBIS(forthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(4).getCell(4).setText(maths.getAmountWithITBIS(forthFee.get("Monto: ")));
        }

        //fill fifth fee
        if (fifthFee != null) {
            // Fill concept
            table.getRow(5).getCell(0).setText(fifthFee.get("Razon: "));
            //Fill date
            table.getRow(5).getCell(1).setText(fifthFee.get("Fecha: "));
            //Fill amount
            table.getRow(5).getCell(2).setText(fifthFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(5).getCell(3).setText(maths.getITBIS(fifthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(5).getCell(4).setText(maths.getAmountWithITBIS(fifthFee.get("Monto: ")));
        }

        //Fill totals
        table.getRow(6).getCell(2).setText(sumAllValues(amounts));
        table.getRow(6).getCell(3).setText(sumAllValues(ITBIS));
        table.getRow(6).getCell(4).setText(sumAllValues(amounts,ITBIS));
        //Fill 30% row
        table.getRow(7).getCell(4).setText(calculateMapfreRetention());
        //Fill total to pay
        table.getRow(8).getCell(4).setText(formatter.formatIntoMoney(plainTotal - mapfreRetention));

    }

    private static String sumAllValues(ArrayList<Double> listToSum) {
        double total = 0;
        for(double amount : listToSum) {
            total = total + amount;
        }
        return formatter.formatIntoMoney(total);
    }

    private static String sumAllValues(ArrayList<Double> firstListToSum, ArrayList<Double> secondListToSum) {
        double total = 0;
        for(double amount : firstListToSum) {
            total = total + amount;
        }
        for(double amount : secondListToSum) {
            total = total + amount;
        }
        plainTotal = total;
        return formatter.formatIntoMoney(total);
    }

    private static String calculateMapfreRetention(){
        double total = 0;
        for(double amount : ITBIS) {
            total = total + amount;
        }
        mapfreRetention = total * 0.3;
        return formatter.formatIntoMoney(total * 0.3);
    }

}

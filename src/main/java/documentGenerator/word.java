package documentGenerator;


import com.fasterxml.jackson.databind.ObjectMapper;
import core.core;
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
            data = mapper.readValue(new File(core.sourceFolder + "/json/factura.json"), Map.class);
        } catch (IOException ex) {
            ex.printStackTrace();
            notifications.showErrorNotification("Error generando documento");
        }
        return data;
    }

    public static void writeOnWord() throws IOException {

        Map<String, Map<String, String>> receiptJson = readReceiptJson();
        Map<String, String> generalInfo = receiptJson.get("INFORMACION GENERAL");

        // Create a new document
        XWPFDocument document =
                new XWPFDocument(new FileInputStream(core.sourceFolder + "/templates/template_receipt.docx"));

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
                        text = text.replace("[CLIENT_NAME          ]", String.format("%-23s", generalInfo.get("Cliente")));
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

    public static String getITBIS(String amount) {
        double amountFromJson;

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");
        amount = amount.replace(".00", "");
        amountFromJson = Double.parseDouble(amount);
        //Here we add the amount specified
        addAmount(amountFromJson);
        amountFromJson = (amountFromJson * 18) / 100;
        //Here we add the calculated ITBIS
        addITBIS(amountFromJson);
        return formatter.formatIntoMoney(amountFromJson);
    }

    public static String getAmountWithITBIS(String amount) {
        double amountFromJson;

        amount = amount.replace("$", "");
        amount = amount.replace(",", "");
        amount = amount.replace(".00", "");
        amountFromJson = Double.parseDouble(amount);
        amountFromJson = (amountFromJson * 18) / 100 + amountFromJson;
        return formatter.formatIntoMoney(amountFromJson);
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
        Map<String, String> sixthFee = readReceiptJson().get("SEXTO HONORARIO:");
        Map<String, String> seventhFee = readReceiptJson().get("SEPTIMO HONORARIO:");
        Map<String, String> eighthFee = readReceiptJson().get("OCTAVO HONORARIO:");
        Map<String, String> ninethFee = readReceiptJson().get("NOVENO HONORARIO:");
        Map<String, String> tenthFee = readReceiptJson().get("DECIMO HONORARIO:");
        Map<String, String> eleventhFee = readReceiptJson().get("ONCEAVO HONORARIO:");
        XWPFTable table = document.getTables().get(0);
        boolean deleteSecond = false;
        boolean deleteThird = false;
        boolean deleteForth = false;
        boolean deleteFifth = false;
        boolean deleteSixth = false;
        boolean deleteSeventh = false;
        boolean deleteEighth = false;
        boolean deleteNineth = false;
        boolean deleteTenth = false;
        boolean deleteEleventh = false;

        //fill first fee
        // Fill concept
        table.getRow(1).getCell(0).setText(firstFee.get("Razon: "));
        //Fill date
        table.getRow(1).getCell(1).setText(firstFee.get("Fecha: "));
        //Fill amount
        table.getRow(1).getCell(2).setText(firstFee.get("Monto: "));
        //Fill ITBIS
        table.getRow(1).getCell(3).setText(getITBIS(firstFee.get("Monto: ")));
        //Fill amount with ITBIS
        table.getRow(1).getCell(4).setText(getAmountWithITBIS(firstFee.get("Monto: ")));

        //fill second fee
        if (secondFee != null) {
            // Fill concept
            table.getRow(2).getCell(0).setText(secondFee.get("Razon: "));
            //Fill date
            table.getRow(2).getCell(1).setText(secondFee.get("Fecha: "));
            //Fill amount
            table.getRow(2).getCell(2).setText(secondFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(2).getCell(3).setText(getITBIS(secondFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(2).getCell(4).setText(getAmountWithITBIS(secondFee.get("Monto: ")));
        } else {
            deleteSecond = true;
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
            table.getRow(3).getCell(3).setText(getITBIS(thirdFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(3).getCell(4).setText(getAmountWithITBIS(thirdFee.get("Monto: ")));
        } else {
            deleteThird = true;
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
            table.getRow(4).getCell(3).setText(getITBIS(forthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(4).getCell(4).setText(getAmountWithITBIS(forthFee.get("Monto: ")));
        } else {
            deleteForth = true;
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
            table.getRow(5).getCell(3).setText(getITBIS(fifthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(5).getCell(4).setText(getAmountWithITBIS(fifthFee.get("Monto: ")));
        } else {
            deleteFifth = true;
        }

        //fill sixth fee
        if (sixthFee != null) {
            // Fill concept
            table.getRow(6).getCell(0).setText(sixthFee.get("Razon: "));
            //Fill date
            table.getRow(6).getCell(1).setText(sixthFee.get("Fecha: "));
            //Fill amount
            table.getRow(6).getCell(2).setText(sixthFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(6).getCell(3).setText(getITBIS(sixthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(6).getCell(4).setText(getAmountWithITBIS(sixthFee.get("Monto: ")));
        } else {
            deleteSixth = true;
        }

        //fill sixth fee
        if (seventhFee != null) {
            // Fill concept
            table.getRow(7).getCell(0).setText(seventhFee.get("Razon: "));
            //Fill date
            table.getRow(7).getCell(1).setText(seventhFee.get("Fecha: "));
            //Fill amount
            table.getRow(7).getCell(2).setText(seventhFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(7).getCell(3).setText(getITBIS(seventhFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(7).getCell(4).setText(getAmountWithITBIS(seventhFee.get("Monto: ")));
        } else {
            deleteSeventh = true;
        }

        //fill sixth fee
        if (eighthFee != null) {
            // Fill concept
            table.getRow(8).getCell(0).setText(eighthFee.get("Razon: "));
            //Fill date
            table.getRow(8).getCell(1).setText(eighthFee.get("Fecha: "));
            //Fill amount
            table.getRow(8).getCell(2).setText(eighthFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(8).getCell(3).setText(getITBIS(eighthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(8).getCell(4).setText(getAmountWithITBIS(eighthFee.get("Monto: ")));
        } else {
            deleteEighth = true;
        }

        if (ninethFee != null) {
            // Fill concept
            table.getRow(9).getCell(0).setText(ninethFee.get("Razon: "));
            //Fill date
            table.getRow(9).getCell(1).setText(ninethFee.get("Fecha: "));
            //Fill amount
            table.getRow(9).getCell(2).setText(ninethFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(9).getCell(3).setText(getITBIS(ninethFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(9).getCell(4).setText(getAmountWithITBIS(ninethFee.get("Monto: ")));
        } else {
            deleteNineth = true;
        }

        if (tenthFee != null) {
            // Fill concept
            table.getRow(10).getCell(0).setText(tenthFee.get("Razon: "));
            //Fill date
            table.getRow(10).getCell(1).setText(tenthFee.get("Fecha: "));
            //Fill amount
            table.getRow(10).getCell(2).setText(tenthFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(10).getCell(3).setText(getITBIS(tenthFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(10).getCell(4).setText(getAmountWithITBIS(tenthFee.get("Monto: ")));
        } else {
            deleteTenth = true;
        }

        if (eleventhFee != null) {
            // Fill concept
            table.getRow(11).getCell(0).setText(eleventhFee.get("Razon: "));
            //Fill date
            table.getRow(11).getCell(1).setText(eleventhFee.get("Fecha: "));
            //Fill amount
            table.getRow(11).getCell(2).setText(eleventhFee.get("Monto: "));
            //Fill ITBIS
            table.getRow(11).getCell(3).setText(getITBIS(eleventhFee.get("Monto: ")));
            //Fill amount with ITBIS
            table.getRow(11).getCell(4).setText(getAmountWithITBIS(eleventhFee.get("Monto: ")));
        } else {
            deleteEleventh = true;
        }

        if (deleteEleventh) {
            table.removeRow(11);
        }
        if (deleteTenth) {
            table.removeRow(10);
        }
        if (deleteNineth) {
            table.removeRow(9);
        }
        if (deleteEighth) {
            table.removeRow(8);
        }
        if (deleteSeventh) {
            table.removeRow(7);
        }
        if (deleteSixth) {
            table.removeRow(6);
        }
        if (deleteFifth) {
            table.removeRow(5);
        }
        if (deleteForth) {
            table.removeRow(4);
        }
        if (deleteThird) {
            table.removeRow(3);
        }
        if (deleteSecond) {
            table.removeRow(2);
        }

        //Fill totals
        if (table.getNumberOfRows() != 5) {
            table.getRow(table.getNumberOfRows() - 3).getCell(2).setText(sumAllValues(amounts));
            table.getRow(table.getNumberOfRows() - 3).getCell(3).setText(sumAllValues(ITBIS));
            table.getRow(table.getNumberOfRows() - 3).getCell(4).setText(sumAllValues(amounts, ITBIS));
        } else {
            sumAllValues(amounts);
            sumAllValues(ITBIS);
            sumAllValues(amounts, ITBIS);
            table.removeRow(table.getNumberOfRows() - 3);
        }
        //Fill 30% row
        table.getRow(table.getNumberOfRows() - 2).getCell(4).setText(calculateMapfreRetention());
        //Fill total to pay
        table.getRow(table.getNumberOfRows() - 1).getCell(4).setText(formatter.formatIntoMoney(plainTotal - mapfreRetention));

    }

    private static String sumAllValues(ArrayList<Double> listToSum) {
        double total = 0;
        for (double amount : listToSum) {
            total = total + amount;
        }
        return formatter.formatIntoMoney(total);
    }

    private static String sumAllValues(ArrayList<Double> firstListToSum, ArrayList<Double> secondListToSum) {
        double total = 0;
        for (double amount : firstListToSum) {
            total = total + amount;
        }
        for (double amount : secondListToSum) {
            total = total + amount;
        }
        plainTotal = total;
        return formatter.formatIntoMoney(total);
    }

    private static String calculateMapfreRetention() {
        double total = 0;
        for (double amount : ITBIS) {
            total = total + amount;
        }
        mapfreRetention = total * 0.3;
        return formatter.formatIntoMoney(total * 0.3);
    }

}

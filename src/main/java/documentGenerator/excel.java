package documentGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.core;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.maths;
import util.notifications;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class excel {

    public static String filePath = null;

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

    public static void writeOnExcel() throws IOException {

        Map<String, Map<String, String>> receiptJson = readReceiptJson();
        Map<String, String> generalInfo = receiptJson.get("INFORMACION GENERAL");
        Workbook document = null;
        int continueFromExisting = 0;


        continueFromExisting =
                JOptionPane.showConfirmDialog(null, "Desea continuar con un excel existente?"
                        , "Continuar con factura existente?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        notifications.showPopUpNotification("Seleccione el destino del excel", "Seleccione el destino del excel");
        if (continueFromExisting == JOptionPane.YES_OPTION) {
            JFileChooser existingFileChooser = new JFileChooser();
            int choice = existingFileChooser.showSaveDialog(null);

            if (choice == JFileChooser.APPROVE_OPTION) {
                File selectedFile = existingFileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
                document =
                        new XSSFWorkbook(new FileInputStream(filePath));
            }
        } else {
            // Create a new document
            filePath = null;
            document =
                    new XSSFWorkbook(new FileInputStream(core.sourceFolder + "/templates/template_receipt.xlsx"));
        }

        assert document != null;
        Sheet sheet = document.getSheetAt(0);
        String cellValue;
        for (Row row : sheet) {

            try {
                cellValue = row.getCell(0).getStringCellValue();
            } catch (NullPointerException ignore) {
                cellValue = "";
            }
            if (cellValue.equals("")) {
                //select type of ID
                row.getCell(0).setCellValue("RNC");
                row.getCell(1).setCellValue(generalInfo.get("RNC"));
                row.getCell(2).setCellValue(generalInfo.get("Nombre del asegurado"));
                row.getCell(3).setCellValue(generalInfo.get("Numero de expediente"));
                row.getCell(4).setCellValue(generalInfo.get("Fecha"));
                row.getCell(5).setCellValue(generalInfo.get("Numero de Comprobante Fiscal"));
                row.getCell(6).setCellValue(maths.calculateTotalWithITBIS(receiptJson));
                break;
            }
        }

        if (filePath == null) {
            // Prompt the user to choose a file location and name
            JFileChooser fileChooser = new JFileChooser();
            int userChoice = fileChooser.showSaveDialog(null);

            if (userChoice == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePath = selectedFile.getAbsolutePath();

                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }
            }
        }

        // Save the document
        try {
            assert filePath != null;
            FileOutputStream out = new FileOutputStream(filePath);
            document.write(out);
            out.close();
            document.close();
            notifications.showPopUpNotification("Por favor seleccione el destino de la factura en Word", "Factura en Excel creada");
        } catch (Exception e) {
            e.printStackTrace();
            notifications.showErrorNotification("Error generando documento");
        }
    }

}

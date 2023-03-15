package documentGenerator;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class word {

    public static void writeOnWord() throws IOException, InvalidFormatException {
        // Create a new document
        XWPFDocument document = new XWPFDocument();

        // Create header
        XWPFHeader header = document.createHeader(HeaderFooterType.DEFAULT);

        //Add an image to the header

        XWPFParagraph headerImage = header.createParagraph();
        headerImage.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun runHeaderImage = headerImage.createRun();
        runHeaderImage.addPicture(
                new FileInputStream("src/main/resources/logo/headerLogo.png"),
                XWPFDocument.PICTURE_TYPE_PNG, "image.png", Units.toEMU(190), Units.toEMU(100));

        //Writes on the header
        XWPFParagraph headerText = header.createParagraph();
        XWPFRun run = headerText.createRun();
        run.setText("Abraham Lincoln No. 1017 – B, Res. Lincoln,");
        headerText.setAlignment(ParagraphAlignment.RIGHT);

        XWPFParagraph headerText2 = header.createParagraph();
        run = headerText2.createRun();
        run.setText("Apto. 15, Ens. Piantini, Sto, Dgo, D.N.");
        headerText2.setAlignment(ParagraphAlignment.RIGHT);

        XWPFParagraph headerText3 = header.createParagraph();
        run = headerText3.createRun();
        run.setText("Tel: 809-245-7438/ 809-697-6625");
        headerText3.setAlignment(ParagraphAlignment.RIGHT);

        XWPFParagraph headerText4 = header.createParagraph();
        run = headerText4.createRun();
        run.setText("ctavarez@tavarezmancebo.com");
        headerText4.setAlignment(ParagraphAlignment.RIGHT);

        XWPFParagraph headerText5 = header.createParagraph();
        run = headerText5.createRun();
        run.setText("www.tavarezmancebo.com");
        headerText5.setAlignment(ParagraphAlignment.RIGHT);

        // Create a new paragraph
        XWPFParagraph paragraph = document.createParagraph();

        // Add text to the paragraph
        run = paragraph.createRun();
        run.setText("Hello, world!");

        // Save the document
        try {
            FileOutputStream out = new FileOutputStream("src/main/java/facturas/document.docx");
            document.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package be.leerstad.helpers.PdfGenerators;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfBackdrop {

    private PdfBackdrop(){}

    private static final String pdfBackdrop = "src/main/resources/tables/backdrop.pdf";
    private static final String IMAGE = "src/main/resources/images/chez.jpg";

    public static void createPDF() throws IOException, DocumentException {


        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, new FileOutputStream(pdfBackdrop));
        document.setMargins(5, 5, 25, 5);
        document.open();

        Image image = Image.getInstance(IMAGE);
        image.scalePercent(50);
        image.setAlignment(1);
        document.add(image);
        document.close();
    }
}

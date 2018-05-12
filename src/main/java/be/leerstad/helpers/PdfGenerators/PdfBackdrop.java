package be.leerstad.helpers.PdfGenerators;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfBackdrop {

    private PdfBackdrop(){}

    private static final String pdfBackdrop = "src/main/resources/tables/backdrop.pdf";
    private static final String IMAGE = "src/main/resources/images/chez.jpg";

    public static void createPDF() throws IOException, DocumentException {


        //path maken indien niet existerend
        Path location = Paths.get("src/main/resources/tables".replace("/", File.separator) );

        if (!Files.exists(location)){
            try {
                Files.createDirectories(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, new FileOutputStream(pdfBackdrop));
        document.setMargins(5, 5, 25, 5);
        document.open();

        Image image = Image.getInstance(IMAGE);
        image.scalePercent(50);
        image.setAlignment(1);
        document.add(image);
        document.close();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

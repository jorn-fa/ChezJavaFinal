package be.leerstad.helpers.PdfGenerators;

import be.leerstad.Database.RapportDaoImpl;
import be.leerstad.Ober;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;


public class PdfTotalByWaiter {

    private PdfTotalByWaiter(){}
    private static final RapportDaoImpl rapportDaoImpl = new RapportDaoImpl();

    private static final int top = 842;  //hoogte A4 formaat itext - rekenwaarde
    private static final int leftRightMargin = 50;


    private static final String pdfBackdrop = "src/main/resources/tables/backdrop.pdf";

    public static void totalWaiter(String destination, Ober ober) throws IOException, DocumentException {
        PdfBackdrop.createPDF();
        PdfReader pdfReader = new PdfReader(pdfBackdrop);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(destination));

        String text = "Total sale amount by waiter: " + ober.getVoornaam() + " " + ober.getNaam();
        DecimalFormat df = new DecimalFormat("##.### €");
        String text2 = "Total Revenue of " + df.format(rapportDaoImpl.giveSaleResult(ober));


        PdfContentByte canvas = pdfStamper.getOverContent(1);
        ColumnText.showTextAligned(canvas, 50, new Phrase(text), leftRightMargin, top - 200, 0);
        ColumnText.showTextAligned(canvas, 50, new Phrase(text2), leftRightMargin, top - 240, 0);
        pdfStamper.close();
    }
}

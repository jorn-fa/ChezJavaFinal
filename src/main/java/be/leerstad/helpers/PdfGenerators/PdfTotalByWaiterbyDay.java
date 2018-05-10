package be.leerstad.helpers.PdfGenerators;

import be.leerstad.Consumption;
import be.leerstad.Database.BeveragesDAOImpl;
import be.leerstad.Database.RapportDaoImpl;
import be.leerstad.Database.WaiterDAOImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class PdfTotalByWaiterbyDay {

    private static int top = 842;  //hoogte A4 formaat itext - rekenwaarde
    private static int right = 595; //breedte ^
    private static int leftRightMargin = 50;
    private static double totalSum=0;  //rekenwaarde overzicht per datum

    private static final String pdfBackdrop = "src/main/resources/tables/backdrop.pdf";
    private static HashMap<Integer, String> waiters = new WaiterDAOImpl().waiterList();
    private static List<Consumption> beveragelijst = new BeveragesDAOImpl().pricelijst();

    private static RapportDaoImpl rapportDaoImpl = new RapportDaoImpl();
    private static final String IMAGE = "src/main/resources/images/chez.jpg";




    private PdfTotalByWaiterbyDay(){}

    public static void totalByWaitersSortedByday(String destination, LocalDate localDate) throws IOException, DocumentException  {
        List<Consumption> lijst = rapportDaoImpl.printByDay(localDate);


        PdfBackdrop.createPDF();
        PdfReader pdfReader = new PdfReader(pdfBackdrop);

        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(destination));
        int columnCount = 4;

        PdfPTable table = new PdfPTable(columnCount);
        table.setWidthPercentage(50);

        PdfPCell cell;

        table.setSpacingBefore(30f);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        cell = new PdfPCell((new Phrase("Waiter")));
        cell.setFixedHeight(18f);
        cell.setIndent(3f);
        table.addCell(cell);

        cell = new PdfPCell((new Phrase("Type")));
        cell.setFixedHeight(18f);
        cell.setIndent(3f);
        table.addCell(cell);

        cell = new PdfPCell((new Phrase("Quantity ")));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setIndent(3f);
        table.addCell(cell);

        cell = new PdfPCell((new Phrase("Sum ")));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setIndent(3f);
        table.addCell(cell);

        cell = new PdfPCell((new Phrase(" ")));
        cell.setBorder(0);
        for (int teller = 0; teller < columnCount; teller++) {
            table.addCell(cell);
        }


        for (Consumption Lijst : lijst) {

            table.addCell(waiters.get(Lijst.getWaiterID())); //naam
            table.addCell(beveragelijst.get(Lijst.getBeverageId() - 1).getNaam());
            int aantal = Lijst.getAantal();

            PdfPCell right = new PdfPCell(new Phrase(Integer.toString(aantal)));
            right.setHorizontalAlignment(Element.ALIGN_RIGHT);
            right.setBorder(0);
            table.addCell(right);
            DecimalFormat df = new DecimalFormat("##.### €");

            double getal = beveragelijst.get(Lijst.getBeverageId() - 1).getPrijs() * aantal;
            totalSum += getal;

            String afdrukGetal = df.format(getal);
            right = new PdfPCell(new Phrase(afdrukGetal));
            right.setHorizontalAlignment(Element.ALIGN_RIGHT);
            right.setBorder(0);
            table.addCell(right);
        }




        NumberFormat numberformat = NumberFormat.getInstance();
        numberformat.setMaximumFractionDigits(2);
        String text = "Sales by waiters -> Sorted on date = " + localDate.toString();
        String text2 = "Total sale on day = " + numberformat.format(totalSum) + " €.";
        PdfContentByte canvas = pdfStamper.getOverContent(1);
        ColumnText.showTextAligned(canvas, 50, new Phrase(text), leftRightMargin, top - 200, 0);
        ColumnText.showTextAligned(canvas, 50, new Phrase(text2), leftRightMargin, top - 230, 0);

        table.setTotalWidth(right - leftRightMargin - leftRightMargin);


        table.writeSelectedRows(0, 27, leftRightMargin, top - 250, canvas);

        Image image = Image.getInstance(IMAGE);
        image.scalePercent(50);
        image.setAlignment(1);
        image.setAbsolutePosition((right / 2) - 100, top - 150);


        int paginaNummer = 1;
        int rijteller = table.getRows().size();
        int maxPerPagina = 35;
        if (rijteller > 28) {
            paginaNummer++;
            pdfStamper.insertPage(paginaNummer, PageSize.A4);
            canvas = pdfStamper.getOverContent(paginaNummer);
            canvas.addImage(image);
            table.writeSelectedRows(27, (27 + maxPerPagina), leftRightMargin, top - 170, canvas);
            rijteller -= 27;
        }


        while (rijteller > maxPerPagina) {
            paginaNummer++;
            pdfStamper.insertPage(paginaNummer, PageSize.A4);
            canvas = pdfStamper.getOverContent(paginaNummer);
            canvas.addImage(image);
            table.writeSelectedRows(((paginaNummer - 1) * maxPerPagina) - 27, (paginaNummer * maxPerPagina) - 27, leftRightMargin, top - 170, canvas);
            rijteller -= maxPerPagina;
        }
        pdfStamper.close();
        totalSum=0;//reset
    }
}

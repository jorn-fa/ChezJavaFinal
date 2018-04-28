package be.leerstad.helpers.PdfGenerators;

import be.leerstad.Consumption;
import be.leerstad.Database.BeveragesDAOImpl;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.Database.WaiterDAOImpl;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class PdfTotalByWaiters {

    private static final String pdfBackdrop = "src/main/resources/tables/backdrop.pdf";
    private static final int top = 842;  //hoogte A4 formaat itext - rekenwaarde
    private static final int right = 595; //breedte ^
    private static final int leftRightMargin = 50;

    private  HashMap PdfTotalByWaiter(){return new HashMap();}
    private static HashMap<Integer, String> waiters = new WaiterDAOImpl().waiterList();
    private static List<Consumption> consumptionList = new OrdersDAOImpl().orderList();
    private static HashMap<Integer, Double> pricelist = new BeveragesDAOImpl().priceList();

    private static double totalByWaiters(int getal) {

        List<Consumption> employees = consumptionList.stream()
                .filter(p -> p.getWaiterID() == getal)

                .map(p -> new Consumption(p.getBeverageId(), p.getAantal(), p.getWaiterID()))
                .collect(Collectors.toList());
        employees.forEach(x -> x.setPrijs((pricelist.get(x.getBeverageId())) * x.getAantal()));
        return (employees.stream().mapToDouble(Consumption::getPrijs).sum());

    }


    public static HashMap totalByWaiters(String destination) throws IOException, DocumentException {


        HashMap<String,Double> summary = new HashMap<>();


        PdfBackdrop.createPDF();
        PdfReader pdfReader = new PdfReader(pdfBackdrop);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(destination));

        PdfPTable table = new PdfPTable(2);
        PdfPCell cell;

        table.setSpacingBefore(30f);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        cell = new PdfPCell((new Phrase("Waiter")));
        cell.setFixedHeight(18f);

        cell.setIndent(3f);
        table.addCell(cell);


        cell = new PdfPCell((new Phrase("Amount")));
        cell.setFixedHeight(18f);
        cell.setIndent(3f);
        table.addCell(cell);


        cell = new PdfPCell((new Phrase(" ")));
        cell.setBorder(0);
        cell.setIndent(3f);
        table.addCell(cell);
        table.addCell(cell);

        for (int teller = 1; teller <= waiters.size(); teller++) {
            table.addCell(waiters.get(teller)); //naam
            DecimalFormat df = new DecimalFormat("##.### €");
            table.addCell(String.valueOf(df.format((totalByWaiters(teller)))));

            summary.put(waiters.get(teller),totalByWaiters(teller));
        }

        String text = "Total Revenue by waiters";
        PdfContentByte canvas = pdfStamper.getOverContent(1);

        ColumnText.showTextAligned(canvas, 50, new Phrase(text), leftRightMargin, top - 200, 0);
        table.setTotalWidth(right - leftRightMargin - leftRightMargin);
        table.writeSelectedRows(0, -1, leftRightMargin, top - 250, canvas);
        pdfStamper.close();



        return summary;
    }
}

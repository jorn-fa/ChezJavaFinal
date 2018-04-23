package be.leerstad;

import be.leerstad.Database.BeveragesDAOImpl;
import be.leerstad.Database.OrdersDAO;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.Database.WaiterDAOImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class PDFtest {

    //todo  bijwerken naar aanpasbaar

    private WaiterDAOImpl a = new WaiterDAOImpl();
    private BeveragesDAOImpl b = new BeveragesDAOImpl();
    private OrdersDAO c = new OrdersDAOImpl();

    private HashMap<Integer, String> waiters = a.waiterList();
    private HashMap<Integer,Double> pricelist = b.priceList();
    private List<Consumption> lijst = c.orderList();

    //nog aan te passen
    private static final String DEST = "results/tables/simple_table.pdf";
    private static final String IMAGE = "src/main/resources/images/chez.jpg";


    public double getal(int getal){
        List<Consumption> employees = lijst.stream()
                .filter(p -> p.getWaiterID()==getal)
                .map(p -> new Consumption(p.getBeverageId(),p.getAantal(),p.getWaiterID()))
                .collect(Collectors.toList());
        employees.forEach(x -> x.setPrijs((pricelist.get(x.getBeverageId())) * x.getAantal() )  );
        return ( employees.stream().mapToDouble(Consumption::getPrijs).sum());

    }

    //nog uitbouwen naar dag
    public void createPdfbyDay(String dest) throws IOException, DocumentException {

        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.setMargins(5,5,25,5);
        document.open();

        Image image = Image.getInstance(IMAGE);
        image.scalePercent(50);
        image.setAlignment(1);

        Paragraph p2 = new Paragraph( "Revenue by waiters - sorted by date: " + "datum");
        p2.setAlignment(0);
        p2.setIndentationLeft(60);
        p2.setSpacingAfter(15f);
        p2.setSpacingBefore(5f);



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
        //lege rij
        cell = new PdfPCell((new Phrase(" ")));
        cell.setBorder(0);
        cell.setIndent(3f);
        table.addCell(cell);
        table.addCell(cell);

        for(int aw = 1; aw <= waiters.size(); aw++){
            table.addCell(waiters.get(aw));
            DecimalFormat df = new DecimalFormat("##.### €");
            table.addCell(String.valueOf(df.format((getal(aw)))));
        }

        document.add(image);
        document.add(p2);
        document.add(table);
        document.close();
    }




    public static void main(String[] args) throws IOException,

         DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PDFtest().createPdfbyDay(DEST);
    }


}
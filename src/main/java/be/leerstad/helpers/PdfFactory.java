package be.leerstad.helpers;

import be.leerstad.Ober;
import be.leerstad.helpers.PdfGenerators.PdfPiechartTop3;
import be.leerstad.helpers.PdfGenerators.PdfTotalByWaiter;
import be.leerstad.helpers.PdfGenerators.PdfTotalByWaiterbyDay;
import be.leerstad.helpers.PdfGenerators.PdfTotalByWaiters;
import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class PdfFactory {

    private static Logger logger = Logger.getLogger(PdfFactory.class.getName());

//todo  wissen
    private static final String pdfDestination = "results/tables/simple_table1.pdf";
    private static final String pdfDestination2 = "results/tables/simple_table2.pdf";
    private static final String pdfDestination3 = "results/tables/simple_table3.pdf";
    private static final String pdfDestination4 = "results/tables/simple_table4.pdf";


    /**
     *
     * @param type totalwaiterssortedbyday
     * @param destination destination
     * @param localDate localdate
     *
     */
    public static void GetPDFbyType(String type, String destination, LocalDate localDate) throws IOException, DocumentException {

        switch (type.toLowerCase()) {
            case "totalbywaiterssortedbyday":
                logger.debug("opvragen waiters per dag");
                PdfTotalByWaiterbyDay.totalByWaitersSortedByday(destination,localDate);
                break;
        }

    }

    /**
     *
     * @param type totalwaiter,topWaiterPieChart
     *
     */
    public  static HashMap GetPDFbyType(String type, String destination) throws IOException, DocumentException {
            HashMap result = new HashMap();

        switch (type.toLowerCase()) {
                case "totalwaiter":
                    result=PdfTotalByWaiters.totalByWaiters(destination);
                break;
            case "topwaiterpiechart":
                PdfPiechartTop3.topWaiterPieChart(destination);
                break;
            case "totalwaiters":
                PdfTotalByWaiters.totalByWaiters(destination);
                break;
            }
            return result;
        }


    /**
     *
     * @param type totalWaiter
     * @param destination file location
     * @param ober waiter
     * @throws IOException Ioexeption
     * @throws DocumentException DocumentExeption
     */
    public static void GetPDFbyType(String type, String destination, Ober ober) throws IOException, DocumentException {

        switch (type.toLowerCase()) {
            case "totalwaiter":
                PdfTotalByWaiter.totalWaiter(destination, ober);
                break;
            default:
                System.out.println("ja");
                break;
        }
}



    public static void main(String[] args) throws IOException, DocumentException {

        LocalDate datum = LocalDate.of(2017, 12, 21);
        File file = new File(pdfDestination);
        Ober ober = new Ober(1, "peeters", "wout");

        file.getParentFile().mkdirs();


        PdfFactory.GetPDFbyType("totalwaiter", pdfDestination, ober );


        PdfFactory.GetPDFbyType("totalwaiters", pdfDestination2);
        PdfFactory.GetPDFbyType("totalByWaitersSortedByday",pdfDestination3,datum);
        PdfFactory.GetPDFbyType("topWaiterPieChart",pdfDestination4);


    }


}

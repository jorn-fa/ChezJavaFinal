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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;

public class PdfFactory {

    private static Logger logger = Logger.getLogger(PdfFactory.class.getName());
    private static String waar = System.getProperty("user.dir")+"-src-main-resources-tables-";



    public PdfFactory(){


        //path maken indien niet existerend
        Path location = Paths.get(waar.replace("-",File.separator) );

        if (!Files.exists(location)){
            try {
                Files.createDirectories(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }





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


}

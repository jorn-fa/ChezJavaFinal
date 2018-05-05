package be.leerstad.helpers;

import com.itextpdf.text.DocumentException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class PieChart {

    private static final String destination = "results/tables/piechart.jpg";
    private static final String pdfDestination3 = "results/tables/temp.pdf";


    public static void GeneratePiechart() throws IOException, DocumentException {

        PdfFactory pdfFactory = new PdfFactory();

        HashMap<String, Double> unsorted =pdfFactory.GetPDFbyType("totalbywaiters",pdfDestination3);
                 Map<String, Double> sorted = new LinkedHashMap<>();


        unsorted.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));


        List<String> keyList = Collections.list(Collections.enumeration(sorted.keySet()));
        List<Double> valueList = Collections.list(Collections.enumeration(sorted.values()));


        for (int teller = 0; teller < 3; teller++) {
            System.out.println(keyList.get(teller) + " " + valueList.get(teller));
        }

        DefaultPieDataset dataset = new DefaultPieDataset();

        for (int teller = 0; teller < 3; teller++) {
            dataset.setValue(keyList.get(teller), valueList.get(teller));
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Top waiter",   // chart title
                dataset,          // data
                true,             // include legend
                true,
                false);

        int width = 640;   /* Width of the image */
        int height = 480;  /* Height of the image */
        File pieChart = new File(destination);
        ChartUtilities.saveChartAsJPEG(pieChart, chart, width, height);

        Path file1 = Paths.get(pdfDestination3);
        try {
            Files.deleteIfExists(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
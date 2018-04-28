package be.leerstad.helpers.PdfGenerators;

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

public class Piechart {

    private static final String piechartLocation = "src/main/resources/images/piechart.jpg";
    private static final String tempDestination = "src/main/resources/tables/temp.pdf";

    public static void generatePiechart() throws IOException, DocumentException {


        HashMap<String, Double> unsorted = PdfTotalByWaiters.totalByWaiters(tempDestination);
        Map<String, Double> sorted = new LinkedHashMap<>();

        unsorted.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEachOrdered(x -> sorted.put(x.getKey(), x.getValue()));

        List<String> keyList = Collections.list(Collections.enumeration(sorted.keySet()));
        List<Double> valueList = Collections.list(Collections.enumeration(sorted.values()));

        DefaultPieDataset dataset = new DefaultPieDataset();

        for (int teller = 0; teller < 3; teller++) {
            dataset.setValue(keyList.get(teller), new Double(valueList.get(teller)));
        }

        JFreeChart chart = ChartFactory.createPieChart("Top waiters", dataset, true, true, false);

        int width = 600;   /* Width of the image */
        int height = 480;  /* Height of the image */
        File pieChart = new File(piechartLocation);
        ChartUtilities.saveChartAsJPEG(pieChart, chart, width, height);

        Path file1 = Paths.get(tempDestination);
        try {
            Files.deleteIfExists(file1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

import be.leerstad.Cafe;
import be.leerstad.Ober;
import be.leerstad.helpers.PdfFactory;
import com.itextpdf.text.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PdfFactoryTest {



    private Cafe cafe;
    private LocalDate localDate= LocalDate.now();
    private Ober ober = new Ober (1,"peters","wout");
    private String totalWaiterProp;
    private String totalWaitersProp;
    private String totalSortedProp;
    private String topWaiterPie;


    private void loadProps(){
        final String propertiesName = "cafe.properties";
        Properties props = new Properties ();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream (propertiesName)) {

            props.load (inputStream);

        } catch (Exception e) {
            e.printStackTrace();

        }

        totalWaiterProp = props.getProperty ("totalwaiter");
        totalWaitersProp = props.getProperty ("totalwaiters");
        totalSortedProp = props.getProperty ("totalByWaitersSortedByday");
        topWaiterPie = props.getProperty ("topWaiterPieChart");

    }



    @Before
    public void setUp(){
        cafe = new Cafe("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPDF()
            //unknown type -> throw error
    {
        try {
            PdfFactory.GetPDFbyType("", "", localDate);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException f) {
            f.getLocalizedMessage();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void getHashmapPDF()
    //unknown type -> throw error
    {
        try {
            PdfFactory.GetPDFbyType("", "");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException f) {
            f.getLocalizedMessage();
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void getPDFOber()
    //unknown type -> throw error
    {

        try {
            PdfFactory.GetPDFbyType("", "",ober);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException f) {
            f.getLocalizedMessage();
        }
    }

    @Test(timeout = 5000)
    public void getPdfs(){
        loadProps();

        try {
            PdfFactory.GetPDFbyType("totalwaiter", totalWaiterProp, ober);
            PdfFactory.GetPDFbyType("totalwaiter", totalWaiterProp);
            PdfFactory.GetPDFbyType("totalbywaiterssortedbyday", totalSortedProp, localDate);
            PdfFactory.GetPDFbyType("topWaiterPieChart", topWaiterPie);
            PdfFactory.GetPDFbyType("totalwaiters", totalWaitersProp);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.getLocalizedMessage();
        }

    }

    @Test
    public void noDirectory(){

        loadProps();
        String folder = System.getProperty("user.dir")+"-src-main-resources-tables-".replace("-", File.separator);

        File folderDirectory = new File(folder);
        if(folderDirectory.isDirectory()){
            File[] files = folderDirectory.listFiles();
            for (File wisFile:files) {
                wisFile.delete();
            }
        }
        try {
            folderDirectory.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(folderDirectory.exists());
        //zou nieuwe folder moeten maken
        try {
            PdfFactory.GetPDFbyType("totalwaiters", totalWaitersProp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.getLocalizedMessage();
        }
        assertTrue(folderDirectory.exists());
    }

    @Test
    public void constructorTest()
    {
        String folder = System.getProperty("user.dir")+"-src-main-resources-tables-".replace("-", File.separator);
        File folderDirectory = new File(folder);

        PdfFactory pdfFactory = new PdfFactory();
        assertTrue(folderDirectory.exists());
    }



}

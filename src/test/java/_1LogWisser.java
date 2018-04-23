import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class _1LogWisser {

    @Test
    public void logWissen() throws IOException {

            String lognaam = "ChezJava";
            String lognaam2 = "dbase";
            String lognaam3 = "email";
            String lognaam4 = "javafx";

            String teken = File.separator;
            String waar = (System.getProperty("user.dir") + teken + "src" + teken + "main" + teken + "logs" + teken + lognaam + ".log");


            Path file1 = Paths.get(waar + teken);
            Path file2 = Paths.get(waar.replace(lognaam,lognaam2) + teken);

            try {
                Files.deleteIfExists(file1);
                Files.deleteIfExists(file2);
            }
            finally {

            }
        }
    }

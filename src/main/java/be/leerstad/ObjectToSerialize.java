package be.leerstad;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ObjectToSerialize {

    private static Logger logger = Logger.getLogger(Tafel.class.getName());

    public static void main(String [] args) {
        Tafel e = new Tafel( "tafel 1");

        //Ober e = new Ober(1,"jorn","hiel","password");


        try {

            String waar = System.getProperty("user.dir")+"-src-main-serialize-";

            Path location = Paths.get(waar.replace("-",File.separator) );

            // naam baseren op class + id    eg.  tafel.1
            Path name =  Paths.get (location + File.separator + e.getClass().getSimpleName() +"." + e.getNaam() );

            //path maken indien niet existerend
            if (!Files.exists(location)){Files.createDirectories(location);}


            FileOutputStream fileOut = new FileOutputStream(name.toString());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(e);
            out.close();
            fileOut.close();


            logger.info("Serialized data is saved in "+ name.toString() );

        } catch (IOException i) {
            logger.debug("Something gone wrong with file handling ");
            i.printStackTrace();
        }
    }


}

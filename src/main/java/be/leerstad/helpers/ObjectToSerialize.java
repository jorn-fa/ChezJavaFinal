package be.leerstad.helpers;

import be.leerstad.Tafel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ObjectToSerialize {

    private static Logger logger = Logger.getLogger(Tafel.class.getName());

    private String waar = System.getProperty("user.dir")+"-src-main-resources-serialize-";

    public Path getWaar(){
        return Paths.get(waar.replace("-",File.separator) );
    }

    public void Serialize(Tafel tafel){


        try {

            Path location = Paths.get(waar.replace("-", File.separator));

            // naam baseren op class + id    ex.  tafel.1
            Path name = Paths.get(location + File.separator + tafel.getClass().getSimpleName() + "." + tafel.getNaam());

            //path maken indien niet existerend
            if (!Files.exists(location)) {
                Files.createDirectories(location);
            }


            //nakijken op read-only
            File file = new File(name.toString());
            if (file.exists() && !file.canWrite()) {
                geefMelding();
            }


            FileOutputStream fileOut = new FileOutputStream(name.toString());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            out.writeObject(tafel);
            out.close();
            fileOut.close();


            logger.info("Serialized data is saved in " + name.toString());
        }
        catch (IOException i) {
            logger.debug("Something gone wrong with file handling ");
            i.printStackTrace();
        }


    }

    private void geefMelding() throws IllegalAccessError
    {
        logger.debug("File is read-only");
        throw new IllegalAccessError("File is Read only");
    }


}

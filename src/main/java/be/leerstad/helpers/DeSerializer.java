package be.leerstad.helpers;

import be.leerstad.Tafel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public final class DeSerializer {

    private ObjectToSerialize ob = new ObjectToSerialize();
    private String waar = ob.getWaar().toString();
    private long count = 0;
    private String fileNames[];
    //private static Logger logger = Logger.getLogger(DeSerializer.class.getName());
    private static Logger logger = Logger.getLogger("chezjava");


    public void wisTafel(Tafel tafel)
    {


            try {
                File file = new File(waar + File.separator + "Tafel." + tafel.getNaam());

                if (file.delete()) {
                    logger.debug(file.getName() + " is deleted!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public Tafel[] giveTafel(Tafel[] tafellijst){

        //path maken indien niet existerend

        if (!Files.exists(Paths.get(waar))){
            try {
                Files.createDirectories(Paths.get(waar));
            } catch (IOException e) {
                logger.debug("File directory did not exist");
            }
        }

        try (Stream<Path> files = Files.list(Paths.get(waar))) {
            count=files.count();
            if (count>0){
                //grab file names
                fileNames=new File(waar).list();
            }
            }
         catch (IOException e) {
            logger.debug("No files in directory");
        }

        for(long fileteller=count;fileteller>0;fileteller--){

            Tafel temp;


            if (fileNames != null) {
                try (
                        FileInputStream fis = new FileInputStream (waar + File.separator +  fileNames[Math.toIntExact(fileteller-1)]);
                        ObjectInputStream ois = new ObjectInputStream (fis)) {
                    temp = (Tafel) ois.readObject (); // 4

                    //collectie overlopen en indien hit, overschrijven

                    for (int tafelteller = 0; tafelteller< tafellijst.length; tafelteller++){
                        if (tafellijst[tafelteller].getNaam().equals(temp.getNaam())){
                            tafellijst[tafelteller]=temp;}
                    }


                } catch (Exception e) {
                    e.printStackTrace ();
                }
            }
        }




        return tafellijst;
    }
}

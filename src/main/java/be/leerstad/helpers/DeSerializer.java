package be.leerstad.helpers;

import be.leerstad.Tafel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DeSerializer {

    private ObjectToSerialize ob = new ObjectToSerialize();
    private String waar = ob.getWaar().toString();
    private long count = 0;
    private String fileNames[];


    private void wisser()
    {

        for (int teller=1;teller<10;teller++) {
            try {
                File file = new File(waar + File.separator + "Tafel." + teller);

                if (file.delete()) {
                    System.err.println(file.getName() + " is deleted!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Tafel[] giveTafel(Tafel[] tafellijst){

        try (Stream<Path> files = Files.list(Paths.get(waar))) {
            count=files.count();


            if (count>0){
                //grab file names
                fileNames=new File(waar).list();
            }
            }
         catch (IOException e) {
            e.printStackTrace();
        }

        for(long fileteller=count;fileteller>0;fileteller--){

            Tafel temp;
            boolean magWissen=false;

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

        wisser();

        return tafellijst;


    }

    public static void main(String[] args) {
        DeSerializer ds = new DeSerializer();
        Tafel een = new Tafel("test1");
        Tafel twee = new Tafel("test2");
        Tafel drie = new Tafel("3");
        Tafel vier = new Tafel("test4");
        Tafel[] lijst = new Tafel[]{een,twee,drie,vier};

        for (Tafel tafel:lijst) {
            System.out.println(tafel.getNaam() + " hasorders: " + tafel.hasOrders() ) ;

        }

        lijst=ds.giveTafel(lijst);
        System.out.println("----");

        for (Tafel tafel:lijst) {
            System.out.println(tafel.getNaam() + " hasorders: " + tafel.hasOrders() ) ;

        }


    }
}

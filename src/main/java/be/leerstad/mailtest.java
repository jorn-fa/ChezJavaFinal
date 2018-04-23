package be.leerstad;
import java.io.File;

public class mailtest {



    public static void main(String[] args) {

        File bestand = new File("results/tables/simple_table.pdf");

       Email a = new be.leerstad.Email();

       a.sendMail(bestand.getAbsolutePath(),"Title2");

        if(bestand.exists()){
            System.out.println("ja");
        }

    }

}

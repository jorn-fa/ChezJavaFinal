package be.leerstad;


import be.leerstad.Database.RapportDaoImpl;

import java.time.LocalDate;


public class test {



    public static void main(String[] args) {

        RapportDaoImpl a = new RapportDaoImpl();

        LocalDate datum = LocalDate.of(2017,12,21);
        //java.sql.Date datum = new java.sql.Date(2017,12,-01);


        a.printByDay(datum);

        System.out.println("----------");

        Ober een = new Ober(4,"hiel","jorn","jorn");

        //a.printByDay(datum,een);

        System.out.println("----------");

        a.giveSaleResult(een);

    }
}

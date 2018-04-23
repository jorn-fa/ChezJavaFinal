package be.leerstad.Database;


import be.leerstad.Ober;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public interface RapportDAO extends Serializable {

    List printByDay(LocalDate datum);
    //List printByDay(LocalDate datum, Ober ober);
    double giveSaleResult(Ober ober);


}

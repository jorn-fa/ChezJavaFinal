package be.leerstad;

import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.util.Comparator;

/**
 *
 * @author Hiel Jorn
 * @version 1.0, jan 2018
 * @since 1.0
 *
 */

public final class Consumption implements Comparable<Consumption>,java.io.Serializable {

    private static Logger logger = Logger.getLogger(Consumption.class.getName());
    private final int beverageId;
    private final String naam;
    public int aantal;
    private double prijs;
    private int orderNummer;
    private int waiterID;


    //gebruikt om orders uit sql te halen
    public Consumption(int beverageId, int aantal, int waiterID){
        this.beverageId = beverageId;
        this.naam = null;
        this.aantal = aantal;
        this.prijs = 0 ;
        this.orderNummer=0;
        this.waiterID=waiterID;
    }

    public Consumption(int beverageId, String naam, double prijs ) {
        this.beverageId = beverageId;
        this.naam = naam;
        this.prijs = prijs ;
        this.orderNummer=0;
        this.waiterID=0;

        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());

    }

    public Consumption(int beverageId, String naam, double prijs, int aantal) {
        this.beverageId = beverageId;
        this.naam = naam;
        this.aantal = aantal;
        this.prijs = prijs ;
        this.orderNummer=0;
        this.waiterID=0;

        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());

    }

    public Consumption(int orderNummer, int beverageID, int aantal, int waiterID)
    {
        this.orderNummer=orderNummer;
        this.beverageId=beverageID;
        this.aantal=aantal;
        this.waiterID=waiterID;
        this.naam=null;
        this.prijs=0d;

        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());

    }


    //region getters + setters
    public int getBeverageId() {
        return beverageId;
    }

    public String getNaam() {
        return naam;
    }

    public double getPrijs() {return prijs;}

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public int getWaiterID() {
        return waiterID;
    }

    public int getAantal() {
        return aantal;
    }

    public double getTotaal(){return aantal * prijs;}


    //endregion


    public void changeAantal(int getal){
        aantal += getal;
    }

    public void AddWaiterID(int waiterID) {
        this.waiterID = waiterID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Consumption that = (Consumption) o;

        if (beverageId != that.beverageId) return false;
        return naam.equals(that.naam);
    }

    @Override
    public int hashCode() {
        int result = beverageId;
        result = 31 * result + naam.hashCode();
        return result;
    }

    @Override
    public int compareTo(Consumption consumption) {
        return Comparator.comparing(Consumption::getNaam)
                .thenComparing(Consumption::getBeverageId)
                .compare(this,consumption);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#0.00");
        if (naam!=null) {
            sb.append("Consumption{")
            .append("naam='" + naam + '\'')
            .append("; prijs= " + df.format(prijs ))
            .append("; aantal=" + aantal + "}");
        }
        else
        {
            sb.append("Ordernumber = " + orderNummer)
            .append(", beverageid = " + beverageId )
            .append(", Aantal = " + aantal)
            .append(", Waiter = " + waiterID)
            .append("\n");
        }
        return sb.toString();
    }
}
package be.leerstad;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
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

public final class Consumption implements Comparable<Consumption> {

    private final int beverageId;
    private final String naam;
    private StringProperty naamProperty;
    private double prijs;
    private int orderNummer;
    private int waiterID;
    public int aantal;
    private IntegerProperty aantalProperty;

    private static Logger logger = Logger.getLogger(Consumption.class.getName());


    //gebruikt om orders uit sql te halen
    public Consumption(int beverageId, int aantal, int waiterID){
        this.beverageId = beverageId;
        this.naam = null;
        this.naamProperty= null;
        this.aantal = aantal;
        this.aantalProperty=new SimpleIntegerProperty(aantal);
        this.prijs = 0 ;
        this.orderNummer=0;
        this.waiterID=waiterID;
    }

    public Consumption(int beverageId, String naam, double prijs ) {
        this.beverageId = beverageId;
        this.naam = naam;
        this.naamProperty=new SimpleStringProperty(naam);
        this.prijs = prijs ;
        this.orderNummer=0;
        this.waiterID=0;

        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());

    }

    public Consumption(int beverageId, String naam, double prijs, int aantal) {
        this.beverageId = beverageId;
        this.naam = naam;
        this.naamProperty=new SimpleStringProperty(naam);
        this.aantal = aantal;
        this.aantalProperty=new SimpleIntegerProperty(aantal);
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
        this.aantalProperty=new SimpleIntegerProperty(aantal);
        this.waiterID=waiterID;
        this.naam=null;
        naamProperty=null;
        this.prijs=0d;

        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());

    }



    public int getBeverageId() {
        return beverageId;
    }


    public String getNaam() {
        return naam;
    }

    @FXML
    public StringProperty getNaamProperty() {
        return naamProperty;
    }

    public double getPrijs() {return prijs;}

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public int getOrderNummer() {
        return orderNummer;
    }
    public void withOrderNummer(int nummer){this.orderNummer=nummer;}

    public int getWaiterID() {
        return waiterID;
    }

    public void AddWaiterID(int waiterID) {
        this.waiterID = waiterID;
    }

    public int getAantal() {
        return aantal;
    }

    @FXML
    public IntegerProperty getAantalProperty(){
        return  aantalProperty;
    }


    public void setAantal(int aantal) {this.aantal = aantal;}

    public void changeAantal(int getal){
        aantal += getal;
    }

    public double getTotaal(){return aantal * prijs;}

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
        char a = df.getDecimalFormatSymbols().getDecimalSeparator();
        if (naam!=null) {
            sb.append("Consumption{");
            sb.append("naam='" + naam + '\'');
            sb.append("; prijs= " + df.format(prijs ));
            sb.append("; aantal=" + aantal + "}");
        }
        else
            {
                sb.append("Ordernumber = " + orderNummer);
                sb.append(", beverageid = " + beverageId );
                sb.append(", Aantal = " + aantal);
                sb.append(", Waiter = " + waiterID);
                sb.append("\n");
            }
        return sb.toString();
    }



}
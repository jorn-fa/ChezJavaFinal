package be.leerstad;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.List;

/**
 * @author Hiel Jorn
 * @version 1.0, jan 2018
 * @since 1.0
 */

public final class Tafel implements Comparable<Tafel>, java.io.Serializable {


    private final String naam;
    private boolean hasOrders = false;
    private Order order=new Order();
    private static Logger logger = Logger.getLogger(Tafel.class.getName());


    public Tafel(String naam) {
        this.naam = naam;
        logger.debug("created : " + this.toString());
        logger.info("created : " + this.toString());
    }


    public String getNaam() {
        return naam;
    }

    public boolean hasOrders() {
        logger.debug("Check if table has items");
        return hasOrders;
    }



    public void addConsumption(Consumption consumption, Ober ober) {
        consumption.AddWaiterID(ober.getID());
        logger.debug("add consumption to table order");
        order.addConsumption(consumption);
        hasOrders=true;
    }

    public boolean hasPaid(){
        hasOrders=false;
        order.clearOrder();
        return true;
    }


    public List<Consumption> getLijstForPayment(){
        return order.getLijstForPayment();
    }

    public ObservableList<Consumption> getFXLijstForPayment(){return  FXCollections.observableArrayList(order.getLijstForPayment());}


    public void afdrukken() {
        List<Consumption> Orders = order.getLijstForPayment();
        for (int i = 0; i < Orders.size(); i++) {

            System.out.println(Orders.get(i).toString());
        }
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tafel tafel = (Tafel) o;
        return naam.equals(tafel.naam);
    }

    @Override
    public int hashCode() {
        return naam.hashCode();
    }

    @Override
    public int compareTo(Tafel tafel) {
        return Comparator.comparing(Tafel::getNaam)
                .compare(this, tafel);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tafel{");
        sb.append(naam.toLowerCase() + "}");
        return sb.toString();
    }


}

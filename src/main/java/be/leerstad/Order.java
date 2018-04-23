package be.leerstad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order implements java.io.Serializable{

    private List<Consumption> lijst = new ArrayList<>();

    public List<Consumption> getLijstForPayment() { return Collections.unmodifiableList(lijst); }


    public void addConsumption(Consumption consumption)
    {
        if (lijst.contains(consumption)){
            //lijst.indexOf(consumption)
            lijst.get(lijst.indexOf(consumption)).changeAantal(consumption.aantal);
        }

        else {
            lijst.add(consumption);

        }

    }

    public double getTotalPrice(){

        return lijst.stream().mapToDouble(i -> i.getTotaal()).sum();
    }

    public boolean clearOrder()
    {
    lijst.clear();
    if (lijst.size()==00){return true;}
    return false;
    }



}



package be.leerstad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Order implements java.io.Serializable{

    private List<Consumption> lijst = new ArrayList<>();

    public List<Consumption> getLijstForPayment() { return Collections.unmodifiableList(lijst); }



    public void addConsumption(Consumption consumption)
    {
        if (lijst.contains(consumption)){
            lijst.get(lijst.indexOf(consumption)).changeAantal(consumption.aantal);
            }

        else {
            lijst.add(consumption);

        }
        if(lijst.get(lijst.indexOf(consumption)).aantal<=0){lijst.remove(consumption);}

    }

    public double getTotalPrice(){

        return lijst.stream().mapToDouble(Consumption::getTotaal).sum();
    }

    public boolean clearOrder()
    {
        lijst.clear();
        return lijst.size() == 00;
    }

    public int getWaiterID(){
        return lijst.get(0).getWaiterID();
    }

    protected int getOrderSize(){
        return lijst.size();
    }

}



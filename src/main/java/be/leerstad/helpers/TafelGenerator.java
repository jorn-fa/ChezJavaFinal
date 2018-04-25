package be.leerstad.helpers;

import be.leerstad.Cafe;
import be.leerstad.Tafel;


public class TafelGenerator {

    Tafel tafel1 = new Tafel("1");
    Tafel tafel2 = new Tafel("2");
    Tafel tafel3 = new Tafel("3");
    Tafel tafel4 = new Tafel("4");
    Tafel tafel5 = new Tafel("5");
    Tafel tafel6 = new Tafel("6");

public Tafel swapTable(int invoer){

    //default tafel = 1
    Tafel temp = tafel1;

    switch (invoer){
        case 1:
            temp = tafel1;
        break;
        case 2:
            temp = tafel2;
            break;
        case 3:
            temp = tafel3;
            break;
        case 4:
            temp = tafel4;
            break;
        case 5:
            temp = tafel5;
            break;
        case 6:
            temp = tafel6;
            break;
    }


    return temp;
}

}

package be.leerstad;


import be.leerstad.Database.BeveragesDAOImpl;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.Database.WaiterDAOImpl;
import be.leerstad.View.RootController;
import be.leerstad.helpers.*;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public final class Cafe extends Application  {

    private static Logger frontlogger = Logger.getLogger("frontend");
    private static Logger dbaseLogger = Logger.getLogger("dbase");
    private static Logger chezJavaLogger = Logger.getLogger("ChezJava");



    private String Naam;
    private boolean ingelogd;
    private static Cafe instance;
    private DeSerializer deSerializer = new DeSerializer();

    //pdf locaties
    public String totalWaiterProp;
    public String totalWaitersProp;
    public String totalSortedProp;
    public String topWaiterPie;


    public Cafe(){this("geen");instance=this;}
    public Cafe(String naam) { this.Naam=naam; chezJavaLogger.info("cafe created with name " + naam); }

    private Ober currentWaiter;

    private Tafel tafel1 = new Tafel("1");
    private Tafel tafel2 = new Tafel("2");
    private Tafel tafel3 = new Tafel("3");
    private Tafel tafel4 = new Tafel("4");
    private Tafel tafel5 = new Tafel("5");
    private Tafel tafel6 = new Tafel("6");

    public Tafel currentTafel = tafel1;

    private Tafel[] tafels = new Tafel[]{tafel1,tafel2,tafel3,tafel4,tafel5,tafel6};

    private List<Consumption> beverageList = new ArrayList<>();


    private Stage primaryStage;

    private AnchorPane rootLayout;
    private Scene scene1;

    //region getters

    public static Cafe getInstance(){return instance;}
    public Ober getOber(){return currentWaiter;}
    public Tafel[] getTafels() {return tafels; }
    public List<Consumption> getBeverageList() {return beverageList;}
    public String getOberNaam(){
        if (currentWaiter != null) {
            return currentWaiter.getVoornaam() + " " + currentWaiter.getNaam();
        }
        return "please log in.";
    }
    public boolean isIngelogd(){return ingelogd;}

    public String getTafelNaam() {

        if (currentWaiter == null) {
            frontlogger.debug("oproepen order window zonder ingelogd te zijn");
            return "Not Logged In";
        }

        frontlogger.debug("Call current tabel name");
        return "Current table: " + currentTafel.getNaam();
    }

    public int getCurrentTafelWaiterId(){
        if(currentTafel.getOberId()==null){return currentWaiter.getID();}
        return currentTafel.getOberId();
    }



    //endregion
    //------------------------------

    //region mainapp start / stop
    //------------------------------
    @Override
    public void start(Stage primaryStage) throws Exception {
        tafels = deSerializer.giveTafel(tafels);

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chez Java");
        this.primaryStage.setResizable(true);
        this.primaryStage.setWidth(1200);
        this.primaryStage.setHeight(700);
        this.primaryStage.setResizable(false);
        initRootLayout(currentWaiter);
    }

    @Override
    public void stop()
    {
        wegSchrijven();
        frontlogger.debug("system shutdown on: " + LocalDateTime.now());
    }
    //endregion
    //------------------------------

    //region javaFX
    public void initRootLayout(Ober ober) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            RootController controller= loader.getController();

            rootLayout =  loader.load();          // Show the scene containing the root layout.

            scene1 = new Scene(rootLayout);

            primaryStage.setScene(scene1);
            primaryStage.show();

            try {
                if (!DbaseConnection.isAlive()){showalert();}
            } catch (Exception e) {
                dbaseLogger.error("Dbase not connected");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showalert() {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Database error");
        alert.setHeaderText("Please check database connection ");
        alert.setContentText("Please connect to database and restart app");

        alert.showAndWait();
        dbaseLogger.debug("DBASE NOT CONNECTED");
        frontlogger.debug("DBASE NOT CONNECTED");

    }

    //endregion
    //------------------------------






    public boolean loadProps(){
        final String propertiesName = "cafe.properties";
        Properties props = new Properties ();

        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream (propertiesName)) {

            props.load (inputStream);

        } catch (Exception e) {
            dbaseLogger.error (e);

        }

        totalWaiterProp = props.getProperty ("totalwaiter");
        totalWaitersProp = props.getProperty ("totalwaiters");
        totalSortedProp = props.getProperty ("totalByWaitersSortedByday");
        topWaiterPie = props.getProperty ("topWaiterPieChart");

        return totalWaitersProp != null && totalWaiterProp != null && totalSortedProp != null && topWaiterPie != null;
    }

    public boolean inloggen(Ober ober)
    {

        Ober vergelijk = new WaiterDAOImpl().compareOber(ober);


        if(vergelijk!=null){
            currentWaiter=vergelijk;
            ingelogd=true;
            fillBeverageList();
            return true;}

        return false;
    }



    public boolean uitloggen()
    {
        if (currentWaiter!=null){
            currentWaiter=null;
            ingelogd=false;
            return true;}
        return false;
    }

    public boolean fillBeverageList()
    {


        if (beverageList.isEmpty()&&currentWaiter!=null)
     {
         BeveragesDAOImpl beveragesDAO = new BeveragesDAOImpl();
         beverageList=beveragesDAO.pricelijst();

         return true;
     }
        if (!beverageList.isEmpty()&&currentWaiter!=null){
            frontlogger.debug("reeds gevuld door andere ober");
            return true;
        }

     return false;
    }

    public void wisselTafel(int getal){
        try {
            currentTafel=tafels[getal-1];
            frontlogger.debug("tafel wisselen naar tafel:" + getal);
        } catch (IndexOutOfBoundsException e) {
            frontlogger.debug("tafelnummer is niet bestaande");
        }
    }


    public void afrekenen()
    {
        if (currentWaiter.getID()==currentTafel.getOberId()) {
            OrdersDAOImpl ordersDAOimpl = new OrdersDAOImpl();
            ordersDAOimpl.writeOrder(currentTafel.getLijstForPayment());
            dbaseLogger.debug("Order write procedure started");
            frontlogger.debug("Payment button pressed");
            currentTafel.hasPaid();
            wegSchrijven();
            deSerializer.wisTafel(currentTafel);
        }
        else
        {
            frontlogger.debug("Wrong waiter on order payment");
        }

    }

    private void wegSchrijven(){
        ObjectToSerialize ob = new ObjectToSerialize();

        for (int teller = 0; teller<tafels.length;teller++)
            if (tafels[teller].hasOrders()){
                ob.Serialize(tafels[teller]);
                frontlogger.debug("On exit - serial table: " + (teller+1));
            }
    }

    public void wegSchrijvenTafel(){
        ObjectToSerialize ob = new ObjectToSerialize();
        try {
            ob.Serialize(currentTafel);
        }
        catch (Exception e) {
            frontlogger.debug("Failure on write file");
        }
    }



    public boolean mailFile(String location,String titel)
    {
        Email email = new Email();
        return email.sendMail(location,titel);
    }


    public static void main(String[] args) {
        launch(args);
    }


}

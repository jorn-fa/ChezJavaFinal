package be.leerstad;


import be.leerstad.Database.BeveragesDAOImpl;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.Database.WaiterDAOImpl;
import be.leerstad.View.RootController;
import be.leerstad.helpers.Clock;
import be.leerstad.helpers.DbaseConnection;
import be.leerstad.helpers.ObjectToSerialize;
import be.leerstad.helpers.PdfFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;


public class Cafe extends Application {

    private static Logger frontlogger = Logger.getLogger("frontend");
    static Logger dbaseLogger = Logger.getLogger("dbase");



    private   String Naam;
    private boolean ingelogd;

    private static Cafe instance;
    public Cafe(){this(null);instance=this;}
    public PdfFactory pdfFactory = new PdfFactory();



    public Cafe(String naam)
    {
        this.Naam=naam;
    }

    private Ober currentWaiter;

    Tafel tafel1 = new Tafel("1");
    Tafel tafel2 = new Tafel("2");
    Tafel tafel3 = new Tafel("3");
    Tafel tafel4 = new Tafel("4");
    Tafel tafel5 = new Tafel("5");
    Tafel tafel6 = new Tafel("6");

    private Tafel[] tafels = new Tafel[]{tafel1,tafel2,tafel3,tafel4,tafel5,tafel6};

    public Tafel currentTafel = tafel1;

    private List<Consumption> beverageList = new ArrayList<>();
    private ObservableList<Consumption> FXbeveragelist =  FXCollections.observableArrayList(beverageList);

    public List<Consumption> getBeverageList() {
        return beverageList;
    }

    private Stage primaryStage;

    private AnchorPane rootLayout;
    private Scene scene1;

    public String totalWaiterProp;
    public String totalWaitersProp;
    public String totalSortedProp;
    public String TopWaiterPie;


    public void wisselTafel(int getal){
        try {
            currentTafel=tafels[getal-1];
            frontlogger.debug("tafel wisselen naar tafel:" + getal);
        } catch (IndexOutOfBoundsException e) {
            frontlogger.debug("tafelnummer is niet bestaande");
        }
    }


    public static Cafe getInstance(){return instance;}


    public Ober getOber(){return currentWaiter;}


    //todo bybye
    public ObservableList<Consumption> getFXbeveragelist() {
        Collections.sort(beverageList);
        return FXbeveragelist;
    }



    public void loadProps(){
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
        TopWaiterPie = props.getProperty ("topWaiterPieChart");

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Chez Java");
        this.primaryStage.setResizable(true);
        this.primaryStage.setWidth(1200);
        this.primaryStage.setHeight(800);
        this.primaryStage.setResizable(false);
        initRootLayout(currentWaiter);
    }

    @Override
    public void stop()
    {
        ObjectToSerialize ob = new ObjectToSerialize();

        for (int teller = 0; teller<tafels.length;teller++)
        if (tafels[teller].hasOrders()&&isIngelogd()){
            ob.Serialize(tafels[teller]);
            frontlogger.debug("On exit - serial table: " + (teller+1));
        }

    }

    public void initRootLayout(Ober ober) {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            RootController controller= loader.getController();

            rootLayout = (AnchorPane) loader.load();          // Show the scene containing the root layout.

            Clock clock;
            clock = new Clock(Color.BLACK.brighter(), Color.TRANSPARENT);
            clock.setLayoutX(1000);
            clock.setLayoutY(700);
            clock.getTransforms().add(new Scale(0.4f, 0.4f, 0, 0));

            rootLayout.getChildren().add(clock);
            scene1 = new Scene(rootLayout);

            primaryStage.setScene(scene1);
            primaryStage.show();


            try {
                if (DbaseConnection.isAlive()==false){showalert();}
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

    public boolean isIngelogd(){return ingelogd;}

    public boolean uitloggen()
    {
        if (currentWaiter!=null){
            currentWaiter=null;
            ingelogd=false;
            return true;}
        return false;
    }



    public String getOberNaam(){
        if (currentWaiter != null) {
            return currentWaiter.getVoornaam() + " " + currentWaiter.getNaam();
        }
        return "please log in.";

    }


    public String getTafelNaam() {

        if (currentWaiter == null) {
            frontlogger.debug("oproepen order window zonder ingelogd te zijn");
            return "Not Logged In";
        }

        frontlogger.debug("Call current tabel name");
        return "Current table: " + currentTafel.getNaam();
    }



    public boolean fillBeverageList()
    {
     if (beverageList.isEmpty()&&currentWaiter!=null)
     {
         BeveragesDAOImpl beveragesDAO = new BeveragesDAOImpl();
         beverageList=beveragesDAO.pricelijst();
         FXbeveragelist = FXCollections.observableList(beverageList);

         return true;
     }
     return false;
    }


    public void addConsumption(Consumption consumption)
    {
        currentTafel.addConsumption(consumption, currentWaiter);
    }


    public void afrekenen()
    {
        OrdersDAOImpl ordersDAOimpl = new OrdersDAOImpl();
        ordersDAOimpl.writeOrder(currentTafel.getLijstForPayment());
        dbaseLogger.debug("Order write procedure started");
        frontlogger.debug("Payment button pressed");
        currentTafel.hasPaid();
    }


    public void getbyday()
    {
        WaiterDAOImpl waiterDAOImpl = new WaiterDAOImpl();
        //waiterDAOImpl.
    }


    public static void main(String[] args) {
        launch(args);
    }




}

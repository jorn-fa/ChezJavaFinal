package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Consumption;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {



    private static Logger log = Logger.getLogger("frontend");
    private Consumption consumption;

    private boolean isIngelogd;



    //ovezicht van bestelling

    private ObservableList<Consumption> besteldList = FXCollections.observableArrayList();
    @FXML
    private TableView<Consumption> besteldData;
    @FXML
    private TableColumn<Consumption, String> besteldName;
    @FXML
    private TableColumn<Consumption, String> besteldQty;


    //overzicht van consumpties
    @FXML
    private TableView<Consumption> consumtionTable;
    @FXML
    private TableColumn<Consumption, String> consumptionNameColumn;
    @FXML
    private TableColumn<Consumption, String> consumptionQtyColumn;




    @FXML
    private Label tafelNaamLabel;

    @FXML
    private TextField qtyField;

    @FXML
    private Button totaalID;


    @FXML
    private Label naamLabel;


    public OrdersController() {}

    private ObservableList<Consumption> getConsumptionData() {
        return FXCollections.observableArrayList(Cafe.getInstance().getBeverageList()).sorted();
    }

    private ObservableList<Consumption> getbesteldData() {
        return FXCollections.observableArrayList(Cafe.getInstance().currentTafel.getLijstForPayment()).sorted();
    }

    private StringProperty convertToStringProperty(String naam){
        return new SimpleStringProperty(naam);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        updateTotaal();

        naamLabel.setText(Cafe.getInstance().getOberNaam());

        consumtionTable.setItems(getConsumptionData());
        consumptionNameColumn.setCellValueFactory(cellData -> convertToStringProperty(cellData.getValue().getNaam()));
        consumptionQtyColumn.setCellValueFactory((cellData -> convertToStringProperty(String.valueOf(cellData.getValue().getPrijs()))));



        //direct inladen bestelde items

        besteldData.setItems(getbesteldData());
        besteldData.setSelectionModel(null); //niet toelaten om te selecteren
        besteldList = getbesteldData();




        besteldName.setCellValueFactory(new PropertyValueFactory<>("naam"));
        besteldQty.setCellValueFactory(new PropertyValueFactory<>("aantal"));

        tafelNaamLabel.setText(Cafe.getInstance().getTafelNaam());
        isIngelogd= Cafe.getInstance().isIngelogd();


        //qty field forceren om enkel getallen te aanvaarden
        qtyField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,7}(\\d{0,4})?")) {
                qtyField.setText(oldValue);
            }
        });

    }



    @FXML
    private void goBack(ActionEvent event) throws IOException{


        Parent parent = FXMLLoader.load(getClass().getResource("/view/RootLayout.fxml"));
        Scene rootScene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Chez-Java.");
        window.setScene(rootScene);

    }




    private boolean verifyQty(){

        //opvangen lege string

        if ( qtyField.getText().isEmpty()){
            log.debug("opgevangen leeg invoerveld");
            qtyField.setText("0");        }

        return Double.valueOf(qtyField.getText()) > 0;
    }


    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection // not logged in");

        alert.setContentText("Please select a consumtion in the table.\n\n");
        if(!isIngelogd){alert.setContentText("Please log in to enable features.\n\n");}
        alert.showAndWait();
    }


    @FXML
    private void addConsumtionAction() throws IOException {



        consumption = consumtionTable.getSelectionModel().getSelectedItem();

        if (consumption==null){

            showAlert();
        }

        else {
            consumption.aantal = Integer.valueOf(qtyField.getText());

            if (isIngelogd) {

                if (verifyQty()) {
                    log.debug("Add: " + consumption.getNaam() + " " + consumption.getAantal());
                    Cafe.getInstance().currentTafel.addConsumption(consumption, Cafe.getInstance().getOber());

                }
            }
        }
        besteldData.setItems(getbesteldData());
        besteldData.refresh();
        updateTotaal();
    }

    @FXML
    private void removeConsumtionAction() throws  NumberFormatException{

        consumption = consumtionTable.getSelectionModel().getSelectedItem();

        if (consumption==null){
            showAlert();
        }
        else {
            consumption.aantal = 0;
            if (isIngelogd && Cafe.getInstance().currentTafel.hasOrders()) {

                if (verifyQty()) {

                    //omzetten van add naar remove -> positief naar negatief getal
                    consumption.aantal -= Integer.valueOf(qtyField.getText()) ;
                    log.debug("Remove: " + consumption.getNaam() + " " + consumption.getAantal());
                    Cafe.getInstance().currentTafel.addConsumption(consumption, Cafe.getInstance().getOber());

                }
            }
        }
        besteldData.setItems(getbesteldData());
        besteldData.refresh();
        updateTotaal();
    }

    @FXML
    private void afrekenAction()throws NumberFormatException
    {
        if (Cafe.getInstance().getOber()!=null) {
            Cafe.getInstance().afrekenen();
            log.debug("afrekenen");
            besteldData.setItems(getbesteldData());
            besteldData.refresh();
            updateTotaal();

        }
        else
        {
            log.debug("Afrekenen zonder ingelogd te zijn");
        }
    }

    private void updateTotaal()
    {
        double totaal = Cafe.getInstance().currentTafel.getTotalPrice();

        //max 2 getallen na komma
        NumberFormat numberformat = NumberFormat.getInstance();
        numberformat.setMaximumFractionDigits(2);

        totaalID.setText("Afrekenen= " + numberformat.format(totaal) + "€");
    }


/*
    @FXML
    private void showConsumptionDetails(Consumption consumption) {

        if (consumption != null) {
            // Fill the labels with info from the person object.

            naamLabel.setText(consumption.getNaam());
            qtyLabel.setText(String.valueOf(consumption.getAantal()));
            consumptionNameColumn.setText(consumption.getNaam());

        } else {
            log.debug("list cleared before reload");
        }

    }*/





}


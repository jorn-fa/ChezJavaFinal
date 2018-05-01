package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Consumption;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
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
    private Label qtyLabel;

    @FXML
    private TextField qtyField;


    @FXML
    private Label NaamLabel;


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


        consumtionTable.setItems(getConsumptionData());
        consumptionNameColumn.setCellValueFactory(cellData -> convertToStringProperty(cellData.getValue().getNaam()));
        consumptionQtyColumn.setCellValueFactory((cellData -> convertToStringProperty(String.valueOf(cellData.getValue().getPrijs()))));

        //direct inladen bestelde items
        //besteldData.setItems(Cafe.getInstance().currentTafel.getFXLijstForPayment().sorted());
        besteldData.setItems(getbesteldData());
        besteldList = getbesteldData();
        //besteldList = Cafe.getInstance().currentTafel.getFXLijstForPayment();



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






    private boolean verifyQty(){

        //opvangen lege string

        if ( qtyField.getText().isEmpty()){
            log.debug("opgevangen leeg invoerveld");
            qtyField.setText("0");        }

        return Double.valueOf(qtyField.getText()) > 0;
    }


    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Consumtion selected");
        alert.setContentText("Please select a person in the table.");

        alert.showAndWait();
    }


    @FXML
    private void addConsumtionAction(ActionEvent event) throws IOException {



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
        //todo dit is niet snel genoeg
        besteldData.setItems(getbesteldData());
        besteldData.refresh();
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
                    System.out.println(qtyField.getText());
                    consumption.aantal -= Integer.valueOf(qtyField.getText()) ;                   
                    log.debug("Remove: " + consumption.getNaam() + " " + consumption.getAantal());
                    Cafe.getInstance().currentTafel.addConsumption(consumption, Cafe.getInstance().getOber());

                }
            }
        }
        besteldData.setItems(getbesteldData());
        besteldData.refresh();
    }

    @FXML
    private void afrekenAction()throws NumberFormatException
    {
        Cafe.getInstance().afrekenen();
        log.debug("afrekenen");
    }



    @FXML
    private void showConsumptionDetails(Consumption consumption) {

        if (consumption != null) {
            // Fill the labels with info from the person object.

            NaamLabel.setText(consumption.getNaam());
            qtyLabel.setText(String.valueOf(consumption.getAantal()));
            consumptionNameColumn.setText(consumption.getNaam());

        } else {
            log.debug("list cleared before reload");
        }

    }





}


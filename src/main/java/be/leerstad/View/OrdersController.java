package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Consumption;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    public OrdersController() {
    }

    private boolean isIngelogd;



    @FXML
    private TableView<Consumption> consumptionData;
    @FXML
    private TableColumn<Consumption, String> consumptionName;
    @FXML
    private TableColumn<Consumption, Integer> consumptionQty;

    private ObservableList<Consumption> consumptionlist = FXCollections.observableArrayList();

    @FXML
    private ListView<Consumption> temp = new ListView<>();

    @FXML
    private Label tafelNaamLabel;

    @FXML
    private Label qtyLabel;

    @FXML
    private TextField qtyField;


    @FXML
    private Label NaamLabel;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //todo aanpassen tafel lijst
        consumptionlist = Cafe.getInstance().getFXbeveragelist();

        consumptionlist = Cafe.getInstance().currentTafel.getFXLijstForPayment();
        temp.getItems().addAll(consumptionlist);


        consumptionData.setItems(Cafe.getInstance().getFXbeveragelist());
        consumptionName.setCellValueFactory(celldata -> celldata.getValue().getNaamProperty());
        consumptionQty.setCellValueFactory(new PropertyValueFactory<Consumption, Integer>("prijs"));

        showConsumptionDetails(null);
        consumptionData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showConsumptionDetails(newValue));

        tafelNaamLabel.setText(Cafe.getInstance().getTafelNaam());
        isIngelogd= Cafe.getInstance().isIngelogd();


        //qty field forceren om enkel getallen te aanvaarden
        qtyField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    qtyField.setText(oldValue);
                }
            }
        });





    }

    private boolean verifyQty(){

        //opvangen lege string
        if ( qtyField.getText().isEmpty()){

            qtyField.setText("0");
        }

        if(Double.valueOf(qtyField.getText())>0){
        return true;}
        return false;
    }


    //todo   hier bezig
    @FXML
    private void addConsumtionAction(ActionEvent event) throws IOException{

        if (isIngelogd){

            if (verifyQty()){
        System.out.println("toevoegen");}
        }
    }

    @FXML
    private void removeConsumtionAction(ActionEvent event) throws  NumberFormatException{
        if(verifyQty()){
        //omzetten naar int
        System.out.println(  (Double.valueOf(qtyField.getText())).intValue());}

        if (isIngelogd){ System.out.println("weghalen");}
    }

    @FXML
    private void afrekenAction(ActionEvent event)throws NumberFormatException
    {
        Cafe.getInstance().afrekenen();

    }



    @FXML
    private void showConsumptionDetails(Consumption consumption) {
        if (consumption != null) {
            // Fill the labels with info from the person object.
            NaamLabel.setText(consumption.getNaam());
            qtyLabel.setText(String.valueOf(consumption.getAantal()));
            consumptionName.setText(consumption.getNaam());

        } else {
            // Person is null, remove all the text.
            //NaamLabel.setText("");
            //qtyLabel.setText("");

        }

    }





}


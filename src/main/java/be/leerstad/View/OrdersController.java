package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Consumption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {

    public OrdersController() {    }

    private static Logger log = Logger.getLogger("frontend");
    private Consumption consumption;

    private boolean isIngelogd;

    //overzicht van consumpties

    private ObservableList<Consumption> consumptionlist = FXCollections.observableArrayList();

    @FXML
    private TableView<Consumption> consumptionData;
    @FXML
    private TableColumn<Consumption, String> consumptionName;
    @FXML
    private TableColumn<Consumption, Integer> consumptionQty;

    //ovezicht van bestelling

    private ObservableList<Consumption> besteldList = FXCollections.observableArrayList();
    @FXML
    private TableView<Consumption> besteldData;
    @FXML
    private TableColumn<Consumption, String> besteldName;
    @FXML
    private TableColumn<Consumption, Integer> besteldQty;



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
        consumptionData.setItems(Cafe.getInstance().getFXbeveragelist());
        consumptionlist = Cafe.getInstance().getFXbeveragelist();

        besteldList = Cafe.getInstance().currentTafel.getFXLijstForPayment();


        //clear data
        showConsumptionDetails(null);

        consumptionName.setCellValueFactory(celldata -> celldata.getValue().getNaamProperty());
        consumptionQty.setCellValueFactory(new PropertyValueFactory<>("prijs"));

        besteldName.setCellValueFactory(celldata -> celldata.getValue().getNaamProperty());
        besteldQty.setCellValueFactory(new PropertyValueFactory<>("aantal"));



        consumptionData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.toString()));
        besteldData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.toString()));


        consumption = consumptionData.getSelectionModel().getSelectedItem();


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



    @FXML
    private void addConsumtionAction(ActionEvent event) throws IOException {

        //todo aanpassen naar geselecteerde consumptie
        consumption = new Consumption(1, "cola", 2.4d);

        consumption.aantal=Integer.valueOf(qtyField.getText());

        if (isIngelogd) {

            if (verifyQty()) {
                log.debug("Add: " + consumption.getNaam() + " " + consumption.getAantal());
                Cafe.getInstance().currentTafel.addConsumption(consumption, Cafe.getInstance().getOber());
            }
        }
    }

    @FXML
    private void removeConsumtionAction() throws  NumberFormatException{


        //todo beveiliging inbouwen tegen niets geselecteerd
        consumption = new Consumption(1, "cola", 2.4d);
        consumption.aantal=0;
        if (isIngelogd&&Cafe.getInstance().currentTafel.hasOrders()){

            if (verifyQty()) {

                //omzetten van add naar remove -> positief naar negatief getal
                System.out.println(qtyField.getText());
                consumption.aantal-=Integer.valueOf(qtyField.getText())*2;

                log.debug("Remove: " + consumption.getNaam() + " " + consumption.getAantal());
                Cafe.getInstance().currentTafel.addConsumption(consumption, Cafe.getInstance().getOber());
            }
        }
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
            consumptionName.setText(consumption.getNaam());

        } else {
            log.debug("list cleared before reload");
        }

    }





}


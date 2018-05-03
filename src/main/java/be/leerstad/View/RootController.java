package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Tafel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable{


    private boolean isIngelogd(){
        return Cafe.getInstance().isIngelogd();
    }
    private static Logger log = Logger.getLogger("frontend");


    public RootController(){}
    private Stage dialogStage;



    @FXML
    private Label fullNameField;

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    @FXML
    private Circle circle4;

    @FXML
    private Circle circle5;

    @FXML
    private Circle circle6;

    @FXML
    private Pane pane;
    private AnchorPane homePane;


    private void verifyColor(Circle circel){

        circel.setVisible(false);
        int aantalTafels=Cafe.getInstance().getTafels().length;
        int circleId = 0;


        try {
            circleId = Integer.valueOf(circel.getId().substring(6));
        } catch (NumberFormatException e) {
            log.debug("Something went wrong with fx:id on circle");
        }


        Tafel[] tafels = Cafe.getInstance().getTafels();

        for(int teller = 0;teller<aantalTafels;teller++) {
            if(tafels[teller].hasOrders()&&teller==circleId-1) {
                circel.setVisible(true);
                circel.setFill(Color.RED);
                try {
                if (tafels[teller].getOberId() == Cafe.getInstance().getOber().getID()) {
                    circel.setFill(Color.GREEN);}
                }
                catch (NullPointerException e) {
                    log.debug("inlezen zonder ingelogd ");
                }
                }
            }

    }

/*
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }*/

    @FXML
    public void initialize(URL location, ResourceBundle resouces) {
        fullNameField.setText(Cafe.getInstance().getOberNaam());

        verifyColor(circle1);
        verifyColor(circle2);
        verifyColor(circle3);
        verifyColor(circle4);
        verifyColor(circle5);
        verifyColor(circle6);
    }

    public void setNode(Node node)
    {
        pane.getChildren().clear();
        pane.getChildren().add((Node)node);
    }




    @FXML
    public void LogInAction(ActionEvent event) throws IOException
        {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/LogIn.fxml"));
            if (isIngelogd()){parent = FXMLLoader.load(getClass().getResource("/view/LogOff.fxml"));}

            Scene LoginScene = new Scene(parent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(LoginScene);
            window.show();
            log.debug("User login screen");
        }



    @FXML
    public void RapportAction(ActionEvent event) throws IOException
    {
        if(isIngelogd()) {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/Rapport.fxml"));
            Scene rapportScene = new Scene(parent);
            //Stage window = new Stage();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(rapportScene);
            window.setTitle("Chez-Java : Rapporten");
            window.show();
            log.debug("Rapport Screen");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("User not logged in");
            //alert.setHeaderText("Please log in");
            alert.setContentText("Please log in before access is granted to the report section.\n\n");

            alert.showAndWait();
            log.debug("Not logged in popup");


        }
    }


    @FXML
    public void ShowTafel(ActionEvent event) throws IOException
    {
        Button btn = (Button) event.getSource();
        int tafelNummer=0;

        try {
            tafelNummer = Integer.valueOf(btn.getId().substring(5));
        } catch (NumberFormatException e) {
            log.debug("Something went wrong with fx:id on button");
        }

        Cafe.getInstance().wisselTafel(tafelNummer);
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Orders.fxml"));
        Scene orderScene = new Scene(parent);
        //Stage window = new Stage();
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();


        window.setScene(orderScene);
        window.setTitle("chez-java : Orders");
        window.show();
        log.debug("order Screen");


    }

}





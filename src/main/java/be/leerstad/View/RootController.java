package be.leerstad.View;

import be.leerstad.Cafe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private Label firstNameField;

    @FXML
    private Label lastNameField;

    @FXML
    private Label fullNameField;


    @FXML
    private Pane pane;
    private AnchorPane homePane;


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void initialize(URL location, ResourceBundle resouces) {
        fullNameField.setText(Cafe.getInstance().getOberNaam());


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
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Rapport.fxml"));
        Scene rapportScene = new Scene(parent);
        Stage window = new Stage();
        window.setScene(rapportScene);
        window.setTitle("Rapporten");
        window.show();
        log.debug("Rapport Screen");
    }


    @FXML
    public void ShowTafel(ActionEvent event) throws IOException
    {
        Cafe.getInstance().wisselTafel(3);
        Parent parent = FXMLLoader.load(getClass().getResource("/view/Orders.fxml"));
        Scene orderScene = new Scene(parent);
        Stage window = new Stage();
        window.setScene(orderScene);
        window.setTitle("Orders");
        window.show();
        log.debug("order Screen");


    }

}





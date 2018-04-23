package be.leerstad.View;

import be.leerstad.Cafe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LogOffController {

    @FXML
    private void goBack(ActionEvent event) throws IOException {


        Parent parent = FXMLLoader.load(getClass().getResource("/view/RootLayout.fxml"));
        Scene rootScene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(rootScene);
        window.show();
    }

    @FXML
    private void logOFf(ActionEvent event) throws IOException {
        Cafe.getInstance().uitloggen();
        goBack(event);
    }



}

package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Ober;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private PasswordField passwordField;

    private Stage dialogStage;

    public LoginController(){}

    @FXML
    private void initialize() {  }



    @FXML
    private void handleCancel(ActionEvent event) throws IOException {
        goBack(event);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException{

        Parent parent = FXMLLoader.load(getClass().getResource("/view/RootLayout.fxml"));
        Scene rootScene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(rootScene);

    }

    @FXML
    private void logIn(ActionEvent event) throws IOException {
        if (isInputValid()) {
            String first = firstNameField.getText();
            String name = lastNameField.getText();
            String pwd = passwordField.getText();

            Ober ober=new Ober(0, name, first, pwd);
            Cafe.getInstance().inloggen(ober);
            goBack(event);
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "No valid password!\n";
        }

        if (errorMessage.length() == 0)

        {
            return true;
        } else

        {
            // Show the error message.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

}

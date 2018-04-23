package be.leerstad;/*
package be.leerstad;

import be.leerstad.View.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;



import java.io.IOException;

public class UItest extends Application{

    private Stage primaryStage;
    private BorderPane rootLayout;

    public UItest(){}

    Ober ober;



    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Cafe");
        initRootLayout();

        showLogIn(ober);
    }


    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();          // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public boolean showLogIn(Ober ober) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(UItest.class.getResource("/view/LogIn.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            //dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            LoginController controller = loader.getController();
            controller.setDialogStage(dialogStage);


            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
*/
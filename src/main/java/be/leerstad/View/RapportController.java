package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.helpers.PdfFactory;
import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RapportController implements Initializable {

    private static Logger log = Logger.getLogger("frontend");

    private boolean isIngelogd;
    private Stage dialogStage;

    @FXML
    private Label fullNameField;

    @FXML
    private DatePicker datePicker;


    @FXML
    public void initialize(URL location, ResourceBundle resouces) {
        fullNameField.setText(Cafe.getInstance().getOberNaam());
        isIngelogd = Cafe.getInstance().isIngelogd();
        Date today = Date.valueOf(LocalDate.now());
        Cafe.getInstance().loadProps();

        if (!isIngelogd) {showalert();}
    }


    @FXML
    private void goBack(ActionEvent event) throws IOException {
                    Parent parent = FXMLLoader.load(getClass().getResource("/view/RootLayout.fxml"));
            Scene rootScene = new Scene(parent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Chez-Java");
            window.setScene(rootScene);



    }

    @FXML
    private void getByDay() throws IOException, DocumentException {
        LocalDate localDate = datePicker.getValue();
        if (localDate==null){
            localDate = LocalDate.now();log.debug("Forgot to enter date on selection");
        }

        try {
            PdfFactory.GetPDFbyType("totalbywaiterssortedbyday", Cafe.getInstance().totalSortedProp, localDate);
        } catch (IOException e) {
           log.debug("File in use by other process");
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        showPdf(Cafe.getInstance().totalSortedProp);

    }

    @FXML
    private void totalByWaiter() throws IOException, DocumentException {
        PdfFactory.GetPDFbyType("totalwaiter", Cafe.getInstance().totalWaiterProp, Cafe.getInstance().getOber());
        showPdf(Cafe.getInstance().totalWaiterProp);

    }

    @FXML
    private void top3() throws IOException, DocumentException {
        PdfFactory.GetPDFbyType("topWaiterPieChart", Cafe.getInstance().TopWaiterPie);
        showPdf(Cafe.getInstance().TopWaiterPie);
    }

    @FXML
    private void totalSalesForAllWaiters() throws IOException, DocumentException {
        PdfFactory.GetPDFbyType("totalwaiters", Cafe.getInstance().totalWaitersProp);
        showPdf(Cafe.getInstance().totalWaitersProp);
    }


    @FXML
    private void showalert() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("User login");
        alert.setHeaderText("Please log in");

        alert.showAndWait();
        log.debug("Not logged in popup");


    }

    @FXML
    private void mailRapport(ActionEvent event) throws IOException , DocumentException {
        Button btn = (Button) event.getSource();
        String type = "";

        try {
            type = btn.getId().substring(4);
        } catch (NumberFormatException e) {
            log.debug("Something went wrong with fx:id on button");
        }


        switch (type.toLowerCase()){
            case "total":{
                totalByWaiter();
                mailer(Cafe.getInstance().totalWaiterProp,"Total by waiter");
                break;}

            case "top3":{
                top3();
                mailer(Cafe.getInstance().TopWaiterPie,"Top 3 Waiters");
                break;}

                case "totalwaiter":{
                    totalSalesForAllWaiters();
                    mailer(Cafe.getInstance().totalWaitersProp,"Total Sales by Waiters");
                break;}

                case "onday":{
                    getByDay();
                    mailer(Cafe.getInstance().totalSortedProp,"Total waiter by day");
                    break;}

                case "":{log.debug("empty Fx:id in buttons rapport");
                    break;}

                default:{log.debug("Something went wrong with fix:id on buttons mail rapport");
                break;}

        }

    }

    private void showPdf(String file){
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(file);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                log.debug("no application registered for PDFs");
            }

        }
    }


    private void mailer(String property,String titel) {
        if (property != null && titel != null) {
            Cafe.getInstance().mailFile(property, titel);
        } else {
            log.debug("Something went wrong with mail command");
        }
    }
}

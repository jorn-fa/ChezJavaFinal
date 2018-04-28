package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.helpers.PdfFactory;
import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
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
    private Date today ;

    @FXML
    private DatePicker datePicker;






    @FXML
    public void initialize(URL location, ResourceBundle resouces) {
        fullNameField.setText(Cafe.getInstance().getOberNaam());
        isIngelogd= Cafe.getInstance().isIngelogd();
        today= Date.valueOf(LocalDate.now());
        Cafe.getInstance().loadProps();

        if(isIngelogd==false){showalert();}
    }


    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.close();
    }

    @FXML
    private void getByDay() throws IOException, DocumentException {
        LocalDate localDate = datePicker.getValue();
        PdfFactory.GetPDFbyType("totalbywaiterssortedbyday", Cafe.getInstance().totalSortedProp,localDate);
        System.out.println("ja");

    }

    @FXML
    private void totalByWaiter() throws IOException, DocumentException {
        PdfFactory.GetPDFbyType("totalwaiter", Cafe.getInstance().totalWaiterProp, Cafe.getInstance().getOber());
    }

    @FXML
    private void top3() throws IOException, DocumentException {
        PdfFactory.GetPDFbyType("topWaiterPieChart", Cafe.getInstance().TopWaiterPie);
    }

    @FXML
    private void totalSalesForAllWaiters() throws IOException, DocumentException {
        PdfFactory.GetPDFbyType("totalwaiters", Cafe.getInstance().totalWaitersProp);
    }


    @FXML
    private void showalert(){

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle("User login");
        alert.setHeaderText("Please log in");

        alert.showAndWait();
        log.debug("Not logged in popup");

    }



}

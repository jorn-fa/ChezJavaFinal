package be.leerstad.View;

import be.leerstad.Cafe;
import be.leerstad.Database.OrdersDAOImpl;
import be.leerstad.helpers.PdfFactory;
import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

public class RapportController implements Initializable {

    private static Logger log = Logger.getLogger("frontend");

    private boolean isIngelogd;
    private Stage dialogStage;

    private Set<Date> orderDatums;

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
        orderDatums =getOrderDatums();

        if (!isIngelogd) {showalert();}

        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.setShowWeekNumbers(true);
        datePicker.setValue(LocalDate.now());


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
        catch (IllegalArgumentException e)
        {
            log.debug("Wrong type request in getByDay");
        }
        showPdf(Cafe.getInstance().totalSortedProp);

    }

    @FXML
    private void totalByWaiter() throws IOException, DocumentException,IllegalArgumentException {
        PdfFactory.GetPDFbyType("totalwaiter", Cafe.getInstance().totalWaiterProp, Cafe.getInstance().getOber());
        showPdf(Cafe.getInstance().totalWaiterProp);

    }

    @FXML
    private void top3() throws IOException, DocumentException,IllegalArgumentException {
        PdfFactory.GetPDFbyType("topWaiterPieChart", Cafe.getInstance().topWaiterPie);
        showPdf(Cafe.getInstance().topWaiterPie);
    }

    @FXML
    private void totalSalesForAllWaiters() throws IOException, DocumentException,IllegalArgumentException {
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

    private void waitForFile(int miliseconds)
    {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mailRapport(ActionEvent event) throws IOException , DocumentException {
        Button btn = (Button) event.getSource();
        String type = "";
        String file ="";
        String titel ="";

        try {
            type = btn.getId().substring(4);
        } catch (NumberFormatException e) {
            log.debug("Something went wrong with fx:id on button");
        }


        switch (type.toLowerCase()){
            case "total":{
                totalByWaiter();
                waitForFile(1000);
                file=Cafe.getInstance().totalWaiterProp;
                titel= "Total by waiter";
                break;}

            case "top3":{
                top3();
                waitForFile(1000);
                file=Cafe.getInstance().topWaiterPie;
                titel="Top 3 Waiters";
                break;}

                case "totalwaiter":{
                    totalSalesForAllWaiters();
                    waitForFile(1000);
                    file=Cafe.getInstance().totalWaitersProp;
                    titel="Total Sales by Waiters";
                break;}

                case "onday":{
                    getByDay();
                    waitForFile(1000);
                    file=Cafe.getInstance().totalSortedProp;
                    titel="Total waiter by day";
                    break;}

                case "":{log.debug("empty Fx:id in buttons rapport");
                    break;}

                default:{log.debug("Something went wrong with fix:id on buttons mail rapport");
                break;}

        }

        if(file!=""&&titel!=""){
        mailer(file,titel);}

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

    // Create a day cell factory --> S.

    Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override

                public void updateItem(LocalDate item, boolean empty)

                { // Must call super
                    super.updateItem(item, empty);
                    // weekends

                    DayOfWeek day = DayOfWeek.from(item);

                    if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                        this.setTextFill(Color.RED);
                    }
                    //toekomst is niet selecteerbaar + niet zichtbaar
                    if (item.isAfter(LocalDate.now())) {
                        setDisable(true);
                    }
                    this.setVisible(false);

                    for(Date date : orderDatums){
                        if (item.equals(date.toLocalDate())){setVisible(true);}                    }
                    }
                };
                }
            };



    private Set<Date>getOrderDatums()
    {
        OrdersDAOImpl ordersDAO = new OrdersDAOImpl();
        return ordersDAO.dateList();

    }



}

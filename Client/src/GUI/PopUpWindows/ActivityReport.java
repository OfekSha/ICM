package GUI.PopUpWindows;

import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import reporting.ReportController;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static Entity.clientRequestFromServer.requestOptions.createNewActivitiesReport;
import static GUI.UserForm.activitiesReport;
import static reporting.ReportController.reportScope.*;

public class ActivityReport extends AbstractPopUp {

    public Text txtReport;
    public TextArea taReport;
    public ComboBox<String> cbScope;
    public DatePicker dpEndDate;
    public DatePicker dpStartDate;
    public Button btnGetReport;

    private LocalDate startDate, endDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> Scopes = FXCollections
                .observableArrayList("Months", "Day Of Week", "Day Of Month");
        cbScope.setItems(Scopes); //
        btnGetReport.setOnMouseClicked(event -> {
            ReportController.reportScope scope;
            switch (cbScope.getSelectionModel().getSelectedItem()) {
                case "Day Of Week": scope = dayOfweek;
                    break;
                case "Day Of Month": scope = dayOfmonth;
                    break;
                default: scope = months;
            }
            getReports(scope,dpStartDate.getValue(), dpEndDate.getValue());

            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            taReport.setText(activitiesReport.toString());
        });

    }
    public void getReports( ReportController.reportScope scope,LocalDate start,LocalDate end) {
    	clientRequestFromServer newRequest = new clientRequestFromServer(createNewActivitiesReport,
                new Object[] {start,end, scope});
        ClientLauncher.client.handleMessageFromClientUI(newRequest);
    }
    
    
    //
    //
}

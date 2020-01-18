package GUI.PopUpWindows;

import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import reporting.ReportController;

import java.net.URL;
import java.util.ResourceBundle;

import static Entity.clientRequestFromServer.requestOptions.getDelayReport;
import static GUI.UserForm.delayReport;
import static reporting.ReportController.reportScope.*;

public class DelayReport extends AbstractPopUp {

    public TextArea taReport;
    public DatePicker dpStartDate;
    public DatePicker dpEndDate;
    public ComboBox<String> cbScope;
    public Button btnGetReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> Scopes = FXCollections
                .observableArrayList("Months", "Day Of Week", "Day Of Month");
        cbScope.setItems(Scopes); //
        btnGetReport.setOnMouseClicked(event -> {
            ReportController.reportScope scope;
            switch (cbScope.getSelectionModel().getSelectedItem()) {
                case "Day Of Week":
                    scope = dayOfweek;
                    break;
                case "Day Of Month":
                    scope = dayOfmonth;
                    break;
                default:
                    scope = months;
            }
            clientRequestFromServer newRequest = new clientRequestFromServer(getDelayReport, scope);
            ClientLauncher.client.handleMessageFromClientUI(newRequest);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taReport.setText(delayReport.toString());
        });

    }
}

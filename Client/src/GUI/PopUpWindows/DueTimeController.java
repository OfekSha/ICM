package GUI.PopUpWindows;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;

public class DueTimeController extends AbstractPopUp {

    private static ChangeRequest changeRequest;

    @FXML
    private DatePicker dpDueTime;
    public Button btnDone;

    @FXML
    private void getDone(ActionEvent event) {
        LocalDate dataDue = dpDueTime.getValue();
        if (dataDue != null) {
            if (dataDue.isAfter(LocalDate.now())) {
                changeRequest.getProcessStage().addDueDate(dataDue);
                clientRequestFromServer newRequest =
                        new clientRequestFromServer(updateProcessStage, changeRequest);
                ClientLauncher.client.handleMessageFromClientUI(newRequest);
                getCancel(event);
            }
        }
    }

    public static void setChangeRequest(ChangeRequest changeRequest) {
        DueTimeController.changeRequest = changeRequest;
    }
}

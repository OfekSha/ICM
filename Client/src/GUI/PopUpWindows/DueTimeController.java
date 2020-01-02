package GUI.PopUpWindows;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

import static Entity.clientRequestFromServer.requestOptions.getAll;
import static Entity.clientRequestFromServer.requestOptions.updateChangeRequest;

public class DueTimeController extends AbstractPopUp {

    private static ChangeRequest changeRequest;

    @FXML
    private DatePicker dpDueTime;

    @FXML
    public Button btnDone;

    @FXML
    private void getDone(ActionEvent event) {
        LocalDate dataDue = dpDueTime.getValue();

        changeRequest.getProcessStage().addDueDate(dataDue);

        clientRequestFromServer newRequest =
                new clientRequestFromServer(updateChangeRequest, changeRequest);

        ClientLauncher.client.handleMessageFromClientUI(newRequest);

        newRequest = new clientRequestFromServer(getAll);

        ClientLauncher.client.handleMessageFromClientUI(newRequest);
    }

    public static void setChangeRequest(ChangeRequest changeRequest) {
        DueTimeController.changeRequest = changeRequest;
    }
}

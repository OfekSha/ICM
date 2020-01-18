package GUI.PopUpWindows;

import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

import static Entity.clientRequestFromServer.requestOptions.getDelayReport;
import static GUI.UserForm.delayReport;

public class DelayReport extends AbstractPopUp {

    public TextArea taReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientRequestFromServer newRequest = new clientRequestFromServer(getDelayReport);
        ClientLauncher.client.handleMessageFromClientUI(newRequest);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        taReport.setText(delayReport.toString());
    }
}

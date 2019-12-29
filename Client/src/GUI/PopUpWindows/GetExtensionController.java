package GUI.PopUpWindows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class GetExtensionController extends AbstractPopUp {

    @FXML
    private TextArea taExplanation;

    @FXML
    private Button btnSendRequest;

    @FXML
    void SendExtensionRequest(ActionEvent event) {
        String explanation = taExplanation.getText();
    }
}

package GUI.PopUpWindows;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import static GUI.ExecutionLeaderForm.changeRequest;
import static GUI.ExecutionLeaderForm.sendUpdateForRequest;

public class GetExtensionController extends AbstractPopUp {

    @FXML
    private TextArea taExplanation;

    @FXML
    private Button btnSendRequest;

    @FXML
    void sendExtensionRequest() {
        String explanation = taExplanation.getText();
        if (!explanation.isEmpty()) {
            changeRequest.getProcessStage().inputExtensionExplanation(explanation);
            sendUpdateForRequest();
            getCancel();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Explanation is empty");
            alert.setContentText("Please explain why you need an extension time");
            alert.showAndWait();
        }
    }
}

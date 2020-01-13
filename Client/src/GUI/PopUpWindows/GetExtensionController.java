package GUI.PopUpWindows;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import static GUI.ExecutionLeaderForm.sendUpdateForRequest;
import static GUI.PopUpWindows.DueTimeController.processStage;

public class GetExtensionController extends AbstractPopUp {

    @FXML
    private TextArea taExplanation;

    @FXML
    private Button btnSendRequest;

    public static boolean Approve = false;

    @FXML
    void sendExtensionRequest() {
        String explanation = taExplanation.getText();
        if (explanation.isEmpty()) {
            Approve = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Explanation is empty");
            alert.setContentText("Please explain why you need an extension time");
            alert.showAndWait();
        }
        else {
            Approve = true;
            processStage.setExtensionExplanation(explanation);
            sendUpdateForRequest();
            getCancel();
        }
    }
}

package GUI.PopUpWindows;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;


public class GetExtensionController extends AbstractPopUp {

    @FXML
    private TextArea taExplanation;
	@FXML
	private DatePicker dpDueTime;
    @FXML
    private Button btnSendRequest;

    public static boolean Approve = false;

    @FXML
    void sendExtensionRequest() {
        String explanation = taExplanation.getText();
        LocalDate dueDate=dpDueTime.getValue();
        if (explanation.isEmpty()|| dueDate==null ) {
            Approve = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Explanation is empty");
            alert.setContentText("Please explain why you need an extension time");
            alert.showAndWait();
        }
        else {
            Approve = true;
            processStage.setExtensionExplanation(explanation);
            processStage.setFlagExtensionRequested();
            processStage.setExtensionRequestDate(dueDate);
            sendUpdateForRequest();
            getCancel();
        }
    }
}

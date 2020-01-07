package GUI.PopUpWindows;

import Controllers.InspectorController;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ApproveDueTimeController extends AbstractPopUp implements IcmForm {

    @FXML
    private Button btnApprove;
    @FXML
    private Button btnDisapprove;
    @FXML
    private Label title;
    @FXML
    void getApprove(ActionEvent event) {
    	InspectorController.approveDueTime(true, InspectorController.selectedReqFromTable);
    	((Stage) btnApprove.getScene().getWindow()).close();
    }

    @FXML
    void getDisapprove(ActionEvent event) {
    	InspectorController.approveDueTime(false, InspectorController.selectedReqFromTable);
    	((Stage) btnApprove.getScene().getWindow()).close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setText("Approve Due Time ("+InspectorController.selectedReqFromTable.getDueTime()+") for request" +InspectorController.selectedReqFromTable.getId());
		
	}

	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		
	}
}

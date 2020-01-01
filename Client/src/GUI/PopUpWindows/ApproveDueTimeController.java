package GUI.PopUpWindows;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.InspectorController;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ApproveDueTimeController extends AbstractPopUp implements IcmForm {

    @FXML
    private Button btnApprove;
    @FXML
    private Button btnDisapprove;
    @FXML
    private Label title;
    @FXML
    void getApprove(ActionEvent event) {
    	InspectorController.approveDueTime(true, InspectorController.selctedReqFromTable);
    	((Stage) btnApprove.getScene().getWindow()).close();
    }

    @FXML
    void getDisapprove(ActionEvent event) {
    	InspectorController.approveDueTime(false, InspectorController.selctedReqFromTable);
    	((Stage) btnApprove.getScene().getWindow()).close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setText("Approve Due Time ("+InspectorController.selctedReqFromTable.getDueTime()+") for request" +InspectorController.selctedReqFromTable.getId());
		
	}

	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		
	}
}

package GUI.PopUpWindows;

import Controllers.InspectorController;
import Controllers.StageSupervisorController;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.TextField;
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
    private TextArea description;
    public static StageSupervisorController controller;
    @FXML
    void getApprove(ActionEvent event) {
    	InspectorController.approveDueTime(true, InspectorController.selectedRequest,description.getText());
    	((Stage) btnApprove.getScene().getWindow()).close();
    }

    @FXML
    void getDisapprove(ActionEvent event) {
    	InspectorController.approveDueTime(false, InspectorController.selectedRequest,description.getText());
    	((Stage) btnApprove.getScene().getWindow()).close();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setText("Approve Due Time ("+InspectorController.selectedRequest.getProcessStage().getDueDate()+") for request" +InspectorController.selectedRequest.getRequestID());
		
	}

	@Override
	public void getFromServer(Object message) {
		controller.messageFromServer(message);
		
	}
}

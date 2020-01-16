package GUI.PopUpWindows;

import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Controllers.InspectorController;
import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.User;
import Entity.clientRequestFromServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class InspectorChangeStatusForm extends AbstractPopUp {
	@FXML
	private Label title;

	@FXML
	private TextArea description;

	@FXML
	private Button btnApprove;
	public enum Status {
		freeze,unfreeze,close
	}
	public static Status status;
	public static StageSupervisorController controller;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		title.setText("Approve Change Status");
		ChangeRequest req=InspectorController.selectedRequest;
		switch (status) {
		case close:
			title.setText("Close request ID "+req.getRequestID());
			String str=sendMail(req.getInitiator().getTheInitiator(),req.getRequestID(),(req.getProcessStage().getDates()[3][0]));
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText("email and sms was sent to the user");
			alert.setContentText("The message is: " +str);
			alert.showAndWait();
			break;
		case freeze:
			title.setText("Freeze request ID "+req.getRequestID());
			break;
		case unfreeze:
			title.setText("Unfreeze request ID "+req.getRequestID());
			break;
		}
	}

	@FXML
	void getApprove(ActionEvent event) {
		switch (status) {
		case close:
			InspectorController.closeRequest(InspectorController.selectedRequest);
			break;
		case freeze:
			InspectorController.freeze(InspectorController.selectedRequest);
			break;
		case unfreeze:
			InspectorController.unfreeze(InspectorController.selectedRequest);
			break;
		}
		getCancel();
	}
	@Override
    public void getFromServer(Object message) {
    	// TODO: need to get message from server that client update succeed. 
    	if (((clientRequestFromServer) message).getRequest()==updateProcessStage ) {
    		controller.messageFromServer(message);
    		getCancel();
    	}
    	else {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Error");
    		alert.setHeaderText(null);
    		alert.setContentText("The change didn't work please try again.");
    		alert.showAndWait();
    	}
    }
	private static String sendMail(User user,int id,LocalDate stage4StartDate) {
		String closeStr= "request ID "+ id+ " closed";
		 String description;
		if (stage4StartDate==null)description=" The request was rejected";
		else description=" The request handled successfully.";
		return closeStr+description;
		
	}
}

package GUI.PopUpWindows;

import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.InspectorController;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		title.setText("Approve Change Status");
		switch (status) {
		case close:
			title.setText("Close request ID "+InspectorController.selectedRequest.getRequestID());
			break;
		case freeze:
			title.setText("Freeze request ID "+InspectorController.selectedRequest.getRequestID());
			break;
		case unfreeze:
			title.setText("Unfreeze request ID "+InspectorController.selectedRequest.getRequestID());
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
}

package GUI;

import Entity.Requirement;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ExecutionLeaderForm extends EstimatorExecutorForm {
	@FXML
    public ComboBox<String> cmbRequests;
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taInitiatorRequest;
	public TextArea taExaminersReport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		setRequestsComboBox();
	}

	@Override
	public void getFromServer(Object message) {
		System.out.print("\nMessage from osf.server Received:\nLoad list of requests: ");
		clientRequestFromServer request = (clientRequestFromServer) message;
		ReqListForClient = (ArrayList<Requirement>)request.getObject();
		ReqListForClient.forEach(e -> System.out.print("[" + e.getID() + "] "));
	}

	private void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		ReqListForClient.forEach(req -> al.add(Integer.toString((req.getID()))));
		cmbRequests.setItems(FXCollections.observableArrayList(al));
	}

	public void RequestsComboBoxUsed() {
		int s = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		for (Requirement req : ReqListForClient) {
			if (s == req.getID()) {
				this.taInitiatorRequest.setPromptText(req.getRequestDetails());
				//TODO Implement this in Request Entity
				//this.taRequestReason.setText(req.getRequestReason());
				//this.taComment.setText(req.getRequestComment());
				break;
			}
		}
	}

	//TODO: the following  methods are from the class diagram:
	public void getExecutionApproved() {}
	public void getReport() {}

	public void openDueTime(ActionEvent actionEvent) {
	}

	public void openApproveExecution(ActionEvent actionEvent) {
	}

	public void opentExtension(ActionEvent actionEvent) {
	}
}

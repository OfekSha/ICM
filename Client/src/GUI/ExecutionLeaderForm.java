package GUI;

import Entity.Requirement;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ExecutionLeaderForm extends EstimatorExecutorForm {

	@FXML
	public TextArea taInitiatorRequest;
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		setRequestsComboBox();
	}

	public void RequestsComboBoxUsed() {
		int s = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		for (Requirement req : ReqListForClient) {
			if (s == req.getID()) {
				this.taInitiatorRequest.setText(req.getRequestDetails());
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

	public void openExtension(ActionEvent actionEvent) {
	}
}

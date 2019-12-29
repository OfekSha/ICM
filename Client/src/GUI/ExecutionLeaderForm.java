package GUI;

import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ExecutionLeaderForm extends EstimatorExecutorForm {

	@FXML
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;
	public TextArea taInitiatorRequest;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		setRequestsComboBox();
	}

	public void RequestsComboBoxUsed() {
		String selected = cmbRequests.getSelectionModel().getSelectedItem();
		changeRequests.forEach(cR -> {
			if (selected.equals(cR.getRequestID())) {
				this.taInitiatorRequest.setText(cR.getProblemDescription());
				this.taExaminerReport.setText(cR.getComment());
			}
		});
	}

	//TODO: the following  methods are from the class diagram:
	public void getExecutionApproved() {}
	public void getReport() {}

	public void openDueTime(ActionEvent actionEvent) throws Exception {
		NextWindowLauncher(actionEvent, "/GUI/PopUpWindows/DeterminingDueTime.fxml", this, false);
	}

	public void openApproveExecution(ActionEvent actionEvent) throws Exception {
		NextWindowLauncher(actionEvent, "/GUI/PopUpWindows/ApproveExecutionLeader.fxml", this, false);
	}

	public void openExtension(ActionEvent actionEvent) throws Exception {
		NextWindowLauncher(actionEvent, "/GUI/PopUpWindows/ApproveExtension.fxml", this, false);
	}
}

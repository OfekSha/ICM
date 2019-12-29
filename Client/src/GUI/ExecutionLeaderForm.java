package GUI;

import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ExecutionLeaderForm extends EstimatorExecutorForm {

	@FXML
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;
	public TextArea taInitiatorRequest;

	private String selected;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		setRequestsComboBox();
	}

	public void RequestsComboBoxUsed() {
		selected = cmbRequests.getSelectionModel().getSelectedItem();
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

	public void getApproveExecution(ActionEvent actionEvent) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Approve performing change");
		alert.setHeaderText("Are you perform requested changes?");
		alert.setContentText("Choose OK if you approve");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent()) {
			if (result.get() == ButtonType.OK) {
				changeRequests.forEach(cR -> {
					if (selected.equals(cR.getRequestID())) {
						//TODO Change status or stage whatever is needed
					}
				});
			}
			/*if (result.get() == ButtonType.CANCEL) {

			}*/
		}
	}

	public void requestExtension(ActionEvent actionEvent) {

	}
}

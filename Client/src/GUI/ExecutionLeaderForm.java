package GUI;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import GUI.PopUpWindows.DueTimeController;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static Entity.ProcessStage.subStages.determiningDueTime;
import static Entity.ProcessStage.subStages.supervisorAction;
import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;

public class ExecutionLeaderForm extends EstimatorExecutorForm {

	@FXML
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;
	public TextArea taInitiatorRequest;
	public Text txtDueTime;
	public Label lbDueTime;

	private String selected;
	private ChangeRequest changeRequest;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initUI();
	}

	private void initUI() {
		getRequests();
		btnDueTime.setDisable(true);
		btnApprove.setDisable(true);
		btnGetExtension.setDisable(true);
		Platform.runLater(this::setRequestsComboBox);
	}

	public void RequestsComboBoxUsed() {
		selected = cmbRequests.getSelectionModel().getSelectedItem();
		if (selected != null) {
			changeRequests.forEach(cR -> {
				if (selected.equals(cR.getRequestID())) {
					changeRequest = cR;
				}
			});
			this.taInitiatorRequest.setText(changeRequest.getProblemDescription());
			this.taExaminerReport.setText(changeRequest.getComment());
			LocalDate dueTime = changeRequest.getProcessStage().getDueDate();
			if (dueTime != null) {
				txtDueTime.setText(dueTime.toString());
				lbDueTime.setVisible(true);
				btnDueTime.setDisable(true);
			} else {
				btnDueTime.setDisable(false);
				lbDueTime.setVisible(false);
				txtDueTime.setText(null);
			}
			//TODO some condition to turn them active
			btnApprove.setDisable(false);
			btnGetExtension.setDisable(false);
			DueTimeController.setChangeRequest(changeRequest);
		}
	}

	// the following  methods are from the class diagram:
	public void getReport() {
	}

	public void openDueTime() throws Exception {
		popupWindowLauncher("/GUI/PopUpWindows/DeterminingDueTime.fxml");
		LocalDate dueTime = changeRequest.getProcessStage().getDueDate();
		if (dueTime != null) {
			this.txtDueTime.setText(dueTime.toString());
			this.lbDueTime.setVisible(true);
		}
	}

	public void getExecutionApproved() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Approve performing change");
		alert.setHeaderText("Are you perform requested changes?");

		ButtonType btnApprove = new ButtonType("Approve");
		ButtonType btnCancel = ButtonType.CANCEL;
		alert.getButtonTypes().setAll(btnApprove, btnCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent()) {
			if (result.get() == btnApprove) {
				changeRequests.forEach(cR -> {
					if (selected.equals(cR.getRequestID()) &&
							cR.getProcessStage().getCurrentSubStage().equals(determiningDueTime)) {
						cR.getProcessStage().setCurrentSubStage(supervisorAction);
						clientRequestFromServer newRequest =
								new clientRequestFromServer(updateProcessStage, changeRequest);
						ClientLauncher.client.handleMessageFromClientUI(newRequest);
					}
				});
			}
		}
	}

	public void requestExtension(ActionEvent actionEvent) {

	}
}

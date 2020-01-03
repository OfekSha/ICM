package GUI;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static Entity.ProcessStage.ChargeRequestStages.examination;
import static Entity.ProcessStage.ChargeRequestStages.execution;
import static Entity.ProcessStage.subStages.supervisorAllocation;
import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;

public class ExecutionLeaderForm extends EstimatorExecutorForm {

	@FXML
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;
	public TextArea taInitiatorRequest;
	public Text txtDueTime;
	public Text txtStage;
	public Label lbDueTime;
	public Label lbStage;


	private String selected;
	public static ChangeRequest changeRequest;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initUI();
	}

	private void initUI() {
		getRequests();
		btnDueTime.setDisable(true);
		btnApprove.setDisable(true);
		btnGetExtension.setDisable(true);
		txtDueTime.setTextAlignment(TextAlignment.CENTER);
		txtStage.setTextAlignment(TextAlignment.CENTER);
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
			taInitiatorRequest.setText(changeRequest.getProblemDescription());
			taExaminerReport.setText(changeRequest.getComment());
			LocalDate dueTime = changeRequest.getProcessStage().getDueDate();
			if (dueTime != null) {
				setDueTimeBlockVisible(txtDueTime, dueTime.toString(), lbDueTime);
				setApproveAndGetEnabled();
			} else {
				btnDueTime.setDisable(false);
				lbDueTime.setVisible(false);
				txtDueTime.setText(null);
			}
			setRequestStagesVisible();
		}
	}

	private void setRequestStagesVisible() {
		txtStage.setText(changeRequest.getProcessStage().getCurrentStage().name()
				+ "\n" + changeRequest.getProcessStage().getCurrentSubStage().name());
		lbStage.setVisible(true);
	}

	private void setDueTimeBlockVisible(Text txtDueTime, String s, Label lbDueTime) {
		btnDueTime.setDisable(true);
		txtDueTime.setText(s);
		lbDueTime.setVisible(true);
	}

	private void setApproveAndGetEnabled() {
		if (changeRequest.getProcessStage().getCurrentSubStage().ordinal() != 1) {
			btnApprove.setDisable(false);
			btnGetExtension.setDisable(false);
		}
	}

	// the following  methods are from the class diagram:
	public void getReport() {
	}

	public void openDueTime() throws Exception {
		popupWindowLauncher("/GUI/PopUpWindows/DeterminingDueTime.fxml");
		popupWindow.setOnHiding(windowEvent -> {
			LocalDate dueTime = changeRequest.getProcessStage().getDueDate();
			setDueTimeBlockVisible(txtDueTime, dueTime.toString(), lbDueTime);
			setRequestStagesVisible();
			setApproveAndGetEnabled();
		});
	}

	public void getExecutionApproved() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Approve performing change");
		alert.setHeaderText("Are you perform requested changes?");

		ButtonType btnApprove = new ButtonType("Approve");
		ButtonType btnCancel = ButtonType.CANCEL;
		alert.getButtonTypes().setAll(btnApprove, btnCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == btnApprove) {
			if (changeRequest.getProcessStage().getCurrentStage().equals(execution)) {
				changeRequest.getProcessStage().setCurrentStage(examination);
				changeRequest.getProcessStage().setCurrentSubStage(supervisorAllocation);
				sendUpdateForRequest();
			}
		}

		alert.setOnHidden(event -> setDueTimeBlockVisible
				(txtStage, changeRequest.getProcessStage().getCurrentStage().name(), lbStage));
	}

	public static void sendUpdateForRequest() {
		clientRequestFromServer newRequest = new clientRequestFromServer(updateProcessStage, changeRequest);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
	}

	public void requestExtension(ActionEvent actionEvent) throws IOException {
		popupWindowLauncher("/GUI/PopUpWindows/GetExtension.fxml");

	}
}

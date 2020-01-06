package GUI;

import Entity.ChangeRequest;
import Entity.ProcessStage;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static Entity.ProcessStage.ChargeRequestStages.execution;
import static Entity.ProcessStage.subStages.determiningDueTime;
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
	private ChangeRequest changeRequest;
	public static ProcessStage processStage;
	private LocalDate currentDueTime;
	private subStages currentSubStage;
	private ChargeRequestStages currentRequestStage;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initUI();
	}

	private void initUI() {
		getRequests();
		btnDueTime.setDisable(true);
		btnApprove.setDisable(true);
		txtDueTime.setTextAlignment(TextAlignment.CENTER);
		txtStage.setTextAlignment(TextAlignment.CENTER);
		Platform.runLater(this::setRequestsComboBox);
	}

	public void RequestsComboBoxUsed() {
		currentSubStage = null;
		currentDueTime = null;
		currentRequestStage = null;

		selected = cmbRequests.getSelectionModel().getSelectedItem();
		if (selected != null) {
			changeRequests.forEach(cR -> {
				if (selected.equals(cR.getRequestID())) {
					changeRequest = cR;
					processStage = cR.getProcessStage();
				}
			});
			taInitiatorRequest.setText(changeRequest.getProblemDescription());
			taExaminerReport.setText(changeRequest.getComment());

			currentDueTime = processStage.getDueDate();
			currentRequestStage = processStage.getCurrentStage();
			currentSubStage = processStage.getCurrentSubStage();

			if (currentSubStage.equals(determiningDueTime)) {
				if (currentDueTime != null) {
					btnDueTime.setDisable(true);
					lbDueTime.setVisible(true);
					txtDueTime.setText(currentDueTime.toString());
					//txtDueTime.setVisible(true);
					setGetExtensionEnabled();
					setApproveExecutionEnabled(true);
				} else {
					btnDueTime.setDisable(false);
					lbDueTime.setVisible(false);
					//txtDueTime.setVisible(false);
				}
			} else {
				btnDueTime.setDisable(true);
				lbDueTime.setVisible(false);
				//txtDueTime.setVisible(false);
				btnGetExtension.setDisable(true);
				btnApprove.setDisable(true);
			}
			//TODO remove later
			setRequestStagesVisible(true);
		}
	}
	
	// the following  methods are from the class diagram:
	public void getReport() {

	}

	public void openDueTime() throws Exception {
		popupWindowLauncher("/GUI/PopUpWindows/DeterminingDueTime.fxml");
		popupWindow.setOnHiding(windowEvent -> {
			currentDueTime = processStage.getDueDate();
			if (currentDueTime != null) {
				btnDueTime.setDisable(true);
				txtDueTime.setText(currentDueTime.toString());
				//txtDueTime.setVisible(true);
				lbDueTime.setVisible(true);
				setApproveExecutionEnabled(true);
				setRequestStagesVisible(true);
				setGetExtensionEnabled();
			}
		});
	}

	public void openExecutionApproval() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Approve performing change");
		alert.setHeaderText("Are you perform requested changes?");

		ButtonType btnApprove = new ButtonType("Approve");
		ButtonType btnCancel = ButtonType.CANCEL;
		alert.getButtonTypes().setAll(btnApprove, btnCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == btnApprove) {
			setApproveExecutionEnabled(false);
			btnGetExtension.setDisable(true);
			sendUpdateForRequest();
		}
	}

	public void openRequestExtension() throws IOException {
		popupWindowLauncher("/GUI/PopUpWindows/GetExtension.fxml");
		popupWindow.setOnHidden(event -> {
			if (!processStage.getExtensionExplanation().isEmpty()) {
				btnGetExtension.setDisable(true);
			}
		});
	}

	public static void sendUpdateForRequest() {
		if (processStage != null) {
			clientRequestFromServer newRequest =
					new clientRequestFromServer(updateProcessStage, processStage);
			ClientLauncher.client.handleMessageFromClientUI(newRequest);
		}
	}

	private void setRequestStagesVisible(boolean bool) {
		txtStage.setText(currentRequestStage.name()
				+ "\n" + currentSubStage.name());
		lbStage.setVisible(bool);
	}

	/**
	 * boolean value as a switcher
	 * @param bool == true ? dueTime is exist, button disabled and label shows up
	 * : dueTime is empty, button enabled and label disabled
	 */

	private void setApproveExecutionEnabled(boolean bool) {
		if (currentRequestStage.equals(execution)
				&& currentSubStage.equals(determiningDueTime)) {
			btnApprove.setDisable(!bool);
		}
	}

	private void setGetExtensionEnabled() {
		if (currentDueTime != null && currentDueTime.minusDays(3).isBefore(LocalDate.now())) {
			btnGetExtension.setDisable(false);
		} else btnGetExtension.setDisable(true);
	}
}

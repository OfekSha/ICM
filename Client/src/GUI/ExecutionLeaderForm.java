package GUI;

import Entity.ChangeRequest;
import Entity.ProcessStage.subStages;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static Entity.ProcessStage.ChargeRequestStages.examination;
import static Entity.ProcessStage.ChargeRequestStages.execution;
import static Entity.ProcessStage.subStages.*;
import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;
import static GUI.PopUpWindows.DueTimeController.processStage;
import static GUI.PopUpWindows.GetExtensionController.Approve;

public class ExecutionLeaderForm extends UserForm {

	@FXML
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;
	public TextArea taInitiatorRequest;
	public TableView<requirementForTable> tblRequests;
	public TableColumn<requirementForTable, String> colID;
	public TableColumn<requirementForTable, Object> colStatus;
	public TableColumn<requirementForTable, Object> colDueTime;

	private RequestTableView requestTableView;
	private String selectedID;
	private ChangeRequest changeRequest;
	private LocalDate currentDueTime;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		initUI();
	}

	public void initUI() {
        getRequests();

		requestTableView = new RequestTableView(tblRequests, colID, colStatus, colDueTime);

		btnDueTime.setDisable(true);
		btnApprove.setDisable(true);
		btnGetExtension.setDisable(true);

		taExaminerReport.clear();
		taInitiatorRequest.clear();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Platform.runLater(this::setTableRequests);
	}

	protected void setTableRequests() {
		changeRequests.forEach(e -> {
			if (e.getProcessStage().getCurrentStage().equals(execution) &&
			e.getProcessStage().getCurrentSubStage().equals(determiningDueTime)) {
				tblRequests.getItems().add(new requirementForTable(e));
			}
		});
	}

	public void tableGotClicked() {
		subStages currentSubStage;
		currentDueTime = null;

		requirementForTable req = tblRequests.getSelectionModel().getSelectedItem();
		if (req != null) {
			selectedID = req.getId();

			if (selectedID != null) {
				changeRequests.forEach(cR -> {
					if (selectedID.equals(String.valueOf(cR.getRequestID()))) {
						changeRequest = cR;
						processStage = cR.getProcessStage();
					}
				});
				taInitiatorRequest.setText(changeRequest.getProblemDescription());
				taExaminerReport.setText(changeRequest.getComment());

				currentDueTime = processStage.getDueDate();
				currentSubStage = processStage.getCurrentSubStage();

				if (currentSubStage.equals(determiningDueTime)) {
					if (currentDueTime != null) {
						btnDueTime.setDisable(true);
						setGetExtensionEnabled();
						btnApprove.setDisable(false);
					} else {
						btnDueTime.setDisable(false);
					}
				} else {
					btnDueTime.setDisable(true);
					btnGetExtension.setDisable(true);
					btnApprove.setDisable(true);
				}
			}
		}
	}

	public void openDueTime() throws Exception {
		popupWindowLauncher("/GUI/PopUpWindows/DeterminingDueTime.fxml");
		popupWindow.setOnHiding(windowEvent -> {
			currentDueTime = processStage.getDueDate();
			if (currentDueTime != null) {
				btnDueTime.setDisable(true);
				btnApprove.setDisable(false);
				processStage.setCurrentSubStage(ApprovingDueTime);
				sendUpdateForRequest();
				setGetExtensionEnabled();
			}
		});
	}

	public void openExecutionApproval() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Approve performing change");
		alert.setHeaderText("Are you perform requested changes?");

		ButtonType approveButton = new ButtonType("Approve");
		ButtonType btnCancel = ButtonType.CANCEL;
		alert.getButtonTypes().setAll(approveButton, btnCancel);

		Optional<ButtonType> result = alert.showAndWait();

		if (result.isPresent() && result.get() == approveButton) {
			if (processStage.getCurrentStage().equals(execution)) {
				processStage.setCurrentStage(examination);
				processStage.setCurrentSubStage(supervisorAllocation);
				btnApprove.setDisable(true);
				btnGetExtension.setDisable(true);
				sendUpdateForRequest();
			}
		}
	}

	public void openRequestExtension() throws IOException {
		popupWindowLauncher("/GUI/PopUpWindows/GetExtension.fxml");
		popupWindow.setOnHidden(event -> {
			if (!processStage.getExtensionExplanation().isEmpty()) {
				btnGetExtension.setDisable(Approve);
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

	private void setGetExtensionEnabled() {
		if (currentDueTime != null &&
				processStage.getExtensionExplanation() != null &&
				currentDueTime.minusDays(3).isBefore(LocalDate.now())) {
			btnGetExtension.setDisable(false);
		} else btnGetExtension.setDisable(true);
	}
}

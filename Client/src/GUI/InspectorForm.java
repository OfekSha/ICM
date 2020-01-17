package GUI;

import Controllers.InspectorController;
import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import GUI.PopUpWindows.ApproveDueTimeController;
import GUI.PopUpWindows.ApproveRoleForm;
import GUI.PopUpWindows.InspectorChangeStatusForm;
import GUI.PopUpWindows.InspectorChangeStatusForm.Status;
import GUI.PopUpWindows.ApproveRoleForm.Role;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.util.ResourceBundle;

public class InspectorForm extends StageSupervisorForm {
	// fxml vars:

	@FXML
	private Button btnFreezeUnfreeze, btnRoleApprove, btnDueTimeApprove, btnExtensionApprove, btnCloseRequest;
	
	@FXML
	private tabPaneInspectorForm tabPaneController; // tabs that get more info about request.
	// menu items of menubtnWatch (the types of request):
	@FXML
	private MenuItem freeze, unfreeze, estimator, executionLeader, dueTime, close, extension;

	@FXML
	private TableColumn<requirementForTable, Object> stageColumn;

	// not fxml vars:
	private requirementForTable selectedReq;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		table = new RequestTableView(tblView, idColumn, statusColumn, stageColumn, dueTimeColumn, messageColumn);
		tabPaneController.DocumentTableController.initializeDocumentTableView();
	}

	@Override
	public void getFromServer(Object message) {
		controller.messageFromServer(message);
		table.setData(InspectorController.requests);

	}

	// functions for gui:
	@FXML
	public void freezeOrUnfreeze(ActionEvent event) throws Exception {
		switch (selectedReq.getStatus()) {
			// the requirement wasn't freeze.
			case ongoing:
				InspectorChangeStatusForm.status=Status.freeze;
				break;
			case suspended:
				InspectorChangeStatusForm.status=Status.unfreeze;
				break;
			default:
				throw new Exception("clicked freeze / unfreeze on request thats not ongoing or susspended status.(closed)");
		}
		InspectorChangeStatusForm.controller=controller;
		popupWindow("/GUI/PopUpWindows/InspectorChangeStatus.fxml");
	}

	@FXML
	public void roleApprove(ActionEvent event) throws Exception {

		switch (selectedReq.getStage().getCurrentStage()) {
			case meaningEvaluation: // need to approve Estimator
				ApproveRoleForm.role = Role.estimator;
				break;
			case execution: // need to approve Execution Leader
				ApproveRoleForm.role = Role.executionLeader;
				break;
			default:
				// need to throw new exception.
				break;
		}
		ApproveRoleForm.controller=controller;
		popupWindow("/GUI/PopUpWindows/ApproveRole.fxml");
	}

	@FXML
	public void dueTimeApprove(ActionEvent event) throws Exception {
		ApproveDueTimeController.controller=controller;
		popupWindow("/GUI/PopUpWindows/ApproveDueTime.fxml");
	}

	@FXML
	public void extensionApprove(ActionEvent event) throws Exception {
		popupWindow("/GUI/PopUpWindows/ApproveExtension.fxml");

	}

	@FXML
	public void closeRequest(ActionEvent event) { // @@ TODO: add pop up window.

	}

	@FXML
	public void onRequestClicked(MouseEvent event) {
		 selectedReq = table.onRequirementClicked(event);
		if (selectedReq == null) {
			setButtons(false, false, false, false, false);
			return;
		}
		InspectorController.setSelectedRequest(selectedReq);
		tabPaneController.onRequirementClicked(selectedReq);
		//enable close:
		if (selectedReq.getStage().getCurrentStage() == ChargeRequestStages.closure) {
			setButtons(false, false, false, false, true);
			return;
		}
		// when extension is on:
		int stageLevel = selectedReq.getStage().getCurrentStage().ordinal(); // 0-4
		boolean turnExtension = selectedReq.getStage().getWasThereAnExtensionRequest()[stageLevel] == 1; // need approve of inspector.

		switch (selectedReq.getStatus()) {
			//enable freeze:
			case ongoing:
				btnFreezeUnfreeze.setText("Freeze");
				switch (selectedReq.getStage().getCurrentSubStage()) {
					case ApprovingDueTime: //enable due time approve
						setButtons(true, false, true, turnExtension, false);
						break;
					case supervisorAllocation: // enable role approve
						setButtons(true, true, false, turnExtension, false);
						switch (selectedReq.getStage().getCurrentStage()) {
							case meaningEvaluation: // need to approve Estimator
								btnRoleApprove.setText("Estimator Approve");
								break;
							case execution: // need to approve Execution Leader
								btnRoleApprove.setText("Execution Leader Approve");
								break;
							default:
								btnDueTimeApprove.setDisable(true);
								btnCloseRequest.setDisable(true);
								btnRoleApprove.setDisable(true);
								btnRoleApprove.setText("Role Approve");
								break;
						}
						break;
					default:// TODO: throw exception?
						setButtons(true, false, false, turnExtension, false);
						break;
				}
				break;
			//enable unfreeze:
			case suspended:
				setButtons(true, false, false, false, false);
				btnFreezeUnfreeze.setText("Unfreeze");
				break;
			// when requirement is close (not need to be).
			case closed:
				setButtons(false, false, false, false, false);
			default:
				btnFreezeUnfreeze.setDisable(true);
				// @@need to throw new Exception.
				break;
		}
		// when role, due time will be not disable

	}

	private void setButtons(boolean freezeOrUn, boolean role, boolean dueTime, boolean extension, boolean close) {
		btnFreezeUnfreeze.setDisable(!freezeOrUn);
		btnRoleApprove.setDisable(!role);
		btnDueTimeApprove.setDisable(!dueTime);
		btnExtensionApprove.setDisable(!extension);
		btnCloseRequest.setDisable(!close);
		if (!role) btnRoleApprove.setText("Role Approve");
		if (!freezeOrUn) btnFreezeUnfreeze.setText("Freeze / Unfreeze");
	}

	@Override
	public StageSupervisorController getController() {
		return new InspectorController();
	}

	@Override
	public IcmForm getIcmForm() {
		return this;
	}

	@Override
	public void filterRequests(ActionEvent event) {
		controller.filterRequests((MenuItem) event.getSource());
	}

	@Override
	public ChangeRequest getSelectedReq() {
		return InspectorController.selectedRequest;
	}
}

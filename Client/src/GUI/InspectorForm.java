package GUI;

import Controllers.InspectorController;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import GUI.PopUpWindows.ApproveRoleForm;
import GUI.PopUpWindows.ApproveRoleForm.Role;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InspectorForm extends UserForm {
	// fxml vars:

	@FXML
	private Button btnFreezeUnfreeze, btnRoleApprove, btnDueTimeApprove, btnExtensionApprove, btnCloseRequest;
	@FXML
	private MenuButton menubtnWatch;
	@FXML
	private tabPaneInspectorForm tabPaneController; // tabs that get more info about request.
	// menu items of menubtnWatch (the types of request):
	@FXML
	private MenuItem freeze, unfreeze, estimator, executionLeader, dueTime, close, extension;

	@FXML
	private TableView<requirementForTable> tblViewRequests;
	// table colums:
	@FXML
	private TableColumn<requirementForTable, String> columnId, columnMessage;
	@FXML
	private TableColumn<requirementForTable, Object> columnStatus, columnStage, columnDueTime;

	// not fxml vars:
	private RequestTableView table; // make adaptable class for table view.
	private static Stage popupWindow;
	public static Stage inspectorWindow;
	private requirementForTable selectedReq;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		table = new RequestTableView(tblViewRequests, columnId, columnStatus, columnStage, columnDueTime, columnMessage);
		tabPaneController.DocumentTableController.initializeDocumentTableView();
	}

	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		table.setData(InspectorController.requests);

	}

	// functions for gui:
	private void popupWindow(String target) throws IOException {
		// inspectorWindow.setScene(((Node)event.getTarget()).getScene());
		popupWindow = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource(target));
		Scene scene = new Scene(root);
		popupWindow.setScene(scene);
		popupWindow.initModality(Modality.APPLICATION_MODAL);
		popupWindow.show();
		InspectorForm icmForm = this; //this just works to inspector.

		// what happened when close window from out or from stage.close / stage.hide
		// method
		popupWindow.setOnCloseRequest(windowEvent ->  // close from out (alt +f4)
				ClientLauncher.client.setClientUI(icmForm));
		// stage.close / stage.hide method
		popupWindow.setOnHidden(we -> ClientLauncher.client.setClientUI(icmForm));
	}

	@FXML
	public void watchRequest(ActionEvent event) { // get event from the menuItem.
		InspectorController.watchRequests(((MenuItem) event.getSource()));
	}

	@FXML
	public void freezeOrUnfreeze(ActionEvent event) throws Exception {
		switch (selectedReq.getStatus()) {
			// the requirement wasn't freeze.
			case ongoing:
				InspectorController.freeze(selectedReq.getOriginalRequest());
				btnFreezeUnfreeze.setText("Unfreeze");
				break;
			case suspended:
				InspectorController.unfreeze(selectedReq.getOriginalRequest());
				btnFreezeUnfreeze.setText("freeze");
				break;
			default:
				throw new Exception("clicked freeze / unfreeze on request thats not ongoing or susspended status.");
		}
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
		popupWindow("/GUI/PopUpWindows/ApproveRole.fxml");
	}

	@FXML
	public void dueTimeApprove(ActionEvent event) throws Exception {
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
	public void onRequirementClicked(MouseEvent event) {
		selectedReq = table.onRequirementClicked(event);
		if (selectedReq == null) {
			setButtons(false, false, false, false, false);
			return;
		}
		InspectorController.selectedRequest = selectedReq.getOriginalRequest();
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
						break;
				}
				break;
			//enable unfreeze:
			case suspended:
				setButtons(true, false, false, turnExtension, false);
				btnFreezeUnfreeze.setText("Unfreeze");
				break;
			// when requirement is close (not need to be).
			case closed:
				setButtons(false, false, false, turnExtension, false);
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
}

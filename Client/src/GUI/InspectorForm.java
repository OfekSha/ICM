package GUI;

import Controllers.InspectorController;
import Entity.ChangeRequest;
import Entity.DocumentForTable;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import GUI.PopUpWindows.ApproveRoleForm;
import GUI.PopUpWindows.ApproveRoleForm.Role;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class InspectorForm extends UserForm {
	// fxml vars:
	@FXML
	public Button btnGetDetails;
	@FXML
	public Button btnFreezeUnfreeze;
	@FXML
	public Button btnRoleApprove;
	@FXML
	public Button btnDueTimeApprove;
	@FXML
	public Button btnExtensionApprove;
	@FXML
	public Button btnCloseRequest;

	// menu button for watch types of requests
	@FXML
	public MenuButton menubtnWatch;

	// menu items of menubtnWatch (the types of request):
	@FXML
	private MenuItem freeze;
	@FXML
	private MenuItem unfreeze;
	@FXML
	private MenuItem estimator;
	@FXML
	private MenuItem executionLeader;
	@FXML
	private MenuItem dueTime;
	@FXML
	private MenuItem close;
	@FXML
	private MenuItem extension;

	@FXML
	public TableView<requirementForTable> tblViewRequests;
	// table colums:
	@FXML
	public TableColumn<requirementForTable, String> columnId;
	@FXML
	public TableColumn<requirementForTable, Object> columnStatus;
	@FXML
	public TableColumn<requirementForTable, Object> columnStage;
	@FXML
	public TableColumn<requirementForTable, Object> columnDueTime;

	/*
	 * //for test only:
	 * 
	 * @FXML public TableColumn<requirementForTable, String> columnStage;
	 * 
	 * @FXML public TableColumn<requirementForTable, String> columnDueTime;
	 */

	@FXML
	public TableColumn<requirementForTable, String> columnMessage;
	
	// document table vars
	
	// table stuff
		@FXML
		public TableView<DocumentForTable> tblViewDocuments;
		// table columns:
			@FXML
			public TableColumn<DocumentForTable, String> columnFileName;
			@FXML
			public TableColumn<DocumentForTable, String> columnFileSize;
			private ObservableList<DocumentForTable> documentTableData;
			@FXML
			public Button btnDownload;
			
	// not fxml vars:
	RequestTableView table; // make adaptable class for table view.
	private static Stage popupWindow;
	public static Stage inspectorWindow;
	private requirementForTable selectedReq;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		table=new RequestTableView(tblViewRequests,columnId,columnStatus,columnStage,columnDueTime,columnMessage);
		initializeDocumentTableView();
	}

	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		table.setData(InspectorController.requests);

	}

	// functions for gui:
	private void popupWindow(String target, ActionEvent event) throws IOException {
		// inspectorWindow.setScene(((Node)event.getTarget()).getScene());
		popupWindow = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource(target));
		Scene scene = new Scene(root);
		popupWindow.setScene(scene);
		popupWindow.initModality(Modality.APPLICATION_MODAL);
		popupWindow.show();
		InspectorForm icmForm = this;

		// what happened when close window from out or from stage.close / stage.hide
		// method
		popupWindow.setOnCloseRequest(windowEvent ->  // close from out (alt +f4)
					ClientLauncher.client.setClientUI(icmForm));
		// stage.close / stage.hide method
		popupWindow.setOnHidden(we -> ClientLauncher.client.setClientUI(icmForm));
	}

	public void watchRequest(ActionEvent event) { // get event from the menuItem.
		InspectorController.watchRequests(((MenuItem) event.getSource()));
	}

	public void getDetails(ActionEvent event) {

	}

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

	public void roleApprove(ActionEvent event) throws Exception {
		
		switch (selectedReq.getStage().getCurrentStage()) {
		case meaningEvaluation: // need to approve Estimator
			ApproveRoleForm.role=Role.estimator;
			break;
		case execution: // need to approve Execution Leader
			ApproveRoleForm.role=Role.executionLeader;
			break;
		default:
			// need to throw new exception.
			break;
		}
		popupWindow("/GUI/PopUpWindows/ApproveRole.fxml", event);
	}

	public void dueTimeApprove(ActionEvent event) throws Exception {
		popupWindow("/GUI/PopUpWindows/ApproveDueTime.fxml", event);
	}

	public void extensionApprove(ActionEvent event) throws Exception {
		popupWindow("/GUI/PopUpWindows/ApproveExtension.fxml", event);

	}

	public void closeRequest(ActionEvent event) {

	}

	public void onRequirementClicked(MouseEvent event) {
		 selectedReq = table.onRequirementClicked(event);
		 InspectorController.selectedRequest=selectedReq.getOriginalRequest();
		 //setting up the document table
		documentTableData = FXCollections.observableArrayList( InspectorController.DocumentForTableList());
		tblViewDocuments.setItems(documentTableData);
		
		if (selectedReq == null)
			return;
		//@ yonathan to @ofeck  - the commented commend cused all the exceptions
		//btnGetDetails.setDisable(false);

		// when extension is on:
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[selectedReq.getStage().getCurrentStage()
				.ordinal()] == 1) {
			btnExtensionApprove.setDisable(false);
		} else
			btnExtensionApprove.setDisable(true);

		// when freeze / unfreeze and close will be not disable:
		//enable close:
		if (selectedReq.getStage().getCurrentStage() == ChargeRequestStages.closure) {
			btnFreezeUnfreeze.setText("Freeze / Unfreeze");
			btnRoleApprove.setText("Role Approve");
			btnCloseRequest.setDisable(false);
			btnFreezeUnfreeze.setDisable(true);
			btnExtensionApprove.setDisable(true);
			btnDueTimeApprove.setDisable(true);
			btnRoleApprove.setDisable(true);
			return;
		}

		switch (selectedReq.getStatus()) {
		//enable freeze:
		case ongoing:
			btnFreezeUnfreeze.setDisable(false);
			btnFreezeUnfreeze.setText("Freeze");
			break;
		//enable unfreeze:
		case suspended:
			btnFreezeUnfreeze.setDisable(false);
			btnFreezeUnfreeze.setText("Unfreeze");
			break;
			// when requirement is close (not need to be).
		case closed:
			btnFreezeUnfreeze.setText("Freeze / Unfreeze");
			btnCloseRequest.setDisable(true);
			btnFreezeUnfreeze.setDisable(true);
			btnExtensionApprove.setDisable(true);
			btnDueTimeApprove.setDisable(true);
			btnRoleApprove.setDisable(true);
		default:
			btnFreezeUnfreeze.setDisable(true);
			// @@need to throw new Exception.
			break;
		}
		// when role, due time will be not disable
		switch (selectedReq.getStage().getCurrentSubStage()) {
		//enable due time approve
		case determiningDueTime:
			btnDueTimeApprove.setDisable(false);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(true);
			break;
		case supervisorAction:
			btnDueTimeApprove.setDisable(true);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(true);
			break;
		case supervisorAllocation:
			btnDueTimeApprove.setDisable(true);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(false);
			switch (selectedReq.getStage().getCurrentStage()) {
			case meaningEvaluation: // need to approve Estimator
				btnRoleApprove.setText("Estimator Approve");
				break;
			case execution: // need to approve Execution Leader
				btnRoleApprove.setText("Execution Leader Approve");
				break;
			default:
				btnRoleApprove.setText("Role Approve");
				break;
			}
			break;
		}
	}
	
	/** setting up the document  table columns 
	 * 
	 */
	private void initializeDocumentTableView() {
																									
		columnFileName.setCellValueFactory(new PropertyValueFactory<>("name")); // set values for id
		columnFileSize.setCellValueFactory(new PropertyValueFactory<>("size")); // set values
	
	}
}

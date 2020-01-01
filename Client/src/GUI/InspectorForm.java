package GUI;

import WindowApp.IcmForm;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.InspectorController;
import Controllers.InspectorController.requirmentForTable;
import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.Requirement.statusOptions;
import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class InspectorForm extends UserForm implements IcmForm {
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
	public TableView<requirmentForTable> tblviewRequests;
	// table colums:
	@FXML
	public TableColumn<requirmentForTable, String> columnId;
	@FXML
	public TableColumn<requirmentForTable, Object> columnStatus;
	@FXML
	public TableColumn<requirmentForTable, Object> columnStage;
	@FXML
	public TableColumn<requirmentForTable, Object> columnDueTime;

	/*
	 * //for test only:
	 * 
	 * @FXML public TableColumn<requirmentForTable, String> columnStage;
	 * 
	 * @FXML public TableColumn<requirmentForTable, String> columnDueTime;
	 */

	@FXML
	public TableColumn<requirmentForTable, String> columnMessage;

	// not fxml vars:
	public static ArrayList<ChangeRequest> reqList;
	private ObservableList<requirmentForTable> tableData;
	private static Stage popupWindow;
	public static Stage inspectorWindow;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		initializeTableView();

	}

	private void initializeTableView() {
		columnMessage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("message")); // set
																											// values
																											// for
																											// messages
		columnId.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("id")); // set values for id
		columnStatus.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("status")); // set values
																											// for
																											// status
		columnStage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("stage"));
		columnDueTime.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("dueTime"));

	}

	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		tableData = FXCollections.observableArrayList(InspectorController.requirmentForTableList(reqList));
		tblviewRequests.setItems(tableData);

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
		InspectorForm icmform=this;
		
		//what happend when close window from out or from stage.close / stage.hide method
		popupWindow.setOnCloseRequest(new EventHandler<WindowEvent>() { // close from out (alt +f4)
			public void handle(WindowEvent we) {
				ClientLauncher.client.setClientUI(icmform);
			}
		});
		popupWindow.setOnHidden(new EventHandler<WindowEvent>() { // stage.close / stage.hide method
			public void handle(WindowEvent we) {
				ClientLauncher.client.setClientUI(icmform);
			}
		});
	}

	public void watchRequest(ActionEvent event) throws Exception { // get event from the menuItem.
		InspectorController.watchRequests(((MenuItem) event.getSource()));
	}

	public void getDetails(ActionEvent event) throws Exception {

	}

	public void freezeOrUnfreeze(ActionEvent event) throws Exception {
		requirmentForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
		switch (selectedReq.getStatus()) {
		// the requirement wasn't freeze.
		case ongoing:
			InspectorController.freeze(selectedReq);
			btnFreezeUnfreeze.setText("Unfreeze");
			break;
		case suspended:
			InspectorController.unfreeze(selectedReq);
			btnFreezeUnfreeze.setText("freeze");
			break;
		default:
			throw new Exception("clicked freeze / unfreeze on request thats not ongoing or susspended status.");
		}
	}

	public void roleApprove(ActionEvent event) throws Exception {
		requirmentForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
		switch (selectedReq.getStage().getCurrentStage()) {
		case meaningEvaluation: // need to approve Estimator
			popupWindow("/GUI/PopUpWindows/ApproveEstimator.fxml", event);
			break;
		case execution: // need to approve Execution Leader
			popupWindow("/GUI/PopUpWindows/ApproveExecutionLeader.fxml", event);
			break;
		default:
			// need to throw new exception.
			break;
		}
	}

	public void dueTimeApprove(ActionEvent event) throws Exception {
		popupWindow("/GUI/PopUpWindows/ApproveDueTime.fxml", event);
	}

	public void extensionApprove(ActionEvent event) throws Exception {
		popupWindow("/GUI/PopUpWindows/ApproveExtension.fxml", event);

	}

	public void closeRequest(ActionEvent event) throws Exception {

	}

	public void onRequirmentClicked(MouseEvent event) throws Exception {

		requirmentForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
		if (selectedReq == null)
			return;
		InspectorController.selctedReqFromTable=selectedReq;
		btnGetDetails.setDisable(false);
		// when freeze / unfreeze and close will be not disable.
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
		// the requirement wasn't freeze.
		case ongoing:
			btnFreezeUnfreeze.setDisable(false);
			btnFreezeUnfreeze.setText("Freeze");
			break;
		case suspended:
			btnFreezeUnfreeze.setDisable(false);
			btnFreezeUnfreeze.setText("Unfreeze");
			break;
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
		// when role, due time, and extension will be not disable
		switch (selectedReq.getStage().getCurrentSubStage()) {
		case determiningDueTime:
			btnDueTimeApprove.setDisable(false);
			btnExtensionApprove.setDisable(true);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(true);
			break;
		case supervisorAction:
			btnDueTimeApprove.setDisable(true);
			btnExtensionApprove.setDisable(true);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(true);
			break;
		case supervisorAllocation:
			btnDueTimeApprove.setDisable(true);
			btnExtensionApprove.setDisable(true);
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
	// TODO: the following methods are from the class diagram:

	public void getUserId() {
	}

	public void getRule() {
	}

	public void getRequestId() {
	}

	public void getDoneReport() {
	}

	public void getTalkApprove() {
	}
}

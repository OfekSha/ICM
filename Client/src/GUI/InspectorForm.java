package GUI;

import WindowApp.IcmForm;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.sun.corba.se.pept.transport.EventHandler;

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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
	public TableColumn<requirmentForTable, Integer> columnId;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initializeTableView();

	}

	private void initializeTableView() {
		columnMessage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("message")); // set
																											// values
																											// for
																											// messages
		columnId.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Integer>("id")); // set values for id
		columnStatus.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("status")); // set values
																											// for
																											// status
		/*
		 * //tests: columnStage.setCellValueFactory(new
		 * PropertyValueFactory<requirmentForTable, String>("stage"));
		 * columnDueTime.setCellValueFactory(new
		 * PropertyValueFactory<requirmentForTable, String>("dueTime"));
		 */

		columnStage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("stage"));
		columnDueTime.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("dueTime"));
		// tblviewRequests.setItems(tableData);

	}

	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		tableData = FXCollections.observableArrayList(InspectorController.requirmentForTableList(reqList));
		tblviewRequests.setItems(tableData);

	}

	// functions for gui:
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

	}

	public void dueTimeApprove(ActionEvent event) throws Exception {

	}

	public void extensionApprove(ActionEvent event) throws Exception {

	}

	public void closeRequest(ActionEvent event) throws Exception {

	}

	public void onRequirmentClicked(ActionEvent event) throws Exception {
		requirmentForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
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
			btnFreezeUnfreeze.setText("Unfreeze");
			break;
		case suspended:
			btnFreezeUnfreeze.setDisable(false);
			btnFreezeUnfreeze.setText("freeze");
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
		case supervisorAction:
			btnDueTimeApprove.setDisable(true);
			btnExtensionApprove.setDisable(true);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(true);
		case supervisorAllocation:
			btnDueTimeApprove.setDisable(true);
			btnExtensionApprove.setDisable(true);
			btnCloseRequest.setDisable(true);
			btnRoleApprove.setDisable(false);
			switch (selectedReq.getStage().getCurrentStage()) {
			case meaningEvaluation:
				btnRoleApprove.setText("Estimator Approve");
				break;
			case execution:
				btnRoleApprove.setText("Execution Leader Approve");
				break;
			default:
				btnRoleApprove.setText("Role Approve");
				break;
			}
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

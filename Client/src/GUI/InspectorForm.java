package GUI;

import Controllers.InspectorController;
import Entity.ChangeRequest;
import Entity.EstimatorReport;
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
import java.time.LocalDate;
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
	//fxml for tab pan:
	@FXML
    private TextArea requestDetails;

    @FXML
    private TextArea requestReason;

    @FXML
    private TextArea comment;

    @FXML
    private TextField informationSystem;

    @FXML
    private TableView<?> tblViewDocuments;

    @FXML
    private TableColumn<?, ?> columnFileName;

    @FXML
    private TableColumn<?, ?> columnFileSize;

    @FXML
    private Button donwloadBtn;

    @FXML
    private TextArea location;

    @FXML
    private TextArea changeDescription;

    @FXML
    private TextArea desiredResult;

    @FXML
    private TextArea constraints;

    @FXML
    private TextArea risks;

    @FXML
    private TextField dueTimeEstimate;

    @FXML
    private TextField start1;

    @FXML
    private TextField end1;

    @FXML
    private CheckBox extension1;

    @FXML
    private TextArea explain1;

    @FXML
    private TextField start2;

    @FXML
    private TextField end2;

    @FXML
    private CheckBox extension2;

    @FXML
    private TextArea explain2;

    @FXML
    private TextField start3;

    @FXML
    private TextField end3;

    @FXML
    private CheckBox extension3;

    @FXML
    private TextArea explain3;

    @FXML
    private TextField start4;

    @FXML
    private TextField end4;

    @FXML
    private CheckBox extension4;

    @FXML
    private TextArea explain4;

    @FXML
    private TextField start5;

    @FXML
    private TextField end5;

    @FXML
    private CheckBox extension5;

    @FXML
    private TextArea explain5;
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
	
	// not fxml vars:
	RequestTableView table; // make adaptable class for table view.
	private static Stage popupWindow;
	public static Stage inspectorWindow;
	private requirementForTable selectedReq;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		table=new RequestTableView(tblViewRequests,columnId,columnStatus,columnStage,columnDueTime,columnMessage);
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
		 //set details into tab pane:
		// requestDetails.setText(InspectorController.selectedRequest.);
		 requestReason.setText(InspectorController.selectedRequest.getChangeReason());
		 comment.setText(InspectorController.selectedRequest.getComment());
		 informationSystem.setText(InspectorController.selectedRequest.getSystem());
		 if (InspectorController.selectedRequest.getProcessStage().getEstimatorReport()!=null) {
			 EstimatorReport estimatorReport = InspectorController.selectedRequest.getProcessStage().getEstimatorReport();
			 location.setText(estimatorReport.getlocation());
			 changeDescription.setText(estimatorReport.getChangeDescription());
			 desiredResult.setText(estimatorReport.getResultingResult());
			 constraints.setText(estimatorReport.getConstraints());
			 risks.setText(estimatorReport.getRisks());
			 dueTimeEstimate.setText(estimatorReport.getTimeEstimate().toString());
		 }
		 String[] explanations = selectedReq.getStage().getAllExtensionExplanation();
		LocalDate[][] allDates = selectedReq.getStage().getDates();
		 start1.setText("");
		 end1.setText("");
		 explain1.setText("");
		 extension1.setSelected(false);
		 start2.setText("");
		 end2.setText("");
		 explain2.setText("");
		 extension2.setSelected(false);
		 start3.setText("");
		 end3.setText("");
		 explain3.setText("");
		 extension3.setSelected(false);
		 start4.setText("");
		 end4.setText("");
		 explain4.setText("");
		 extension4.setSelected(false);
		 start5.setText("");
		 end5.setText("");
		 explain5.setText("");
		 extension5.setSelected(false);
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[0]==2) {
		 start1.setText(allDates[0][0].toString());
		 end1.setText(allDates[0][2].toString());
		 explain1.setText(explanations[0]);
		 extension1.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[1]==2) {
		 start2.setText(allDates[1][0].toString());
		 end2.setText(allDates[1][2].toString());
		 explain2.setText(explanations[1]);
		 extension2.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[2]==2) {
		 start3.setText(allDates[2][0].toString());
		 end3.setText(allDates[2][2].toString());
		 explain3.setText(explanations[2]);
		 extension3.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[3]==2) {
		 start4.setText(allDates[3][0].toString());
		 end4.setText(allDates[3][2].toString());
		 explain4.setText(explanations[3]);
		 extension4.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[4]==2) {
		 start5.setText(allDates[4][0].toString());
		 end5.setText(allDates[4][2].toString());
		 explain5.setText(explanations[4]);
		 extension5.setSelected(true);
		}
		 
		if (selectedReq == null)
			return;
		btnGetDetails.setDisable(false);

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
}

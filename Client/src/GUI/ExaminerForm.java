package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import Controllers.ExaminerController;
import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


public class ExaminerForm extends StageSupervisorForm {


	    @FXML
	    private TextArea location,changeDescription,desiredResult,constraints,risks;
	    
	    @FXML
	    private TextField dueTimeEstimate,createdDate,createdBy;

	    @FXML
	    private Button btnSetDueTime,btnWriteReport,btnAskForTimeExtension;
		private RequestTableView table;
		public void onRequestClicked(MouseEvent event) {

			requirementForTable selectedReq = table.onRequirementClicked(event);
			if (selectedReq == null)
				return;
			ExaminerController.setSelectedRequest(selectedReq);

			//set details for initiator:
			location.setText(ExaminerController.selectedRequest.getBaseforChange());
			changeDescription.setText(ExaminerController.selectedRequest.getChangeReason());
			desiredResult.setText(ExaminerController.selectedRequest.getComment());
			constraints.setText(ExaminerController.selectedRequest.getSystem());
			risks.setText(ExaminerController.selectedRequest.getSystem());
			createdDate.setText(ExaminerController.selectedRequest.getStartDate().toString());
			createdBy.setText(ExaminerController.selectedRequest.getInitiator().getTheInitiator().getUserName());
			//end initiator details.
			btnAskForTimeExtension.setDisable(false);
			switch (selectedReq.getStage().getCurrentSubStage()) {
				case determiningDueTime:
					btnSetDueTime.setDisable(false);
					btnWriteReport.setDisable(true);
					break;
				case supervisorAction:
					btnWriteReport.setDisable(false);
					btnSetDueTime.setDisable(true);
					break;
				default:
					btnAskForTimeExtension.setDisable(true);
					btnWriteReport.setDisable(true);
					btnSetDueTime.setDisable(true);
					break;
			}
		}

	    @FXML
		public void filterRequests(ActionEvent event) {
	    	ExaminerController.filterRequests(((MenuItem) event.getSource()));
	    }

	    @FXML
	    void onRequestClicked(ActionEvent event) {

	    }

	    @FXML
		public void setDueTimeClicked(ActionEvent event) {

	    }

	    @FXML
	    void writeReportClicked(ActionEvent event) {
	    	try {
				popupWindow("/GUI/PopUpWindows/EstimateReport.fxml");
			} catch (IOException e) {
				//TODO Warning:(113, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
				System.out.println(e.getCause());
			}
	    }


	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}
	
	//TODO: the following  methods are from the class diagram:  
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		icmForm = this;
		ClientLauncher.client.setClientUI(icmForm);
		table = new RequestTableView(tblView, idColumn, statusColumn, null, dueTimeColumn, messageColumn);
	}
	@Override
	public ChangeRequest getSelectedReq() {
		
		return ExaminerController.selectedRequest;
	}

	@Override
	public StageSupervisorController getController() {
		
		return new ExaminerController();
	}

	@Override
	public IcmForm getIcmForm() {
		
		return this;
	}

}

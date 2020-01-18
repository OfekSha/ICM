package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import Controllers.ExaminerController;
import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.EstimatorReport;
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


/**
 * This class is for the examiner window.
 * @see ExaminerController
 * @see StageSupervisorForm
 * @see StageSupervisorController
 *
 */
public class ExaminerForm extends StageSupervisorForm {


	    @FXML
	    private TextArea location,changeDescription,desiredResult,constraints,risks;
	    
	    @FXML
	    private TextField dueTimeEstimate,createdDate,createdBy;

	    @FXML
	    private Button btnSetDueTime,btnApprove,btnFailed,btnAskForTimeExtension;
		private RequestTableView table;
		public void onRequestClicked(MouseEvent event) {
			
			requirementForTable selectedReq = table.onRequirementClicked(event);
			if (selectedReq == null)
				return;
			ExaminerController.setSelectedRequest(selectedReq);
			//Estimtor report details:
			EstimatorReport report = ExaminerController.selectedRequest.getProcessStage().getEstimatorReport();
			dueTimeEstimate.setText(String.valueOf(report.getTimeEstimate()));
			location.setText(report.getlocation());
			changeDescription.setText(report.getChangeDescription());
			desiredResult.setText(report.getResultingResult());
			constraints.setText(report.getConstraints());
			risks.setText(report.getRisks());
			//End Estimator report details.
			createdDate.setText(ExaminerController.selectedRequest.getStartDate().toString());
			createdBy.setText(ExaminerController.selectedRequest.getInitiator().getTheInitiator().getUserName());
			
			btnAskForTimeExtension.setDisable(!(ExaminerController.checkExtension(ExaminerController.selectedRequest)));
			switch (selectedReq.getStage().getCurrentSubStage()) {
				case determiningDueTime:
					btnSetDueTime.setDisable(false);
					btnFailed.setDisable(true);
					btnApprove.setDisable(true);
					break;
				case supervisorAction:
					btnApprove.setDisable(false);
					btnFailed.setDisable(false);
					btnSetDueTime.setDisable(true);
					break;
				default:
					btnAskForTimeExtension.setDisable(true);
					btnApprove.setDisable(true);
					btnFailed.setDisable(true);
					btnSetDueTime.setDisable(true);
					break;
			}
		}

	    @FXML
		public void filterRequests(ActionEvent event) {
	    	controller.filterRequests(((MenuItem) event.getSource()));
	    }

	    @FXML
		public void setDueTimeClicked(ActionEvent event) {

	    }

	    @FXML
	    void FailClicked(ActionEvent event) {
	    	try {
				popupWindow("/GUI/PopUpWindows/FailReport.fxml");
			} catch (IOException e) {
				//TODO Warning:(113, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
				System.out.println(e.getCause());
			}
	    }
	    @FXML
	    void Approve(ActionEvent event) {
	    	
	    }

	@Override
	public void getFromServer(Object message) {
		controller.messageFromServer(message);
		table.setData(ExaminerController.requests);

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

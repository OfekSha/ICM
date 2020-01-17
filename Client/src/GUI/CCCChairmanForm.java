package GUI;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Controllers.ChairmanController;
import Controllers.EstimatorController;
import Controllers.ExaminerController;
import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CCCChairmanForm extends StageSupervisorForm  implements Initializable, IcmForm {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnExit;

    @FXML
    private MenuButton menubtnWatch;

    @FXML
    private MenuItem DueTimeFilter;

    @FXML
    private MenuItem ReportFilter;

    @FXML
    private TableView<requirementForTable> tblView;

    @FXML
    private TableColumn<requirementForTable, String> idColumn;

    @FXML
    private TableColumn<requirementForTable, Object> statusColumn;

    @FXML
    private TableColumn<requirementForTable, Object> dueTimeColumn;

    @FXML
    private TableColumn<requirementForTable, String> messageColumn;

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
    private TextField createdDate;

    @FXML
    private TextField createdBy;

    @FXML
    private Button btnApprove;

    @FXML
    private Button btnDissapprove;

    @FXML
    private Button btnAskForDetails;

    @FXML
    private Button btnAppointAnExaminer;

    @FXML
    void ApproveExecution(ActionEvent event) {

    private RequestTableView table;

    @FXML
    void ApproveExecution(ActionEvent event) {

    }
    
    @FXML
    void DissaproveExecution(ActionEvent event) {

    }


    @FXML
    void askForDetails(ActionEvent event) {

    }

	    @FXML
		public void filterRequests(ActionEvent event) {
	    	controller.filterRequests(((MenuItem) event.getSource()));
	    }

	    @FXML
		protected void onRequestClicked(MouseEvent event) {
	    	requirementForTable selectedReq = table.onRequirementClicked(event);
			if (selectedReq == null)
				return;
			controller.setSelectedRequest(selectedReq);

			//set details for initiator:
			location.setText(controller.selectedRequest.getBaseforChange());
			changeDescription.setText(controller.selectedRequest.getChangeReason());
			desiredResult.setText(controller.selectedRequest.getComment());
			constraints.setText(controller.selectedRequest.getSystem());
			risks.setText(controller.selectedRequest.getSystem());
			createdDate.setText(controller.selectedRequest.getStartDate().toString());
			createdBy.setText(controller.selectedRequest.getInitiator().getTheInitiator().getUserName());
			//end initiator details.
			switch (selectedReq.getStage().getCurrentSubStage()) {
				case supervisorAction:
					btnApprove.setDisable(false);
					btnDissapprove.setDisable(false);
					btnAskForDetails.setDisable(false);
					btnAppointAnExaminer.setDisable(true);
					break;
				case supervisorAllocation:
					btnApprove.setDisable(true);
					btnDissapprove.setDisable(true);
					btnAskForDetails.setDisable(true);
					btnAppointAnExaminer.setDisable(false);
					break;
				default:
					btnApprove.setDisable(true);
					btnDissapprove.setDisable(true);
					btnAskForDetails.setDisable(true);
					btnAppointAnExaminer.setDisable(true);
					break;
			}
		}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		icmForm = this;
		ClientLauncher.client.setClientUI(icmForm);
		table = new RequestTableView(tblView, idColumn, statusColumn, null, dueTimeColumn, messageColumn);
	}

	@Override
	public void getFromServer(Object message) {
		controller.messageFromServer(message);
		table.setData(ChairmanController.requests);

	}

	//TODO: the following  methods are from the class diagram:

	@Override
	public StageSupervisorController getController() {
		// TODO Auto-generated method stub
		return new ChairmanController();
	}

	@Override
	public IcmForm getIcmForm() {
		// TODO Auto-generated method stub
		return this;
	}


	@Override
	public ChangeRequest getSelectedReq() {
		// TODO Auto-generated method stub
		return StageSupervisorController.selectedRequest;
	}

}

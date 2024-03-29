package GUI;

import Controllers.EstimatorController;
import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class for the form of estimator it is extends of StageSupervisorForm
 * @see StageSupervisorForm
 *
 */
public class EstimatorMainForm extends StageSupervisorForm {

	@FXML
	public DocumentTableForDownloadsForm DocumentTableController; // the document table with download capability

	@FXML
	public Button btnWriteReport;

	// initiator details:
	@FXML
	private TextArea baseForChange,RequestDetails,RequestReason,Comment;

	@FXML
	private TextField system,createdDate,createdBy;


	// end initiator details.


	@FXML
	private void writeReportClicked(ActionEvent event) {
		try {
			popupWindow("/GUI/PopUpWindows/EstimateReport.fxml");
		} catch (IOException e) {
			System.out.println(e.getCause());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ClientLauncher.client.setClientUI(icmForm);
		DocumentTableController.initializeDocumentTableView();
		table = new RequestTableView(tblView, idColumn, statusColumn, null, dueTimeColumn, messageColumn);

	}

	@Override
	public void getFromServer(Object message) {
		controller.messageFromServer(message);
		table.setData(EstimatorController.requests);

	}

	@FXML
	public void filterRequests(ActionEvent event) { // get event from the menuItem.
		controller.filterRequests(((MenuItem) event.getSource()));
	}

	@Override
	public void onRequestClicked(MouseEvent event) {

		requirementForTable selectedReq = table.onRequirementClicked(event);
		if (selectedReq == null)
			return;
		EstimatorController.setSelectedRequest(selectedReq);

		// set details for initiator:
		baseForChange.setText(EstimatorController.selectedRequest.getBaseforChange());
		RequestDetails.setText(EstimatorController.selectedRequest.getProblemDescription());
		RequestReason.setText(EstimatorController.selectedRequest.getChangeReason());
		Comment.setText(EstimatorController.selectedRequest.getComment());
		system.setText(EstimatorController.selectedRequest.getSystem());
		createdDate.setText(EstimatorController.selectedRequest.getStartDate().toString());
		createdBy.setText(EstimatorController.selectedRequest.getInitiator().getTheInitiator().getUserName());
		// end initiator details.

		switch (selectedReq.getStage().getCurrentSubStage()) {
		case determiningDueTime:
			btnSetDueTime.setDisable(false);
			btnWriteReport.setDisable(true);
			break;
		case supervisorAction:
			btnAskForTimeExtension
					.setDisable(!(EstimatorController.checkExtension(EstimatorController.selectedRequest)));
			btnWriteReport.setDisable(false);
			btnSetDueTime.setDisable(true);
			break;
		default:
			btnAskForTimeExtension.setDisable(true);
			btnWriteReport.setDisable(true);
			btnSetDueTime.setDisable(true);
			break;
		}
		DocumentTableController.onRequirementTableClick(EstimatorController.selectedRequest);
	}

	@Override
	public ChangeRequest getSelectedReq() {
		return EstimatorController.selectedRequest;
	}

	@Override
	public StageSupervisorController getController() {
		return new EstimatorController();
	}

	@Override
	public IcmForm getIcmForm() {
		// TODO Auto-generated method stub
		return this;
	}

}

package GUI;

import Controllers.EstimatorController;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import GUI.PopUpWindows.DueTimeController;
import GUI.PopUpWindows.GetExtensionController;
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

public class EstimatorMainForm extends UserForm {


	@FXML
	public DocumentTableForDownloadsForm DocumentTableController; // the document table with download capability

	@FXML
	public Button btnSetDueTime;
	@FXML
	public Button btnWriteReport;
	@FXML
	public Button btnAskForTimeExtension;

	// menu button for watch types of requests
	@FXML
	public MenuButton menubtnWatch;

	// menu items of menubtnWatch (the types of request):
	@FXML
	private MenuItem DueTimeFilter;
	@FXML
	private MenuItem ReportFilter;
	//initiator details:
	@FXML
	private TextArea RequestDetails;

	@FXML
	private TextArea RequestReason;

	@FXML
	private TextArea Comment;

	@FXML
	private TextField system;

	@FXML
	private TextField createdDate;

	@FXML
	private TextField createdBy;


	//end initiator details.

	private RequestTableView table;

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

	public static Stage popupWindow;

	@FXML
	public void setDueTimeClicked(ActionEvent event) {
		try {
			// not work because the fxml work only with execution leader.
			DueTimeController.processStage = EstimatorController.selectedRequest.getProcessStage();
			popupWindow("/GUI/PopUpWindows/DeterminingDueTime.fxml", event);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//TODO Warning:(91, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
			System.out.println(e.getCause());
		}
	}

	@FXML
	public void askExtensionClicked(ActionEvent event) {
		try {
			// not work because the fxml work only with execution leader.
			GetExtensionController.processStage = EstimatorController.selectedRequest.getProcessStage();
			popupWindow("/GUI/PopUpWindows/GetExtension.fxml", event);
		} catch (IOException e) {
			//TODO Warning:(103, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
			System.out.println(e.getCause());
		}
	}

	@FXML
	private void writeReportClicked(ActionEvent event) {
		try {
			popupWindow("/GUI/PopUpWindows/EstimateReport.fxml", event);
		} catch (IOException e) {
			//TODO Warning:(113, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
			System.out.println(e.getCause());
		}
	}

	private void popupWindow(String target, ActionEvent event) throws IOException {
		popupWindow = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource(target));
		Scene scene = new Scene(root);
		popupWindow.setScene(scene);
		popupWindow.initModality(Modality.APPLICATION_MODAL);
		popupWindow.show();
		EstimatorMainForm icmForm = this;

		// what happened when close window from out or from stage.close / stage.hide
		// method
		popupWindow.setOnCloseRequest(windowEvent ->  // close from out (alt +f4)
				ClientLauncher.client.setClientUI(icmForm));
		// stage.close / stage.hide method
		popupWindow.setOnHidden(we -> ClientLauncher.client.setClientUI(icmForm));
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ClientLauncher.client.setClientUI(this);
		table = new RequestTableView(tblView, idColumn, statusColumn, null, dueTimeColumn, messageColumn);
		DocumentTableController.initializeDocumentTableView();

	}

	@Override
	public void getFromServer(Object message) {
		EstimatorController.messageFromServer(message);
		table.setData(EstimatorController.requests);

	}
	@FXML
	public void filterRequests(ActionEvent event) { // get event from the menuItem.
		EstimatorController.filterRequests(((MenuItem) event.getSource()));
	}

	public void onRequestClicked(MouseEvent event) {

		requirementForTable selectedReq = table.onRequirementClicked(event);
		if (selectedReq == null)
			return;
		EstimatorController.setSelectedRequest(selectedReq);

		//set details for initiator:
		RequestDetails.setText(EstimatorController.selectedRequest.getBaseforChange());
		RequestReason.setText(EstimatorController.selectedRequest.getChangeReason());
		Comment.setText(EstimatorController.selectedRequest.getComment());
		system.setText(EstimatorController.selectedRequest.getSystem());
		createdDate.setText(EstimatorController.selectedRequest.getStartDate().toString());
		createdBy.setText(EstimatorController.selectedRequest.getInitiator().getTheInitiator().getUserName());
		//end initiator details.
		btnAskForTimeExtension.setDisable(!(EstimatorController.checkExtension(EstimatorController.selectedRequest)));
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
		DocumentTableController.onRequirementTableClick(EstimatorController.selectedRequest);
	}
}

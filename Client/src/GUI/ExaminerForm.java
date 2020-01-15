package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import Controllers.ExaminerController;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import GUI.PopUpWindows.GetExtensionController;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExaminerForm extends UserForm {


		@FXML
	    private MenuButton menubtnWatch;

	    @FXML
	    private MenuItem DueTimeFilter,ReportFilter;

	    @FXML
		private TableView<requirementForTable> tblView;
		@FXML
		private TableColumn<requirementForTable, String> idColumn,messageColumn;
		@FXML
		private TableColumn<requirementForTable, Object> statusColumn,dueTimeColumn;

	    @FXML
	    private TextArea location,changeDescription,desiredResult,constraints,risks;
	    
	    @FXML
	    private TextField dueTimeEstimate,createdDate,createdBy;

	    @FXML
	    private Button btnSetDueTime,btnWriteReport,btnAskForTimeExtension;
		private RequestTableView table;

		private void popupWindow(String target, ActionEvent event) throws IOException {
			popupWindow = new Stage();
			Parent root = FXMLLoader.load(this.getClass().getResource(target));
			Scene scene = new Scene(root);
			popupWindow.setScene(scene);
			popupWindow.initModality(Modality.APPLICATION_MODAL);
			popupWindow.show();
			ExaminerForm icmForm = this;

			// what happened when close window from out or from stage.close / stage.hide
			// method
			popupWindow.setOnCloseRequest(windowEvent ->  // close from out (alt +f4)
					ClientLauncher.client.setClientUI(icmForm));
			// stage.close / stage.hide method
			popupWindow.setOnHidden(we -> ClientLauncher.client.setClientUI(icmForm));
		}
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
	    void askExtensionClicked(ActionEvent event) {
	    	try {
				// not work because the fxml work only with execution leader.
				GetExtensionController.processStage = ExaminerController.selectedRequest.getProcessStage();
				popupWindow("/GUI/PopUpWindows/GetExtension.fxml", event);
			} catch (IOException e) {
				//TODO Warning:(103, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
				System.out.println(e.getCause());
			}
	    }

	    @FXML
	    void filterRequests(ActionEvent event) {
	    	ExaminerController.filterRequests(((MenuItem) event.getSource()));
	    }

	    @FXML
	    void onRequestClicked(ActionEvent event) {

	    }

	    @FXML
	    void setDueTimeClicked(ActionEvent event) {

	    }

	    @FXML
	    void writeReportClicked(ActionEvent event) {
	    	try {
				popupWindow("/GUI/PopUpWindows/EstimateReport.fxml", event);
			} catch (IOException e) {
				//TODO Warning:(113, 23) Throwable argument 'e.getCause()' to 'System.out.println()' call
				System.out.println(e.getCause());
			}
	    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}
	
	//TODO: the following  methods are from the class diagram:  

	public void ExecApproved() {}
	public void ExecDesapproved() {}

}

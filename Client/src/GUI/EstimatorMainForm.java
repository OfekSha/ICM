package GUI;

import Controllers.EstimatorController;
import Controllers.InspectorController;
import Controllers.InspectorController.requirementForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class EstimatorMainForm extends UserForm implements IcmForm {

		@FXML
		public Button btnGetDetails;
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
		private MenuItem NeedToWriteAReport;
		@FXML
		private MenuItem NeedToSetDueTime;
		

		@FXML
		public TableView<InspectorController.requirementForTable> tblviewRequests;
		// table columns:
		@FXML
		public TableColumn<requirementForTable, String> columnId;
		@FXML
		public TableColumn<InspectorController.requirementForTable, Object> columnStatus;
		@FXML
		public TableColumn<InspectorController.requirementForTable, Object> columnDueTime;
		
		@FXML
		public TableColumn<requirementForTable, String> columnMessage;
		
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			ClientLauncher.client.setClientUI(this);
			initializeTableView();
		}
		public void getFromServer(Object message) {
			EstimatorController.messageFromServer(message);
		}
		
		private void initializeTableView() {
			columnMessage.setCellValueFactory(new PropertyValueFactory<InspectorController.requirementForTable, String>("message")); // set
																												// values
																												// for
																												// messages
			columnId.setCellValueFactory(new PropertyValueFactory<InspectorController.requirementForTable, String>("id")); // set values for id
			columnStatus.setCellValueFactory(new PropertyValueFactory<requirementForTable, Object>("status")); // set values
																												// for
																												// status
			columnDueTime.setCellValueFactory(new PropertyValueFactory<InspectorController.requirementForTable, Object>("dueTime"));

		}
		public void filterRequests(ActionEvent event) { // get event from the menuItem.
			EstimatorController.filterRequests(((MenuItem) event.getSource()));
		}
		public void onRequestClicked(MouseEvent event) {

			requirementForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
			if (selectedReq == null)
				return;
			EstimatorController.setSelectedRequest(selectedReq);
			btnGetDetails.setDisable(false);
			btnAskForTimeExtension.setDisable(false);
			switch (selectedReq.getStage().getCurrentSubStage()) {
			case determiningDueTime: 
				btnSetDueTime.setDisable(false);
				btnWriteReport.setDisable(true);
				break;
			case supervisorAction:
				btnWriteReport.setDisable(false);
				btnSetDueTime.setDisable(true);
			default:
				btnAskForTimeExtension.setDisable(true);
				btnWriteReport.setDisable(true);
				btnSetDueTime.setDisable(true);
				break;
			}
			
		}
		
}

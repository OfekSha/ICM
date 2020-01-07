package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.EstimatorController;
import Controllers.InspectorController.requirmentForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
		public TableView<requirmentForTable> tblviewRequests;
		// table columns:
		@FXML
		public TableColumn<requirmentForTable, String> columnId;
		@FXML
		public TableColumn<requirmentForTable, Object> columnStatus;
		@FXML
		public TableColumn<requirmentForTable, Object> columnDueTime;
		
		@FXML
		public TableColumn<requirmentForTable, String> columnMessage;
		
		
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
			columnMessage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("message")); // set
																												// values
																												// for
																												// messages
			columnId.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("id")); // set values for id
			columnStatus.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("status")); // set values
																												// for
																												// status
			columnDueTime.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("dueTime"));

		}
		public void filterRequests(ActionEvent event) { // get event from the menuItem.
			EstimatorController.filterRequests(((MenuItem) event.getSource()));
		}
		public void onRequestClicked(MouseEvent event) {

			requirmentForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
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

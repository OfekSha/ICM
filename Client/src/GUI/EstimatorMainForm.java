package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import Controllers.InspectorController.requirmentForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
		// table colums:
		@FXML
		public TableColumn<requirmentForTable, String> columnId;
		@FXML
		public TableColumn<requirmentForTable, Object> columnStatus;
		@FXML
		public TableColumn<requirmentForTable, Object> columnStage;
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
		
		private void initializeTableView() {
			columnMessage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("message")); // set
																												// values
																												// for
																												// messages
			columnId.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("id")); // set values for id
			columnStatus.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("status")); // set values
																												// for
																												// status
			columnStage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("stage"));
			columnDueTime.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("dueTime"));

		}
}

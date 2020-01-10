package GUI;

import Controllers.EstimatorController;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
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
		private MenuItem DueTimeFilter;
		@FXML
		private MenuItem ReportFilter;
		
		private RequestTableView table;
		
		@FXML
		private TableView tblView;
		@FXML
		private TableColumn idColumn;
		@FXML
		private TableColumn statusColumn;
		@FXML
		private TableColumn dueTimeColumn;
		@FXML
		private TableColumn messageColumn;
		
		public static Stage popupWindow;
		@FXML
		public void setDueTimeClicked(ActionEvent event) {
			try {
				// not work because the fxml work only with execution leader.
				popupWindow("DeterminingDueTime.fxml",event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getCause());
			}
		}
		@FXML
		public void askExtensionClicked(ActionEvent event) {
			try {
				// not work because the fxml work only with execution leader.
				popupWindow("GetExtension.fxml",event);
			} catch (IOException e) {
				System.out.println(e.getCause());
			}
		}
		@FXML
		private void writeReportClicked(ActionEvent event) {
			try {
				popupWindow("EstimateReport.fxml",event);
			} catch (IOException e) {
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
			table=new RequestTableView(tblView,idColumn,statusColumn,null,dueTimeColumn,messageColumn);
			
		}
		@Override
		public void getFromServer(Object message) {
			EstimatorController.messageFromServer(message);
			table.setData(EstimatorController.requests);
			
		}
		public void filterRequests(ActionEvent event) { // get event from the menuItem.
			EstimatorController.filterRequests(((MenuItem) event.getSource()));
		}
		public void onRequestClicked(MouseEvent event) {

			requirementForTable selectedReq = table.onRequirementClicked(event);
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

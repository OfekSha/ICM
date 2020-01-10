package GUI;

import Controllers.SubmitRequestController;
import Controllers.SubmitRequestController.DocumentForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubmitRequestForm extends UserForm implements Initializable, IcmForm {
//TextArea
	@FXML
	public TextArea taBaseForChange;
	@FXML
	public TextArea taRequestDetails;
	@FXML
	public TextArea taRequestReason;
	@FXML
	public TextArea taComment;
	//Button
	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnAddFile;
	
	@FXML
	private Button btnRomoveFile;
	//ComboBox
	@FXML
	private ComboBox<String> cmbSystems;

	
	
	// table stuff
	@FXML
	public TableView<DocumentForTable> tblViewDocuments;
	// table columns:
		@FXML
		public TableColumn<DocumentForTable, String> columnFileName;
		@FXML
		public TableColumn<DocumentForTable, String> columnFileSize;
		
		
		private ObservableList<DocumentForTable> tableData;
	// the  forms controller 
		private SubmitRequestController submitRequestController;
	/**
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		submitRequestController =new SubmitRequestController();
		setSystemsComboBox();
		initializeTableView();
	}

	/** adding the relevant systems to the combo box
	 * 
	 */
	public void setSystemsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		al.add("ICM");
		al.add("Moodle");
		al.add("Information Station");
		ObservableList<String> list = FXCollections.observableArrayList(al);
		cmbSystems.setItems(list);
	}

	/** sends request to server on click the Submit button <p>
	 *  if some fields are empty sends an alert 
	 */
	public void getRequestData() {
		 if (submitRequestController.getSubmition( taRequestDetails.getText(),  taRequestReason.getText(), taComment.getText(), getSys(),taRequestDetails.getText(),user) ) {
		taRequestDetails.clear();
		taRequestReason.clear();
		taComment.clear();
		cmbSystems.setValue("");
		tableData = FXCollections.observableArrayList(submitRequestController.DocumentForTableList());
		tblViewDocuments.setItems(tableData);
		 }
		 else {
		String missing =submitRequestController.AppendEmpty( taRequestDetails.getText(),taRequestReason.getText(),getSys(),taRequestDetails.getText());
		alertWindowLauncher(AlertType.ERROR,"ERROR Dialog","Error:empty fields","The following fields must not be empty:"+missing);
		 }
	}
	
	/** Attaches a file to request<p>
	 * uses the FileChooser to get the file
	 * @throws IOException ?
	 */
	public void AddFile() throws IOException {

		FileChooser fileChooser = new FileChooser();
		File file =fileChooser.showOpenDialog(null);
		if(file!=null) {
		
		if (!submitRequestController.AddThefile(file))
			alertWindowLauncher(AlertType.ERROR, "ERROR Dialog", "Error:file to large",
					"file must be smaller then 16mb");
		tableData = FXCollections.observableArrayList(submitRequestController.DocumentForTableList());
		tblViewDocuments.setItems(tableData);
		}
	}
	
	/** setting up the table columns 
	 * 
	 */
	private void initializeTableView() {
																									// messages
		columnFileName.setCellValueFactory(new PropertyValueFactory<>("name")); // set values for id
		columnFileSize.setCellValueFactory(new PropertyValueFactory<>("size")); // set values
	
	}
	public void removeFile() {
		DocumentForTable selectedDoc = tblViewDocuments.getSelectionModel().getSelectedItem();
		if(selectedDoc!=null) {
		tblViewDocuments.getItems().remove(selectedDoc);
		submitRequestController.removeDoc(selectedDoc.gettheDoc());
		}
	}
	
	
	/** logging out of the system and going to the logIn screen <p>
	 * 	 override addition is asking if the user is sure when started filling some of the fields
	 */
	@Override
	public void LogOutButton(ActionEvent event) throws Exception {
		if (!(taRequestDetails.getText().equals("")) || !(taRequestReason.getText().equals(""))
				|| !((getSys()).equals(""))) {
			if (areYouSureAlert(AlertType.CONFIRMATION, "Living the submit form",
					"Are you ok with  stoping the request submition?", "The detials you enterd will not be saved")) {
				logingOut(event);
			}

		} else {
			logingOut(event);
		}
	}
	
	/** The back button takes the user back to main menu 
	 * override  addition is asking if the user is sure when started filling some of the fields
	 */
	@Override
	public void MainScene(ActionEvent event) throws Exception {

		if (!(taRequestDetails.getText().equals("")) || !(taRequestReason.getText().equals(""))
				|| !((getSys()).equals(""))) {
			if (areYouSureAlert(AlertType.CONFIRMATION, "Living the submit form",
					"Are you ok with  stoping the request submition?", "The detials you enterd will not be saved")) {
				NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, true);
			}

		} else {
			NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, true);
		}
	}

/** 
 * @return  the  pressed system  String form  combo box
 */
private String getSys() {
	String sys = "";
	Object temp = cmbSystems.getSelectionModel().getSelectedItem();
	if (temp != null) sys = temp.toString();
	return sys;
}

/** Telling the server the user logged out and lunching the log in screen 
 * @param event ?
 * @throws Exception ?
 */
private  void logingOut (ActionEvent event) throws Exception  {
	setUserLogOff();
	user = null;
	NextWindowLauncher(event, "/GUI/LogInForm.fxml", this, true);
}
	
}



package GUI;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import Controllers.SubmitRequestController;

public class SubmitRequestForm extends UserForm implements Initializable, IcmForm {

	@FXML
	public TextArea taRequestDetails;
	@FXML
	public TextArea taRequestReason;
	@FXML
	public TextArea taComment;
	@FXML
	private Button btnSubmit;
	@FXML
	private Button btnAddFile;
	@FXML
	private ComboBox<String> cmbSystems;

	
	 private SubmitRequestController submitRequestController;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		submitRequestController =new SubmitRequestController();
		setSystemsComboBox();
	}

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
		 if (submitRequestController.getSubmition( taRequestDetails.getText(),  taRequestReason.getText(), taComment.getText(), getSys(),user) ) {
		taRequestDetails.clear();
		taRequestReason.clear();
		taComment.clear();
		cmbSystems.setValue("");
		 }
		 else {
		String missing =submitRequestController.AppendEmpty( taRequestDetails.getText(),taRequestReason.getText(),getSys());
		alertWindowLauncher(AlertType.ERROR,"ERROR Dialog","Error:empty fields","The following fields must not be empty:"+missing);
		 }
	}
 // TODO move are  you sure to difreent mehod
	@Override
	public void LogOutButton(ActionEvent event) throws Exception {
		if (!(taRequestDetails.getText().equals("")) || !(taRequestReason.getText().equals(""))
				|| !((getSys()).equals(""))) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("log out");
			alert.setHeaderText("You are sure you want to stope the request submition?");
			alert.setContentText("the detials you enterd will not be saved");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) { // the user pressed ok
				setUserLogOff();
				user = null;
				// ClientLauncher.client = null; // NextWindowLauncher - needs clinte
				// lunching main menu
				NextWindowLauncher(event, "/GUI/LogInForm.fxml", this, true);
			} else
				alert.close();
		}
	}



/** getting the system form its combobox
 * @return
 */
private String getSys() {
	String sys = "";
	Object temp = cmbSystems.getSelectionModel().getSelectedItem();
	if (temp != null) sys = temp.toString();
	return sys;
}
	
}



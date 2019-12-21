package GUI;

import Entity.Requirement;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class WatchRequestForm extends UserForm implements IcmForm {
	@FXML
	public ComboBox<String> cmbRequestID;
	public TextField tfRequestStatus;
	public TextArea taRequestDetails;
	public TextArea taRequestReason;
	public TextArea taComment;


	@Override
	public void getFromServer(Object message) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}



//	///** Variables */
//	//private static ArrayList<Requirement> ReqListForClient = null;
//
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		// request DB
//		//getRequests();
//	//	setRequests();
//	}
//
//	private void setRequests() {
//	/*	clientRequestFromServer commend = new clientRequestFromServer(getAll);
//		ClientLauncher.client.handleMessageFromClientUI(commend);
//
//		ArrayList<String> al = new ArrayList<>();
//		for (Requirement req : ReqListForClient) {
//			al.add(Integer.toString((req.getID())));
//		}
//		ObservableList<String> listFor_cmbRequests = FXCollections.observableArrayList(al);
//		cmbRequestID.setItems(listFor_cmbRequests); */
//	}
//
//	public void RequestsComboBoxUsed() {
//		/* int s = Integer.parseInt(cmbRequestID.getSelectionModel().getSelectedItem());
//		for (Requirement req : ReqListForClient) {
//			if (s == req.getID()) {
//				this.tfRequestStatus.setPromptText((req.getStatus()).name());
//				this.taRequestDetails.setText(req.getRequestDetails());
//				//TODO Implement this in Request Entity
//				//this.taRequestReason.setText(req.getRequestReason());
//				//this.taComment.setText(req.getRequestComment());
//				break;
//			}
//		} */
//	}
}

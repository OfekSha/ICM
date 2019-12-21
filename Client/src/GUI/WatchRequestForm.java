package GUI;

import Entity.Requirement;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WatchRequestForm extends UserForm {
	@FXML
	public ComboBox<String> cmbRequests;
	public TextField tfRequestStatus;
	public TextArea taRequestDetails;
	public TextArea taRequestReason;
	public TextArea taComment;

	//Variables
	//private static ArrayList<Requirement> ReqListForClient = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		setRequestsComboBox();
	}

	private void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		ReqListForClient.forEach(req -> al.add(Integer.toString((req.getID()))));
		ObservableList<String> listFor_cmbRequests = FXCollections.observableArrayList(al);
		cmbRequests.setItems(listFor_cmbRequests);
	}

	public void RequestsComboBoxUsed() {
		int s = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		for (Requirement req : ReqListForClient) {
			if (s == req.getID()) {
				this.tfRequestStatus.setPromptText((req.getStatus()).name());
				this.taRequestDetails.setText(req.getRequestDetails());
				//TODO Implement this in Request Entity
				//this.taRequestReason.setText(req.getRequestReason());
				//this.taComment.setText(req.getRequestComment());
				break;
			}
		}
	}

	@Override
	public void getFromServer(Object message) {
		clientRequestFromServer request = (clientRequestFromServer) message;
		// msg is ArrayLost of Entity.Requirement classes
		ReqListForClient = request.getObj();
		System.out.println("\nMessage from osf.server Received:");
	}
}
package GUI;

import Entity.Requirement;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WatchRequestForm extends UserForm {
	@FXML
	public TextField tfRequestStatus;
	public TextArea taRequestDetails;
	public TextArea taRequestReason;
	public TextArea taComment;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		setRequestsComboBox();
	}

	private void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		ReqListForClient.forEach(req -> al.add(Integer.toString((req.getID()))));
		cmbRequests.setItems(FXCollections.observableArrayList(al));
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
}
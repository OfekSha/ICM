package GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class WatchRequestForm extends UserForm {
	@FXML
	public TextField tfRequestStatus;
	public TextArea taRequestDetails;
	public TextArea taRequestReason;
	public TextArea taComment;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getRequests();
		Platform.runLater(this::setRequestsComboBox);
	}

	/*protected void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		changeRequests.forEach(cR -> al.add(cR.getRequestID()));
		cmbRequests.setItems(FXCollections.observableArrayList(al));
	}*/

	public void RequestsComboBoxUsed() {
		//int s = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		String selected = cmbRequests.getSelectionModel().getSelectedItem();
		changeRequests.forEach(cR -> {
			if (selected.equals(cR.getRequestID())) {
				this.tfRequestStatus.setText(cR.getStatus().name());
				this.taRequestDetails.setText(cR.getProblemDescription());
				this.taRequestReason.setText(cR.getChangeReason());
				this.taComment.setText(cR.getComment());
			}
		});
	}
}
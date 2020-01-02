package GUI;

import GUI.PopUpWindows.DueTimeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Entity.ProcessStage.subStages.determiningDueTime;
import static Entity.ProcessStage.subStages.supervisorAction;

public class ExecutionLeaderForm extends EstimatorExecutorForm {

	@FXML
	public Button btnDueTime;
	public Button btnApprove;
	public Button btnGetExtension;
	public TextArea taExaminerReport;
	public TextArea taInitiatorRequest;

	private String selected;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		getRequests();
		setRequestsComboBox();
	}

	public void RequestsComboBoxUsed() {
		selected = cmbRequests.getSelectionModel().getSelectedItem();
		changeRequests.forEach(cR -> {
			if (selected.equals(cR.getRequestID())) {
				this.taInitiatorRequest.setText(cR.getProblemDescription());
				this.taExaminerReport.setText(cR.getComment());
				DueTimeController.setChangeRequest(cR);
			}
		});
	}

	//TODO: the following  methods are from the class diagram:
	public void getReport() {
	}

	public void openDueTime(ActionEvent actionEvent) throws Exception {
		NextWindowLauncher(actionEvent, "/GUI/PopUpWindows/DeterminingDueTime.fxml", this, false, this);
	}

	public void getExecutionApproved() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Approve performing change");
		alert.setHeaderText("Are you perform requested changes?");
		//alert.setContentText("Choose OK if you approve");

		ButtonType btnApprove = new ButtonType("Approve");
		ButtonType btnCancel = ButtonType.CANCEL;
		alert.getButtonTypes().setAll(btnApprove, btnCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent()) {
			if (result.get() == btnApprove) {
				changeRequests.forEach(cR -> {
					if (selected.equals(cR.getRequestID()) &&
							cR.getProcessStage().getCurrentSubStage().equals(determiningDueTime)) {
						cR.getProcessStage().setCurrentSubStage(supervisorAction);
						//TODO Change status or stage whatever is needed
					}
				});
			}
		}
	}

	public void requestExtension(ActionEvent actionEvent) {

	}

	/*public void getDone() {
		LocalDate dueDate = dpDueTime.getValue();
		changeRequests.forEach(cR -> {
			if (selected.equals(cR.getRequestID()) &&
					cR.getProcessStage().getCurrentSubStage().equals(determiningDueTime)) {
				cR.getProcessStage().addStartDate(dueDate);
				//TODO Something else?
			}
		});
	}*/
}

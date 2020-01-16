package GUI.PopUpWindows;

import java.awt.TextField;
import java.net.URL;
import java.util.ResourceBundle;

import Controllers.InspectorController;
import Controllers.StageSupervisorController;
import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ApproveRoleForm extends AbstractPopUp implements IcmForm {

	@FXML
	private ComboBox<User> cmbITMembers;
	@FXML
	private Button btnApprove;
	@FXML
	private Label title;
	@FXML
	private TextField description;

	public enum Role {
		estimator, executionLeader
	};
	public static StageSupervisorController controller;
	public static Role role;

	@FXML
	public void getApprove(ActionEvent event) {
		InspectorController.changeRole(InspectorController.selectedRequest,
				cmbITMembers.getSelectionModel().getSelectedItem());
		((Stage) btnApprove.getScene().getWindow()).close();
	}

	private ObservableList<User> userList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		switch (role) {
		case estimator:
			title.setText("Approve Estimator for request " + InspectorController.selectedRequest.getRequestID());
			break;
		case executionLeader:
			title.setText("Approve Execution Leader for request " + InspectorController.selectedRequest.getRequestID());
			break;

		}
		ClientLauncher.client.setClientUI(this);
		InspectorController.getInformationEngineers();
	}

	@Override
	public void getFromServer(Object message) {
		controller.messageFromServer(message);
		userList = FXCollections.observableArrayList(InspectorController.informationEngineers);
		cmbITMembers.setItems(userList);
	}
}

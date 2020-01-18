package GUI.PopUpWindows;

import java.awt.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.StageSupervisorController;

import Entity.User;
import Entity.clientRequestFromServer;
import Entity.ProcessStage.subStages;
import Entity.User.collegeStatus;
import Entity.User.icmPermission;
import Entity.clientRequestFromServer.requestOptions;
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
		estimator, executionLeader, examiner
	};

	public static Role role;
	public ArrayList<User> informationEngineers;
	public ArrayList<User> committeeMembers;

	@FXML
	public void getApprove(ActionEvent event) {
		changeRole(cmbITMembers.getSelectionModel().getSelectedItem());
		((Stage) btnApprove.getScene().getWindow()).close();
	}

	private ObservableList<User> userList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		clientRequestFromServer newRequest;
		switch (role) {
		case estimator: // approve estimator
			title.setText("Approve Estimator for request " + StageSupervisorController.selectedRequest.getRequestID());
			newRequest = new clientRequestFromServer(requestOptions.getAllUsersByJob,
					collegeStatus.informationEngineer);
			break;
		case executionLeader: // approve execution leader
			title.setText(
					"Approve Execution Leader for request " + StageSupervisorController.selectedRequest.getRequestID());
			newRequest = new clientRequestFromServer(requestOptions.getAllUsersByJob,
					collegeStatus.informationEngineer);
			break;
		case examiner: // approve examiner.
			title.setText(
					"Approve Examiner for request " + StageSupervisorController.selectedRequest.getRequestID());
			newRequest = new clientRequestFromServer(requestOptions.getUsersByICMPermissions,
					icmPermission.changeControlCommitteeMember);
			break;
		default:
			title.setText("Approve Role for Request ");
			newRequest = null;
			break;
		}
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
		processStage = StageSupervisorController.selectedRequest.getProcessStage();
	}

	/**
	 * This method get messages from server.<br>
	 * <p>
	 * requestOptions:
	 * <li>getAllUsersByJob - for inspector</li>
	 * <li>getUsersByICMPermissions - for chairman</li>
	 * </p>
	 * 
	 * @see clientRequestFromServer
	 */
	@Override
	public void getFromServer(Object message) {
		clientRequestFromServer response = (clientRequestFromServer) message;
		switch (response.getRequest()) {
		case getAllUsersByJob: // for inspector.
			informationEngineers = (ArrayList<User>) ((Object[]) response.getObject())[0];
			userList = FXCollections.observableArrayList(informationEngineers);
			break;
		case getUsersByICMPermissions: // for chairman.
			committeeMembers = (ArrayList<User>) ((Object[]) response.getObject())[0];
			userList = FXCollections.observableArrayList(committeeMembers);
			break;
		}
		cmbITMembers.setItems(userList);
		StageSupervisorController.controller.messageFromServer(message);

	}

	/**
	 * change role of user and set him as supervisor for the selected stage.
	 * 
	 * @param user -User
	 * @see User
	 */
	public static void changeRole(User user) {
		processStage.newStageSupervisor(user); // set user to be supervisor
		processStage.setCurrentSubStage(subStages.determiningDueTime); // next sub stage
		switch (processStage.getCurrentStage()) {
		// give permission to user
		case meaningEvaluation:
			user.getICMPermissions().add(icmPermission.estimator);
			processStage.setStartDate(LocalDate.now());
			break;
		case execution:
			user.getICMPermissions().add(User.icmPermission.executionLeader);
			break;
		default:
			throw new IllegalArgumentException(" can\"t approve role to stage " + processStage.getCurrentStage());
		}
		// send request to server
		sendUpdateForRequest();
		// send user with the new permission to the server.
		clientRequestFromServer newRequest = new clientRequestFromServer(requestOptions.updateUser, user);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);

	}
}

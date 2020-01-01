package GUI;

import Entity.User;
import Entity.User.ICMPermissions;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.EnumSet;
import java.util.ResourceBundle;

/**
 * @author Yonathan
 * 
 *         controller for the mainMenu fxml
 * 
 *         -opens rule specific menues
 * 
 *         -opens user ablites
 */
public class MainMenuForm extends UserForm {

	// buttons
	@FXML
	private Button btnWatchRequest;
	@FXML
	private Button btnMakeAChangeRequest;
	@FXML
	private Button btnInformationTechnologiesDepartmentManager;
	@FXML
	private Button btnInspector;
	@FXML
	private Button btnEstimator;
	@FXML
	private Button btnExecutionLeader;
	@FXML
	private Button btnExaminer;
	@FXML
	private Button btnChangeControlCommitteeChairman;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ClientLauncher.client.setClientUI(this);
		//updating server user is logged in
		user.changeLoginStatus(true);
		//TODO Implement it before use
		Object msg = new clientRequestFromServer(requestOptions.changeInLogIn, user);
		ClientLauncher.client.handleMessageFromClientUI(msg);
		// access according to Permissions
		btnInformationTechnologiesDepartmentManager.setDisable(true);
		btnInspector.setDisable(true);
		btnEstimator.setDisable(true);
		btnExecutionLeader.setDisable(true);
		btnExaminer.setDisable(true);
		btnChangeControlCommitteeChairman.setDisable(true);

		EnumSet<ICMPermissions> Permissions = user.getICMPermissions();
		for (User.ICMPermissions p : Permissions) {
			switch (p) {
				case informationTechnologiesDepartmentManager:
					btnInformationTechnologiesDepartmentManager.setDisable(false);
					break;
				case inspector:
					btnInspector.setDisable(false);
					break;
				case estimator:
					btnEstimator.setDisable(false);
					break;
				case executionLeader:
					btnExecutionLeader.setDisable(false);
					break;
				case examiner:
					btnExaminer.setDisable(false);
					break;
				case changeControlCommitteeChairman:
					btnChangeControlCommitteeChairman.setDisable(false);
					break;
			}
		}
	} // END of initialize();

	// TODO: Load suitable list for each new form
	public void WatchRequest(ActionEvent event) throws Exception {
		//getRequests();
		NextWindowLauncher(event, "/GUI/WatchRequest.fxml", this, true);
	}

	public void MakeAChangeRequest(ActionEvent event) throws Exception {
		 NextWindowLauncher(event, "/GUI/SubmitRequest.fxml", this, true);
	}

	public void InformationTechnologiesDepartmentManagerMenu(ActionEvent event) throws Exception {
		// NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new
		// WatchRequestForm(), true);
	}

	public void InspectorMenu(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/InspectorMain.fxml", this, true);
	}

	public void EstimatorMenu(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/EstimatorMain.fxml", this, true);
	}

	public void ExecutionLeaderMenu(ActionEvent event) throws Exception {
		//getRequests();
		NextWindowLauncher(event, "/GUI/ExecutionLeader.fxml", this, true);
	}

	public void ExaminerMenu(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/Examiner.fxml", this, true);
	}

	public void ChangeControlCommitteeChairmanMenu(ActionEvent event) throws Exception {
		// NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new
		// WatchRequestForm(), true);
	}

}// END of MainMenuForm
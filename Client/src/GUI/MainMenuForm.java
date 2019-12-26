package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import Entity.User;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
		
		/*  @yonathan -work in proggress 
		//TODO: move to sec controller 
		
		
		User.ICMPermissions[] perArr = user.getICMPermissions();
		for( User.ICMPermissions p: perArr) {}
		//
		 */
	}
	

	//TODO: Load suitable list for each new form
	public void WatchRequest(ActionEvent event) throws Exception {
		getRequests();
		NextWindowLauncher(event, "/GUI/WatchRequest.fxml", this,  true);
	}

	public void MakeAChangeRequest(ActionEvent event) throws Exception {
		//NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void InformationTechnologiesDepartmentManagerMenu(ActionEvent event) throws Exception {
		//NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void InspectorMenu(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/InspectorMain.fxml", this,  true);

	}

	public void EstimatorMenu(ActionEvent event) throws Exception {
	//	NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void ExecutionLeaderMenu(ActionEvent event) throws Exception {
		//NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void ExaminerMenu(ActionEvent event) throws Exception {
		//NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new RequestForm(), true);
	}

	public void ChangeControlCommitteeChairmanMenu(ActionEvent event) throws Exception {
		//NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

}//END of MainMenuForm
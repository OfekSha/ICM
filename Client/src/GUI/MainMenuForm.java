package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Yonathan
 * 
 *         controller for the mainMenu fxml
 * 
 *         -opens rule specific menues
 * 
 *         -opens user ablites
 */
public class MainMenuForm extends UserForm implements Initializable, IcmForm {

	// buttons
	/*@FXML
	private Button btnLogout;
	@FXML
	private Button btnExit;*/
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
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void WatchRequest(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/WatchRequest.fxml", this, new WatchRequestForm(), true);
	}

	public void MakeAChangeRequest(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void InformationTechnologiesDepartmentManagerMenue(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void InspectorMenu(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void EstimatorMenu(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void ExecutionleaderMenu(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void ExaminerMenu(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

	public void ChangeControlCommitteeChairmanMenu(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenuForm.fxml", this, new WatchRequestForm(), true);
	}

}//END of MainMenuForm
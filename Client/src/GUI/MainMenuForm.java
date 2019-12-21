package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
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
public class MainMenuForm implements Initializable, IcmForm {

	// buttons
	@FXML
	private Button Log_out;
	@FXML
	private Button exit;
	@FXML
	private Button WatchRequest;
	@FXML
	private Button MakeAChangeEequest;
	@FXML
	private Button InformationTechnologiesDepartmentManager;
	@FXML
	private Button Inspector;
	@FXML
	private Button Estimator;
	@FXML
	private Button ExecutionLeader;
	@FXML
	private Button Examiner;
	@FXML
	private Button ChangeControlCommitteeChairman;

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void ExitBtn() {
		System.exit(0);
	}

	public void LogOutButton(ActionEvent event) throws Exception {
		ClientLauncher.SnextWindowLuncher(event, "/GUI/LogInForm.fxml", this, new LogInForm(), true);
	}

	public void WatchrRequest(ActionEvent event) throws Exception {

	}

	public void MakeAChangeRequest(ActionEvent event) throws Exception {

	}

	public void InformationTechnologiesDepartmentManagerMenue(ActionEvent event) throws Exception {

	}

	public void InspectorMenu(ActionEvent event) throws Exception {
		ClientLauncher.SnextWindowLuncher(event, "/GUI/InspectorMain.fxml", this, new InspectorForm(), true);

	}

	public void EstimatorMenue(ActionEvent event) throws Exception {

	}

	public void ExecutionleaderMenue(ActionEvent event) throws Exception {

	}

	public void ExaminerMenue(ActionEvent event) throws Exception {

	}

	public void ChangeControlCommitteeChairmanMenue(ActionEvent event) throws Exception {

	}

}//END of MainMenuForm
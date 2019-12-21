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

public class InspectorForm extends UserForm implements IcmForm {

	//buttons
	@FXML
	private Button btnExit; // by Yonathan
	@FXML
	private Button btnBack;
	@FXML
	private Button Log_out; // by Yonathan
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}
	
	public void BackButton(ActionEvent event) throws Exception { 
		ClientLauncher.SnextWindowLuncher(event, "/GUI/MainMenu.fxml", this, new MainMenuForm(), true);
	}
	
	public void LogOutButton(ActionEvent event) throws Exception { // by Yonathan
		ClientLauncher.SnextWindowLuncher(event, "/GUI/LogInForm.fxml", this, new LogInForm(), true);
	}
	public void ExitBtn() { //by Yonathan
		System.exit(0);
	}
	
	//TODO: the following  methods are from the class diagram:  
	
	public void getUserId() {}
	public void getRule() {}
	public void getRequestId() {}
	public void getDoneReport() {}
	public void getTalkeApprove() {}

}

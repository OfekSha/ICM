package GUI;

import java.net.URL;
import java.util.ResourceBundle;

<<<<<<< HEAD
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
=======
import WindowApp.IcmForm;
import javafx.fxml.Initializable;
>>>>>>> parent of 38280db... MainForm now extends Initializable, so you dont need to duplicate extends everywhere
import javafx.stage.Stage;

public class UserForm extends MainMenuForm implements MainForm, IcmForm, Initializable {

	@FXML
	private HBox makeRequestBox;

	@FXML
	private Label InformationSystem;

	@FXML
	private Label RequestID;

	@FXML
	private Button watchRequests;

	@FXML
	private TextArea taRequestDetails;

	@FXML
	private TextArea taRequestReason;

	@FXML
	private TextArea taComment;

	@FXML
	private Button btnSubmit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//TODO: the following  methods are from the class diagram:  
	
	public void getRequest() {

	}

	public void watchRequests() {

	}
}

package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.IcmForm;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SubmitRequestForm extends UserForm implements Initializable, IcmForm {

	public TextArea taRequestDetails;
	public TextArea taRequestReason;
	public TextArea taComment;
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


	
	//TODO: the following  methods are from the class diagram:  
public void getRequestData() {}
public void getRequestID() {}
	
}

package GUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FormController implements Initializable {
	//text fields
	@FXML
	private TextField txtInitiator;
	@FXML
	private TextField txtCurrentSituationDetails;
	@FXML
	private TextField txtRequestDetails ;
	@FXML
	private TextField txtStageSupervisor;
	@FXML
	private TextField txtStatus;
	//buttons
	@FXML
	private Button btnExit;
	@FXML
	private Button btnUpdateStatus;
	
	@FXML
	private ComboBox<?> cmbRequests;
	
	
	public void test() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		//setFacultyComboBox();		
	}
	
}

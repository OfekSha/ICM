package GUI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class FormController implements Initializable {
	//text fields
	@FXML
	private TextField txtInitiator;
	@FXML
	private TextArea txtCurrentSituationDetails;
	@FXML
	private TextArea txtRequestDetails ;
	@FXML
	private TextField txtStageSupervisor;

	//buttons
	@FXML
	private Button btnExit;
	@FXML
	private Button btnUpdateStatus;

	//Combo Boxes
	@FXML
	private ComboBox<?> cmbRequests;
	@FXML
	private ComboBox<?> cmbStatus;
	
	
	public void test() {}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		//setFacultyComboBox();		
	}
	
}

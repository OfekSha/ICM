package GUI;
import java.net.URL;
import java.util.ResourceBundle;

import defaultPackage.IForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormController implements Initializable, IForm {
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

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
		primaryStage.setTitle("Update Tool");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	//from IForm
	/*
	 * server answer
	 *
	 *
	 */
	@Override
	public void display(String message) {

	}
}

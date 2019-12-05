package GUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import defaultPackage.ICMform;
import defaultPackage.IForm;
import defaultPackage.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormController implements Initializable, ICMform {
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

	// 
	 private ArrayList<String> names = new ArrayList<String>();
	
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
	
	// 
	private void setFacultyComboBox() {
		ArrayList<String> al = new ArrayList<String>();
		String[] commend = new String[1];				// sending commeds to  server
		commend[1]= "allRequestNum"; 
		Main.client.handleMessageFromClientUI(commend);
		/**TODO:
		 * 1 request the data
		 * 2 enter the request numbers to the list 
		 * 3 save the arraylist for  the intere class (thas itch time some one changes request the data wold be updated)
		 * 4 
		 */
		}



	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}
}

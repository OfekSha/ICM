package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.SecurityController;
import Entity.Requirement;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Yonathan
 *
 * -is the controller for LogInForm.fxml
 * -connects to the server
 */
public class LogInForm extends UserForm {

	//Variables
	private static ArrayList<Requirement> ReqListForClient = null; //TO DO : change to whatever entity will have the username and password
	//Text fields
	@FXML
	private TextField tfIP;
	@FXML
	private TextField tfUserName;
	@FXML
	private TextField tfPassword;
	//Buttons
	@FXML
	private Button btnLogin;

	public void start(Stage primaryStage) throws Exception {
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/LogInForm.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Log in");
		primaryStage.setScene(scene);
		setUndecorated(primaryStage, root);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO remove befor final :
		tfUserName.setText("admin");
		tfPassword.setText("admin");
		//----
	}

	/*@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	} //END of getFromServer(Object message)
*/
	/** connects to ip , tests user name and password
	 * @param event ??
	 * @throws Exception ??
	 */
	public void getInput(ActionEvent event) throws Exception {
		SecurityController securityController = new SecurityController();
		securityController.connectToServer(tfIP.getAccessibleText(), this);
		String username = tfUserName.getText();
		Object msg = new clientRequestFromServer(requestOptions.getUser, username);
		ClientLauncher.client.handleMessageFromClientUI(msg);

		// temp till testing is done ;
		boolean temp = true;
		//TODO: maybe switch to be notified by the server?> --then handling of lost connection and such problomes
		 Thread.sleep(2000);
		temp = securityController.checkLogin(tfPassword.getText(), user);
		// if you want to log in set temp to true now
		//temp = true;

		if (temp) NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, true);
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("password or user name is incorrect");
			alert.showAndWait();
		}
	}

	// private methods

	/**
	 * connects to the server
	 *
	 * -the ip is from the texfiled IP
	 *
	 * - Default port is from the client luncher
	 */
	
}// End of LogInForm

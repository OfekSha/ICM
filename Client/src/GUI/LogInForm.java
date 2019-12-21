package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Requirement;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
		// TODO Auto-generated method stub
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
		connectToServer();
		NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, new MainMenuForm(), true);
		// TODO: test password / user name is correct
	}

	// private methods

	/**
	 * connects to the server
	 *
	 * -the ip is from the texfiled IP
	 *
	 * - Default port is from the client luncher
	 */
	private void connectToServer() {
		String host = tfIP.getAccessibleText();
		// System.out.println("This is host: "+host);
		if (host == null)
			host = "localhost";
		try {
			ClientLauncher.client = new IcmClient(host, ClientLauncher.DEFAULT_PORT, this);
			System.out.println("Connection established!\n" //  TODO: to be removed/changed
					+ "Welcome to ICM.");
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" // TODO: to be removed/changed
					+ " Terminating client.");
			System.exit(1);
		}
	} // END connectToServer()
}// End of LogInForm

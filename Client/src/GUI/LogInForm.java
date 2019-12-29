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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author Yonathan
 *
 * -is the controller for LogInForm.fxml
 * -connects to the server
 */
public class LogInForm extends UserForm {

	//Text fields
	@FXML
	private TextField tfIP;
	@FXML
	private TextField tfUserName;
	@FXML
	private PasswordField pfPassword;
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

	/*@Override
	public void getFromServer(Object message) {
		clientRequestFromServer request = (clientRequestFromServer) message;
		user = (User) request.getObject();
	}*/

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//TODO remove before final :
		tfUserName.setText("admin");
		pfPassword.setText("admin");
		//----
	}

	/** connects to ip , tests user name and password
	 * @param event ??
	 * @throws Exception ??
	 */
	public void getInput(ActionEvent event) throws Exception {
		SecurityController securityController = new SecurityController();
		if (securityController.connectToServer(tfIP.getAccessibleText(), this)) {
			String username = tfUserName.getText();
			Object msg = new clientRequestFromServer(requestOptions.getUser, username);
			ClientLauncher.client.handleMessageFromClientUI(msg);
			//TODO: maybe switch to be notified by the server?> --then handling of lost connection and such problems
			Thread.sleep(2000);
			boolean temp = securityController.checkLogin(pfPassword.getText(), user);

			if (temp) MainScene(event);
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Password or Username is incorrect");
				alert.showAndWait();
			}
		}
	}
}// End of LogInForm

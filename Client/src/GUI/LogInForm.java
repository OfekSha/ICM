package GUI;

import Controllers.SecurityController;
import Entity.ChangeRequest;
import Entity.User;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.User.ICMPermissions;
import Entity.User.Job;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Yonathan
 *
 *         -is the controller for LogInForm.fxml -connects to the server
 */
public class LogInForm extends UserForm {

	// Text fields
	@FXML
	private TextField tfIP;
	@FXML
	private TextField tfUserName;
	@FXML
	private PasswordField pfPassword;
	// Buttons
	@FXML
	private Button btnLogin;

	// controller
	private SecurityController securityController = new SecurityController(this);
	// vars
	private ActionEvent eventIput;
	private Thread log;
	private boolean canLogIn = false;
	private Object someobjec = new Object();

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
		// TODO remove before final :
		tfUserName.setText("admin");
		pfPassword.setText("admin");
		// ----
	}

	/**
	 * connects to ip , tests user name and password
	 * 
	 * @param event ??
	 * @throws Exception ??
	 */

	public void getInput(ActionEvent event) {
		eventIput = event;
		if (securityController.connectToServer(tfIP.getAccessibleText(), this)) {
			securityController.input(tfUserName.getText(), pfPassword.getText());
		}
		log = Thread.currentThread();
		try {
			log.sleep(9999999);
		} catch (InterruptedException e) {
		} 

		if (canLogIn) {
			try {
				MainScene(eventIput);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} // END of getInput;

	@Override
	public void getFromServer(Object message) {
		clientRequestFromServer request = (clientRequestFromServer) message;
		System.out.print("\nMessage from server received: ");
		Object[] objectArray;
		switch (request.getRequest()) {
		case getUser:
			if (securityController.serveHandler(message)) {
				wakeUpOriginalThread();
			} else {
				incorrectDetails();
				wakeUpOriginalThread();
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown Request From Server Returned: " + request.getObject());
		}
		System.out.println();

	}

	private void wakeUpOriginalThread() {
		try {
			canLogIn = true;
			log.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void incorrectDetails() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Password or Username is incorrect");
		alert.showAndWait();
	}

}// End of LogInForm

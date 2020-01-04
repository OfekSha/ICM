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

	public void getInput(ActionEvent event) {
		eventIput = event;
		if (securityController.connectToServer(tfIP.getAccessibleText(), this)) {
			securityController.input(tfUserName.getText(), pfPassword.getText());
			putLunchedToSleep();
			if (canLogIn) {
				lunchMain();
			}
			else incorrectDetails();
		}
	} // END of getInput;

	@Override
	public void getFromServer(Object message) {
		clientRequestFromServer request = (clientRequestFromServer) message;
		Object[] objectArray;
		switch (request.getRequest()) {
		case getUser:
			if (securityController.serveHandler(request)) {
				canLogIn = true;
				wakeUpLunchedThread();
			}
			else {
				canLogIn = false;
				wakeUpLunchedThread();
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown Request From Server Returned: " + request.getObject());
		}
		System.out.println();

	}

	/** Saves the the lunched thread and puts it to  sleep
	 * 
	 * - saves it so  wakeUpLunchedThread would be able to wake it up
	 * 
	 */
	private void putLunchedToSleep(){
		log = Thread.currentThread();
		try {
			log.sleep(9999999);
		} catch (InterruptedException e) {
		}
	}
	/** Wakes up the lunched thread
	 * 
	 */
	private void wakeUpLunchedThread() {
		try {
			log.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** activates pop up that informs the user his one of the detail he entered is wrong
	 * 
	 */
	private void incorrectDetails() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Password or Username is incorrect");
		alert.showAndWait();
	}

	
	private void  lunchMain() {
		try {
			MainScene(eventIput);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}// End of LogInForm

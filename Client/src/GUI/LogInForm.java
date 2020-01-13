package GUI;

import Controllers.SecurityController;
import Entity.User;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
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
			putLaunchedThreadToSleep();
			if (canLogIn) {
				successfulLogIn(user);
				lunchMain();
			}
			else
			{
			if(user!=null)	alertWindowLauncher(AlertType.ERROR, "Information Dialog", null, "Password or Username is incorrect");
			else alertWindowLauncher(AlertType.ERROR, "Information Dialog", null, "User is already connected");
			}
		} else {
			String contant = "Things you can do : \n" + "1) Make sure the server is running\n"
					+ "2) Ask  for the server IP";
			alertWindowLauncher(AlertType.ERROR, "Information Dialog", "Error: Can't setup connection!", contant);
		}
	

	} // END of getInput;

	@Override
	public void getFromServer(Object message) {
		clientRequestFromServer request = (clientRequestFromServer) message;
		Object[] objectArray;
		switch (request.getRequest()) {
		case LogIN:
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
	private void putLaunchedThreadToSleep(){
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

	private void  lunchMain() {
		try {
			MainScene(eventIput);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void successfulLogIn(User user) {
		Object msg = new clientRequestFromServer(requestOptions.successfulLogInOut, user);
		ClientLauncher.client.handleMessageFromClientUI(msg);
	}
	@Override
	public  void ExitBtn() {
		System.exit(0);
	}
}// End of LogInForm

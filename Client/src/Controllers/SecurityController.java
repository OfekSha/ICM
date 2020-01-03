package Controllers;

import Entity.User;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.LogInForm;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

/**
 * @author Yonathan TODO : needs testing
 *
 */
public class SecurityController {
	// vars
	private User user = null;
	private LogInForm log = null;
	private String password = null;

	public SecurityController(LogInForm log) {
		this.log = log;
	}

	public boolean serveHandler(Object message) {
		clientRequestFromServer request = (clientRequestFromServer) message;
		user = (User) request.getObject();
		if (checkLogin(user)) {
			log.SetUser(user);
			return true;
		} else {
			return false;
		}
	}

	public void input(String username, String password) {
		Object msg = new clientRequestFromServer(requestOptions.getUser, username);
		ClientLauncher.client.handleMessageFromClientUI(msg);
		this.password = password;

	}

	public boolean checkLogin(User user) {
		if (!(user == null)) {
			return password.equals(user.getPassword());
		}
		return false;
	}// End of checkLogin()

	public boolean connectToServer(String host, IcmForm form) {
		if (host == null)
			host = "localhost";
		try {
			ClientLauncher.client = new IcmClient(host, ClientLauncher.DEFAULT_PORT, form);
			System.out.println("Connection established!\n" // TODO: to be removed/changed
					+ "Welcome to ICM.");
			return true;
		} catch (IOException exception) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Error: Can't setup connection!");
			alert.setContentText(
					"Things you can do : \n" + "1) Make sure the server is running\n" + "2) Ask  for the server IP");
			alert.showAndWait();
			return false;
		}
	} // END connectToServer()

} // End of SecurityController class

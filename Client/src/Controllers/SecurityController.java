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

	/** Handles the getUser request 
	 * - the method is activated by getFromServer server there for it should have all details 
	 * @param request
	 * @return
	 */
	public boolean serveHandler(clientRequestFromServer request) {
		user = (User) request.getObject();
		if (testPassword(user)) {
			log.SetUser(user);
			return true;
		} else {
			return false;
		}
	}

	/** saves the user input and askes the server for further details
	 * @param username
	 * @param password
	 */
	public void input(String username, String password) {
		Object msg = new clientRequestFromServer(requestOptions.getUser, username);
		ClientLauncher.client.handleMessageFromClientUI(msg);
		this.password = password;

	}

	/** tests if the use given password matches the  password in DB
	 * @param user
	 * @return
	 */
	public boolean testPassword(User user) {
		if (!(user == null)) {
			return password.equals(user.getPassword());
		}
		return false;
	}// End of testPassword()
 
	/** connects to the server with given ip
	 * @param host
	 * @param form
	 * @return
	 */
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

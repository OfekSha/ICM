package Controllers;

import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

/**
 * @author Yonathan TODO : needs testing
 *
 */
public class SecurityController {

	public boolean checkLogin(String password, User user) {
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
			alert.setContentText("Things you can do : \n" +
					"1) Make sure the server is running\n" +
					"2) Ask  for the server IP");
			alert.showAndWait();
			return false;		
		}
	} // END connectToServer()

} // End of SecurityController class

package Controllers;

import java.io.IOException;

import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author Yonathan TODO : needs testing
 *
 */
public class SecurityController {

	public boolean checkLogin(String password, User user) {
		return user != null && password.equals(user.getPassword());
		/*if (!(user == null)) {
			return password.equals(user.getPassword());
		}
		return false;*/
	}// End of checkLogin()

	public boolean connectToServer(String host, IcmForm form) {
		// System.out.println("This is host: "+host);
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
			alert.setContentText("things you can do : \nmake sure the server is runnig\nask  for the server ip ");
			alert.showAndWait();
			return false;		
		}
	} // END connectToServer()

} // End of SecurityController class

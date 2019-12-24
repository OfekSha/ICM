package Controllers;

import java.io.IOException;

import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;

/**
 * @author Yonathan TODO : needs testing
 *
 */
public class SecurityController {

	public boolean checkLogin(String password, User user) {
		if (!(user ==null ) ) {
		if (!(password.equals(user.getPassword()))) return true;
		}
		return false;
	}// End of checkLogin()
	
	
	
	
	public  void connectToServer(String host,IcmForm form) {
		// System.out.println("This is host: "+host);
		if (host == null)
			host = "localhost";
		try {
			ClientLauncher.client = new IcmClient(host, ClientLauncher.DEFAULT_PORT, form);
			System.out.println("Connection established!\n" //  TODO: to be removed/changed
					+ "Welcome to ICM.");
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" // TODO: to be removed/changed
					+ " Terminating client.");
			System.exit(1);
		}
	} // END connectToServer()
	
} // End of SecurityController class
	

package Controllers;

import java.io.IOException;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;

public class SecurityController {

	public void checkLogin(String userName,String password) {}
	
	
	
	
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
	
}
	

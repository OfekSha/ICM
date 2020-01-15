package Controllers;

import Entity.User;
import Entity.clientRequestFromServer;
import Entity.User.collegeStatus;
import Entity.clientRequestFromServer.requestOptions;
import GUI.LogInForm;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;

import java.io.IOException;
import java.util.EnumSet;

/**  dose logics for the log in form
 * 
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
	 * @param request ?
	 * @return ?
	 */
	public boolean serveHandler(clientRequestFromServer request) {
		Object[] obj =(Object[]) request.getObject();
		user = (User) obj[0];
		log.setUser(user);
		return (boolean) obj[1];
	}

	/** saves the user input and ask the server for further details
	 * @param username ?
	 * @param password ?
	 */
	public void input(String username, String password) {
		EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
		EnumSet<User.icmPermission> lessPermissions = EnumSet.complementOf(Permissions); //empty enum set
		User log = new  User(username, password, null, null, null, null, lessPermissions);
		Object msg = new clientRequestFromServer(requestOptions.LogIN, log);
		ClientLauncher.client.handleMessageFromClientUI(msg);
		this.password = password;

	}

	/** tests if the use given password matches the  password in DB
	 * @param user ?
	 * @return ?
	 * @deprecated
	 */
	public boolean testPassword(User user) {
		if (!(user == null)) {
			return password.equals(user.getPassword());
		}
		return false;
	}// End of testPassword()
 
	/** connects to the server with given ip
	 * @param host ? - server to connect to 
	 * @param form ?- the controlling form  , the form witch is currently open
	 * @return  true when a successful connection is established 
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
			return false;
		}
	} // END connectToServer()

} // End of SecurityController class

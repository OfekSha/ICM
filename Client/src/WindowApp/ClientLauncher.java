package WindowApp;

import GUI.LogInForm;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Yonathan
 * 
 *         1) creates the connection, and holds it
 * 
 *         2) launches the login
 * 
 *         3) holds useful static methods
 */
public class ClientLauncher extends Application {
	// Class variables
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	// Instance variables
	/**
	 * The instance of the client
	 */
	public static IcmClient client;
	/**
	 * The instance of the frame
	 */
	public LogInForm aFrame;

	/**
	 * the real main method
	 *
	 * @param args ????
	 */
	public static void main(String[] args) {
		launch(args); // Wait for data
	}

	/**
	 * "launches" the frame
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		aFrame = new LogInForm(); // create the frame
		aFrame.start(arg0);
	}
	 // END of nextWindowLauncher
}
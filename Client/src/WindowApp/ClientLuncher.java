package WindowApp;
import java.io.IOException;

import GUI.FormController;
import javafx.application.Application;
import javafx.stage.Stage;



/**
 * @author Yonathan
 * 
 * 1) creates the connection, and holds it
 * 2) creates and lunches the manin sence  
 * 
 */
public class ClientLuncher extends Application {

	//Class variables *************************************************
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;


	//Instance variables **********************************************
	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	public static IcmClient client;
	/**
	 * The instance of the frame
	 */
	public FormController aFrame;

	/**
	 * the  real main method
	 *
	 * @param args ????
	 */
	public static void main(String[] args) throws Exception {
		launch(args);      //Wait for  data
	}

	/**
	 * "lunches" the frame
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		String host="localhost";
		aFrame = new FormController(); // create the frame
		
		
		try {
			client = new IcmClient(host, DEFAULT_PORT, aFrame);
			System.out.println("Connection established!\n" //to be removed/changed
					+ "Welcome to ICM.");
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" // to be removed/changed
					+ " Terminating client.");
			System.exit(1);
		}
		aFrame.start(arg0);
		
		
	}
}
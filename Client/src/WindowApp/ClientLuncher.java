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
	 * @param host ????
	 * @param port ????
	 * @author Yonathan
	 * The contractor for the Main class - crates the connection and the frame
	 */
	public ClientLuncher(String host, int port) {
		aFrame = new FormController(); // create the frame
		try {
			client = new IcmClient(host, port, aFrame);
			System.out.println("Connection established!\n" //to be removed/changed
					+ "Welcome to ICM.");
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" // to be removed/changed
					+ " Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * the  real main method
	 *
	 * @param args ????
	 */
	public static void main(String[] args) throws Exception {
		int port = 0; 
		String host;
		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
	ClientLuncher TheClient = new ClientLuncher(host,port);
		
		launch(args);      //Wait for  data
	}

	/**
	 * "lunches" the frame
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		aFrame.start(arg0);
	}
}
package defaultPackage;
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
public class Main extends Application {

	//Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;


	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	 public static ICMclient client;
	/**
	 * The instance of the frame
	 */
	FormController aFrame;

	/**
	 * @param host
	 * @param port
	 * @author Yonathan
	 * The contractor for the Main class - crates the connection and the frame
	 */
	public Main(String host, int port) {


		aFrame = new FormController(); // create the frame
		try {
			client = new ICMclient(host, port, aFrame);
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
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 0;  //The port number
		String host;
		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		//TODO what for 'chat' object ?
		Main chat = new Main(host, DEFAULT_PORT);
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

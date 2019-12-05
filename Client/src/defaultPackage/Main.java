package defaultPackage;
import java.io.IOException;

import GUI.FormController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main   extends Application {

	//Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;


	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;
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
			client = new ChatClient(host, port, aFrame);
			System.out.println("Connection established!\n" //to be removed/changed
					+ "Welcome to ICM.\n");
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

package WindowApp;
import java.io.IOException;
import java.util.Scanner;

import GUI.FormController;
import GUI.LogInForm;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Yonathan
 * 
 * 1) creates the connection, and holds it
 * 2) creates and lunches the manin sence  
 * 
 */
public class ClientLauncher extends Application {

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
	public  LogInForm aFrame;

	/**
	 * the  real main method
	 *
	 * @param args ????
	 */
	public static void main(String[] args) {
		launch(args);      //Wait for  data
	}

	/**
	 * "lunches" the frame
	 */
	@Override
	public void start(Stage arg0) throws Exception {
	
		aFrame = new LogInForm(); // create the frame
		aFrame.start(arg0);
		
		
	}
}
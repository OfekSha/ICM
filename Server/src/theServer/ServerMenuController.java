package theServer;


import javafx.application.Application;
import javafx.stage.Stage;

/**creates a server Menu scene 
 * @see ServerMenuForm
 *
 */
public class ServerMenuController extends Application {

	public  static ServerMenuForm aFrame;
	public static String[] theArgs;
	/**
	 * the real main method
	 * @param args ????
	 */
	public static void main(String[] args) {
		theArgs = args;
		launch(args); // Wait for data
	}

	/**
	 * "launches" the frame
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		aFrame = new ServerMenuForm(); // create the frame
		aFrame.start(arg0);
	}

}

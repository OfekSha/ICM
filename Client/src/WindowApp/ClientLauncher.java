package WindowApp;

import java.io.IOException;
import GUI.LogInForm;
import GUI.UserForm;
import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Yonathan
 * 
 *         1) creates the connection, and holds it
 * 
 *         2) launches the login
 * 
 *         3) holds useful static methods
 * 
 * 
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

	/**
	 * loads new Scene
	 * 
	 * @param event       - ActionEvent event
	 * @param path        - the path of the fxml : example :"/GUI/MainMenu.fxml"
	 * @param launcherClass - the class witch is creating the new Scene (pleas send
	 *                    :this)
	 * @param controller  - the class thah is the controler of the sent fxml
	 * @param hide        - true if you want to hide the lunching window
	 * @throws Exception ???
	 */
	public static void NextWindowLauncher(ActionEvent event, String path, IcmForm launcherClass, IcmForm controller,
										  boolean hide) throws Exception {
		if (hide) {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		}
		Stage stage = new Stage();
		Parent root;
		root = (Parent) FXMLLoader.load(launcherClass.getClass().getResource(path));
		Scene scene = new Scene(root);
		scene.setRoot(root);
		FXMLLoader fxmlLoader = new FXMLLoader();
		controller = (IcmForm) fxmlLoader.getController();
		stage.setScene(scene);
		UserForm.setUndecorated(stage, root);
		stage.show();
	} // END of nextWindowLauncher
}
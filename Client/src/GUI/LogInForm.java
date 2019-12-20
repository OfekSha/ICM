package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import Entity.Requirement;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author Yonathan
 *
 * -is the controller for LogInForm.fxml
 * -connects to the server
 */
public class LogInForm implements Initializable, IcmForm {

	// Variables
	private static ArrayList<Requirement> ReqListForClient = null; //TO DO : change to whatever entity will have the username and password
	MainMenuForm TheMainMenuForm2;
	// text fields
	@FXML
	private TextField IP;
	@FXML
	private TextField UserName;
	@FXML
	private TextField Password;

	// buttons
	@FXML
	private Button btnExit;
	@FXML
	private Button btnLogin;

	// UNDECORATED
	private double xOffset = 0;
	private double yOffset = 0;

	public void start(Stage primaryStage) throws Exception {
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/LogInForm.fxml"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - xOffset);
			primaryStage.setY(event.getScreenY() - yOffset);
		});
		Scene scene = new Scene(root);
		primaryStage.setTitle("Log in");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	} //END of getFromServer(Object message)

	/** connects to ip , tests user name and password
	 * @param event
	 * @throws Exception
	 */
	public void getInput(ActionEvent event) throws Exception {
		connectToServer();
		ClientLauncher.SnextWindowLuncher(event, "/GUI/MainMenu.fxml", this, new MainMenuForm(), true);
		// TODO: test password / user name is correct
	}

	public void ExitBtn() {
		System.exit(0);
	}

	// private methods

	/**
	 * connects to the server
	 * 
	 * -the ip is from the texfiled IP 
	 * 
	 * - Default port is from the client luncher
	 */
	private void connectToServer() {
		String host = IP.getAccessibleText();
		// System.out.println("This is host: "+host);
		if (host == null)
			host = "localhost";
		try {
			ClientLauncher.client = new IcmClient(host, ClientLauncher.DEFAULT_PORT, this);
			System.out.println("Connection established!\n" //  TODO: to be removed/changed
					+ "Welcome to ICM.");
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" // TODO: to be removed/changed
					+ " Terminating client.");
			System.exit(1);
		}
	} // END connectToServer()
}// End of LogInForm

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

public class LogInForm implements Initializable, IcmForm {

	// Variables
	private static ArrayList<Requirement> ReqListForClient = null;
	ObservableList<String> listFor_cmbRequests;
	ObservableList<String> listFor_cmbStatus;
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
	// TODO Auto-generated method stub

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}

	// TODO: the following methods are from the class diagram:

	public void getInput(ActionEvent event) throws Exception {
		connectToServer();
		// lunching main
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
	 * -the ip is from the texfiled IP - Default port is from the client luncher
	 */
	private void connectToServer() {
		String host = IP.getAccessibleText();
		// System.out.println("This is host: "+host);
		if (host == null)
			host = "localhost";
		try {
			ClientLauncher.client = new IcmClient(host, ClientLauncher.DEFAULT_PORT, this);
			System.out.println("Connection established!\n" // to be removed/changed
					+ "Welcome to ICM.");
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" // to be removed/changed
					+ " Terminating client.");
			System.exit(1);
		}
	} // END connectToServer()
}// End of LogInForm

package GUI;

import Entity.Requirement;
import Entity.User;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Optional;

import static Entity.clientRequestFromServer.requestOptions.getAll;

public abstract class UserForm implements IcmForm {

	// vars
	protected static User user = null; // connected user;
	static ArrayList<Requirement> ReqListForClient = null;

	@FXML
	public Button btnExit;
	public Button btnLogout;
	public Button btnBack;
	public ComboBox<String> cmbRequests;

	// UNDECORATED
	private static double xOffset = 0;
	private static double yOffset = 0;

	public static void setUndecorated(Stage primaryStage, Parent root) {
		primaryStage.initStyle(StageStyle.UNDECORATED);
		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - xOffset);
			primaryStage.setY(event.getScreenY() - yOffset);
		});
	}
	// END UNDECORATED

	// Standard buttons for each scene
	public void MainScene(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, true);
	}

	public void LogOutButton(ActionEvent event) throws Exception {
		// updating server user is logged out
		user.changeLoginStatus(false);
		Object msg = new clientRequestFromServer(requestOptions.changeInLogIn, user);
		ClientLauncher.client.handleMessageFromClientUI(msg);
		// lunching main menu
		NextWindowLauncher(event, "/GUI/LogInForm.fxml", this, true);
	}

	public void ExitBtn() {
		if (user != null && !(this instanceof LogInForm)) {
			// making sure the user wants to exit
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("");
			alert.setHeaderText("You are exiting the ICM");
			alert.setContentText("Are you sure you want to do that?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) { // the user pressed ok
				// updating server user is logged out
				user.changeLoginStatus(false);
				Object msg = new clientRequestFromServer(requestOptions.changeInLogIn, user);
				ClientLauncher.client.handleMessageFromClientUI(msg);
				//
				ClientLauncher.client.quit();
			}
		} else { // if we are at the log in screen
			if (ClientLauncher.client == null)
				System.exit(0);
			ClientLauncher.client.quit();
		}
	}
	// End of standard buttons for each scene

	/**
	 * loads new Scene
	 *
	 * @param event         - ActionEvent event
	 * @param path          - the path of the fxml : example :"/GUI/MainMenu.fxml"
	 * @param launcherClass - the class which is creating the new Scene (please
	 *                      send: this)
	 * @param hide          - true if you want to hide the launching window
	 * @throws Exception ???
	 */
	public void NextWindowLauncher(ActionEvent event, String path, IcmForm launcherClass, boolean hide)
			throws Exception {
		if (hide) {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		}
		Stage stage = new Stage();
		Parent root = FXMLLoader.load(launcherClass.getClass().getResource(path));
		Scene scene = new Scene(root);
		setUndecorated(stage, root);
		stage.setScene(scene);
		stage.show();
	}

	public void getRequests() {
		clientRequestFromServer newRequest = new clientRequestFromServer(getAll);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getFromServer(Object message) { // msg is ArrayList of Entity.Requirement classes
		clientRequestFromServer request = (clientRequestFromServer) message;
		System.out.println("\nMessage from server received: ");
		switch (request.getRequest()) {
			case getAll:
				ReqListForClient = (ArrayList<Requirement>) request.getObject();
				ReqListForClient.forEach(e -> System.out.print("[" + e.getID() + "] "));
				break;
			case updateStatus:
				ReqListForClient = (ArrayList<Requirement>) request.getObject();
				ReqListForClient.forEach(e ->
						System.out.println("Status of request ID:[" + e.getID() + "] updated to "
								+ e.getStatus().toString()));
				break;
			case getUser:
				user = (User) request.getObject();
				System.out.println("User entity received: [" + user.getUserName() + "]");
				break;
/*			case getRequirement:
				break;
			case updateUser: break;*/
			default:
				throw new IllegalArgumentException("Unknown Request From Server Returned: " + request.getObject());
		}
		// TODO End of todo
	}
}

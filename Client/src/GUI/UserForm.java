package GUI;

import Entity.Requirement;
import Entity.User;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

import static Entity.clientRequestFromServer.requestOptions.getAll;

public abstract class UserForm implements IcmForm {

	// vars
	protected static User user = null; //connected user;
	static ArrayList<Requirement> ReqListForClient = null;

	@FXML
	public Button btnExit;
	public Button btnLogout;
	public Button btnBack;

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
	public void BackScene(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, true);
	}

	public void LogOutButton(ActionEvent event) throws Exception {
		NextWindowLauncher(event, "/GUI/LogInForm.fxml", this, true);
	}

	public void ExitBtn() {
		ClientLauncher.client.quit();
	}
	// End of standard buttons for each scene

	public void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer(getAll);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}

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
	public void NextWindowLauncher(ActionEvent event, String path, IcmForm launcherClass, boolean hide) throws Exception {
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

	@Override
	public void getFromServer(Object message) { // msg is ArrayList of Entity.Requirement classes
		clientRequestFromServer request = (clientRequestFromServer) message;
		ReqListForClient = request.getObj();

		// TODO Only for testing, delete it before assignment
		System.out.println("\nMessage from osf.server Received:");
		//

		switch (request.getRequest()) {
			case getAll:
				System.out.print("Load list of requests: ");
				ReqListForClient.forEach(e -> System.out.print("[" + e.getID() + "] "));
				break;
			case updateStatus:
				ReqListForClient.forEach(e -> System.out.println("Status of request ID:["
						+ e.getID() + "] updated to " + e.getStatus().toString()));
				break;
			case getRequirement:
				break;
			case getUser:
				user = (User) request.getObject();
				break;
			default:
				throw new NotImplementedException();
		}
		// TODO End of todo
	}
}

package GUI;

import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.User;
import Entity.User.ICMPermissions;
import Entity.User.Job;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static Entity.clientRequestFromServer.requestOptions.getAll;

public abstract class UserForm implements IcmForm {

	// vars
	protected static User user = null; // connected user;
	static ArrayList<ChangeRequest> changeRequests = null;
	static ArrayList<User> allUsers = null;
	static ChangeRequestStatus requestStatus;
	static ICMPermissions iCMPermission;
	static Job job;
	static Stage popupWindow;

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
		setUserLogOff();
		user = null;
		//ClientLauncher.client = null;   // NextWindowLauncher - needs clinte
		// lunching main menu
		NextWindowLauncher(event, "/GUI/LogInForm.fxml", this, true);
	}

	public void ExitBtn() {
		// if there is no server there will be no client
		if (user != null ) {
			// making sure the user wants to exit
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Exit");
			alert.setHeaderText("You are exiting the ICM");
			alert.setContentText("Are you sure you want to do that?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) { // the user pressed ok
				setUserLogOff();
				ClientLauncher.client.quit();
			} else
				alert.close();
		} else
			System.exit(0);
	}

	/**updating server user is logged out
	 * 
	 */
	private void setUserLogOff() {
		Object msg = new clientRequestFromServer(requestOptions.successfulLogInOut, null);
		ClientLauncher.client.handleMessageFromClientUI(msg);
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
	public void NextWindowLauncher(ActionEvent event, String path,
								   IcmForm launcherClass, boolean hide) throws Exception {
		ClientLauncher.client.setClientUI(launcherClass);
		Stage stage = new Stage();
		if (hide) {
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		}
		Parent root = FXMLLoader.load(launcherClass.getClass().getResource(path));
		Scene scene = new Scene(root);
		setUndecorated(stage, root);
		stage.setScene(scene);
		stage.show();
	}

	protected void popupWindowLauncher(String target) throws IOException {
		// inspectorWindow.setScene(((Node)event.getTarget()).getScene());
		popupWindow = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource(target));
		Scene scene = new Scene(root);
		popupWindow.setScene(scene);
		popupWindow.initModality(Modality.APPLICATION_MODAL);
		popupWindow.show();
	}
	
	
	/** creates an alert and shows it 
	 * @param type
	 * @param title
	 * @param header
	 * @param Content
	 * 
	 */
	protected void alertWindowLauncher(AlertType type,String title, String header, String Content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(Content);
		alert.showAndWait();	
	}

	public void getRequests() {
		clientRequestFromServer newRequest = new clientRequestFromServer(getAll);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
	}

	protected void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		changeRequests.forEach(cR -> al.add(cR.getRequestID()));
		cmbRequests.setItems(FXCollections.observableArrayList(al));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getFromServer(Object message) { // msg is ArrayList of Entity.ChangeRequest classes
		clientRequestFromServer request = (clientRequestFromServer) message;
		System.out.print("\n["
				+ ZonedDateTime.now().toString()
				.split("T")[1]
				.split("\\+")[0]
				+ "]: Message from server -> " + message.toString());
		Object[] objectArray;
		switch (request.getRequest()) {
			case getAll:
				changeRequests = (ArrayList<ChangeRequest>) request.getObject();
				changeRequests.forEach(e -> System.out.print("[" + e.getRequestID() + "]"));
				break;
			case updateStatus:
				changeRequests = (ArrayList<ChangeRequest>) request.getObject();
				changeRequests.forEach(e ->
						System.out.println("Status of request ID:[" + e.getRequestID() + "] updated to "
								+ e.getStatus().toString()));
				break;
			case getUser:
				user = (User) request.getObject();
				//System.out.println("User entity received: [" + user.getUserName() + "]");
				break;
			case getAllUsers:
				allUsers = (ArrayList<User>) request.getObject();
				break;
			case getChangeRequestByStatus:
				objectArray = (Object[]) request.getObject();
				changeRequests = (ArrayList<ChangeRequest>) objectArray[0];
				requestStatus = (ChangeRequestStatus) objectArray[1];
				break;
			case getUsersByICMPermissions:
				objectArray = (Object[]) request.getObject();
				allUsers = (ArrayList<User>) objectArray[0];
				iCMPermission = (ICMPermissions) objectArray[1];
				break;
			case getAllUsersByJob:
				objectArray = (Object[]) request.getObject();
				allUsers = (ArrayList<User>) objectArray[0];
				job = (Job) objectArray[1];
				break;
			
/*			case getRequirement:
				break;
			case updateUser: break;*/
			default:
				throw new IllegalArgumentException("Unknown Request From Server Returned: " + request.getObject());
		}
		System.out.println();
	}
	
	 public void SetUser(User u) {
		 user =u;
	}
	 public User getUser(User u) {
		  return user;
	}
}// END of class UserForm

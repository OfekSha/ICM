package GUI;


import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class UserForm implements IcmForm {
	@FXML
	public Button btnExit;
	public Button btnLogout;
	public Button btnBack;

	//UNDECORATED
	private static double xOffset = 0;
	private static double yOffset = 0;

	//public UserForm(Stage primaryStage, String path){
	public static void setUndecorated(Stage primaryStage, Parent root) {
		primaryStage.initStyle(StageStyle.UNDECORATED);
		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event ->{
			primaryStage.setX(event.getScreenX() - xOffset);
			primaryStage.setY(event.getScreenY() - yOffset);
		});
	}

	//TODO: to be implemented
	/* all forms will use to get the request */
	public void getRequest() {

	}

	public void BackScene(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/MainMenu.fxml", this, new MainMenuForm(), true);
	}
	public void LogOutButton(ActionEvent event) throws Exception {
		ClientLauncher.NextWindowLauncher(event, "/GUI/LogInForm.fxml", this, new LogInForm(), true);
	}
	public void ExitBtn() {
		System.exit(0);
	}
}

package GUI;

import WindowApp.IcmForm;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public interface MainForm {
	void start(Stage primaryStage) throws Exception ;

	/**
	 * Method that when overriden is used to display objects onto
	 * a UI.
	 */
	void getFromServer(Object message);
}

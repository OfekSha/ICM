package GUI;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

public interface MainForm extends Initializable {

	void start(Stage primaryStage) throws Exception ;

	/**
	 * getFromServer moved here from IcmForm
	 * @param message received from server
	 */
	void getFromServer(Object message);
}

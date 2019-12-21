package WindowApp;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * This interface implements the abstract method used to display
 * objects onto the client or server UIs.
 */
public interface IcmForm {
	void getFromServer(Object message);
}

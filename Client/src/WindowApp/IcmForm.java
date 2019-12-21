package WindowApp;

import javafx.fxml.Initializable;

/**
 * This interface implements the abstract method used to display
 * objects onto the client or server UIs.
 */
public interface IcmForm extends Initializable {
	void getFromServer(Object message);
}

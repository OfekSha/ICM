package WindowApp;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;

/**
 * This interface implements the abstract method used to display
 * objects onto the client or server UIs.
 */
public interface IcmForm {
	void getFromServer(Object message);
}

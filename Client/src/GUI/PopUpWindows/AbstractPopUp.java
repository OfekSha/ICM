package GUI.PopUpWindows;

import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;


import java.net.URL;
import java.util.ResourceBundle;

import Entity.ProcessStage;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * abstract pop up window use as extends to use general buttons and function
 * @see #getCancel
 * @see #btnCancel
 */
public abstract class AbstractPopUp implements IcmForm{

	public static ProcessStage processStage; // the selected process stage to change.
    /**
     * cancel button to close window
     * @see #getCancel
     */
    @FXML
    private Button btnCancel; // 

    /**
     * function that connected to the cancel button.
     * @see #btnCancel
     */
    @FXML
    public void getCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        processStage=null;
        stage.close();
        Platform.setImplicitExit(true);
    }
    public static void sendUpdateForRequest() {
		if (processStage != null) {
			clientRequestFromServer newRequest =
					new clientRequestFromServer(updateProcessStage, processStage);
			ClientLauncher.client.handleMessageFromClientUI(newRequest);
		}
	}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	ClientLauncher.client.setClientUI(this);
    	
    }
    @Override
    public void getFromServer(Object message) {
    	// TODO: need to get message from server that client update succeed. 
    	if (((clientRequestFromServer) message).getRequest()==updateProcessStage ) {
    		// do something.
    		getCancel();
    	}
    }
}

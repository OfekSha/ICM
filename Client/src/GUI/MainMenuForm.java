package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuForm implements Initializable, IcmForm{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogout;

    @FXML
    void initialize() {
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'MainMenu.fxml'.";
    }

    public void ExitBtn() {
        System.exit(0);
    }

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}

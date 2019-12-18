package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuForm extends stdForm {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnLogout;

    @FXML
    void initialize() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new Exception(); //buttplug remove warning, can be deleted
    }

    @Override
    public void getFromServer(Object message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

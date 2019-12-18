package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class stdForm implements MainForm {

    @FXML
    private Button btnExit;

    @FXML
    private Button btnLogout;

    @FXML
    void ExitBtn(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void logout(ActionEvent event) {

    }

    //UNDECORATED
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event ->{
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
        primaryStage.setTitle("IT Control Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void getFromServer(Object message) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

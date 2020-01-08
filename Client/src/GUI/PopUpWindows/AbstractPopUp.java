package GUI.PopUpWindows;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class AbstractPopUp {

    @FXML
    private Button btnCancel;

    @FXML
    public void getCancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
        Platform.setImplicitExit(true);
    }
}

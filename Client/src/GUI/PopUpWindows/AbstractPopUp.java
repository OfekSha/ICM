package GUI.PopUpWindows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class AbstractPopUp{

    @FXML
    private Button btnCancel;
    @FXML
    public void getCancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}

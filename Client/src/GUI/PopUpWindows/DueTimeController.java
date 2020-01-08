package GUI.PopUpWindows;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

import static GUI.ExecutionLeaderForm.processStage;
import static GUI.ExecutionLeaderForm.sendUpdateForRequest;

public class DueTimeController extends AbstractPopUp {

    @FXML
    private DatePicker dpDueTime;
    public Button btnDone;

    @FXML
    private void getDone() {
        LocalDate dataDue = dpDueTime.getValue();
        if (dataDue != null && dataDue.isAfter(LocalDate.now())) {
            processStage.setDueDate(dataDue);
            sendUpdateForRequest();
            getCancel();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Due time is null or incorrect!");
            alert.setContentText("Please choose correct time.");
            alert.showAndWait();
        }
    }
}

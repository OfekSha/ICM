package GUI.PopUpWindows;

import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static GUI.InformationTechnologiesDepartmentManagerForm.requirementForTable;

public class PerformanceReport extends AbstractPopUp {
    public Text txtReturns;
    public Text txtExtensions;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtReturns.setText("Time Added Because of Returns: " + requirementForTable.getOriginalRequest().getProcessStage().getTimeAddedBecuseOfReturns());
        txtExtensions.setText("Time Added From Extensions: " + requirementForTable.getOriginalRequest().getProcessStage().getTimeAddedFromExtentions());
    }
}

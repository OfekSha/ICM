package GUI.PopUpWindows;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static GUI.InformationTechnologiesDepartmentManagerForm.requirementForTable;

public class RequestDetailsForm implements Initializable {
    public Text txtRequestID;
    public TextArea taRequestDetails;
    public TextArea taRequestReason;
    public TextArea taComment;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtRequestID.setText("Request #" + requirementForTable.getId());
        taRequestDetails.setText(requirementForTable.getProblemDescription());
        taRequestReason.setText(requirementForTable.getWhyChange());
        taComment.setText(requirementForTable.getComment());
    }
}

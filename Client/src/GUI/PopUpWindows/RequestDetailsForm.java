package GUI.PopUpWindows;

import Controllers.DocmentTableForDownloadsController;
import Entity.ChangeRequest;
import Entity.DocumentForTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static GUI.InformationTechnologiesDepartmentManagerForm.requirementForTable;

public class RequestDetailsForm implements Initializable {
    public Text txtRequestID;
    public TextArea taRequestDetails;
    public TextArea taRequestReason;
    public TextArea taComment;
    public TextArea taChangeBase;
    public TextField tfInformSystem;
    public TextField tfCreationDate;
    public TextField tfCreatedBy;
    public TableView<DocumentForTable> tblFiles;
    public Button btnDownload;

    public DocmentTableForDownloadsController controller = new DocmentTableForDownloadsController();
    private ChangeRequest selectedRequest = requirementForTable.getOriginalRequest();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtRequestID.setText("Request #" + requirementForTable.getId());
        taRequestDetails.setText(requirementForTable.getProblemDescription());
        taRequestReason.setText(requirementForTable.getWhyChange());
        taComment.setText(requirementForTable.getComment());
        taChangeBase.setText(requirementForTable.getMessage());
        tfInformSystem.setText(requirementForTable.getSystem());
        tfCreatedBy.setText(requirementForTable.getInitiator().getTheInitiator().getFullName());
        tfCreationDate.setText(requirementForTable.getStartDate().toString());

        ObservableList<DocumentForTable> documentTableData = FXCollections.observableArrayList(
                controller.DocumentForTableList(requirementForTable.getOriginalRequest()));
        tblFiles.setItems(documentTableData);
    }
}
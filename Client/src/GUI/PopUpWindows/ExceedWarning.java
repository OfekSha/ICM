package GUI.PopUpWindows;

import Entity.RequestTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

import static GUI.InformationTechnologiesDepartmentManagerForm.exceededRequests;

public class ExceedWarning extends AbstractPopUp {
    public Text txtWarning;
    public TableView<RequestTableView.requirementForTable> tblRequests;
    public TableColumn<RequestTableView.requirementForTable, String> colID;
    public TableColumn<RequestTableView.requirementForTable, Object> colDueTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtWarning.setText("Requests below exceeded their execution time");
        RequestTableView requestTableView = new RequestTableView(tblRequests, colID, colDueTime);
        exceededRequests.forEach(e -> tblRequests.getItems().add(new RequestTableView.requirementForTable(e)));
    }
}

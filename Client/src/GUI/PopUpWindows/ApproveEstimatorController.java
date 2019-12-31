package GUI.PopUpWindows;

import static Entity.Requirement.statusOptions.closed;
import static Entity.Requirement.statusOptions.ongoing;
import static Entity.Requirement.statusOptions.suspended;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.InspectorController;
import Entity.ChangeRequest;
import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class ApproveEstimatorController extends AbstractPopUp implements IcmForm{

    @FXML
    private ComboBox<User> cmbEstimator;
    @FXML
    private Button btnApprove;
    @FXML
    public void getApprove(ActionEvent event) { 
    	InspectorController.changeRole(InspectorController.selctedReqFromTable, cmbEstimator.getSelectionModel().getSelectedItem());
    }
    private ObservableList<User> userList;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		InspectorController.getEstimators();
		
	}
	@Override
	public void getFromServer(Object message) {
		
		InspectorController.messageFromServer(message);
		userList= FXCollections.observableArrayList(InspectorController.estimators);
		cmbEstimator.setItems(userList);
		
	}
}

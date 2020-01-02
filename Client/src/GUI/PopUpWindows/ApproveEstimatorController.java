package GUI.PopUpWindows;

import Controllers.InspectorController;
import Entity.User;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ApproveEstimatorController extends AbstractPopUp implements IcmForm{

    @FXML
    private ComboBox<User> cmbEstimator;
    @FXML
    private Button btnApprove;
    @FXML
    private Label title;
    @FXML
    public void getApprove(ActionEvent event) { 
    	InspectorController.changeRole(InspectorController.selctedReqFromTable, cmbEstimator.getSelectionModel().getSelectedItem());
    	((Stage) btnApprove.getScene().getWindow()).close();
    }
    private ObservableList<User> userList;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setText("Approve Estimator for request "+InspectorController.selctedReqFromTable.getId());
		ClientLauncher.client.setClientUI(this);
		InspectorController.getInformationEngineers();
	}
	@Override
	public void getFromServer(Object message) {
		InspectorController.messageFromServer(message);
		userList= FXCollections.observableArrayList(InspectorController.informationEngineers);
		cmbEstimator.setItems(userList);
		int maxItems=cmbEstimator.getItems().size();
		cmbEstimator.getSelectionModel().select((int)(Math.random()*maxItems)); // choose randomly estimator before approved.
	}

	
}

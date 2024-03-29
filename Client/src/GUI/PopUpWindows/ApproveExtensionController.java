package GUI.PopUpWindows;


import java.net.URL;
import java.util.ResourceBundle;


import Controllers.InspectorController;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ApproveExtensionController extends AbstractPopUp implements IcmForm {
	@FXML
	private Label title;
    @FXML
    private Button btnApprove;

    @FXML
    private Button btnDisapprove;
    @FXML
    private TextArea reactionReason;
    @FXML
    private TextArea extensionExplanation;
    @FXML
    private TextField extensionDueDate;

    @FXML
    void getApprove(ActionEvent event) {  
    	InspectorController.approveExtension(true, InspectorController.selectedRequest, reactionReason.getText());
    	getCancel();
    }

    @FXML
    void getDisapprove(ActionEvent event) {  
    	InspectorController.approveExtension(false, InspectorController.selectedRequest, reactionReason.getText());
    	getCancel();
    }
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		title.setText("Do you approve extension for request " +InspectorController.selectedRequest.getRequestID());
		extensionExplanation.setText(InspectorController.selectedRequest.getProcessStage().getExtensionExplanation());
		extensionDueDate.setText(InspectorController.selectedRequest.getProcessStage().getExtensionRequestDate().toString());
		
	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}

}

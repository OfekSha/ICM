/*
package Depricated;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import GUI.UserForm;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static Entity.clientRequestFromServer.requestOptions.getAll;
import static Entity.clientRequestFromServer.requestOptions.updateStatus;

public class RequestForm extends UserForm implements Initializable, IcmForm {
	// text fields
	@FXML
	private TextField txtInitiator;
	@FXML
	private TextArea txtCurrentSituationDetails;
	@FXML
	private TextArea txtRequestDetails;
	@FXML
	private TextField txtStageSupervisor;

	// Combo Boxes
	@FXML
	private ComboBox<String> cmbRequests;

	@FXML
	private ComboBox<String> cmbStatus;

	//Variables
	private static ArrayList<ChangeRequest> ReqListForClient = null;
	ObservableList<String> listFor_cmbRequests;
	ObservableList<String> listFor_cmbStatus;

	*/
	/*
	 * @param primaryStage ????
	 * @throws Exception ????
	 *//*

	public void start(Stage primaryStage) throws Exception {
		// request DB
		getRequests();
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/SubmitRequest.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
		primaryStage.setTitle("Update Tool");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	*/
/*
	 * @param message is array of objects where where message[0] is requested action
	 *                and message[1] is answer

	@Override
	public void getFromServer(Object message) throws NotImplementedException {
		// TODO Auto-generated method stub
		clientRequestFromServer request = (clientRequestFromServer) message;
		ReqListForClient = request.getObj();
		System.out.println("\nMessage from osf.server Received:");
		switch(request.getRequest()) {
			case getAll:
				System.out.print("Load list of requests: ");
				ReqListForClient.forEach(e -> System.out.print("[" + e.getID() + "] "));
				break;
			case updateStatus:
				ReqListForClient.forEach(e -> System.out.println("Status of request ID:[" + e.getID() + "] updated to " + e.getStatus().toString()));
				break;
			case getRequirement:
				break;
			default: throw new NotImplementedException();
		}
	}*//*


	// setting up the combo boxes
*/
/*	protected void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		for (Requirement req : ReqListForClient) {
			al.add(Integer.toString((req.getID())));
		}
		listFor_cmbRequests = FXCollections.observableArrayList(al);
		cmbRequests.setItems(listFor_cmbRequests);
	}*//*


	// cmbStatus
	private void setStatusComboBox() {
		ArrayList<String> al = new ArrayList<>();
		al.add(ongoing.name());
		al.add(suspended.name());
		al.add(closed.name());
		listFor_cmbStatus = FXCollections.observableArrayList(al);
		cmbStatus.setItems(listFor_cmbStatus);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setRequestsComboBox();
		setStatusComboBox();
	}
	// end of setting up combo boxes

	// ActionEvent event methods

	public void RequestsComboBoxUsed() {
		String selected = cmbRequests.getSelectionModel().getSelectedItem();
		for (ChangeRequest req : ReqListForClient) {
			if (selected.equals(req.getRequestID())) {
				this.txtInitiator.setText(req.getInitiator().getTheInitiator().getFirstName());
				this.txtCurrentSituationDetails.setText(req.getChangeReason());
				this.txtRequestDetails.setText(req.getProblemDescription());
				this.txtStageSupervisor.setText(req.getProcessStage().getStageSupervisor().getFirstName());
				this.cmbStatus.setValue((req.getStatus()).name());
				this.cmbStatus.setPromptText((req.getStatus()).name());
				break;
			}
		}
	}
		// END of RequestsComboBoxUsed();

	// TODO: connect the button to the method
	
	*/
/**
	 * @throws Exception
	 * when the update button will be pressed the osf.server will be sent
	 *//*

	public void PressedUpdate() throws Exception {
		String sStatus = cmbStatus.getSelectionModel().getSelectedItem();
		String selected = cmbRequests.getSelectionModel().getSelectedItem();
		ArrayList<ChangeRequest> toThisReq = new ArrayList<>();
		for (ChangeRequest req : ReqListForClient) {
			if (selected.equals(req.getRequestID())) {
				toThisReq.add(req);
				req.setStatus(ChangeRequest.ChangeRequestStatus.valueOf(sStatus));
				clientRequestFromServer msg = new clientRequestFromServer(updateStatus, toThisReq);
				ClientLauncher.client.handleMessageFromClientUI(msg);
				break;
			}
		}
	}

	// private methods

	*/
/**
	 * @author Yonathan gets all requests with all the details to
	 *//*

	public void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer(getAll);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}

}// end of FormController class
*/

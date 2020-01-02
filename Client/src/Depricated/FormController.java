/*
package Depricated;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//java
//javafx
//ENTITY IMPORT

public class FormController extends UserForm implements Initializable, IcmForm {
	public TextArea taInitiatorRequest;
	public TextArea taExaminersReport;
	// text fields
	@FXML
	private TextField txtInitiator;
	@FXML
	private TextArea txtCurrentSituationDetails;
	@FXML
	private TextArea txtRequestDetails;
	@FXML
	private TextField txtStageSupervisor;
	@FXML
	private Button btnUpdateStatus;

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
/**
	 * @param primaryStage ????
	 * @throws Exception ????
	 *//*

	public void start(Stage primaryStage) throws Exception {
		// request DB
		getRequests();
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
		primaryStage.setTitle("Update Tool");
		setUndecorated(primaryStage, root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	*/
/**
	 * @param message is array of objects where where message[0] is requested action
	 *                and message[1] is answer
	 *//*

	@Override
	public void getFromServer(Object message) throws NotImplementedException {

		clientRequestFromServer request = (clientRequestFromServer) message;
		// msg is ArrayLost of Requirement classes
		ReqListForClient = (ArrayList<ChangeRequest>) request.getObject();
		System.out.println("\nMessage from server Received:");
		switch (request.getRequest()) {

			case getAll:
				System.out.print("Load list of requests: ");
				ReqListForClient.forEach(e -> System.out.print("[" + e.getRequestID() + "] "));
				break;
			case updateStatus:
				ReqListForClient.forEach(e -> System.out.println("Status of request ID:[" + e.getRequestID() + "] updated to " + e.getStatus().toString()));
				break;
			case getRequirement:
				break;
			default:
				throw new NotImplementedException();
		}
	}

	// setting up the combo boxes
	protected void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		ReqListForClient.forEach(c -> al.add(c.getRequestID()));
		listFor_cmbRequests = FXCollections.observableArrayList(al);
		cmbRequests.setItems(listFor_cmbRequests);
	}

	// cmbStatus
	private void setStatusComboBox() {
		ArrayList<String> al = new ArrayList<>();
		al.add(statusOptions.ongoing.name());
		al.add(statusOptions.suspended.name());
		al.add(statusOptions.closed.name());
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
				*/
/*this.txtInitiator.setText(req.getReqInitiator());
				this.txtCurrentSituationDetails.setText(req.getCurrentSituationDetails());
				this.txtRequestDetails.setText(req.getRequestDetails());
				this.txtStageSupervisor.setText(req.getStageSupervisor());
				this.cmbStatus.setValue((req.getStatus()).name());*//*

				//this.cmbStatus.setPromptText((req.getStatus()).name());
				setRequestsComboBox();
				break;
			}
		}
	}
		// END of RequestsComboBoxUsed();
	*/
/**
	 * @throws Exception
	 * when the update button will be pressed the server will be sent 
	 *//*

	public void PressedUpdate() throws Exception {
		String selected = cmbRequests.getSelectionModel().getSelectedItem();
		ArrayList<ChangeRequest> toThisReq = new ArrayList<>();
		for (ChangeRequest req : ReqListForClient) {
			if (selected.equals(req.getRequestID())) {
				toThisReq.add(req);
				req.setStatus(ChangeRequest.ChangeRequestStatus.valueOf(selected));
				clientRequestFromServer msg = new clientRequestFromServer(requestOptions.updateStatus, toThisReq);
				ClientLauncher.client.handleMessageFromClientUI(msg);
				break;
			}
		}
	}
	
	// private methods
*/
/*


	@author Yonathan gets all requests with all the details to
	public void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer(requestOptions.getAll);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}
*//*


*/
/*
	private void selectRequriement(Requirement req) {
		clientRequestFromServer commend = new clientRequestFromServer(requestOptions.getRequirement, req);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}
*//*


}// end of FormController class
*/

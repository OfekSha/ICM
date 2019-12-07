package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Requirement;
import Entity.Requirement.statusOptions;
import Entity.clientRequestFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import WindowApp.IcmForm;
import WindowApp.ClientLauncher;
import javafx.scene.control.*;

public class FormController implements Initializable, IcmForm {
	// text fields
	@FXML
	private TextField txtInitiator;
	@FXML
	private TextArea txtCurrentSituationDetails;
	@FXML
	private TextArea txtRequestDetails;
	@FXML
	private TextField txtStageSupervisor;

	// buttons
	@FXML
	private Button btnExit;
	@FXML
	private Button btnUpdateStatus;

	// Combo Boxes
	@FXML
	private ComboBox<String> cmbRequests;

	@FXML
	private ComboBox<String> cmbStatus;

	//
	private ArrayList<String> names = new ArrayList<>();
	private ArrayList<Requirement> ReqListForClient = new ArrayList<>() ;
	ObservableList<String> listFor_cmbRequests;
	ObservableList<String> listFor_cmbStatus;


	/**
	 * @param primaryStage ????
	 * @throws Exception ????
	 */
	public void start(Stage primaryStage) throws Exception {
		getRequests();
		// scene
		//	Parent root = FXMLLoader.load(getClass().getResource("/gui/AcademicFrame.fxml"));
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
		primaryStage.setTitle("Update Tool");
		primaryStage.setScene(scene);
		//primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
		
		// request DB
				
	}

	/**
	 * @param message is array of objects where where message[0] is requested action
	 *                and message[1] is answer
	 */
	@Override
	public void getFromServer(Object[] message) {
		// TODO Auto-generated method stub

		clientRequestFromServer request = (clientRequestFromServer) message[0]; // msg is array of
																				// objects first is from // where
		switch (request.getRequest()) {
		case getRequirement:
			if (message[1] instanceof ArrayList<?>) { // TODO: test if the element is correct ?
				ReqListForClient = (ArrayList<Requirement>) message[1];
			} else
				throw new IllegalArgumentException(message.getClass() + " is not correct type");
			break;
		case updateStatus:
			break;
		default:
			throw new IllegalArgumentException("the request " + request + " not implemented in the client.");
		}

	} // END of public void getFromServer(Object message) {

	// setting up the combo boxes

	private void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		for (Requirement req : ReqListForClient) {
			al.add(Integer.toString((req.getID())));
		}

		listFor_cmbRequests = FXCollections.observableArrayList(al);
		cmbRequests.setItems(listFor_cmbRequests);
	} // END OF private void setRequestsComboBox()

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

	/**
	 * @param event //TODO what event?
	 * @throws Exception the user has chosen an a user , all data boxes will be
	 *                   updated accordingly
	 */
	public void RequestsComboBoxUsed(ActionEvent event) throws Exception {
		getRequests();

		String s = cmbRequests.getSelectionModel().getSelectedItem();

		for (Requirement req : ReqListForClient) {

			if (s.equals(Integer.toString((req.getID())))) {
				this.txtInitiator.setText(req.getReqInitiator());
				this.txtCurrentSituationDetails.setText(req.getCurrentSituationDetails());
				this.txtRequestDetails.setText(req.getRequestDetails());
				this.txtStageSupervisor.setText(req.getStageSupervisor());
				this.cmbStatus.setPromptText((req.getStatus()).name());
				break;
			}
		}

	} // END of RequestsComboBoxUsed();

	// TODO:connect the button to the method
	
	/**
	 * @param event //TODO what event?
	 * @throws Exception
	 * 
	 * when the update button will be pressed the server will be sent 
	 * object array with the following :
	 * |String|statusOptions|
	 */
	public void PressedUpdate(ActionEvent event) throws Exception {
		String s = cmbStatus.getSelectionModel().getSelectedItem();
		clientRequestFromServer commend = new clientRequestFromServer("2");
		Object[] o = new Object[2];
		o[0] = commend;

		switch(s) {
			case "ongoing":
				o[1] = statusOptions.ongoing;
				break;
			case "suspended":
				o[1] = statusOptions.suspended;
				break;
			case "closed":
				o[1] = statusOptions.closed;
				break;
		}
		ClientLauncher.client.handleMessageFromClientUI(o);
	}
	
	
	public void ExitBtn(ActionEvent event) throws Exception {
		System.exit(0);			
	}

	// private methods

	/**
	 * @author Yonathan gets all requests with all the details to
	 */
	private void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer("0");
		Object[] o = new Object[1];
		o[0] = commend;
		ClientLauncher.client.handleMessageFromClientUI(o);
	}

}// end of FormController class

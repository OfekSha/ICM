package GUI;

import WindowApp.IcmForm;
import WindowApp.ClientLauncher;

//java
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

//javafx
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.stage.StageStyle;


//ENTITY IMPORT
//TODO check if it is possible and right to do?
import Entity.*;
import Entity.Requirement.statusOptions;
import Entity.clientRequestFromServer.requestOptions;

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

	//Variables
	private ArrayList<String> names = new ArrayList<>();
	private static ArrayList<Requirement> ReqListForClient = null;
	ObservableList<String> listFor_cmbRequests;
	ObservableList<String> listFor_cmbStatus;

	//UNDECORATED
	private double xOffset = 0;
	private double yOffset = 0;

	/**
	 * @param primaryStage ????
	 * @throws Exception ????
	 */
	public void start(Stage primaryStage) throws Exception {
		// request DB
		getRequests();
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));
		primaryStage.initStyle(StageStyle.UNDECORATED);
		root.setOnMousePressed(event -> {
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event ->{
				primaryStage.setX(event.getScreenX() - xOffset);
				primaryStage.setY(event.getScreenY() - yOffset);
		});
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
		primaryStage.setTitle("Update Tool");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param message is array of objects where where message[0] is requested action
	 *                and message[1] is answer
	 */
	@Override
	public void getFromServer(Object message) throws NotImplementedException {
		// TODO Auto-generated method stub
		clientRequestFromServer request = (clientRequestFromServer) message;
		// msg is ArrayLost of Requirement classes
		ReqListForClient = request.getObj();
		switch(request.getRequest()) {
			//TODO some actions to prompt message to client about answer from server
			case getAll:
				break;
			case updateStatus:
				System.out.println("Status updated to " + ReqListForClient.get(0).getStatus().toString());
				break;
			case getRequirement:
				break;
			default: throw new NotImplementedException();
		}
	}

	// setting up the combo boxes
	private void setRequestsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		for (Requirement req : ReqListForClient) {
			al.add(Integer.toString((req.getID())));
		}

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
		int s = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		for (Requirement req : ReqListForClient) {
			if (s == req.getID()) {
				this.txtInitiator.setText(req.getReqInitiator());
				this.txtCurrentSituationDetails.setText(req.getCurrentSituationDetails());
				this.txtRequestDetails.setText(req.getRequestDetails());
				this.txtStageSupervisor.setText(req.getStageSupervisor());
				this.cmbStatus.setValue((req.getStatus()).name());
				this.cmbStatus.setPromptText((req.getStatus()).name());
				break;
			}
		}
	}
		// END of RequestsComboBoxUsed();

	// TODO: connect the button to the method
	
	/**
	 * @throws Exception
	 * 
	 * when the update button will be pressed the server will be sent 
	 */
	public void PressedUpdate() throws Exception {
		String sStatus = cmbStatus.getSelectionModel().getSelectedItem();
		int sRequests = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		ArrayList<Requirement> toThisReq = new ArrayList<>();
		for (Requirement req : ReqListForClient) {
			if (sRequests == req.getID()) {
				toThisReq.add(req);
				req.setStatus(sStatus);
				clientRequestFromServer msg = new clientRequestFromServer(requestOptions.updateStatus, toThisReq);
				ClientLauncher.client.handleMessageFromClientUI(msg);
				break;
			}
		}
	}

	public void ExitBtn() {
		System.exit(0);			
	}

	// private methods

	/**
	 * @author Yonathan gets all requests with all the details to
	 */
	public void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer(requestOptions.getAll);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}

/*
	private void selectRequriement(Requirement req) {
		clientRequestFromServer commend = new clientRequestFromServer(requestOptions.getRequirement, req);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}
*/

}// end of FormController class

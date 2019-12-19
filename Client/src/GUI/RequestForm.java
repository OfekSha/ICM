package GUI;

import Entity.Requirement;
import Entity.clientRequestFromServer;
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

import static Entity.Requirement.statusOptions.*;
import static Entity.clientRequestFromServer.requestOptions.*;

//ENTITY IMPORT

public class RequestForm extends stdForm implements MainForm {
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
	@Override
	public void start(Stage primaryStage) throws Exception {
		// request DB
		getRequests();
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/User.fxml"));
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
		// msg is ArrayLost of Entity.Requirement classes
		ReqListForClient = request.getObj();
		System.out.println("\nMessage from osf.server Received:");
		switch(request.getRequest()) {
			//TODO some actions to prompt message to ocf.client about answer from osf.server
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
	 * when the update button will be pressed the osf.server will be sent
	 */
	public void PressedUpdate() throws Exception {
		String sStatus = cmbStatus.getSelectionModel().getSelectedItem();
		int sRequests = Integer.parseInt(cmbRequests.getSelectionModel().getSelectedItem());
		ArrayList<Requirement> toThisReq = new ArrayList<>();
		for (Requirement req : ReqListForClient) {
			if (sRequests == req.getID()) {
				toThisReq.add(req);
				req.setStatus(sStatus);
				clientRequestFromServer msg = new clientRequestFromServer(updateStatus, toThisReq);
				ClientLauncher.client.handleMessageFromClientUI(msg);
				break;
			}
		}
	}

	// private methods

	/**
	 * @author Yonathan gets all requests with all the details to
	 */
	public void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer(getAll);
		ClientLauncher.client.handleMessageFromClientUI(commend);
	}

/*
	private void selectRequriement(Entity.Requirement req) {
		Entity.clientRequestFromServer commend = new Entity.clientRequestFromServer(requestOptions.getRequirement, req);
		ClientLauncher.ocf.client.handleMessageFromClientUI(commend);
	}
*/

}// end of FormController class

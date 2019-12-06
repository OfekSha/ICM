package GUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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

import Entity.Requirement;
import Entity.clientRequestFromServer;
import WindowApp.IcmForm;

import WindowApp.ClientGUI;
import javafx.scene.control.*;
import javafx.stage.StageStyle;

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
	private ArrayList<Requirement> ReqListForClient ;
	ObservableList<String> list;


	/**
	 * @param primaryStage ????
	 * @throws Exception ????
	 */
	public void start(Stage primaryStage) throws Exception {
		// request DB
		getRequests();
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
		primaryStage.setTitle("Update Tool");
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.show();
	}

	//
	/**
	 * 
	 */

	@SuppressWarnings("unchecked") // tested thwo
	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

		clientRequestFromServer request = (clientRequestFromServer) (((Object[]) message)[0]); // msg is array of
																								// objects first is from
																								// where
		switch (request.getRequest()) {
			case getRequirement:
				if (((Object[]) message)[1] instanceof ArrayList<?>) { //TODO: test if the element is correct ?
					ReqListForClient = (ArrayList<Requirement>) (((Object[]) message)[1]);
				} else throw new IllegalArgumentException(message + "is not correct type");
				break;
			case updateStatus:
				break;
			default:
				throw new IllegalArgumentException("the request " + request + " not implemented in the client.");
		}

	} // END of public void getFromServer(Object message) {

	//all the scene enteractions


	private void setRequestsComboBox() {
		getRequests();
		ArrayList<String> al = new ArrayList<>();
		for(Requirement req : ReqListForClient) {
			al.add(Integer.toString((req.getID())));
		}

		list = FXCollections.observableArrayList(al);
		cmbRequests.setItems(list);
	} //END OF private void setRequestsComboBox()

	//TODO : add the other combo box
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setRequestsComboBox() ;
	}

	//private methods
	private void getRequests() {
		clientRequestFromServer commend = new clientRequestFromServer("getRequirement");
		Object[] o = new Object[1];
		o[0] = commend;
		ClientGUI.client.handleMessageFromClientUI(o);
	}


}// end of FormController class

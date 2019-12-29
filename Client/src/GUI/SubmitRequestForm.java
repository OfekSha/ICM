package GUI;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SubmitRequestForm extends UserForm implements Initializable, IcmForm {
	
	ObservableList<String> list;
	@FXML
	public TextArea taRequestDetails;
	@FXML
	public TextArea taRequestReason;
	@FXML
	public TextArea taComment;
	@FXML
	private Button btnSubmit;
	@FXML
	private ComboBox cmbSystems;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		setSystemsComboBox() ;

	}


	@SuppressWarnings("unchecked")
	public void setSystemsComboBox() 
	{
		ArrayList<String> al = new ArrayList<String>();	
	al.add("ICM");
	al.add("Moodle");
	al.add("Information Station");
	list = FXCollections.observableArrayList(al);
	cmbSystems.setItems(list);
	}

public void getRequestData() {
	String rd= taRequestDetails.getText();
	String rr=taRequestReason.getText();
	String com=taComment.getText();
	String sys=null ;
    Object temp =cmbSystems.getSelectionModel().getSelectedItem();
    if (temp!=null) sys=temp.toString();
    // TODO: move  logics to a controller
    if(rd.equals("")|| rr.equals("") || sys ==null) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Information Dialog");
		//TODO: explain what fileds are missing
		alert.setHeaderText("Error: all fileds must be filed");
		alert.setContentText("");
		alert.showAndWait();	
    }
    else {
    	//TODO: clear fileds
    	Initiator init = new Initiator(user,null);
    	LocalDate start =  LocalDate.now();
    	//TODO: add doc
    	ChangeRequest change = new ChangeRequest(init, start,sys ,rd, rr, com,null);
    	Object msg = new clientRequestFromServer(requestOptions.addRequest, (Object)change);
    	ClientLauncher.client.handleMessageFromClientUI(msg);
    	
    }
	
}
public void getRequestID() {}
	
}



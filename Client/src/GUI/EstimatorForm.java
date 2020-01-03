package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.EstimatorController;
import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class EstimatorForm extends  StageSupervisorForm{


	
	@FXML
	public TextArea taRequestDetails;
	@FXML
	public TextArea taRequestReason;
	@FXML
	public TextArea taComment;
	@FXML
	private ComboBox<String> cmbChangeRequest;

	
	private ArrayList<ChangeRequest> reqList = new ArrayList<>() ;
	
	
	public void setArrayList(ArrayList<ChangeRequest> reqList) {
		this.reqList =  reqList;
	}
	@Override
	public void getFromServer(Object message) {
		EstimatorController.messageFromServer(message,this);
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ClientLauncher.client.setClientUI(this);
		Object[] objectArr = new Object[4];
		objectArr[0]=ChargeRequestStages.meaningEvaluation;
		objectArr[1]=subStages.determiningDueTime;
		objectArr[2]=ChangeRequestStatus.ongoing;
		objectArr[3]=user.getUserName();
		Object msg = new clientRequestFromServer(requestOptions.getAllChangeRequestWithStatusAndStageAndSupervisor, objectArr);
		//ClientLauncher.client.handleMessageFromClientUI(msg);
	}
	public void setSystemsComboBox() {
		ArrayList<String> al = new ArrayList<>();
		for (ChangeRequest e : reqList) {
			al.add(e.getRequestID());
		}
		ObservableList<String> list = FXCollections.observableArrayList(al);
		cmbChangeRequest.setItems(list);
	}


	//TODO: the 1following  methods are from the class diagram:  
	public void getReport() {}

}

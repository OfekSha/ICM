package Controllers;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import java.time.LocalDate;

import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.InspectorForm;
import WindowApp.ClientLauncher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;

public class InspectorController {
	public static class requirmentForTable {
		private  SimpleStringProperty message; 
		private  SimpleStringProperty  id;
		private  SimpleObjectProperty<ChangeRequestStatus> status;
		//private  SimpleStringProperty stage; // for test only
		//private  SimpleStringProperty dueTime; // for test only
		private SimpleObjectProperty<ProcessStage>stage;
		private SimpleObjectProperty<LocalDate> dueTime;
		
		
		public String getMessage() {
			return message.get();
		}
		public String getId() {
			return id.get();
		}
		public ChangeRequestStatus getStatus() {
			return status.get();
		}
		public ProcessStage getStage() {
			return stage.get();
		}
		public LocalDate getDueTime() {
			return dueTime.get();
		}
		
		
		public requirmentForTable(ChangeRequest req) {
			id=new SimpleStringProperty(req.getRequestID());
			status = new SimpleObjectProperty<ChangeRequestStatus>(req.getStatus());
			message= new SimpleStringProperty("test");
			stage= new SimpleObjectProperty<ProcessStage>(req.stage);	
		}
		
	}
	public static ArrayList <requirmentForTable> requirmentForTableList(ArrayList<ChangeRequest> reqList) {
		ArrayList <requirmentForTable> newList= new ArrayList<requirmentForTable>();
		for (ChangeRequest req : reqList) newList.add(new requirmentForTable(req));
		return newList;
	}
	// public ArrayList <lReport> getClosedRequests(){}
	
	public static void freeze(requirmentForTable req) {
		for (ChangeRequest request : InspectorForm.reqList)
			if (request.getRequestID()==req.getId()) {
				request.changeStatus(ChangeRequestStatus.suspended);
			requestToServerProtocol(new clientRequestFromServer(requestOptions.updateStatus,request));
			break;
			}	
	}
	public static void unfreeze(requirmentForTable req) {
		for (ChangeRequest request : InspectorForm.reqList)
			if (request.getRequestID()==req.getId()) {
				request.changeStatus(ChangeRequestStatus.ongoing);
			requestToServerProtocol(new clientRequestFromServer(requestOptions.updateStatus,request));
			break;
			}	
	}
	enum reqFilter{
		freeze,unfreeze,estimator,executionLeader,dueTime,close,extension
	}
	private static reqFilter filter;
	public static void watchRequests(MenuItem item) {
		switch (item.getText()) { // choose what string to send to server
		case "Freeze Requests":
			filter=reqFilter.freeze; // need set object
			break;
		case "Unfreeze Requests":
			filter=reqFilter.unfreeze;
			break;
		case "Approve Estimator":
			filter=reqFilter.estimator;
			break;
		case "Approve Execution Leader":
			filter=reqFilter.executionLeader;
			break;
		case "Approve Due Time":
			filter=reqFilter.dueTime;
			break;
		case "Waiting for close":
			filter=reqFilter.close;
			break;
		case "Waiting for Extension":
			filter=reqFilter.extension;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(requestOptions.getAll,filter); // create new
		// request
		// protocol to
		// server
		requestToServerProtocol(toServer);
	}

	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI((Object) req);
	}

	public static void messageFromServer(Object message) {
		// @@ need to add testing for message
		clientRequestFromServer respone = (clientRequestFromServer) message;
		switch (respone.getRequest()) {
		case getRequirement:
			InspectorForm.reqList=respone.getObj();
			break;
		case getAll:
			InspectorForm.reqList=respone.getObj();
			break;
		}
	}

}

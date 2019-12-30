package Controllers;

import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import java.time.LocalDate;

import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.InspectorForm;
import WindowApp.ClientLauncher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;

public class InspectorController {

	// functions and class to work with Table view :
	/**
	 * 
	 * This class make changeRequest class compatible with table view component.
	 * 
	 * @author ooffe
	 */
	public static class requirmentForTable {
		private SimpleStringProperty message;
		private SimpleStringProperty id;
		private SimpleObjectProperty<ChangeRequestStatus> status;
		private SimpleObjectProperty<ProcessStage> stage;
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
			id = new SimpleStringProperty(req.getRequestID());
			status = new SimpleObjectProperty<ChangeRequestStatus>(req.getStatus());
			message = new SimpleStringProperty("test");
			stage = new SimpleObjectProperty<ProcessStage>(req.getProcessStage());
		}

	}

	public static ArrayList<requirmentForTable> requirmentForTableList(ArrayList<ChangeRequest> reqList) {
		ArrayList<requirmentForTable> newList = new ArrayList<requirmentForTable>();
		for (ChangeRequest req : reqList)
			newList.add(new requirmentForTable(req));
		return newList;
	}

	public static ChangeRequest getReq(requirmentForTable tableReq) throws NullPointerException {
		for (ChangeRequest regular : InspectorForm.reqList) {
			if (regular.getRequestID() == tableReq.getId())
				return regular;
		}
		throw new NullPointerException("The table view not match to the regular change requests.");
	}
	// public ArrayList <lReport> getClosedRequests(){}

	// functions for bottom buttons:
	private static void changeStatus(requirmentForTable req, ChangeRequestStatus newStatus) {
		ChangeRequest selectedRequest = getReq(req);
		selectedRequest.setStatus(newStatus);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateStatus, selectedRequest));
	}

	public static void freeze(requirmentForTable req) {
		changeStatus(req,ChangeRequestStatus.suspended);
	}

	public static void unfreeze(requirmentForTable req) {
		changeStatus(req,ChangeRequestStatus.ongoing);
	}

	public static void closeRequest(requirmentForTable req) {
		changeStatus(req,ChangeRequestStatus.closed);
	}

	public static void approveSubStage(boolean approve, requirmentForTable req) {
		ChangeRequest selectedRequest = getReq(req);
		if (approve == true)
			selectedRequest.getProcessStage().changecurretSubStage(subStages.supervisorAction);
	}

	public static void changeRole(requirmentForTable req, User user) {
		ChangeRequest selectedRequest = getReq(req);
		selectedRequest.getProcessStage().newStageSupervisor(user);
		selectedRequest.getProcessStage().changecurretSubStage(subStages.supervisorAction);
	}

	// functions for watch button:

	enum reqFilter {
		freeze, unfreeze, estimator, executionLeader, dueTime, close, extension
	}

	private static reqFilter filter;

	public static void watchRequests(MenuItem item) {
		switch (item.getText()) { // choose what string to send to server
		case "Freeze Requests":
			filter = reqFilter.freeze;
			break;
		case "Unfreeze Requests":
			filter = reqFilter.unfreeze;
			break;
		case "Approve Estimator":
			filter = reqFilter.estimator;
			break;
		case "Approve Execution Leader":
			filter = reqFilter.executionLeader;
			break;
		case "Approve Due Time":
			filter = reqFilter.dueTime;
			break;
		case "Waiting for close":
			filter = reqFilter.close;
			break;
		case "Waiting for Extension":
			filter = reqFilter.extension;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(requestOptions.getAll, filter);
		requestToServerProtocol(toServer);
	}

	// functions for server - client protocol:

	/**
	 * 
	 * function to send to server client Request.
	 * 
	 * @param req
	 */
	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI((Object) req);
	}

	/**
	 * 
	 * Function to get message from server.
	 * 
	 * @param message
	 */
	public static void messageFromServer(Object message) {
		// @@ need to add testing for message
		clientRequestFromServer respone = (clientRequestFromServer) message;
		switch (respone.getRequest()) {
		case getRequirement:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) respone.getObject();
			break;
		case getAll:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) respone.getObject();
			filter = (reqFilter) respone.getObject();
			break;
		default:
			break;
		}
	}

}

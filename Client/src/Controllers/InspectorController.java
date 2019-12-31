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
import Entity.User.ICMPermissions;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.InspectorForm;
import WindowApp.ClientLauncher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
			int stageNumber = req.getProcessStage().getCurrentStage().ordinal();
			id = new SimpleStringProperty(req.getRequestID());
			status = new SimpleObjectProperty<ChangeRequestStatus>(req.getStatus());
			message = new SimpleStringProperty("test");
			stage = new SimpleObjectProperty<ProcessStage>(req.getProcessStage());
			dueTime = new SimpleObjectProperty<LocalDate>(req.getProcessStage().getDates()[stageNumber][1]);
			if (req.getProcessStage().getDates()[stageNumber][1] == null)
				dueTime = new SimpleObjectProperty<LocalDate>(LocalDate.now());
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
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, selectedRequest));
	}

	public static void freeze(requirmentForTable req) {
		changeStatus(req, ChangeRequestStatus.suspended);
	}

	public static void unfreeze(requirmentForTable req) {
		changeStatus(req, ChangeRequestStatus.ongoing);
	}

	public static void closeRequest(requirmentForTable req) {
		changeStatus(req, ChangeRequestStatus.closed);
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
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, selectedRequest));
		switch(selectedRequest.getProcessStage().getCurrentStage()) {
		case meaningEvaluation:user.getICMPermissions().add(ICMPermissions.estimator);
		break;
		case execution:user.getICMPermissions().add(ICMPermissions.executionLeader);
		break;
		}
		
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateUser, user));
		
	}

	// functions for watch button:

	public enum reqFilter {
		freeze, unfreeze, estimator, executionLeader, dueTime, close, extension
	}

	/*private static void filterTableView(reqFilter filter) {
		switch (filter) {
		case close:
			for (ChangeRequest req : InspectorForm.reqList) {
				if (req.getProcessStage().getCurrentStage() != ChargeRequestStages.closure)
					InspectorForm.reqList.remove(req);
			}
			break;
		case estimator:
			for (ChangeRequest req : InspectorForm.reqList) {
				if (req.getProcessStage()==null) System.out.println("server need to create estimator");
				else if (req.getProcessStage().getCurrentStage() != ChargeRequestStages.meaningEvaluation) InspectorForm.reqList.remove(req);
				else if (req.getProcessStage().getCurrentSubStage() != subStages.supervisorAllocation)
					InspectorForm.reqList.remove(req);
			}
			break;
		case executionLeader:
			for (ChangeRequest req : InspectorForm.reqList) {
				if (req.getProcessStage()==null)  System.out.println("no stage need throw exception");
				if (req.getProcessStage().getCurrentStage() != ChargeRequestStages.execution) InspectorForm.reqList.remove(req);
				else if (req.getProcessStage().getCurrentSubStage() != subStages.supervisorAllocation)
					InspectorForm.reqList.remove(req);
			}
			break;
		case dueTime:
			for (ChangeRequest req : InspectorForm.reqList) {
				if (req.getProcessStage().getCurrentSubStage() != subStages.determiningDueTime)
					InspectorForm.reqList.remove(req);
			}
			break;
			default: break; // no filter
		}
	}*/

	public static reqFilter filter;
	private static MenuItem watchChoosed;;

	public static void watchRequests(MenuItem item) {
		Object toServerFilter = null;
		watchChoosed = item;
		requestOptions toServerOption = null;
		switch (item.getText()) { // choose what string to send to server
		case "Freeze Requests":
			filter = reqFilter.freeze;
			toServerFilter = ChangeRequestStatus.suspended;
			toServerOption = requestOptions.getChangeRequestBystatus;
			break;
		case "Unfreeze Requests":
			filter = reqFilter.unfreeze;
			toServerFilter = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getChangeRequestBystatus;
			break;
		case "Approve Estimator":
			filter = reqFilter.estimator;
			toServerFilter =new Object[3];
			((Object[])toServerFilter)[0]=(Object)ChargeRequestStages.meaningEvaluation; //the stage
			((Object[])toServerFilter)[1]=(Object)subStages.supervisorAllocation; // the sub stage 
			((Object[])toServerFilter)[2]=(Object)ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "Approve Execution Leader":
			filter = reqFilter.executionLeader;
			toServerFilter =new Object[3];
			((Object[])toServerFilter)[0]=(Object)ChargeRequestStages.execution; //the stage
			((Object[])toServerFilter)[1]=(Object)subStages.supervisorAllocation; // the sub stage 
			((Object[])toServerFilter)[2]=(Object)ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "Approve Due Time":
			filter = reqFilter.dueTime;
			toServerFilter =new Object[3];
			((Object[])toServerFilter)[0]=(Object)ChargeRequestStages.execution; //the stage
			((Object[])toServerFilter)[1]=(Object)subStages.determiningDueTime; // the sub stage 
			((Object[])toServerFilter)[2]=(Object)ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "Waiting for close":
			filter = reqFilter.close;
			toServerFilter = ChangeRequestStatus.closed;
			toServerOption = requestOptions.getChangeRequestBystatus;
			break;
		case "Waiting for Extension":
			filter = reqFilter.extension;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		requestToServerProtocol(toServer);
	}

// function for popup windows:
	public static ArrayList estimators;
	public static requirmentForTable selctedReqFromTable;
	public static void getEstimators() {
		requestToServerProtocol(
				new clientRequestFromServer(requestOptions.getUsersByICMPermissions, ICMPermissions.estimator));
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
		clientRequestFromServer respone = (clientRequestFromServer) message;
		switch (respone.getRequest()) {
		case getRequirement:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) respone.getObject();
			break; // @@ not work for now.
		case getAll:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) respone.getObject();
			//filterTableView(filter);
			// filter = (reqFilter)((Object[]) respone.getObject())[1];
			break;
		case getChangeRequestBystatus:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) ((Object[]) respone.getObject())[0];
			//filterTableView(filter);
			// filter = (reqFilter)((Object[]) respone.getObject())[1];
			break;
		case getUsersByICMPermissions:
			// InspectorForm.reqList = (ArrayList<ChangeRequest>) ((Object[])
			// respone.getObject())[0];
			// filter = (reqFilter)((Object[]) respone.getObject())[1];
			estimators = (ArrayList<User>) ((Object[]) respone.getObject())[0];
			break;
		case updateChangeRequest:
			watchRequests(watchChoosed);
			break;
		case getAllChangeRequestWithStatusAndStage:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) ((Object[]) respone.getObject())[0];
			break;
		default:
			throw new IllegalArgumentException(
					"the request " + respone.getRequest() + " not implemented in the inspector controller.");
		}
	}

}

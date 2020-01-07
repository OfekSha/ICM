package Controllers;

import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User;
import Entity.User.ICMPermissions;
import Entity.User.Job;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.InspectorForm;
import WindowApp.ClientLauncher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class InspectorController {

	// functions and class to work with Table view :
	/**
	 * 
	 * This class make changeRequest class compatible with table view component.
	 * 
	 * @author Ofek
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
			status = new SimpleObjectProperty<>(req.getStatus());
			message = new SimpleStringProperty("test");
			stage = new SimpleObjectProperty<>(req.getProcessStage());
			dueTime = new SimpleObjectProperty<>(req.getProcessStage().getDates()[stageNumber][1]);
		}

	}

	public static ArrayList<requirmentForTable> requirementForTableList(ArrayList<ChangeRequest> reqList) {
		ArrayList<requirmentForTable> newList = new ArrayList<>();
		for (ChangeRequest req : reqList)
			newList.add(new requirmentForTable(req));
		return newList;
	}

	public static ChangeRequest getReq(requirmentForTable tableReq) throws NullPointerException {
		for (ChangeRequest regular : InspectorForm.reqList) {
			if (regular.getRequestID().equals(tableReq.getId()))
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

	// functions for watch button:

	private static MenuItem watchChoosed; // the last menu item in watch button that chosen.

	public static void watchRequests(MenuItem item) {
		Object toServerFilter = null;
		watchChoosed = item;
		requestOptions toServerOption = null;
		switch (item.getText()) { // choose what string to send to server
		case "Freeze Requests":
			toServerFilter = ChangeRequestStatus.suspended;
			toServerOption = requestOptions.getChangeRequestByStatus;
			break;
		case "Unfreeze Requests":
			toServerFilter = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getChangeRequestByStatus;
			break;
		case "Approve Estimator":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.meaningEvaluation; // the stage
			((Object[]) toServerFilter)[1] = subStages.supervisorAllocation; // the sub stage
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "Approve Execution Leader":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.execution; // the stage
			((Object[]) toServerFilter)[1] = subStages.supervisorAllocation; // the sub stage
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "Approve Due Time":
			toServerFilter = new Object[2];
			((Object[]) toServerFilter)[0] = subStages.ApprovingDueTime; // the sub stage
			((Object[]) toServerFilter)[1] = ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndSubStageOnly;
			break;
		case "Waiting for close":
			toServerFilter = new Object[2];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.closure; // the stage
			((Object[]) toServerFilter)[1] = ChangeRequestStatus.ongoing; // the status
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStageOnly;
			break;
		case "Waiting for Extension":
			toServerFilter = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getChangeRequestByStatus;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		requestToServerProtocol(toServer);
	}

// function for popup windows:
	public static ArrayList<User> informationEngineers;
	public static requirmentForTable selctedReqFromTable;

	public static void getInformationEngineers() {
		requestToServerProtocol(new clientRequestFromServer(requestOptions.getAllUsersByJob, Job.informationEngineer));
	}

	public static void approveDueTime(boolean approve, requirmentForTable req) {
		ChangeRequest selectedRequest = getReq(req);
		if (approve)
			selectedRequest.getProcessStage().setCurrentSubStage(subStages.supervisorAction);
		else {
			selectedRequest.getProcessStage().setCurrentSubStage(subStages.determiningDueTime);
			selectedRequest.getProcessStage().addDueDate(null);
		}
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, selectedRequest));
	}

	public static void changeRole(requirmentForTable req, User user) {
		ChangeRequest selectedRequest = getReq(req);
		selectedRequest.getProcessStage().newStageSupervisor(user); // set user to be supervisor
		selectedRequest.getProcessStage().setCurrentSubStage(subStages.determiningDueTime); // next sub stage

		switch (selectedRequest.getProcessStage().getCurrentStage()) {
		// give permission to user
		case meaningEvaluation:
			user.getICMPermissions().add(ICMPermissions.estimator);
			break;
		case execution:
			user.getICMPermissions().add(ICMPermissions.executionLeader);
			break;
		default:
			throw new IllegalArgumentException(
					"Inspector can\"t approve role to stage " + selectedRequest.getProcessStage().getCurrentStage());
		}
		// send request to server
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, selectedRequest));
		// send user with the new permission to the server.
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateUser, user));

	}

	public static void approveExtension(boolean approve, requirmentForTable req) {
		ChangeRequest selectedRequest = getReq(req);
		if (approve) {
			selectedRequest.getProcessStage().ExtensionRequestHandled();
		}
	}
	// functions for server - client protocol:

	/**
	 * 
	 * function to send to server client Request.
	 * 
	 * @param req - send class {@link clientRequestFromServer}
	 */
	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI(req);
	}

	/**
	 * 
	 * Function to get message from server.
	 * 
	 * @param message - get class {@link clientRequestFromServer}
	 */
	@SuppressWarnings("unchecked")
	public static void messageFromServer(Object message) {
		clientRequestFromServer response = (clientRequestFromServer) message;
		switch (response.getRequest()) {
		case getAllUsersByJob: // for windows: approve role.
			informationEngineers = (ArrayList<User>) ((Object[]) response.getObject())[0];
			break;
		case updateChangeRequest: // for windows: approve role,approve due date, and freeze/unfreeze/close request.
			watchRequests(watchChoosed);
			break;
		case getChangeRequestByStatus:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
			if (watchChoosed.getText().contains("Waiting for Extension")) {
				ArrayList<ChangeRequest> newList = new ArrayList<>();
				for (ChangeRequest req : InspectorForm.reqList) {
					if (req.getProcessStage().getWasThereAnExtensionRequest()[req.getProcessStage().getCurrentStage()
							.ordinal()] == 1)
						newList.add(req);
				}
				InspectorForm.reqList = newList;
			}
			break;
		case getAllChangeRequestWithStatusAndStage:
		case getAllChangeRequestWithStatusAndSubStageOnly:
		case getAllChangeRequestWithStatusAndStageOnly:
			InspectorForm.reqList = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
			break;
		default:
			throw new IllegalArgumentException(
					"the request " + response.getRequest() + " not implemented in the inspector controller.");
		}
	}

}

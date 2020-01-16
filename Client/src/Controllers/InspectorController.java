package Controllers;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.InspectorUpdateDescription;
import Entity.InspectorUpdateDescription.inspectorUpdateKind;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User;
import Entity.User.icmPermission;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.UserForm;
import javafx.scene.control.MenuItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class InspectorController extends StageSupervisorController {

	// functions for bottom buttons:
	private static void changeStatus(ChangeRequest req, ChangeRequestStatus newStatus) {
		req.setStatus(newStatus);
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
	}

	public static void freeze(ChangeRequest req) {
		changeStatus(req, ChangeRequestStatus.suspended);
	}

	public static void unfreeze(ChangeRequest req) {
		changeStatus(req, ChangeRequestStatus.ongoing);
	}

	public static void closeRequest(ChangeRequest req) {
		changeStatus(req, ChangeRequestStatus.closed);
	}

	// functions for watch button:
	@Override
	public void filterRequests(MenuItem item) {
		Object toServerFilter = null;
		filterSelected = item;
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
		case "Waiting for Extension":
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
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		messageToServer(toServer);
	}

	// function for pop up windows:
	public static ArrayList<User> informationEngineers;

	public static void getInformationEngineers() {
		messageToServer(
				new clientRequestFromServer(requestOptions.getAllUsersByJob, User.collegeStatus.informationEngineer));
	}

	public static void approveDueTime(boolean approve, ChangeRequest req, String reason) {
		InspectorUpdateDescription report;
		if (approve) {
			req.getProcessStage().setCurrentSubStage(subStages.supervisorAction);
			report = new InspectorUpdateDescription(UserForm.user, reason, LocalDate.now(),
					inspectorUpdateKind.approveDueTime);
			req.addInspectorUpdate(report);
		} else {
			req.getProcessStage().setCurrentSubStage(subStages.determiningDueTime);
			req.getProcessStage().setDueDate(null);
			report = new InspectorUpdateDescription(UserForm.user, reason, LocalDate.now(),
					inspectorUpdateKind.DisapproveDueTime);
			req.addInspectorUpdate(report);
		}
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
	}

	public static void changeRole(ChangeRequest req, User user) {
		req.getProcessStage().newStageSupervisor(user); // set user to be supervisor
		req.getProcessStage().setCurrentSubStage(subStages.determiningDueTime); // next sub stage
		switch (req.getProcessStage().getCurrentStage()) {
		// give permission to user
		case meaningEvaluation:
			user.getICMPermissions().add(icmPermission.estimator);
			req.getProcessStage().setStartDate(LocalDate.now());
			break;
		case execution:
			user.getICMPermissions().add(User.icmPermission.executionLeader);
			break;
		default:
			throw new IllegalArgumentException(
					"Inspector can\"t approve role to stage " + req.getProcessStage().getCurrentStage());
		}
		// send request to server
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
		// send user with the new permission to the server.
		messageToServer(new clientRequestFromServer(requestOptions.updateUser, user));

	}

	public static void approveExtension(boolean approve, ChangeRequest req, String reactionReason) {
		InspectorUpdateDescription report;
		if (approve) {
			LocalDate requestDate=req.getProcessStage().getExtensionRequestDate();
			req.getProcessStage().setFlagExtensionRequestHandled();
			req.getProcessStage().setDueDateExtension(requestDate);
			report = new InspectorUpdateDescription(UserForm.user, reactionReason, LocalDate.now(),
					inspectorUpdateKind.approveExtension);
		} else {
			req.getProcessStage().setFlagExtensionRequestNotHandled();
			report = new InspectorUpdateDescription(UserForm.user, reactionReason, LocalDate.now(),
					inspectorUpdateKind.DisapproveExtension);
		}
		req.getProcessStage().setExtensionRequestDate(null);// clear the extension date after approve or disapprove.
		req.addInspectorUpdate(report);
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
	}
	// functions for server - client protocol:

	/**
	 * Function to get message from server.
	 *
	 * @param message - get class {@link clientRequestFromServer}
	 */
	@SuppressWarnings("unchecked")
	public void  messageFromServer(Object message) {
		clientRequestFromServer response = (clientRequestFromServer) message;
		switch (response.getRequest()) {
		case getAllUsersByJob: // for windows: approve role.
			informationEngineers = (ArrayList<User>) ((Object[]) response.getObject())[0];
			break;
		case updateChangeRequest: // for windows: approve role,approve due date, and freeze/unfreeze/close
									// request.
			filterRequests(filterSelected);
			break;
		case getChangeRequestByStatus:
			requests = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
			if (filterSelected.getText().contains("Waiting for Extension")) {
				ArrayList<ChangeRequest> newList = new ArrayList<>();
				for (ChangeRequest req : requests) {
					if (req.getProcessStage().getWasThereAnExtensionRequest()[req.getProcessStage().getCurrentStage()
							.ordinal()] == 1)
						newList.add(req);
				}
				requests = newList;
			}
			break;
		case getAllChangeRequestWithStatusAndStage:
		case getAllChangeRequestWithStatusAndSubStageOnly:
		case getAllChangeRequestWithStatusAndStageOnly:
			requests = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
			break;
		case getDoc:
			DocmentTableForDownloadsController.downloded = (Document) response.getObject();
			DocmentTableForDownloadsController.wakeUpLunchedThread();
			break;
		default:
			throw new IllegalArgumentException(
					"the request " + response.getRequest() + " not implemented in the inspector controller.");
		}
	}

}

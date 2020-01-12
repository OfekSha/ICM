package Controllers;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.DocumentForTable;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.InspectorUpdateDescription;
import Entity.InspectorUpdateDescription.inspectorUpdateKind;
import Entity.ProcessStage;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User;
import Entity.User.ICMPermissions;
import Entity.User.Job;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.InspectorForm;
import GUI.UserForm;
import WindowApp.ClientLauncher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class InspectorController {

	public static ArrayList<ChangeRequest> requests = new ArrayList<ChangeRequest>();
	// public ArrayList <lReport> getClosedRequests(){}
	public static ChangeRequest selectedRequest;
	public static Thread inspetor;
	public static Document downloded;
	// functions for bottom buttons:
	private static void changeStatus(ChangeRequest req, ChangeRequestStatus newStatus) {
		req.setStatus(newStatus);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
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

	private static MenuItem watchChosen; // the last menu item in watch button that chosen.

	public static void watchRequests(MenuItem item) {
		Object toServerFilter = null;
		watchChosen = item;
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
		requestToServerProtocol(toServer);
	}

	// function for popup windows:
	public static ArrayList<User> informationEngineers;

	public static void getInformationEngineers() {
		requestToServerProtocol(new clientRequestFromServer(requestOptions.getAllUsersByJob, Job.informationEngineer));
	}

	public static void approveDueTime(boolean approve, ChangeRequest req,String reason) {
		InspectorUpdateDescription report;;
		if (approve) {
			req.getProcessStage().setCurrentSubStage(subStages.supervisorAction);
			report= new InspectorUpdateDescription(UserForm.user,reason,LocalDate.now(),inspectorUpdateKind.approveDueTime);
			req.addInspectorUpdate(report);
		}
		else {
			req.getProcessStage().setCurrentSubStage(subStages.determiningDueTime);
			req.getProcessStage().setDueDate(null);
			report= new InspectorUpdateDescription(UserForm.user,reason,LocalDate.now(),inspectorUpdateKind.DisapproveDueTime);
			req.addInspectorUpdate(report);
		}
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
	}

	public static void changeRole(ChangeRequest req, User user) {
		req.getProcessStage().newStageSupervisor(user); // set user to be supervisor
		req.getProcessStage().setCurrentSubStage(subStages.determiningDueTime); // next sub stage

		switch (req.getProcessStage().getCurrentStage()) {
			// give permission to user
			case meaningEvaluation:
				user.getICMPermissions().add(ICMPermissions.estimator);
				break;
			case execution:
				user.getICMPermissions().add(ICMPermissions.executionLeader);
				break;
			default:
				throw new IllegalArgumentException(
						"Inspector can\"t approve role to stage " + req.getProcessStage().getCurrentStage());
		}
		// send request to server
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
		// send user with the new permission to the server.
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateUser, user));

	}

	public static void approveExtension(boolean approve, ChangeRequest req,String reactionReason) {
		InspectorUpdateDescription report;
		if (approve) {
			req.getProcessStage().setFlagExtensionRequestHandled();
			report= new InspectorUpdateDescription(UserForm.user,reactionReason,LocalDate.now(),inspectorUpdateKind.approveExtension);
		}else {
			req.getProcessStage().setFlagExtensionRequestNotHandled();
			report= new InspectorUpdateDescription(UserForm.user,reactionReason,LocalDate.now(),inspectorUpdateKind.DisapproveExtension);
		}
		req.addInspectorUpdate(report);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, req));
	}
	// functions for server - client protocol:

	/**
	 * function to send to server client Request.
	 *
	 * @param req - send class {@link clientRequestFromServer}
	 */
	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI(req);
	}

	/**
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
				watchRequests(watchChosen);
				break;
			case getChangeRequestByStatus:
				requests = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
				if (watchChosen.getText().contains("Waiting for Extension")) {
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
				downloded=(Document) response.getObject();
				wakeUpLunchedThread();
				break;
			default:
				throw new IllegalArgumentException(
						"the request " + response.getRequest() + " not implemented in the inspector controller.");
		}
	}
	
	
	/** creates the list of documents witch are attached to the  change request for the table 
	 * @return  DocumentForTable array list for table
	 * @see DocumentForTable
	 */
	public static  ArrayList<DocumentForTable> DocumentForTableList() {
		ArrayList<DocumentForTable> newList = new ArrayList<>();
		for (Document doc : selectedRequest.getDoc())
			newList.add(new DocumentForTable(doc));
		return newList;
	} //END  of DocumentForTableList() 
	
	public static void askForDownload(Document doc) {
		requestToServerProtocol(new clientRequestFromServer(requestOptions.getDoc,doc));
		putLunchedThreadToSleep();
		Download();
	} //END of askForDownload()
	
	public static void Download() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(downloded.getFileName());
		File file =fileChooser.showSaveDialog(null);
	if (file!=null) {	
		try {
			OutputStream os = new FileOutputStream(file);
			// Starts writing the bytes in it
			os.write(downloded.mybytearray);
			os.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
	} //END of Download()
	

	/** Saves the the lunched thread and puts it to  sleep
	 * 
	 * - saves it so  wakeUpLunchedThread would be able to wake it up
	 * 
	 */
	public static void putLunchedThreadToSleep(){
		inspetor = Thread.currentThread();
		try {
			inspetor.sleep(9999999);
		} catch (InterruptedException e) {
		}
	}
	/** Wakes up the lunched thread
	 * 
	 */
	public static void wakeUpLunchedThread() {
		try {
			inspetor.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

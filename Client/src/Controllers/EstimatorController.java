package Controllers;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.scene.control.MenuItem;

import java.time.LocalDate;
import java.util.ArrayList;

import Controllers.InspectorController.requirmentForTable;

public class EstimatorController {
	
	public static ArrayList <ChangeRequest> requests; // requests the controller holds
	public static ChangeRequest selectedRequest; // request that was selected from the forms.
	
	public static void setSelectedRequest(requirmentForTable selectedReq) {
		selectedRequest=getReq(selectedReq);
	}
	public static ChangeRequest getReq(requirmentForTable tableReq) throws NullPointerException {
		for (ChangeRequest regular : requests) {
			if (regular.getRequestID().equals(tableReq.getId()))
				return regular;
		}
		throw new NullPointerException("The table view not match to the regular change requests.");
	}
	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI(req);
	}
	
	@SuppressWarnings("unchecked")
	public static void messageFromServer(Object message) {
		// @@ need to add testing for message
		clientRequestFromServer response = (clientRequestFromServer) message;
		//Object[] objectArray;
		switch (response.getRequest()) {
			case getChangeRequestByStatus:
				requests = (ArrayList <ChangeRequest>) ((Object[])response.getObject())[0];
				break;
			default:throw new IllegalArgumentException(
					"the request " + response.getRequest() + " not implemented in the Estimator controller.");	
		}
	}
	
	
	//function for request:
	public static void setDueTime(ChangeRequest request, LocalDate date) {
		request.getProcessStage().addDueDate(date);
		request.getProcessStage().setCurrentSubStage(subStages.ApprovingDueTime);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}
	public static void setReport(ChangeRequest request) {
		//TODO:need to set report 
		request.getProcessStage().setCurrentSubStage(subStages.supervisorAllocation);
		request.getProcessStage().setCurrentStage(ChargeRequestStages.examinationAndDecision);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}

	public static void askExtension(ChangeRequest request,String explanation) {
		request.getProcessStage().ExtensionRequestMade();
		request.getProcessStage().inputExtensionExplanation(explanation);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}
	// end functions for request.
	
	public static void filterRequests(MenuItem menuItem) {
		Object toServerFilter = null;
		requestOptions toServerOption = null;
		switch (menuItem.getText()) { // choose what string to send to server
		case "No approved due time":
			toServerFilter = new Object[3];
			((Object[])toServerFilter)[0]=ChargeRequestStages.meaningEvaluation;
			((Object[])toServerFilter)[1]=subStages.determiningDueTime;
			((Object[])toServerFilter)[2]=ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "In need of a Report":
			toServerFilter = new Object[3];
			((Object[])toServerFilter)[0]=ChargeRequestStages.meaningEvaluation;
			((Object[])toServerFilter)[1]=subStages.supervisorAction;
			((Object[])toServerFilter)[2]=ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		requestToServerProtocol(toServer);
		
	}
	
	
}

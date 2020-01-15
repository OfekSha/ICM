package Controllers;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.EstimatorReport;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.RequestTableView.requirementForTable;
import Entity.clientRequestFromServer.requestOptions;
import GUI.UserForm;
import WindowApp.ClientLauncher;
import javafx.scene.control.MenuItem;


import java.time.LocalDate;
import java.util.ArrayList;

public class EstimatorController {
	
	public static ArrayList <ChangeRequest> requests; // requests the controller holds
	public static ChangeRequest selectedRequest; // request that was selected from the forms.
	private MenuItem filterSelected;
	public static void setSelectedRequest(requirementForTable selectedReq) {
		selectedRequest=getReq(selectedReq);
	}
	public static ChangeRequest getReq(requirementForTable tableReq) throws NullPointerException {
		for (ChangeRequest regular : requests) {
			if (Integer.toString(regular.getRequestID()).equals(tableReq.getId()))
				return regular;
		}
		throw new NullPointerException("The table view not match to the regular change requests.");
	}
	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI((Object)req);
	}
	
	@SuppressWarnings("unchecked")
	public static void messageFromServer(Object message) {
		// @@ need to add testing for message
		clientRequestFromServer response = (clientRequestFromServer) message;
		//Object[] objectArray;
		switch (response.getRequest()) { //TODO too few cases in switch
			case getAllChangeRequestWithStatusAndStage:
				requests = (ArrayList <ChangeRequest>) ((Object[])response.getObject())[0];
				break;
			case getDoc:
				DocmentTableForDownloadsController.downloded=(Document) response.getObject();
				DocmentTableForDownloadsController.wakeUpLunchedThread();
				break;
			case updateProcessStage: 
				filterRequests();
				break;
			default:throw new IllegalArgumentException(
					"the request " + response.getRequest() + " not implemented in the Estimator controller.");	
		}
	}
	
	
	//function for request:
	public static void setDueTime(ChangeRequest request, LocalDate date) {
		request.getProcessStage().setDueDate(date);
		request.getProcessStage().setCurrentSubStage(subStages.ApprovingDueTime);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}
	public static void setReport(ChangeRequest request,String location,String changeDescription,String desiredResult,String constraints,String risks) {
		EstimatorReport report= new EstimatorReport(UserForm.user, location, changeDescription, desiredResult, constraints, risks, LocalDate.now());
		request.getProcessStage().setEstimatorReport(report); // set new empty report for request.
		//@@ TODO: need to add due date in days.. need int or string.
		request.getProcessStage().setEndDate(LocalDate.now());
		request.getProcessStage().setCurrentSubStage(subStages.supervisorAllocation);
		request.getProcessStage().setCurrentStage(ChargeRequestStages.examinationAndDecision);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}

	public static void askExtension(ChangeRequest request,String explanation) {
		request.getProcessStage().setFlagExtensionRequested();
		request.getProcessStage().setExtensionExplanation(explanation);
		requestToServerProtocol(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}
	// end functions for request.
	
	public static void filterRequests(MenuItem menuItem) {
		filterSelected=menuItem;
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

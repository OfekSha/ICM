package Controllers;

import java.time.LocalDate;
import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.RequestTableView.requirementForTable;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.scene.control.MenuItem;

public abstract class StageSupervisorController {
	public static ArrayList <ChangeRequest> requests; // requests the controller holds
	public static ChangeRequest selectedRequest; // request that was selected from the forms.
	protected static MenuItem filterSelected; // menu item choose to filter.
	abstract public void messageFromServer(Object message); // handle server-client message.
	abstract public void filterRequests(MenuItem menuItem);
	abstract public StageSupervisorController getController();
	static public StageSupervisorController controller;
	public StageSupervisorController(){
		requests=new ArrayList <ChangeRequest>();
		controller=getController();
	}
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
	public static boolean checkExtension(ChangeRequest request) {
		LocalDate threeDaysBefore=request.getProcessStage().getDueDate().minusDays(3);
		return threeDaysBefore.isBefore(LocalDate.now());
	}
	//function for request:
	public static void askExtension(ChangeRequest request,String explanation) {
		if(checkExtension(request)) {
		request.getProcessStage().setFlagExtensionRequested();
		request.getProcessStage().setExtensionExplanation(explanation);
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
		}
	}
	public void setDueTime(ChangeRequest request, LocalDate date) {
		request.getProcessStage().setDueDate(date);
		request.getProcessStage().setCurrentSubStage(subStages.ApprovingDueTime);
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}
	// end functions for request.
	protected static void messageToServer(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI((Object)req);
	}

}

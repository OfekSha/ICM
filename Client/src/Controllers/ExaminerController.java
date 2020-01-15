package Controllers;

import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.RequestTableView.requirementForTable;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.scene.control.MenuItem;

public class ExaminerController {
	public static ArrayList <ChangeRequest> requests; // requests the controller holds
	public static ChangeRequest selectedRequest;
	private static MenuItem filterSelected;
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
	public static void filterRequests(MenuItem menuItem) {
		filterSelected=menuItem;
		Object toServerFilter = null;
		requestOptions toServerOption = null;
		switch (menuItem.getText()) { // choose what string to send to server
		case "No approved due time":
			toServerFilter = new Object[3];
			((Object[])toServerFilter)[0]=ChargeRequestStages.examination;
			((Object[])toServerFilter)[1]=subStages.determiningDueTime;
			((Object[])toServerFilter)[2]=ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "In need of a Report":
			toServerFilter = new Object[3];
			((Object[])toServerFilter)[0]=ChargeRequestStages.examination;
			((Object[])toServerFilter)[1]=subStages.supervisorAction;
			((Object[])toServerFilter)[2]=ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		requestToServerProtocol(toServer);
		
	}
	private static void requestToServerProtocol(clientRequestFromServer req) { // send to server request protocol.
		ClientLauncher.client.handleMessageFromClientUI((Object)req);
	}
}

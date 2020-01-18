package Controllers;

import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;

import Entity.clientRequestFromServer.requestOptions;

import javafx.scene.control.MenuItem;

public class ExaminerController extends StageSupervisorController {
	@Override
	public void filterRequests(MenuItem menuItem) {
		filterSelected = menuItem;
		Object toServerFilter = null;
		requestOptions toServerOption = null;
		switch (menuItem.getText()) { // choose what string to send to server
		case "No approved due time":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.examination;
			((Object[]) toServerFilter)[1] = subStages.determiningDueTime;
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "In need of an exam":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.examination;
			((Object[]) toServerFilter)[1] = subStages.supervisorAction;
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		messageToServer(toServer);

	}

	@Override
	public void messageFromServer(Object message) {
		clientRequestFromServer response = (clientRequestFromServer) message;
		switch (response.getRequest()) {
		case updateProcessStage:
		case updateChangeRequest:
		case updateStatus:
			filterRequests(filterSelected);
			break;
		case getAllChangeRequestWithStatusAndStage:
			requests = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
			break;
		default:
			throw new IllegalArgumentException(
					"the request " + response.getRequest() + " not implemented in the examiner controller.");
		}
	}
	@Override
	public StageSupervisorController getController() {
		return this;
	}
}

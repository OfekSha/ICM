package Controllers;

import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.clientRequestFromServer.requestOptions;
import javafx.scene.control.MenuItem;

public class ChairmanController extends StageSupervisorController {
	
	@Override
	public void filterRequests(MenuItem menuItem) {
		filterSelected = menuItem;
		Object toServerFilter = null;
		requestOptions toServerOption = null;
		switch (menuItem.getText()) { // choose what string to send to server
		case "In need of a Approvement":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.examinationAndDecision;
			((Object[]) toServerFilter)[1] = subStages.determiningDueTime;
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "In need of Examiner Appointment":
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
	
	public void messageFromServer(Object message) {
		// TODO Auto-generated method stub

	}

}

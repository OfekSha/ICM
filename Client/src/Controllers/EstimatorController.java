package Controllers;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.EstimatorReport;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;

import Entity.clientRequestFromServer.requestOptions;
import GUI.UserForm;

import javafx.scene.control.MenuItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class EstimatorController extends StageSupervisorController {

	public void messageFromServer(Object message) {
		// @@ need to add testing for message
		clientRequestFromServer response = (clientRequestFromServer) message;
		// Object[] objectArray;
		switch (response.getRequest()) { // TODO too few cases in switch
		case getAllChangeRequestWithStatusAndStage:
			requests = (ArrayList<ChangeRequest>) ((Object[]) response.getObject())[0];
			break;
		case getDoc:
			DocmentTableForDownloadsController.downloded = (Document) response.getObject();
			DocmentTableForDownloadsController.wakeUpLunchedThread();
			break;
		case updateProcessStage:
		case updateChangeRequest: // when update done, do refresh.
			filterRequests(filterSelected);
			break;
		default:
			throw new IllegalArgumentException(
					"the request " + response.getRequest() + " not implemented in the Estimator controller.");
		}
	}

	// function for request:
	public static void  setReport(ChangeRequest request, String location, String changeDescription, String desiredResult,
			String constraints, String risks) {
		EstimatorReport report = new EstimatorReport(UserForm.user, location, changeDescription, desiredResult,
				constraints, risks, 11);
		request.getProcessStage().setEstimatorReport(report); // set new empty report for request.
		// @@ TODO: need to add due date in days.. need int or string.
		request.getProcessStage().setEndDate(LocalDate.now());
		request.getProcessStage().setCurrentSubStage(subStages.supervisorAllocation);
		request.getProcessStage().setCurrentStage(ChargeRequestStages.examinationAndDecision);
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
	}

	// end functions for request.
	@Override
	public void filterRequests(MenuItem menuItem) {
		filterSelected = menuItem;
		Object toServerFilter = null;
		requestOptions toServerOption = null;
		switch (menuItem.getText()) { // choose what string to send to server
		case "No approved due time":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.meaningEvaluation;
			((Object[]) toServerFilter)[1] = subStages.determiningDueTime;
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		case "In need of a Report":
			toServerFilter = new Object[3];
			((Object[]) toServerFilter)[0] = ChargeRequestStages.meaningEvaluation;
			((Object[]) toServerFilter)[1] = subStages.supervisorAction;
			((Object[]) toServerFilter)[2] = ChangeRequestStatus.ongoing;
			toServerOption = requestOptions.getAllChangeRequestWithStatusAndStage;
			break;
		}
		clientRequestFromServer toServer = new clientRequestFromServer(toServerOption, toServerFilter);
		messageToServer(toServer);

	}

}

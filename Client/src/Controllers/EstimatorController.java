package Controllers;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.EstimatorReport;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User.icmPermission;
import Entity.User;
import Entity.clientRequestFromServer.requestOptions;
import GUI.UserForm;

import javafx.scene.control.MenuItem;

import java.time.LocalDate;
import java.util.ArrayList;



public class EstimatorController extends StageSupervisorController {

	@SuppressWarnings("unchecked")
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
			String constraints, String risks,String dueDaysEstimate) {
		EstimatorReport report = new EstimatorReport(UserForm.user, location, changeDescription, desiredResult,
				constraints, risks, Integer.valueOf(dueDaysEstimate));
		request.getProcessStage().setEstimatorReport(report); // set new report for request.
		request.getProcessStage().setEndDate(LocalDate.now()); //set end date of this stage
		request.getProcessStage().setCurrentSubStage(subStages.supervisorAction);
		request.getProcessStage().setCurrentStage(ChargeRequestStages.examinationAndDecision); // next stage
		request.getProcessStage().setStartDate(LocalDate.now()); //start next stage is now.
		request.getProcessStage().setDueDate(LocalDate.now().plusDays(7)); // next stage due date 7 days
		messageToServer(new clientRequestFromServer(requestOptions.updateChangeRequest, request));
		// remove permission
		User myUser=UserForm.user;
		Object[] objToServer= {myUser,icmPermission.estimator};
		messageToServer(new clientRequestFromServer(requestOptions.removeUserIcmPermission, objToServer));
		
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

	@Override
	public StageSupervisorController getController() {
		return this;
	}

}

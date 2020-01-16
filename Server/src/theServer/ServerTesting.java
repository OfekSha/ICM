package theServer;

import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.User;
import Entity.User.icmPermission;
import queryHandler.QueryHandler;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * a class composed of tests the server dose
 *
 */
public class ServerTesting {

	private QueryHandler queryHandler;

	public enum whatHappened {
		failedNoPermission,
		failedAlreadyExists,
		success,
		failedIsFrozen,
		failedIsResponsibleForAstage
	 }
	  
	public ServerTesting(QueryHandler queryHandler) {
		 this.queryHandler = queryHandler;
	 }

	/**
	 * test whether the user can change users in DB
	 *
	 * @param changer - the user attempting to change a user in DB
	 * @return true if the changing user can change users
	 */
	public whatHappened testUpstingUserCanUpdateUsers(User changer) {
		EnumSet<icmPermission> Permissions = changer.getICMPermissions();
		if (Permissions.contains(icmPermission.informationTechnologiesDepartmentManager)
				|| Permissions.contains(icmPermission.inspector)) {
			return whatHappened.success;
		}
		return whatHappened.failedNoPermission;
	}// END  of testUpstingUserCanUpdateUsers


	/**
	 * tests whether the user can change the request
	 *
	 * @param changer - the user attempting to change the request
	 * @param request - the request he is attempting to change
	 * @return true if the changer can change this request
	 */
	public whatHappened testIfRequestIsFrozen(User changer, ChangeRequest request) {
		ChangeRequest inDB = queryHandler.getChangeRequestQuerys().getChangeRequest(request.getRequestID());
		if (inDB.getStatus().equals(ChangeRequestStatus.suspended) || inDB.getStatus().equals(ChangeRequestStatus.closed)) {
			EnumSet<icmPermission> Permissions = changer.getICMPermissions();
			if (Permissions.contains(icmPermission.informationTechnologiesDepartmentManager)
					|| Permissions.contains(icmPermission.inspector)) {
				return whatHappened.success;
			}
			else return whatHappened.failedIsFrozen;
		} else return whatHappened.success;
	}// END of testIfRequestIsfrozen()



	/** making sure the user is not using his icm Permission
	 * @param changedUser - the  user we want to remove a  permission to
	 * @param permission - the permission we want to remove from the user
	 * @return <li> if can be changed : arr[0] = whatHappend.success </li>
  				<li>if cant be changed : arr[0] = whatHappend.failedIsResponsibleForAstage , arr[1] =ArrayList<ChangeRequest> he is involved in  </li>

	 */
	public Object[] testIfUserIcmPermissionCanBeRemoved(User changedUser , icmPermission  permission) {
		Object[] arr = new Object[2];
		arr[0] = null;
		arr[1] = null;
		ChargeRequestStages stage =null;
		switch (permission) {
		case informationTechnologiesDepartmentManager:
			arr[0]= whatHappened.failedNoPermission;
			break;
		case inspector:
			stage= ChargeRequestStages.closure;
			break;
		case estimator:
			stage = ChargeRequestStages.meaningEvaluation;
			break;
		case executionLeader:
			stage = ChargeRequestStages.execution;
			break;
		case examiner:
			stage = ChargeRequestStages.examination;
			break;
		case changeControlCommitteeChairman:
			stage = ChargeRequestStages.examinationAndDecision;
			break;
		case changeControlCommitteeMember:
			stage = ChargeRequestStages.examination;
			break;
		}
		if(arr[0]==null) {
		ArrayList<ChangeRequest> involved  =queryHandler.getProccesStageQuerys().getProcessStageByStageSupervisorAndStage(changedUser,stage);
		if (involved.isEmpty())  arr[0]= whatHappened.success;
		else {arr[0]= whatHappened.failedIsResponsibleForAstage;
			arr[1] =involved;
		}
		}
		return arr;
	}// END of testifUserIcmPermissionCanBeRmoved()


	/**in buiding
	 *
	 */
	public boolean testsIFTheUserCanLogIn(User tryingToLogInUser) {
		return false;

	} // END testsIFTheUserCanLogIn

	//input
	public void setQueryHandler(QueryHandler queryHandlerIN) {
		queryHandler = queryHandlerIN;
	}

	//output
	public QueryHandler getQueryHandler() {
		return queryHandler;
	}

}// END of ServerTesting

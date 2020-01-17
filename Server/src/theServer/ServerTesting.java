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
		failedIsResponsibleForAstage,
		failedNoSuchUser,
		failedRequestAlreadyHasCorrectSupervisor,
		faildeUserMustBeCommitteeMember,
		faildeUserLimitReached
		
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

	
	
	
	/**
	 * @param changedUser - the user we want to add a permission to
	 * @param permission  - the permission we want to add
	 * @param request     - the permission related to the permission
	 * 
	 * 
	 *                    <dl>
	 *                    <dt>for permissions:
	 *                    estimator,executionLeader,examiner:</dt>
	 *                    <dd>- send a ChangeRequest entity which the user will
	 *                    supervise</dd>
	 *                    <dt>for
	 *                    permissions:informationTechnologiesDepartmentManager,changeControlCommitteeChairman,changeControlCommitteeMember
	 *                    send :</dt>
	 *                    <dd>- ChangeRequest ==null</dd>
	 *                    
	 *                    </dl>
	 * @return
	 */
	public Object[] testifUserIcmPermissionCanBeAdded(User changedUser , icmPermission  permission ,ChangeRequest request) {
		Object[] arr =new Object[2];
		ChangeRequest requestInDB;
		User currentSupervisor;
		ArrayList<User> usersInDB =new ArrayList();
		arr[0]=null;
		arr[1]=null;
		switch(permission) {
		case informationTechnologiesDepartmentManager:
			arr[0]= whatHappened.failedNoPermission;
			break;
		case inspector:
			// need to test if there is one already //excluded: admin
			usersInDB =queryHandler.getUserQuerys().getAllUsersWithICMPermissions(icmPermission.inspector);
			usersInDB=removeAdmin(usersInDB);
			 if(hasicmPermission(usersInDB,icmPermission.inspector,1)) {
				 arr[0]=whatHappened.faildeUserLimitReached;
				 arr[1]=usersInDB;
				 }
			 else arr[0]=whatHappened.success;
			break;
		case estimator:
			// test if there is no estimitor for the request
			requestInDB= queryHandler.getChangeRequestQuerys().getChangeRequest(request.getRequestID());
			currentSupervisor =requestInDB.getProcessStage().getStageSupervisor();
			if(currentSupervisor.getICMPermissions().contains(icmPermission.estimator)) {
				arr[0]=whatHappened.failedRequestAlreadyHasCorrectSupervisor;
				 arr[1]=requestInDB;
				}
			else arr[0]=whatHappened.success;		
			break;
		case executionLeader:
			// test if there is no executionLeader for the request
			requestInDB= queryHandler.getChangeRequestQuerys().getChangeRequest(request.getRequestID());
			currentSupervisor =requestInDB.getProcessStage().getStageSupervisor();
			if(currentSupervisor.getICMPermissions().contains(icmPermission.executionLeader)) {
				arr[0]=whatHappened.failedRequestAlreadyHasCorrectSupervisor;
			 arr[1]=requestInDB;

			}
			else arr[0]=whatHappened.success;	
			break;
		case examiner:
			// test if user is changeControlCommitteeMember
		if	(changedUser.getICMPermissions().contains(icmPermission.changeControlCommitteeMember)) {
			//  test if there is no examiner for the request
			requestInDB= queryHandler.getChangeRequestQuerys().getChangeRequest(request.getRequestID());
			currentSupervisor =requestInDB.getProcessStage().getStageSupervisor();
			if(currentSupervisor.getICMPermissions().contains(icmPermission.examiner)) {
				arr[0]=whatHappened.failedRequestAlreadyHasCorrectSupervisor;
				 arr[1]=requestInDB;
			}
			else arr[0]=whatHappened.success;	
		}
		else arr[0]=whatHappened.faildeUserMustBeCommitteeMember;
			break;
		case changeControlCommitteeChairman:
			// need to test if there is one already // excluded: admin
			usersInDB =queryHandler.getUserQuerys().getAllUsersWithICMPermissions(icmPermission.changeControlCommitteeChairman);
			usersInDB=removeAdmin(usersInDB);
			 if(hasicmPermission(usersInDB,icmPermission.changeControlCommitteeChairman,1))
				 {
				 arr[0]=whatHappened.faildeUserLimitReached;
				 arr[1]=usersInDB;
				 }			 
			 else arr[0]=whatHappened.success;
			break;
		case changeControlCommitteeMember:
			// test there are no more then three //excluded: admin
			usersInDB =queryHandler.getUserQuerys().getAllUsersWithICMPermissions(icmPermission.changeControlCommitteeMember);
			usersInDB=removeAdmin(usersInDB);
			 if(hasicmPermission(usersInDB,icmPermission.changeControlCommitteeMember,3)) {
				 arr[0]=whatHappened.faildeUserLimitReached;
				 arr[1]=usersInDB;
			 }
			 else arr[0]=whatHappened.success;
			break;
		}
		return arr;
	}//END of testifUserIcmPermissionCanBeAdded

	/** removes admin from the user list
	 * @param users
	 * @return
	 */
	private ArrayList<User> removeAdmin(ArrayList<User> users) {
		User admin = null;
		for (User u : users) {
			if (u.getUserName().equals("admin"))
			admin = u;
		}
		users.remove(admin);
		return users;
	}

	/** tests whether amount the given users has the icmPermission
	 * @param users
	 * @param permission 
	 * @param  amount of users we want with the rule
	 * @return true when one of the users in the list has the icmPermission
	 */
	private boolean hasicmPermission(ArrayList<User> users, icmPermission permission,int amount) {
		int cnt=0;
		for (User u : users) {
			if (u.getICMPermissions().contains(permission)) {
				cnt++;
				if (amount==cnt )return true;
			}
		}
		return false;
	}
	
	/**@in buiding
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

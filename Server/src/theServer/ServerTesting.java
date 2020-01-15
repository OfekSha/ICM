package theServer;

import java.util.EnumSet;

import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.User;
import Entity.User.icmPermission;
import queryHandler.QueryHandler;

/**
 * a class composed of tests the server dose
 *
 */
public class ServerTesting {
	
	 private  QueryHandler queryHandler;
	 
	  public enum whatHappend{
		failedNoPermission,
		failedAlreadyExists,
		success,
		failedIsFrozen
		
	 }
	  
	public  ServerTesting(QueryHandler queryHandler) {
		 this.queryHandler=queryHandler;
	 }

	/**  test whether the user can change users in DB
	 * @param changer - the user attempting to change a user in DB
	 * @return true if the changing user can change users
	 */
	public  whatHappend testUpstingUserCanUpdateUsers(User changer) {
		EnumSet<icmPermission> Permissions = changer.getICMPermissions();
		if (Permissions.contains(icmPermission.informationTechnologiesDepartmentManager)||Permissions.contains(icmPermission.inspector))
			return whatHappend.success;
		return whatHappend.failedNoPermission;
	}// END  of testUpstingUserCanUpdateUsers
	
	
	/**  tests whether the user can change the request
	 * @param changer - the user attempting to change the request
	 * @param request	- the request he is attempting to change
	 * @return true if the changer can change this request
	 */
	public  whatHappend testIfRequestIsfrozen(User changer , ChangeRequest request ) {
		ChangeRequest inDB =queryHandler.getChangeRequestQuerys().getChangeRequest(request.getRequestID());
		if(inDB.getStatus().equals(ChangeRequestStatus.suspended) ||inDB.getStatus().equals(ChangeRequestStatus.closed) ) {
			EnumSet<icmPermission> Permissions = changer.getICMPermissions();
			if(Permissions.contains(icmPermission.informationTechnologiesDepartmentManager)||Permissions.contains(icmPermission.inspector))
				return whatHappend.success;
			else return whatHappend.failedIsFrozen;
		}
		else return whatHappend.success;
	}// END of testIfRequestIsfrozen()
	
	
	
	
	/**@in buiding
	 * 
	 */
	public boolean testsIFTheUserCanLogIn(User tryingToLogInUser) {
		return false;
		
	} // END testsIFTheUserCanLogIn
	//input
	public  void setQueryHandler(QueryHandler queryHandlerIN) {
		queryHandler=queryHandlerIN;
	}
	
	//output
	 public QueryHandler  getQueryHandler() {
		 return queryHandler;
	 }
	
}// END of ServerTesting

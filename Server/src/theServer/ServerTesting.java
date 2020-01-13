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
	
	 private QueryHandler queryHandler;
	 
	 public ServerTesting(QueryHandler queryHandler) {
		 this.queryHandler=queryHandler;
	 }

	/**  test whether the user can change users in DB
	 * @param changer - the user attempting to change a user in DB
	 * @return true if the changing user can change users
	 */
	public boolean testUpstingUserCanUpdateUsers(User changer) {
		EnumSet<icmPermission> Permissions = changer.getICMPermissions();
		if (Permissions.contains(icmPermission.informationTechnologiesDepartmentManager)||Permissions.contains(icmPermission.inspector))
			return true;
		return false;
	}// END  of testUpstingUserCanUpdateUsers
	
	
	/**  tests whether the user can change the request
	 * @param changer - the user attempting to change the request
	 * @param request	- the request he is attempting to change
	 * @return true if the changer can change this request
	 */
	public boolean testIfRequestIsfrozen(User changer , ChangeRequest request ) {
		ChangeRequest inDB =queryHandler.getChangeRequestQuerys().getChangeRequest(request.getRequestID());
		if(inDB.getStatus().equals(ChangeRequestStatus.suspended) ||inDB.getStatus().equals(ChangeRequestStatus.closed) ) {
			EnumSet<icmPermission> Permissions = changer.getICMPermissions();
			if(Permissions.contains(icmPermission.informationTechnologiesDepartmentManager)||Permissions.contains(icmPermission.inspector))
				return true;
			else return false;
		}
		else return false;
	}// END of testIfRequestIsfrozen()
	
	
	public boolean testsIFTheUserCanLogIn(User tryingToLogInUser) {
		return false;
		
	} // END testsIFTheUserCanLogIn
	
	
}// END of ServerTesting

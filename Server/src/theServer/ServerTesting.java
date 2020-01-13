package theServer;

import java.util.EnumSet;

import Entity.ChangeRequest;
import Entity.Requirement.statusOptions;
import Entity.User;
import Entity.User.permissionsICM;
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
		EnumSet<permissionsICM> Permissions = changer.getICMPermissions();
		if (Permissions.contains(permissionsICM.informationTechnologiesDepartmentManager)||Permissions.contains(permissionsICM.inspector))
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
		if(inDB.getStatus().equals(statusOptions.suspended) ||inDB.getStatus().equals(statusOptions.closed) ) {
			EnumSet<permissionsICM> Permissions = changer.getICMPermissions();
			if(Permissions.contains(permissionsICM.informationTechnologiesDepartmentManager)||Permissions.contains(permissionsICM.inspector))
				return true;
			else return false;
		}
		else return false;
	}// END of testIfRequestIsfrozen()
	
	
	public boolean testsIFTheUserCanLogIn(User tryingToLogInUser) {
		return false;
		
	} // END testsIFTheUserCanLogIn
	
	
}// END of ServerTesting

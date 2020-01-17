package Entity;

import java.io.Serializable;

public class clientRequestFromServer implements Serializable {
    public enum requestOptions {
        changeInLogIn,  // used to update log in/out
        updateProcessStage,
        updateChangeRequest, // updates all change Request fields (including held inspector and stage)
        addRequest,		// adding a change request to the DB

        getAll,         // for now need to change not good name get all requirements
        updateStatus,   // change requirement status (ongoing ,closed,etc)
        getRequirement, // get requirement from DB by his id.
        getUser ,       // get one user from DB by his user name.
        updateUserCollegeStatus,     // updates user college statues
        getAllUsers,			// getting all users from DB
        getChangeRequestByStatus, // getting all requests with the specified status
        getUsersByICMPermissions,// Getting all users with the  specified ICMPermission
        getAllUsersByJob, 		// getting all users with the specified job
        getAllChangeRequestWithStatusAndStage, // get all requests in a specified:  stage AND substage AND state
        getAllChangeRequestWithStatusAndStageOnly, //get all requests in a specified:  stage  AND state
        getAllChangeRequestWithStatusAndSubStageOnly, //get all requests in a specified:  sub stage  AND state
        getAllChangeRequestWithStatusAndStageAndSupervisor, //  get all requests in a specified:  sub stage  AND state AND StageSupervisor
        LogIN ,//only for logging in - tests whether the user is logged in already
        successfulLogInOut, //Updated the server whether there is a user  related to the connection
        getDoc, // effectively downloding the  a requested document to the clients
        updateUser, //TODO : remove
        removeUserIcmPermission, // removes icmPermission from user while testing if can be done
        alertClient, // send string message to client.
        createNewActivitiesReport, // Create a new activity report 
        getAllActivitiesReports,	// get all activity reports in server
        getAllMessges, //  geting all messeges for client 
        addUserIcmPermission// addes icmPermission for user while testing if can be done
        
        

    }

    private Object object;
    private requestOptions request; // request

    public clientRequestFromServer(requestOptions request) {
    	this.object = null;
    	this.request = request;
    }
    
    /** request from server and send an object
     * @param request ?
     * @param obj ?
     */
    public clientRequestFromServer(requestOptions request, Object obj) {
    	this.object = obj;
    	this.request = request;
    }

    public requestOptions getRequest() {
        return request;
    }

    @Override
    public String toString() {
        if (object == null) {
            return request.name();
        }
        else {
            return request.name() + " object: "
                    + object.toString().split("\\[")[0];
        }
    }
	
	/** returns the held object
	* nothing is @return
	 */
	public Object getObject() {
		return object;
	}

	public String getName() { return request.name(); }
}

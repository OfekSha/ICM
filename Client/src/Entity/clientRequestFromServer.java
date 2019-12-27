package Entity;

import java.io.Serializable;
import java.util.ArrayList;

public class clientRequestFromServer implements Serializable {
    public enum requestOptions {
        getAll, // for now need to change not good name get all requirements
        updateStatus, // change requirement status (ongoing ,closed,etc)
        getRequirement, // get requirement from DB by his id.
        getUser ,// get one user from DB by his user name.
        updateUser, // updates all users details 
        changeInLogIn // used to update log in/out
    }
    //  is needed?
    private ArrayList<Requirement> obj;	// some object that transfer to ocf.client or to osf.server.
    //
    private Object object;
    private requestOptions request; // request

    public clientRequestFromServer(requestOptions request) {
    	this.obj = null;
    	this.request = request;
    }
    
    /** request from server and send an object
     * @param request
     * @param obj
     */
    public clientRequestFromServer(requestOptions request, Object obj) {
    	this.object = obj;
    	this.request = request;
    }
    
     //is needed?
    public clientRequestFromServer(requestOptions request, ArrayList<Requirement> obj) {
    	this.obj = obj;
    	this.request = request;
    }
    //
    public requestOptions getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return request.name() + " ArrayList<Requirement>: " + obj +" object: "+object ;
    }
    // is needed ?
	public ArrayList<Requirement> getObj() {
		return obj;
	}
	//
	
	/** returns the held object
	 * @return
	 */
	public Object getObject() {
		return object;
	}

	public String getName() { return request.name(); }
}

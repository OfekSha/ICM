package Entity;

import java.io.Serializable;

/**
 * @author Yonathan
 *
 * connects between a request and its user creator
 */
public class Initiator implements Serializable {
	
	private User theInitiator;
	private ChangeRequest request;
	
	public Initiator(User theInitiator,ChangeRequest request) {
		this.theInitiator =theInitiator;
		this.request= request;
	}
	public void setRequest(ChangeRequest request)
	{
		this.request= request;
	}
	public User getTheInitiator() {
		return theInitiator;
	}
	
	public ChangeRequest getrequest() {
		return request;
	}
	
}

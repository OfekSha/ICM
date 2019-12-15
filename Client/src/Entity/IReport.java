package Entity;

public abstract class IReport {
 private int requestID;
 

	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	//TODO: the following  methods are from the class diagram:  
	public abstract void createReport();
	
}
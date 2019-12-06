package Entity;

/**
 * 
 * This class includes (entity) requirement => line from table requirement in the database.
 * this object packaged in EchoServer.
 * EchoServer send it to client.
 * 
 * @author ofek
 *
 */
public class Requirement {
	private String reqInitiator, currentSituationDetails, requestDetails, stageSupervisor; 
	public enum statusOptions {ongoing,suspended,closed};
	private statusOptions status;
	private int ID;
	
	/**
	 * 
	 * constructor for Requirement
	 * 
	 * @param reqInitiator
	 * @param currentSituationDetails
	 * @param requestDetails
	 * @param stageSupervisor
	 * @param status
	 * @param iD
	 */
	public Requirement(String reqInitiator, String currentSituationDetails, String requestDetails,
			String stageSupervisor, statusOptions status, int iD) {
		this.reqInitiator = reqInitiator;
		this.currentSituationDetails = currentSituationDetails;
		this.requestDetails = requestDetails;
		this.stageSupervisor = stageSupervisor;
		this.status = status;
		ID = iD;
	}
	public String getReqInitiator() {
		return reqInitiator;
	}
	public String getCurrentSituationDetails() {
		return currentSituationDetails;
	}
	public String getRequestDetails() {
		return requestDetails;
	}
	public String getStageSupervisor() {
		return stageSupervisor;
	}
	public statusOptions getStatus() {
		return status;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	//Todo: add to string and equals.
	
}

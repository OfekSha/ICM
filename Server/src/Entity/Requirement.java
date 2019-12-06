package Entity;

public class Requirement {
	private String reqInitiator, currentSituationDetails, requestDetails, stageSupervisor; 
	enum statusOptions {ongoing,suspended,closed};
	private statusOptions status;
	private int ID;
	public String getReqInitiator() {
		return reqInitiator;
	}
	public void setReqInitiator(String reqInitiator) {
		this.reqInitiator = reqInitiator;
	}
	public String getCurrentSituationDetails() {
		return currentSituationDetails;
	}
	public void setCurrentSituationDetails(String currentSituationDetails) {
		this.currentSituationDetails = currentSituationDetails;
	}
	public String getRequestDetails() {
		return requestDetails;
	}
	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}
	public String getStageSupervisor() {
		return stageSupervisor;
	}
	public void setStageSupervisor(String stageSupervisor) {
		this.stageSupervisor = stageSupervisor;
	}
	public statusOptions getStatus() {
		return status;
	}
	public void setStatus(statusOptions status) {
		this.status = status;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
}

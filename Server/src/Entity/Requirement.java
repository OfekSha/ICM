package Entity;

import javax.naming.directory.InvalidAttributesException;
import java.io.Serializable;

/**
 * 
 * This class includes (entity) requirement => line from table requirement in
 * the database. this object packaged in EchoServer. EchoServer send it to
 * client.
 * 
 * @author ofek
 *
 */
public class Requirement implements Serializable {
	public enum statusOptions {
		ongoing, suspended, closed
	}

	private statusOptions status;
	private String reqInitiator, currentSituationDetails, requestDetails, stageSupervisor;
	private int ID;

	/**
	 * 
	 * constructor for Requirement
	 * 
	 * @param reqInitiator            Initiator of request
	 * @param currentSituationDetails details of current situation
	 * @param requestDetails          details of request
	 * @param stageSupervisor         Supervisor of request
	 * @param status                  status ?????
	 * @param ID                      id ?????
	 */
	public Requirement(String reqInitiator, String currentSituationDetails, String requestDetails,
			String stageSupervisor, statusOptions status, int ID) {
		this.reqInitiator = reqInitiator;
		this.currentSituationDetails = currentSituationDetails;
		this.requestDetails = requestDetails;
		this.stageSupervisor = stageSupervisor;
		this.status = status;
		this.ID = ID;
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

	public void setStatus(String status) throws InvalidAttributesException {
		switch (status) {
			case "ongoing":
				this.status = statusOptions.ongoing;
				break;
			case "suspended":
				this.status = statusOptions.suspended;
				break;
			case "closed":
				this.status = statusOptions.closed;
				break;
			default:
				throw new InvalidAttributesException("Invalid status");
		}
	}

	@Override
	public String toString() {
		return "ID = " + ID +
				", reqInitiator = '" + reqInitiator + '\'' +
				", stageSupervisor = '" + stageSupervisor + '\'' +
				", status = '" + status + '\'' +
				", requestDetails = '" + requestDetails + '\'' +
				", currentSituationDetails = '" + currentSituationDetails + '\'';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Requirement that = (Requirement) o;
		return ID == that.ID && status == that.status && reqInitiator.equals(that.reqInitiator)
				&& currentSituationDetails.equals(that.currentSituationDetails)
				&& requestDetails.equals(that.requestDetails) && stageSupervisor.equals(that.stageSupervisor);
	}


}

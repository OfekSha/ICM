package Entity;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Yonathan in proggress
 */
public class ChangeRequest implements Serializable {

	public enum ChangeRequestStatus { //
		ongoing, suspended, closed
	}
	private String RequestID;
	private Initiator initiator;
	private LocalDate starDate;
	private String system;
	private String problemDescription;
	private String whyChange;
	private ChangeRequestStatus status = ChangeRequestStatus.ongoing;
	private Document doc;
	public ProcessStage stage = new ProcessStage(this);

	public ChangeRequest(Initiator initiator, LocalDate starDate, String problemDescription, String whyChange,
						 Document doc) {
		this.initiator = initiator;
		this.starDate = starDate;
		this.problemDescription = problemDescription;
		this.whyChange = whyChange;
		this.doc = doc;
	}
	// input
	public void setStatus(ChangeRequestStatus newStat) {
		status = newStat;
	}
	public void setRequestID(String id) {
		RequestID = id;
	}
	// output
	public  String getRequestID(){
		return RequestID ;
	}
	public Initiator getInitiator() {
		return initiator;
	}

	public LocalDate getStartDate() {
		return starDate;
	}

	public String getSystem() {
		return this.system;
	}

	public String getProblemDescription() {
		return problemDescription;
	}

	public String getWhyChange() {
		return whyChange;
	}

	public ChangeRequestStatus getStatus() {
		return status;
	}

	public Document getDoc() {
		return doc;
	}
} // END of ChangeRequest class
package Entity;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

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
	private String problomeDescription;
	private String whyChange;
	private ChangeRequestStatus status = ChangeRequestStatus.ongoing;
	private Document doc;
	public ProcessStage stage = new ProcessStage(this);

	public ChangeRequest(Initiator initiator, LocalDate starDate, String problomeDescription, String whyChange,
			Document doc) {
		this.initiator = initiator;
		this.starDate = starDate;
		this.problomeDescription = problomeDescription;
		this.whyChange = whyChange;
		this.doc = doc;
	}
	// input
	public void changeStatus(ChangeRequestStatus newStat) {
		status=newStat;
	}
	public void changeRequestID(String id) {
		RequestID= id;
	}
	// output
	public  String getRequestID(){
		return RequestID ;
	}
	public Initiator getInitiator() {
		return initiator;
	}

	public LocalDate getStarDate() {
		return starDate;

	}

	public String getSystem() {
		return system;

	}

	public String getProblomeDescription() {
		return problomeDescription;

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
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
	private String comment;
	private ChangeRequestStatus status = ChangeRequestStatus.ongoing;
	private Document doc;
	public ProcessStage stage = new ProcessStage(this);

	public ChangeRequest(Initiator initiator, LocalDate starDate,String system, String problomeDescription, String whyChange,String comment,
			Document doc) {
		this.comment=comment;
		this.initiator = initiator;
		this.starDate = starDate;
		this.system =system;
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
	//update 
	/**Related classes on changes  - only impotent they have there this classes ID , no need to keep more updated
	 * 
	 */
	public void updateInitiatorRequest() {
		initiator.setrequest(this);
	}
	public void updateStage() {
		stage.setRequest(this);
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
	public String getComment() {
		return comment;

	}

	public ChangeRequestStatus getStatus() {
		return status;

	}

	public Document getDoc() {
		return doc;

	}
} // END of ChangeRequest class
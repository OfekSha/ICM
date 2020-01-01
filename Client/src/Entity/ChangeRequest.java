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
    private String comment;
    private Document doc;
    private ChangeRequestStatus status = ChangeRequestStatus.ongoing;
    private  ProcessStage stage = new ProcessStage(this);

    public ChangeRequest(Initiator initiator, LocalDate starDate,
                         String system, String problemDescription,
                         String whyChange, String comment, Document doc) {
        this.comment = comment;
        this.initiator = initiator;
        this.starDate = starDate;
        this.system = system;
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
    
    public void setStage(ProcessStage stage) {
    	this.stage= stage;
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
    public String getRequestID(){
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

    public String getComment() {
        return comment;
    }

    public ChangeRequestStatus getStatus() {
        return status;
    }

    public Document getDoc() {
        return doc;
    }
    public  ProcessStage getProcessStage() {
    	return stage;
    }
    @Override // Override object method: equals (use for lists)
    public boolean equals(Object another) {
    	if (another instanceof ChangeRequest) 
    	return RequestID==((ChangeRequest)another).RequestID;
    	return false;
    }
} // END of ChangeRequest class
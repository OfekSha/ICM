package Entity;

import java.io.Serializable;
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
    private LocalDate startDate;
    private String system;
    private String problemDescription;
    private String changeReason;
    private String comment;
    private ArrayList<Document> uploadedDocs;
    private ChangeRequestStatus status = ChangeRequestStatus.ongoing;
    private ProcessStage stage = new ProcessStage(this);

    public ChangeRequest(Initiator initiator, LocalDate startDate,
                         String system, String problemDescription,
                         String changeReason, String comment, ArrayList<Document>  uploadedDocs) {
        this.comment = comment;
        this.initiator = initiator;
        this.startDate = startDate;
        this.system = system;
        this.problemDescription = problemDescription;
        this.changeReason = changeReason;
        this.uploadedDocs = uploadedDocs;
    }

    // input
    public void setStatus(ChangeRequestStatus newStat) {
        status = newStat;
    }
    public void setRequestID(String id) {
        RequestID = id;
    }
    
    public void setStage(ProcessStage stage) {
    	this.stage = stage;
    }
    public void setDocs(ArrayList<Document>  uploadedDocs) {
    	   this.uploadedDocs = uploadedDocs;	
    }

    //update
    /**Related classes on changes  - only impotent they have there this classes ID , no need to keep more updated
     *
     */

    public void updateInitiatorRequest() {
        initiator.setRequest(this);
    }

    public void updateStage() {
        stage.setRequest(this);
    }
    
    public void updateDocs() {
    	 for(Document e: uploadedDocs) {
    		 e.setChangeRequest(this);
    	 }
    }
    
    // output
    public String getRequestID(){
        return RequestID ;
    }

    public Initiator getInitiator() {
        return initiator;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getSystem() {
        return this.system;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public String getComment() {
        return comment;
    }

    public ChangeRequestStatus getStatus() {
        return status;
    }

    public ArrayList<Document>  getDoc() {
        return uploadedDocs;
    }

    public  ProcessStage getProcessStage() {
    	return stage;
    }

    @Override // Override object method: equals (use for lists)
    public boolean equals(Object another) {
    	if (another instanceof ChangeRequest) {
            return RequestID.equals(((ChangeRequest) another).RequestID);
        }
    	return false;
    }
} // END of ChangeRequest class
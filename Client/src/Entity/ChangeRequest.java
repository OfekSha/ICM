package Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *  represents one change request in the system
 */
public class ChangeRequest implements Serializable {

    public enum ChangeRequestStatus { //
        ongoing, suspended, closed
    }
    private int RequestID;
    private Initiator initiator;
    private LocalDate startDate;
    private String  baseforChange ;
    private String system;
    private String problemDescription;
    private String changeReason;
    private String comment;
    private ArrayList<Document> uploadedDocs;
    private ChangeRequestStatus status = ChangeRequestStatus.ongoing;
    private ProcessStage stage = new ProcessStage(this);
    private ArrayList<InspectorUpdateDescription> ionspectorUpdateDescription =  new ArrayList<>();

    public ChangeRequest(Initiator initiator, LocalDate startDate,
                         String system, String problemDescription,
                         String changeReason, String comment,String baseforChange, ArrayList<Document>  uploadedDocs ) {
        this.comment = comment;
        if( initiator!=null) initiator.setRequest(this);
        this.initiator = initiator;    
        this.startDate = startDate;
        this.system = system;
        this.problemDescription = problemDescription;
        this.changeReason = changeReason;
        this.uploadedDocs = uploadedDocs;
        this.baseforChange=baseforChange;
    }

    // input
    public void setStatus(ChangeRequestStatus newStat) {
        status = newStat;
    }
    public void setRequestID(int id) {
        RequestID = id;
    }
    
    public void setStage(ProcessStage stage) {
    	 if (stage!=null)stage.setRequest(this);
    	this.stage = stage;
    }
    public void setDocs(ArrayList<Document>  uploadedDocs) {
    	   this.uploadedDocs = uploadedDocs;	
    }
    public void setInspectorUpdateDescription(ArrayList<InspectorUpdateDescription> ionspectorUpdateDescription ) {
    	if(ionspectorUpdateDescription!=null) {
    		for(InspectorUpdateDescription e:ionspectorUpdateDescription) {
    			e.setReferencedChangeRequest(this);
    		}
    	}
    	   this.ionspectorUpdateDescription =ionspectorUpdateDescription;
    	   }//END of setInspectorUpdateDescription();
    
    public void addInspectorUpdate(InspectorUpdateDescription inspectorUpdateDescription) {
    	inspectorUpdateDescription.setReferencedChangeRequest(this);
    	ionspectorUpdateDescription.add(inspectorUpdateDescription);
    }
    public void  setBaseforChange(String baseforChange) {
    	this.baseforChange=baseforChange;
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
    public int getRequestID(){
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
    public ArrayList<InspectorUpdateDescription> getInspectorUpdateDescription(){
    	return  ionspectorUpdateDescription;
    }
 

    @Override // Override object method: equals (use for lists)
    public boolean equals(Object another) {
    	if (another instanceof ChangeRequest) {
            return RequestID==((ChangeRequest)another).getRequestID();
        }
    	return false;
    }
    
    public String getBaseforChange() {
    	return baseforChange;
    }
} // END of ChangeRequest class
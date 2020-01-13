package Entity;

import java.io.Serializable;
import java.time.LocalDate;

import Entity.User.icmPermission;

/**
 * Represents an update action the inspector can make.
 *
 */
public class InspectorUpdateDescription implements Serializable {

	public enum inspectorUpdateKind {
		freeze, unfreeze, close, approveExtension, DisapproveExtension, ApproveSupervisor, approveDueTime,DisapproveDueTime
	}
	/** the updateds DB  key
	 * 
	 */
	private String updateID; 
	/**the inspector who made the update
	 * 
	 */
	private User inspector;
	/**
	 * why or what did he do in the update
	 */
	private String updateDescription;
	/** when did the update happen
	 * 
	 */
	private LocalDate updateDate;
	private inspectorUpdateKind updateKind;
	/**the Change Request  witch the update was done on
	 * 
	 */
	private ChangeRequest referencedChangeRequest;

	public InspectorUpdateDescription(User inspector, String updateDescription, LocalDate updateDate,
			inspectorUpdateKind updateKind ) {
		setInspector(inspector);
		this.updateDescription = updateDescription;
		this.updateDate = updateDate;
		this.updateKind = updateKind;

	}// END of inspectorUpdateDescription()

//input 
	public void setUpdateID(String ID) {
		updateID =ID ;
		
	}

	public void setInspector(User inspector) {
		if(!inspector.getICMPermissions().contains(icmPermission.inspector)) throw new IllegalArgumentException("only an inspector can make an Inspector Update ");
		this.inspector = inspector;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public void setUpdateDate(LocalDate updateDate) {
		this.updateDate = updateDate;
	}

	public void setUpdateKind(inspectorUpdateKind updateKind) {
		this.updateKind = updateKind;
	}
	public void setReferencedChangeRequest(ChangeRequest referencedChangeRequest) {
		this.referencedChangeRequest=referencedChangeRequest;
	}

//output
	public String getUpdateID() {
		return updateID;
	}

	public User getInspector() {
		return inspector;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public LocalDate getUpdateDate() {
		return updateDate;
	}

	public inspectorUpdateKind getinspectorUpdateKind() {
		return updateKind;
	}
	public ChangeRequest getReferencedChangeRequest() {
		return referencedChangeRequest;
		
	}
} // END inspectorUpdateDescription

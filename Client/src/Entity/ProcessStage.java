package Entity;

import Entity.User.ICMPermissions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.EnumSet;

/**
 * @author Yonathan in progress
 */
public class ProcessStage implements Serializable{
	public enum ChargeRequestStages { //
		meaningEvaluation, // stage 1
		examinationAndDecision, // stage 2
		Execution, // stage 3
		examination, // stage 4
		closure // stage 5
	}
	 // not all stages have  all sub stages
	public enum subStages{
		supervisorAllocation,	 
		// if inspector disapproved  due time you have to go back to this sub stage	 
		DeterminingDueTime,		
		supervisorAction
	}

// vars
	private ChangeRequest Request;
	private ChargeRequestStages currentStage = ChargeRequestStages.meaningEvaluation; // starting in the first stage
	private subStages currentSubStage=subStages.supervisorAllocation;					// first stage subStage
	private User StageSupervisor = null;
	private EnumSet<ICMPermissions> Permissions = null;
	private String EstimatorReport = "";
	private String ExeminorFailReport = "";
	private String inspectorDocumention = "";
	
	/**
	 * an array whit what stages asked for extension
	 * [stage number-1]
	 */
	private boolean[] WasThereAnExtentionRequest =new boolean[5]  ;

	/**
	 *  startEndArray is -an array with the start date and end date for each stage
	 * 
	 * [stage number -1 ][0] - start date for stage
	 * 
	 * [stage number -1 ][1] - due date for stage
	 * 
	 * [stage number -1 ][2] - ending date of stage
	 * 
	 */
	private LocalDate[][] startEndArray = new LocalDate[5][3]; //
	
	public ProcessStage(ChangeRequest Request) {
		this.Request=Request;
		for (int i =0 ;i<5;i++) {
			WasThereAnExtentionRequest[i] =false;
		}
	}
	// input methods
	//  TODO: add constraints to date methods 

	/** adding a start date to the current satge
	 * @param start
	 */
	public void addStartDate (LocalDate start) {
		startEndArray[currentStage.ordinal()][0] =start;
		}
	/**adding a due date to the current satge
	 * @param due
	 */
	public void addDueDate (LocalDate due) {
		startEndArray[currentStage.ordinal()][1] =due;
		}
	/**adding a end date to the current satge
	 * @param end
	 */
	public void addEndDate (LocalDate end) {
		startEndArray[currentStage.ordinal()][2] =end;
		}
	/** Extention Request Made was made at the current stage
	 * 
	 */
	public void ExtentionRequestMade() {
		WasThereAnExtentionRequest[currentStage.ordinal()]=true;
	}
	/** change the sub stage you are in
	 * @param newSubStage
	 */
	public void changecurretSubStage(subStages newSubStage) {
		  currentSubStage = newSubStage;
	}
	
	
	public void newStageSupervisor(User supervisor) {
		EnumSet<ICMPermissions> supervisorPermissions = supervisor.getICMPermissions();
		ICMPermissions requiredPermission = null;
		switch (currentStage) {
		case meaningEvaluation:
			requiredPermission = ICMPermissions.estimator;
			break;
		case examinationAndDecision:
			requiredPermission = ICMPermissions.changeControlCommitteeChairman;
			break;
		case Execution:
			requiredPermission = ICMPermissions.executionLeader;
			break;
		case examination:
			requiredPermission = ICMPermissions.examiner;
			break;
		case closure:
			requiredPermission = ICMPermissions.inspector;
			break;
		}

		try {
			if (!(supervisorPermissions.contains(requiredPermission)))
				throw new IllegalArgumentException("StageSupervisor must have Permission the current stage Permission- "
						+ requiredPermission.name());
			StageSupervisor = supervisor;
			Permissions = supervisorPermissions;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// END newStageSupervisor()

	public void changeStage(ChargeRequestStages newStage) { // TODO inforce stage order
		currentStage = newStage;
	}

	public void addEstimatorReport(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(ICMPermissions.estimator)))
				throw new IllegalArgumentException("StageSupervisor must have Permission - estimator");
			EstimatorReport = report;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// End addEstimatorReport;

	public void addExeminorFailReport(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(ICMPermissions.examiner)))
				throw new IllegalArgumentException("StageSupervisor must have Permission - examiner");
			ExeminorFailReport = report;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// End addExeminorFailReport;

	public void addinspectorDocumention(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(ICMPermissions.inspector)))
				throw new IllegalArgumentException("StageSupervisor must have Permission - inspector");
			inspectorDocumention = report;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// End addExeminorFailReport;
	
	public void setRequest(ChangeRequest Request) {
		 this.Request = Request;
	}

	// output
	public ChangeRequest getRequest(){
		return Request ;
	}
	public ChargeRequestStages getCurrentStage() {
		return currentStage;
	}

	public User getStageSupervisor() {
		return StageSupervisor;
	}

	public String getEstimatorReport() {
		return EstimatorReport;
	}

	public String getExeminorFailReport() {
		return ExeminorFailReport;
	}

	public String getInspectorDocumention() {
		return inspectorDocumention;
	}

	public LocalDate[][] getDates() {
		return startEndArray;
	}
	public boolean[] getWasThereAnExtentionRequest() {
		return WasThereAnExtentionRequest;
	}
	public subStages getCurrentSubStage() {
		 return currentSubStage;
	} 

}// END of Stage

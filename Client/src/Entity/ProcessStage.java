package Entity;

import Entity.User.ICMPermissions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.EnumSet;

/**
 * @author Yonathan in progress
 */
public class ProcessStage implements Serializable{

	public void setRequest(Entity.ChangeRequest changeRequest) {
	}

	public enum ChargeRequestStages { //
		meaningEvaluation, // stage 1
		examinationAndDecision, // stage 2
		Execution, // stage 3
		examination, // stage 4
		closure // stage 5
	}

// vars
	private ChangeRequest Request;
	private ChargeRequestStages currentStage = ChargeRequestStages.meaningEvaluation; // starting in the first stage
	private User StageSupervisor = null;
	private EnumSet<ICMPermissions> Permissions = null;
	private String EstimatorReport = "";
	private String ExeminorFailReport = "";
	private String inspectorDocumention = "";

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
	}
	// input methods
	//  TODO: add constraints to date methods 
	public void addStartDate (LocalDate start) {
		startEndArray[currentStage.ordinal()][0] =start;
		}
	public void addDueDate (LocalDate due) {
		startEndArray[currentStage.ordinal()][1] =due;
		}
	public void addEndDate (LocalDate end) {
		startEndArray[currentStage.ordinal()][2] =end;
		}
	
	
	public void newStageSupervisor(User supervisor) {
		EnumSet<ICMPermissions> supervisorPermissions = supervisor.getICMPermissions();
		User.ICMPermissions requiredPermission = null;
		switch (currentStage) {
		case meaningEvaluation:
			requiredPermission = ICMPermissions.estimator;
			break;
		case examinationAndDecision:
			requiredPermission = ICMPermissions.changeControlCommitteeChairman;
			break;
		case Execution:
			requiredPermission = ICMPermissions.exeutionLeader;
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

}// END of Stage

package Entity;

import Entity.User.ICMPermissions;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.EnumSet;

/**
 * @author Yonathan in progress
 */
public class ProcessStage implements Serializable {
	public enum ChargeRequestStages { //
		meaningEvaluation, 		//stage 1
		examinationAndDecision, //stage 2
		execution, 				//stage 3
		examination, 			//stage 4
		closure 				//stage 5
	}

	// not all stages have all sub-stages
	public enum subStages {
		supervisorAllocation, //if inspector disapproved due time you have to go back to this sub stage
		determiningDueTime,
		supervisorAction
	}

	// vars
	private ChangeRequest Request;
	private ChargeRequestStages currentStage = ChargeRequestStages.meaningEvaluation; // starting in the first stage
	private subStages currentSubStage = subStages.supervisorAllocation;                    // first stage subStage
	private User StageSupervisor = null;
	private EnumSet<ICMPermissions> Permissions = null;
	private String EstimatorReport = "";
	private String ExaminerFailReport = "";
	private String inspectorDocumentation = "";

	/**
	 * an array whit what stages asked for extension
	 * [stage number-1]
	 * 
	 * WasThereAnExtensionRequest = 0 - no  Extension Request
	 * WasThereAnExtensionRequest = 1 - pending Extension Request
	 * WasThereAnExtensionRequest = 2 - inspector approved/disapproved
	 * 
	 */
	private int[] WasThereAnExtensionRequest = new int[5];
	/** an array whit what stages asked for extension
	 * [stage number-1]
	 */
	private String[] ExtensionExplanation = new String[5];
	/** 
	 * startEndArray is -an array with the start date and end date for each stage
	 * <p>
	 * [stage number -1 ][0] - start date for stage
	 * <p>
	 * [stage number -1 ][1] - due date for stage
	 * <p>
	 * [stage number -1 ][2] - ending date of stage
	 */
	private LocalDate[][] startEndArray = new LocalDate[5][3]; //

	public ProcessStage(ChangeRequest Request) {
		this.Request = Request;
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
	}

	public ProcessStage(ChargeRequestStages currentStage, subStages currentSubStage, User StageSupervisor,
						String EstimatorReport, String ExaminerFailReport, String inspectorDocumentation, LocalDate[][] startEndArray,
						int[] WasThereAnExtensionRequest,String[] ExtensionExplanation) {
		this.currentStage = currentStage;
		this.currentSubStage = currentSubStage;
		this.StageSupervisor = StageSupervisor;
		this.EstimatorReport = EstimatorReport;
		this.ExaminerFailReport = ExaminerFailReport;
		this.inspectorDocumentation = inspectorDocumentation;
		this.startEndArray = startEndArray;
		this.WasThereAnExtensionRequest = WasThereAnExtensionRequest;
		this.ExtensionExplanation=ExtensionExplanation;
	}

	// input methods
	
	
	/** input an extension explanation 
	 * @param s ?
	 */
	public void inputExtensionExplanation(String s) {
		ExtensionExplanation[currentStage.ordinal()]=s;
	}
	/**input all extension explanation 
	 * @param s ?
	 */
	public void inputAllExtensionExplanation(String[] s) {
		ExtensionExplanation=s;
	}
	
	//  TODO: add constraints to date methods 

	/**
	 * adding a start date to the current stage
	 *
	 * @param start ?
	 */
	public void addStartDate(LocalDate start) {
		startEndArray[currentStage.ordinal()][0] = start;
	}

	/**
	 * adding a due date to the current stage
	 *
	 * @param due ?
	 */
	public void addDueDate(LocalDate due) {
		startEndArray[currentStage.ordinal()][1] = due;
	}

	public LocalDate getDueDate() {
		return startEndArray[currentStage.ordinal()][1];
	}
	/**
	 * adding a end date to the current satge
	 *
	 * @param end ?
	 */
	public void addEndDate(LocalDate end) {
		startEndArray[currentStage.ordinal()][2] = end;
	}

	/**
	 * Extension Request Made was made at the current stage
	 */
	// TODO: add a test if possible -again the date and if requested
	public void ExtensionRequestMade() {
		WasThereAnExtensionRequest[currentStage.ordinal()] = 1;
	}
	
	/**
	 * Extension Request was approved/disapproved  by the inspector
	 */
	public void ExtensionRequestHandled() {
		WasThereAnExtensionRequest[currentStage.ordinal()] = 2;
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
			case execution:
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

	public void setCurrentStage(ChargeRequestStages newStage) { // TODO inforce stage order
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

	public void addExaminerFailReport(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(ICMPermissions.examiner)))
				throw new IllegalArgumentException("StageSupervisor must have Permission - examiner");
			ExaminerFailReport = report;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// End addExaminerFailReport;

	public void addinspectorDocumentation(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(ICMPermissions.inspector)))
				throw new IllegalArgumentException("StageSupervisor must have Permission - inspector");
			inspectorDocumentation = report;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// End addExaminerFailReport;

	public void setRequest(ChangeRequest Request) {
		this.Request = Request;
	}
	
	public void setCurrentSubStage(subStages currentSubStage) {
		this.currentSubStage = currentSubStage;
	}

	// output
	public ChangeRequest getRequest() {
		return Request;
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

	public String getExaminerFailReport() {
		return ExaminerFailReport;
	}

	public String getInspectorDocumentation() {
		return inspectorDocumentation;
	}

	public LocalDate[][] getDates() {
		return startEndArray;
	}

	public int[] getWasThereAnExtensionRequest() {
		return WasThereAnExtensionRequest;
	}

	public subStages getCurrentSubStage() {
		return currentSubStage;
	}
	public String toString() {
		switch (currentStage) {
		case closure: return "closure";
		case examination: return "examination";
		case examinationAndDecision: return "examination And Decision";
		case execution: return "execution";
		case meaningEvaluation: return "meaning Evaluation";
		default : return "Dosn't has a stage";
		}
	}


	public String getExtensionExplanation() {
		return	ExtensionExplanation[currentStage.ordinal()];
		}

		public String[] getAllExtensionExplanation() {
			return ExtensionExplanation;
		}
}// END of Stage

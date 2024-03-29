package Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.EnumSet;

/**
 * @author Yonathan in progress
 */
public class ProcessStage implements Serializable {
	public enum ChargeRequestStages { //
		meaningEvaluation, // stage 1
		examinationAndDecision, // stage 2
		execution, // stage 3
		examination, // stage 4
		closure // stage 5
	}

	// not all stages have all sub-stages
	public enum subStages {
		supervisorAllocation, // if inspector disapproved due time you have to go back to this sub stage
		determiningDueTime, ApprovingDueTime, supervisorAction
	}

	// vars
	private ChangeRequest Request;
	private ChargeRequestStages currentStage = ChargeRequestStages.meaningEvaluation; // starting in the first stage
	private subStages currentSubStage = subStages.supervisorAllocation; // first stage subStage
	private User StageSupervisor = null;
	private EnumSet<User.icmPermission> Permissions = null;
	private EstimatorReport estimatorReport;
	private String ExaminerFailReport = "";
	private String inspectorDocumentation = "";

	/**
	 * an array whit what stages asked for extension [stage number-1]
	 * 
	 * WasThereAnExtensionRequest = 0 - no Extension Request
	 * WasThereAnExtensionRequest = 1 - pending Extension Request
	 * WasThereAnExtensionRequest = 2 - inspector approved/disapproved
	 * 
	 */
	private int[] WasThereAnExtensionRequest = new int[4];
	/**
	 * an array whit what stages asked for extension [stage number-1]
	 */
	private String[] ExtensionExplanation = new String[4];
	/**
	 * startEndArray is -an array with the start date and end date for each stage
	 * <p>
	 * [stage number -1 ][0] - start date for stage
	 * <p>
	 * [stage number -1 ][1] - due date for stage
	 * <p>
	 * [stage number -1 ][2] - ending date of stage
	 * 
	 * 
	 */
	private LocalDate[][] startEndArray = new LocalDate[5][3]; //
	
	/** [stage number -1] = the extended due date of the stage
	 * 
	 */
	private LocalDate[] dueDateExtension  = new LocalDate[4];
	/** the current extension Request Date  -the new requested date for ending the stage
	 * 
	 */
	private LocalDate extensionRequestDate ;
	
	//--- for Performance report 
	/** the amount of days added to the request from  going back on stages
	 * 
	 */
	private int timeAddedBecuseOfReturns=0;
	
	/**--- for Performance report 
	 *  the amount of days added to the request from getting extensions
	 */
	private int timeAddedFromExtentions=0;
	//---
	


	/**
	 * Constructor for client
	 * 
	 * @param Request ?
	 */
	public ProcessStage(ChangeRequest Request) {
		this.Request = Request;
		for (int i = 0; i < 4; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
	}
	

	/**
	 * Constructor for DB and testing
	 * 
	 * @param currentStage ?
	 * @param currentSubStage ?
	 * @param StageSupervisor ?
	 * @param ExaminerFailReport ?
	 * @param inspectorDocumentation ?
	 * @param startEndArray ?
	 * @param WasThereAnExtensionRequest ?
	 * @param ExtensionExplanation ?
	 */
	public ProcessStage(ChargeRequestStages currentStage, subStages currentSubStage, User StageSupervisor,
			String ExaminerFailReport, String inspectorDocumentation, LocalDate[][] startEndArray,
			int[] WasThereAnExtensionRequest, String[] ExtensionExplanation) {
		this.currentStage = currentStage;
		this.currentSubStage = currentSubStage;
		this.StageSupervisor = StageSupervisor;
		if (StageSupervisor != null)
			Permissions = StageSupervisor.getICMPermissions();
		this.ExaminerFailReport = ExaminerFailReport;
		this.inspectorDocumentation = inspectorDocumentation;
		this.startEndArray = startEndArray;
		this.WasThereAnExtensionRequest = WasThereAnExtensionRequest;
		this.ExtensionExplanation = ExtensionExplanation;
	}

	public void setTimeAddedBecuseOfReturns(int timeAddedBecuseOfReturns) {
		this.timeAddedBecuseOfReturns = timeAddedBecuseOfReturns;
	}


	public void setTimeAddedFromExtentions(int timeAddedFromExtentions) {
		this.timeAddedFromExtentions = timeAddedFromExtentions;
	}


	public void setExtensionRequestDate(LocalDate extensionRequestDate) {
		this.extensionRequestDate=extensionRequestDate;
	}
	public void setStartEndArray(LocalDate[][] startEndArray) {
		this.startEndArray = startEndArray;
	}


	public void setDueDateExtension(LocalDate dueDateExtension) {
		this.dueDateExtension[currentStage.ordinal()]=dueDateExtension;
		timeAddedFromExtentions =+ (int)startEndArray[currentStage.ordinal()][1].until( dueDateExtension, ChronoUnit.DAYS);
	}
	public void setAllDueDateExtension(LocalDate[] dueDateExtension) {
		this.dueDateExtension=dueDateExtension;
	}
	/**
	 * input an extension explanation
	 * 
	 * @param s ?
	 */
	public void setExtensionExplanation(String s) {
		ExtensionExplanation[currentStage.ordinal()] = s;
	}

	/**
	 * input all extension explanation
	 * 
	 * @param s ?
	 */
	public void setExtensionExplanation(String[] s) {
		ExtensionExplanation = s;
	}

	// TODO: add constraints to date methods
	/**
	 * when client ask extension before inspector accept.
	 * @param due - date for extension
	 * @deprecated
	 */
	public void setExtensionDate(LocalDate due) {
		startEndArray[4][1]=due;
	}
	/**
	 * adding a start date to the current stage
	 *
	 * @param start ?
	 */
	public void setStartDate(LocalDate start) {
		startEndArray[currentStage.ordinal()][0] = start;
	}

	/**
	 * adding a due date to the current stage
	 *
	 * @param due ?
	 */
	public void setDueDate(LocalDate due) {
		startEndArray[currentStage.ordinal()][1] = due;
	}
	/**
	 * @return the extension date.
	 */
	public LocalDate getExtensionDate() {
		return startEndArray[4][1];
	}

	public LocalDate getDueDate() {
		return startEndArray[currentStage.ordinal()][1];
	}

	/**
	 * adding a end date to the current stage
	 *
	 * @param end ?
	 */
	public void setEndDate(LocalDate end) {
		startEndArray[currentStage.ordinal()][2] = end;
	}

	/**
	 * Extension Request Made was made at the current stage
	 */
	// TODO: add a test if possible -again the date and if requested
	public void setFlagExtensionRequested() {
		WasThereAnExtensionRequest[currentStage.ordinal()] = 1;
	}

	/**
	 * Extension Request was approved/disapproved by the inspector
	 */
	public void setFlagExtensionRequestHandled() {
		WasThereAnExtensionRequest[currentStage.ordinal()] = 2;
	}
	public void setFlagExtensionRequestNotHandled() {
		WasThereAnExtensionRequest[currentStage.ordinal()] = 0;
	}

	public void newStageSupervisor(User supervisor) {
		if (supervisor != null) {
			EnumSet<User.icmPermission> supervisorPermissions = supervisor.getICMPermissions();
			User.icmPermission requiredPermission = null;
			switch (currentStage) {
			case meaningEvaluation:
				requiredPermission = User.icmPermission.estimator;
				break;
			case examinationAndDecision:
				requiredPermission = User.icmPermission.changeControlCommitteeChairman;
				break;
			case execution:
				requiredPermission = User.icmPermission.executionLeader;
				break;
			case examination:
				requiredPermission = User.icmPermission.examiner;
				break;
			case closure:
				requiredPermission = User.icmPermission.inspector;
				break;
			}

			try {
				if (!(supervisorPermissions.contains(requiredPermission)))
					throw new IllegalArgumentException(
							"StageSupervisor must have Permission the current stage Permission- "
									+ requiredPermission.name());
				StageSupervisor = supervisor;
				Permissions = supervisorPermissions;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}else StageSupervisor=null;
	}// END newStageSupervisor()

	public void setCurrentStage(ChargeRequestStages newStage) { 
		if((this.currentStage == ChargeRequestStages.examinationAndDecision && newStage==ChargeRequestStages.meaningEvaluation )||(this.currentStage == ChargeRequestStages.examination && newStage==ChargeRequestStages.execution  ))
			timeAddedBecuseOfReturns =(int) + startEndArray[currentStage.ordinal()][1].until( dueDateExtension[currentStage.ordinal()], ChronoUnit.DAYS);
		currentStage = newStage;
	}

	public void setEstimatorReport(EstimatorReport report) {
		if (report != null) {
			try {
				
				report.setReferencedRequest(getRequest());
				estimatorReport = report;

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		} else
			estimatorReport = report;
	}// End addEstimatorReport;

	public void setExaminerFailReport(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(User.icmPermission.examiner)))
				throw new IllegalArgumentException("StageSupervisor must have Permission - examiner");
			ExaminerFailReport = report;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}// End addExaminerFailReport;

	public void setInspectorDocumentation(String report) {
		try {
			if (StageSupervisor == null)
				throw new IllegalArgumentException("StageSupervisor cannot be null");
			if (!(Permissions.contains(User.icmPermission.inspector)))
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

	public EstimatorReport getEstimatorReport() {
		return estimatorReport;
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
		case closure:
			return "closure";
		case examination:
			return "examination";
		case examinationAndDecision:
			return "examination And Decision";
		case execution:
			return "execution";
		case meaningEvaluation:
			return "meaning Evaluation";
		default:
			return "Doesn't have a stage";
		}
	}

	public String getExtensionExplanation() {
		return ExtensionExplanation[currentStage.ordinal()];
	}

	public String[] getAllExtensionExplanation() {
		return ExtensionExplanation;
	}
	public LocalDate getDueDateExtension() {
		return dueDateExtension[currentStage.ordinal()];
	}
	public LocalDate[] getAllDueDateExtension() {
		return dueDateExtension;
	}
	public LocalDate getExtensionRequestDate() {
		return extensionRequestDate;
	}


	public int getTimeAddedFromExtentions() {
		return timeAddedFromExtentions;
	}


	public int getTimeAddedBecuseOfReturns() {
		return timeAddedBecuseOfReturns;
	}
	
}// END of Stage

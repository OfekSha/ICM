package testSuit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.ActivitiesReport;
import Entity.ChangeRequest;
import Entity.EstimatorReport;
import Entity.Initiator;
import Entity.InspectorUpdateDescription;
import Entity.ProcessStage;
import Entity.User;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.InspectorUpdateDescription.inspectorUpdateKind;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User.collegeStatus;
import Entity.User.icmPermission;
import injection.stubQueryHandler;
import reporting.ReportController;
import reporting.ReportController.reportScope;
import theServer.ServerTesting;
import theServer.ServerTesting.whatHappened;

class CreateOngoingFiledTesting {
	
	 private  ReportController reporter ;
	 private stubQueryHandler stub  ;
	 private  ActivitiesReport report;
	 
	@BeforeEach
	void initalize() {
		stub = new stubQueryHandler(null);
		reporter = new  ReportController(stub);
		report = new ActivitiesReport();
	} // END  of initalize()

	/** sending faulty inputs to the method
	 * 
	 */
	@Test
	void TestFaultyInputs() {
	ArrayList<ChangeRequest> fakeList =originalTesing();
	stub.setFakeList(fakeList);
	// sending incorrect range
	try {
	reporter.createOngoingFiled(LocalDate.now(), LocalDate.of(2020, 1, 1), reportScope.months, report);
	
	fakeList =originalTesing();
	stub.setFakeList(fakeList);
	reporter.createOngoingFiled(null, null, reportScope.months, report);

	
	fail("need to alert of range");
	}catch(Exception e) {
		fail();
	}
	}// End of  TestFaltyInputs 
	
	
	
	// TESING IDEAS
	// incorrect inputes
	
	/*1) send incorrect send range  - endin is befor the start 
	 *2) send incorrect enum 
	 *3) send null report 
	 *4)send dates as not dates
	 * 
	 */
	
	// correct inputs
	// empty arraylist:
	/*1) there are no ongoing requests
	 *2) all requests are not in range 
	 *3) 
	 */
	
	// arraylist has requests in range 
	/*
	 * 1) send requests with wrong date ranges (start and end are mingelg incorrectly)
	 * 2) send correct and test the calculation are correct
	 */
	
	
	
	/**  creates the testing DB the server stars with 
	 *  <p> only difference is the  all the requests are ongoing 
	 * @return an array list with the change requests created when the server starts
	 */
	private ArrayList<ChangeRequest> originalTesing() {
		ArrayList<ChangeRequest> theList = new ArrayList<ChangeRequest>();
		
		
		EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
		EnumSet<User.icmPermission> lessPermissions; //empty enum set
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", collegeStatus.informationEngineer, Permissions);
		String[] ExtensionExplanation = new String[5];
		Initiator initiator = new Initiator(newUser, null);
		LocalDate start = LocalDate.now();
		ChangeRequest changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		// change request at stage 1
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		theList.add(changeRequest);

		// estimatore 
		//creating estimator
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.icmPermission.estimator);
		User estimator = new User("estimator", "1234", "FirstName", "LastName", "mail@email.com", collegeStatus.informationEngineer, lessPermissions);
		EstimatorReport estimiatorReoport = new EstimatorReport(estimator, "report", "report", "report", "report", "risks", 10);

		//creating change Control Committee Chairman
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.icmPermission.changeControlCommitteeChairman);
		newUser = new User("changeControlCommitteeChairman", "1234", "FirstName", "LastName", "mail@email.com", collegeStatus.informationEngineer, lessPermissions);
		initiator = new Initiator(newUser, null);
		ProcessStage stager ;
		LocalDate[][] startEndArray = new LocalDate[5][3];
		int[] WasThereAnExtensionRequest = new int[5];
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
		// change request stage 4
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange4", null);
		stager = new ProcessStage(ChargeRequestStages.examination, subStages.supervisorAllocation, newUser, "test4", "test4", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStage(stager);
		changeRequest.setStatus(ChangeRequestStatus.ongoing);
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		theList.add(changeRequest);
		// change request stage 2
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange2", null);
		changeRequest.setStatus(ChangeRequestStatus.ongoing);
	
		 stager = new ProcessStage(ChargeRequestStages.examinationAndDecision, subStages.supervisorAction, newUser, "test2", "test2", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStage(stager);
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		changeRequest.getProcessStage().setEstimatorReport(estimiatorReoport);
		theList.add(changeRequest);
		//creating execution Leader
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.icmPermission.executionLeader);
		newUser = new User("executionLeader", "1234", "FirstName", "LastName", "mail@email.com", collegeStatus.informationEngineer, lessPermissions);
		initiator = new Initiator(newUser, null);

		// change request stage 3
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange3", null);
		stager = new ProcessStage(ChargeRequestStages.execution, subStages.determiningDueTime, newUser, "test3", "test3", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStage(stager);
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		// updating due date 
		changeRequest.getProcessStage().setDueDate(LocalDate.now());
		changeRequest.getProcessStage().setDueDateExtension(LocalDate.of(2100, 9, 22));
		//addin estimators report
		changeRequest.getProcessStage().setEstimatorReport(estimiatorReoport);
		changeRequest.getProcessStage().setFlagExtensionRequestHandled();
		theList.add(changeRequest);
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
		startEndArray = new LocalDate[5][3];
		//creating examiner
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.examiner);
		lessPermissions.add(User.icmPermission.changeControlCommitteeMember);
		newUser = new User("examiner", "1234", "FirstName", "LastName", "mail@email.com", collegeStatus.informationEngineer, lessPermissions);
		initiator = new Initiator(newUser, null);

		// change request stage 4
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange4", null);
		stager = new ProcessStage(ChargeRequestStages.examination, subStages.supervisorAction, newUser, "test4", "test4", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStage(stager);
		changeRequest.setStatus(ChangeRequestStatus.ongoing);
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		theList.add(changeRequest);
		//stage 4
		
		//creating inspector
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.icmPermission.inspector);
		newUser = new User("inspector", "1234", "FirstName", "LastName", "mail@email.com", collegeStatus.informationEngineer, lessPermissions);
		initiator = new Initiator(newUser, null);


		// adding  update 
		InspectorUpdateDescription des = new InspectorUpdateDescription(newUser, "test", LocalDate.now(), inspectorUpdateKind.freeze);
		changeRequest.addInspectorUpdate(des);
		des = new InspectorUpdateDescription(newUser, "test", LocalDate.now(), inspectorUpdateKind.unfreeze);
		changeRequest.addInspectorUpdate(des);
		changeRequest.getProcessStage().setExtensionRequestDate(LocalDate.of(2050, 8, 30));
		changeRequest.getProcessStage().setFlagExtensionRequested();
		theList.add(changeRequest);
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}

		// change request stage 5
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
		stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStatus(ChangeRequestStatus.ongoing);
		changeRequest.setStage(stager);
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		// creating examiner
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.examiner);
		lessPermissions.add(User.icmPermission.changeControlCommitteeMember);
		newUser = new User("examiner", "1234", "FirstName", "LastName", "mail@email.com",
				collegeStatus.informationEngineer, lessPermissions);
		
		/// for report testing		
		start= LocalDate.of(2020, 2, 3);
		startEndArray[4][2] =LocalDate.of(2020, 12, 25);
		theList.add(changeRequest);
		LocalDate proccesDue ,proccesEnd ;	
		// change request stage 5
		// late request
		proccesDue = LocalDate.of(2020, 2, 25);
		proccesEnd = LocalDate.of(2020, 2, 28);
		for(int y=0 ; y<5 ; y++) {
			startEndArray[y][1]=proccesDue;
			startEndArray[y][2]=proccesEnd;	
		}
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);
				// change request stage 5
				//late request
				proccesDue = LocalDate.of(2020, 5, 25);
				proccesEnd = LocalDate.of(2020, 6, 28);
				for(int y=0 ; y<5 ; y++) {
					startEndArray[y][1]=proccesDue;
					startEndArray[y][2]=proccesEnd;	
				}
				
				
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				// change request stage 5
				theList.add(changeRequest);
				//late request
				proccesDue = LocalDate.of(2020, 4, 25);
				proccesEnd = LocalDate.of(2020, 9, 28);
				for(int y=0 ; y<5 ; y++) {
					startEndArray[y][1]=proccesDue;
					startEndArray[y][2]=proccesEnd;	
				}
				start= LocalDate.of(2020, 3, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);
				// change request stage 5
				// not late request
				proccesDue = LocalDate.of(2020, 4, 25);
				proccesEnd = LocalDate.of(2020, 4, 22);
				for(int y=0 ; y<5 ; y++) {
					startEndArray[y][1]=proccesDue;
					startEndArray[y][2]=proccesEnd;	
				}
				start= LocalDate.of(2020, 4, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);
				// not late request
				proccesDue = LocalDate.of(2020, 5, 25);
				proccesEnd = LocalDate.of(2020, 4, 22);
				for(int y=0 ; y<5 ; y++) {
					startEndArray[y][1]=proccesDue;
					startEndArray[y][2]=proccesEnd;	
				}
				// change request stage 5
				start= LocalDate.of(2020, 5, 13);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);
				// change request stage 5
				// were don with late stuff 
				startEndArray = new LocalDate[5][3];
				start= LocalDate.of(2020, 5, 26);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);	
				// change request stage 5
				start= LocalDate.of(2020, 11, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);	
				// change request stage 5
				start= LocalDate.of(2020, 5, 8);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);				// change request stage 5
				start= LocalDate.of(2020, 8, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);				// change request stage 5
				start= LocalDate.of(2020, 10, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);	
				// change request stage 5
				start= LocalDate.of(2020, 10, 27);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.ongoing);
				changeRequest.setStage(stager);
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				theList.add(changeRequest);
				
				
				return theList;	
	}
} // End of  CreateOngoingFiledTesting class







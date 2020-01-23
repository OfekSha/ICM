package testSuit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

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

/** testing the method createOngoingFiled() in class ReportController
 * 	@see ReportController
 *	{@link reporting.ReportController#createOngoingFiled(LocalDate, LocalDate, reportScope, ActivitiesReport)} 
 */
/**
 * @author Yonathan
 *
 */
@TestInstance(Lifecycle.PER_CLASS)
class CreateOngoingFiledTesting {
	
	
	/** Indicates  the test sends faulty in put to the method<p>
	 * Expecting the method to respond with an exception to alert of the mistake.<br>
	 * Even thaw it currently dose not throw , it should because then it has no other mechanism to alert of Faulty inputs
	 *
	 */
	public @interface FaultyInputsTesting {/*Marker*/ }
	
	/** Indicates  the test is testing the calculations are done correctly 
	 * 
	 *
	 */
	public @interface claculationsTesting {/*Marker*/ }

	 private   ReportController reporter ;
	 private  stubQueryHandler stub  ;
	 private   ActivitiesReport report;
	 private   ArrayList<ChangeRequest> fakeList; 
	 
	 @BeforeAll
	  void  initalize() {
		stub = new stubQueryHandler(null);
		reporter = new  ReportController(stub);
		report = new ActivitiesReport();
		fakeList=originalTesing();
	} // END  of initalize()

	
	 

	/** Sending the method a revered date range<p>
	 * 
	 */
	@Test
	@FaultyInputsTesting
	void testFaultyInputs_ReversedDates() {
	stub.setFakeList((ArrayList<ChangeRequest>) fakeList.clone());
	// Sending incorrect range
	try {
	reporter.createOngoingFiled(LocalDate.now(), LocalDate.of(2020, 1, 1), reportScope.months, report);
	fail("Need to alert of invalid range");
	}catch(Exception e) {
	}
	}// End of  TestFaultyInputs_ReversedDates 
	
	
	/** sending the method nulls instead of dates <p>
	 *  we are expecting the method to protected against Null Pointer Exception and instead throw its own exception
	 */
	@Test
	@FaultyInputsTesting
	void testFaultyInputs_NullDates() {
		stub.setFakeList((ArrayList<ChangeRequest>) fakeList.clone());		
		try {
			reporter.createOngoingFiled(LocalDate.now(), null, reportScope.months, report);
		reporter.createOngoingFiled(null, LocalDate.of(2020, 1, 1), reportScope.months, report);
		fail("Need to alert of invalid  range");
		}catch(Exception e) {
			if(e instanceof NullPointerException)
				fail("Needs to deal with date nulls");
		}	
	} // 	void TestFaultyInputs_NullDates() 
	
	/** Sending the method null instead of reportScope<p>
	 *  we are expecting the method to protected against Null Pointer exception and instead throw its own exception
	 */
	@Test
	@FaultyInputsTesting
	void testFaultyInputs_NullReportScope() {
		stub.setFakeList((ArrayList<ChangeRequest>) fakeList.clone());		
		try {
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 1), null, report);
		fail("Need to alert of invalid scope");
		}catch(Exception e) {
			if(e instanceof NullPointerException)
				fail("Needs to deal with  scope nulls");
		}	
	} // 	void TestFaultyInputs_NullReportScope() 
	
	/**
	 *  sending the method null instead of Activity report 
	 *  we are expecting the method to protected against Null Pointer exception and instead throw its own exception
	 */
	@Test
	@FaultyInputsTesting
	void testFaultyInputs_NullReport() {
		stub.setFakeList((ArrayList<ChangeRequest>) fakeList.clone());		
		try {
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 1),  reportScope.months, null);
		fail("Need to alert of invalid report");
		}catch(Exception e) {
			if(e instanceof NullPointerException)
				fail("Needs to deal with  report nulls");
		}	
	} // 	void TestFaultyInputs_NullReport() 
	
	
	
	
	/** testing what happens if there are no ongoing requests in the DB 
	 * 
	 */
	@Test
	@claculationsTesting
		void testCalculations_NoOngoingRequestsInDB() {
		stub.setFakeList(new ArrayList<ChangeRequest>());		
		double[] zeroDouble = new double[3]  ;
		int [] zeroInt = new int[12];
		try {
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 1),  reportScope.months, report);
		assertTrue(Arrays.equals(zeroDouble, report.getOngoingRequests()));
		assertTrue(Arrays.equals(zeroInt, report.getOngoingRequestsFrequencyDistribution()));
		}catch(Exception e) {fail("no exceptions should be thrown");}
		
	}// Void TestCalculations_NoOngoingRequestsInDB() 
	
	/** Testing  what happens if requests in DB are not in range
	 * 
	 */
	@Test
	@claculationsTesting
		void testCalculations_NoOngoingRequestsInRange() {
		ArrayList<ChangeRequest> theList = new ArrayList<ChangeRequest>();
		EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
		EnumSet<User.icmPermission> lessPermissions; //empty enum set
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", collegeStatus.informationEngineer, Permissions);
		Initiator initiator = new Initiator(newUser, null);
		LocalDate start = LocalDate.of(2020, 10, 30);
		ChangeRequest changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 7, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 8, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 9, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);

		stub.setFakeList(theList);		
		double[] zeroDouble = new double[3]  ;
		for(double z :zeroDouble)
			z=0;
		int [] zeroInt ;
		try {
			zeroInt = new int[12];
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 30),  reportScope.months, report);
		assertTrue(Arrays.equals(zeroDouble, report.getOngoingRequests()),"Months- should be zeros " );
		assertTrue(Arrays.equals(zeroInt, report.getOngoingRequestsFrequencyDistribution())," Months- should be zeros");
		zeroInt = new int[31];
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 30),  reportScope.dayOfmonth, report);
		assertTrue(Arrays.equals(zeroDouble, report.getOngoingRequests()),"dayOfmonth- should be zeros " );
		assertTrue(Arrays.equals(zeroInt, report.getOngoingRequestsFrequencyDistribution())," dayOfmonth- should be zeros");
		zeroInt = new int[7];
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 6, 30),  reportScope.dayOfweek, report);
		assertTrue(Arrays.equals(zeroDouble, report.getOngoingRequests()),"dayOfweek- should be zeros " );
		assertTrue(Arrays.equals(zeroInt, report.getOngoingRequestsFrequencyDistribution())," dayOfweek- should be zeros");
		}catch(Exception e) {fail("no exceptions should be thrown");}
		
	}// void TestCalculations_NoOngoingRequestsInRange() 
	

	@Test
	@claculationsTesting
		void testCalculations_inRangeNormal() {
		ArrayList<ChangeRequest> theList = new ArrayList<ChangeRequest>();
		EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
		EnumSet<User.icmPermission> lessPermissions; //empty enum set
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", collegeStatus.informationEngineer, Permissions);
		Initiator initiator = new Initiator(newUser, null);
		LocalDate start;
		ChangeRequest changeRequest;
		start =LocalDate.of(2020, 1, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 1, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 1, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 2, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 2, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 3, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);

		start =LocalDate.of(2020, 3, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 4, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 5, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);

		start =LocalDate.of(2020, 5, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 6, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		
		start =LocalDate.of(2020, 7, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);

		start =LocalDate.of(2020, 7, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 7, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 8, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 9, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);	
		start =LocalDate.of(2020, 9, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 10, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 10, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);	
		start =LocalDate.of(2020, 10, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 11, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 12, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);	
		start =LocalDate.of(2020, 12, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);
		start =LocalDate.of(2020, 12, 24);	
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		theList.add(changeRequest);

		stub.setFakeList(theList);		
		double [] reseultsDouble =new double[3]  ;
		double [] expectedDouble =new double[3]  ;
		expectedDouble[0] = 2;
		expectedDouble[1] = 0.8164965809;
		expectedDouble[2] = 24;
		
		try {
			
		reporter.createOngoingFiled( LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 31),  reportScope.months, report);
		reseultsDouble =report.getOngoingRequests();
		for(int u =0 ; u<3 ;u++) 
			assertTrue(comperDouble(expectedDouble[u], reseultsDouble[u]),"Months"+u+expectedDouble[u] +" != "+ reseultsDouble[u]);
		
		
		int a;
		}catch(Exception e) {fail("no exceptions should be thrown");}
		
	}// void TestCalculations_NoOngoingRequestsInRange() 
	
	
	// arraylist has requests in range 
	/*
	 * 2) send correct and test the calculation are correct
	 */
	
	
	
	/**  creates the testing DB the server stars with 
	 *  <p> only difference is the  all the requests are ongoing 
	 * @return an array list with the change requests created when the server starts
	 */
	/**
	 * @return
	 */
	private  ArrayList<ChangeRequest> originalTesing() {
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

		// estimator 
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
	
	
	private boolean comperDouble(double d1, double d2) {
		double epsilon = (double) 0.0001;
		if (Math.abs(d1 - d2) < epsilon)
			return true;
		else
			return false;
	} // ENd of  comperFloats	
	
	
} // End of  CreateOngoingFiledTesting class







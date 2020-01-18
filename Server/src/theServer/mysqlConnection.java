package theServer;
import Entity.*;
import Entity.ActivitiesReport.reportScope;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.InspectorUpdateDescription.inspectorUpdateKind;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User.collegeStatus;
import Entity.User.icmPermission;
import queryHandler.QueryHandler;
import theServer.ServerTesting.whatHappened;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;

public class mysqlConnection {
	private static Connection conn;
	private QueryHandler queryHandler;


	public mysqlConnection() {
		this("root", "Aa123456", "localhost");
	}

	public mysqlConnection(String name, String password, String ip) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + ip + "/?serverTimezone=IST", name, password);
			System.out.println("Driver definition succeed");
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {/* handle any errors*/
			System.out.println("Driver definition failed");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		queryHandler = new QueryHandler(this);
	}

	public Connection getConn() {
		return conn;
	}

	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Problem in close connection.");
			e.printStackTrace();
		}
	}

	public void buildDB() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute("CREATE SCHEMA icm;"); // create schema
			stmt.execute("CREATE TABLE icm.user (" +
					"userName VARCHAR(45) NOT NULL," +                                            //[1]
					"password VARCHAR(45) NOT NULL," +                                            //[2]
					"firstName VARCHAR(45) NULL," +                                            //[3]
					"lastName VARCHAR(45) NULL," +                                                //[4]
					"job VARCHAR(45) NULL," +                                                    //[6]
					"email VARCHAR(45) NULL," +                                                //[7]
					"informationTechnologiesDepartmentMangerPermission TINYINT(1) NOT NULL," +    //[8]
					"inspectorPermission TINYINT(1) NOT NULL," +                                    //[9]
					"estimatorPermission TINYINT(1) NOT NULL," +                                    //[10]
					"executionLeaderPermission TINYINT(1) NOT NULL," +                            //[11]
					"examinerPermission TINYINT(1) NOT NULL," +                                //[12]
					"changeControlCommitteeChairman TINYINT(1) NOT NULL," +                        //[13]
					"changeControlCommitteeMember TINYINT(1) NOT NULL," +                        //[14]
					"PRIMARY KEY (userName));"); // create user table
			//
			stmt.execute("CREATE TABLE icm.initiator (" +
					"RequestID INT NOT NULL," +
					"userName VARCHAR(45) NOT NULL," +
					"PRIMARY KEY (RequestID, userName));");
			stmt.execute("CREATE TABLE icm.stage (" +
					"RequestID INT NOT NULL, " +						//1
					"currentStage ENUM ('meaningEvaluation'," +					//2
										"'examinationAndDecision'," +
										"'execution'," +
										"'examination'," +
										"'closure')," +

					"StageSupervisor VARCHAR(45) NULL, " +						//3
					"ExaminerFailReport VARCHAR(45) NULL, " +					//5
					"inspectorDocumentation VARCHAR(45) NULL, " +				//6
					"meaningEvaluationStartDate VARCHAR(45) NULL, " +			//7
					"meaningEvaluationDueDate VARCHAR(45) NULL, " +				//8
					"meaningEvaluationEndDate VARCHAR(45) NULL, " +				//9
					"examinationAndDecisionStartDate VARCHAR(45) NULL, " +		//10
					"stageColExaminationAndDecisionDueDate VARCHAR(45) NULL, " +	//11
					"examinationAndDecisionEndDate VARCHAR(45) NULL, " +			//12
					"executionStartDate VARCHAR(45) NULL, " +						//13
					"executionDueDate VARCHAR(45) NULL, " +							//14
					"executionEndDate VARCHAR(45) NULL, " +							//15
					"examinationStartDate VARCHAR(45) NULL, " +						//16
					"examinationDueDate VARCHAR(45) NULL, " +						//17
					"examinationEndDate VARCHAR(45) NULL, " +						//18
					"closureStarDate VARCHAR(45) NULL, " +							//19
					"closureEndDate VARCHAR(45) NULL, " +							//20
					"stage1extension TINYINT(1) NOT NULL, "+						//21
					"stage2extension TINYINT(1) NOT NULL, "+						//22
					"stage3extension TINYINT(1) NOT NULL, "+						//23
					"stage4extension TINYINT(1) NOT NULL, "+						//24
					"currentSubStage ENUM ('supervisorAllocation'," +				//26
					"'determiningDueTime'," +
					"'supervisorAction','ApprovingDueTime')," +
					"stage1ExtensionExplanation TEXT NULL, " +						//27
					"stage2ExtensionExplanation TEXT NULL, " +						//28
					"stage3ExtensionExplanation TEXT NULL, " +						//29
					"stage4ExtensionExplanation TEXT NULL, " +						//30
					"stage1dueDateExtension VARCHAR(45) NULL, " +						//27
					"stage2dueDateExtension VARCHAR(45) NULL, " +						//28
					"stage3dueDateExtension VARCHAR(45) NULL, " +						//29
					"stage4dueDateExtension VARCHAR(45) NULL, " +
					"extensionRequestDate VARCHAR(45) NULL, " +
					"PRIMARY KEY (RequestID));");
			stmt.execute("CREATE TABLE icm.changerequest (" +
					"RequestID INT NOT NULL," +
					"startDate VARCHAR(45) NULL," +
					"`system` VARCHAR(45) NULL," +
					"problemDescription TEXT NULL," +
					"changeReason TEXT NULL," +
					"comment VARCHAR(45)," +
					"status VARCHAR(45) NULL," +
					"baseforChange VARCHAR(45) NULL," +
					"PRIMARY KEY (RequestID));");
			stmt.execute("CREATE TABLE icm.docs (\n" +
					"FileID INT NOT NULL ,\n" +
					"RequestID INT NOT NULL," +
					"fileName VARCHAR(45)  NULL," +
					"uploadedFile MEDIUMBLOB,\n" +
					"size INT  NULL," +
					"PRIMARY KEY (FileID)) ;\n");
			stmt.execute("CREATE TABLE `icm`.`inspectorupdates` (\n" +
					"  `updateID` INT NOT NULL,\n" +
					"  `RequestID` INT NULL,\n" +
					"  `inspector` VARCHAR(45) NULL,\n" +
					"  `updatDescription` TEXT NULL,\n" +
					"  `updateDate` VARCHAR(45) NULL,\n" +
					"  `updateKind` VARCHAR(45) NULL,\n" +
					"  PRIMARY KEY (`updateID`));\n" +
					"");
			stmt.execute("CREATE TABLE `icm`.`estimatorreports` (\n" +
					"  `estimatorReportID` INT NOT NULL,\n" +
					"  `referencedRequestID` INT NULL,\n" +
					"  `estimatorUsername` VARCHAR(45) NULL,\n" +
					"  `location` TEXT NULL,\n" +
					"  `changeDescription` TEXT NULL,\n" +
					"  `resultingResult` TEXT NULL,\n" +
					"  `constraints` TEXT NULL,\n" +
					"  `timeEstimate` INT NULL,\n" +
					"  `risks` TEXT NULL,\n" +
					"  PRIMARY KEY (`estimatorReportID`));\n" +
					"");
			stmt.execute("CREATE TABLE `icm`.`activitiesreport` (\r\n" + 
					"  `reportID` INT NOT NULL,\r\n" + 
					"  `reportScope` VARCHAR(45) NULL,\r\n" + 
					"  `startDate` VARCHAR(45) NULL,\r\n" + 
					"  `endDate` VARCHAR(45) NULL,\r\n" + 
					"  `creationDate` VARCHAR(45) NULL,\r\n" + 
					"  `ongoingRequestsMedian` DOUBLE NULL,\r\n" + 
					"  `ongoingRequestsStandardDeviation` DOUBLE NULL,\r\n" + 
					"  `ongoingRequests` DOUBLE NULL,\r\n" + 
					"  `ongoingRequestsFrequencyDistribution` INT NOT NULL,\r\n" + 
					"  ` suspendedRequestsMedian` DOUBLE NULL,\r\n" + 
					"  ` suspendedRequestsStandardDeviation` DOUBLE NULL,\r\n" + 
					"  ` suspendedRequests` DOUBLE NULL,\r\n" + 
					"  ` suspendedRequestsFrequencyDistribution` INT NOT NULL,\r\n" + 
					"  `closedRequestsMedian` DOUBLE NULL,\r\n" + 
					"  `closedRequestsStandardDeviation` DOUBLE NULL,\r\n" + 
					"  `closedRequests`  DOUBLE NULL,\r\n" + 
					"  `closedRequestsFrequencyDistribution` INT NOT NULL,\r\n" + 
					"  `rejectedRequestsMedian` DOUBLE NULL,\r\n" + 
					"  `rejectedRequestsStandardDeviation` DOUBLE NULL,\r\n" + 
					"  `rejectedRequests` DOUBLE NULL,\r\n" + 
					"  `rejectedRequestsFrequencyDistribution` INT NOT NULL,\r\n" + 
					"  `treatmentDaysRequestsMedian` DOUBLE NULL,\r\n" + 
					"  `treatmentDaysStandardDeviation` DOUBLE NULL,\r\n" + 
					"  `treatmentDays` DOUBLE NULL,\r\n" + 
					"  `treatmentDaysFrequencyDistribution` INT NOT NULL,\r\n" + 
					"  PRIMARY KEY (`reportID`));\r\n" + 
					"");
			stmt.execute("CREATE TABLE `icm`.`frequencydistribution-months` (\r\n" + 
					"  `id` INT NOT NULL,\r\n" + 
					"  `1` INT NULL,\r\n" + 
					"  `2` INT NULL,\r\n" + 
					"  `3` INT NULL,\r\n" + 
					"  `4` INT NULL,\r\n" + 
					"  `5` INT NULL,\r\n" + 
					"  `6` INT NULL,\r\n" + 
					"  `7` INT NULL,\r\n" + 
					"  `8` INT NULL,\r\n" + 
					"  `9` INT NULL,\r\n" + 
					"  `10` INT NULL,\r\n" + 
					"  `11` INT NULL,\r\n" + 
					"  `12` INT NULL,\r\n" + 
					"  PRIMARY KEY (`id`));\r\n" + 
					"");
			stmt.execute("CREATE TABLE `icm`.`frequencydistribution-daysinweek` (\r\n" + 
					"  `id` INT NOT NULL,\r\n" + 
					"  `1` INT NULL,\r\n" + 
					"  `2` INT NULL,\r\n" + 
					"  `3` INT NULL,\r\n" + 
					"  `4` INT NULL,\r\n" + 
					"  `5` INT NULL,\r\n" + 
					"  `6` INT NULL,\r\n" + 
					"  `7` INT NULL,\r\n" + 
					"  PRIMARY KEY (`id`));\r\n" + 
					"");
			stmt.execute("CREATE TABLE `icm`.`frequencydistribution-daysinmonth` (\r\n" + 
					"  `id` INT NOT NULL,\r\n" + 
					"  `1` INT NULL,\r\n" + 
					"  `2` INT NULL,\r\n" + 
					"  `3` INT NULL,\r\n" + 
					"  `4` INT NULL,\r\n" + 
					"  `5` INT NULL,\r\n" + 
					"  `6` INT NULL,\r\n" + 
					"  `7` INT NULL,\r\n" + 
					"  `8` INT NULL,\r\n" + 
					"  `9` INT NULL,\r\n" + 
					"  `10` INT NULL,\r\n" + 
					"  `11` INT NULL,\r\n" + 
					"  `12` INT NULL,\r\n" + 
					"  `13` INT NULL,\r\n" + 
					"  `14` INT NULL,\r\n" + 
					"  `15` INT NULL,\r\n" + 
					"  `16` INT NULL,\r\n" + 
					"  `17` INT NULL,\r\n" + 
					"  `18` INT NULL,\r\n" + 
					"  `19` INT NULL,\r\n" + 
					"  `20` INT NULL,\r\n" + 
					"  `21` INT NULL,\r\n" + 
					"  `22` INT NULL,\r\n" + 
					"  `23` INT NULL,\r\n" + 
					"  `24` INT NULL,\r\n" + 
					"  `25` INT NULL,\r\n" + 
					"  `26` INT NULL,\r\n" + 
					"  `27` INT NULL,\r\n" + 
					"  `28` INT NULL,\r\n" + 
					"  `29` INT NULL,\r\n" + 
					"  `30` INT NULL,\r\n" + 
					"  `31` INT NULL,\r\n" + 
					"  PRIMARY KEY (`id`));\r\n" + 
					"" + 
					"");
			stmt.execute("CREATE TABLE `icm`.`messages` (\r\n" + 
					"  `id` INT NOT NULL,\r\n" + 
					"  `from` VARCHAR(45) NULL,\r\n" + 
					"  `to` VARCHAR(45) NULL,\r\n" + 
					"  `messege` TEXT NULL,\r\n" + 
					"  PRIMARY KEY (`id`));\r\n" + 
					"");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return amount of schemas with name icm
	 */
	public boolean checkExistence() {
		String result = "0";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("SELECT count(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'icm';");
			while (rs1.next()) {
				result = rs1.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.equals("1");
	}

	public static void dropDB() {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DROP DATABASE `icm`;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * builds the DB with examples
	 */
	protected void DBwithExamples() {
		if (!checkExistence()) {
			buildDB();
			enterUsersToDB();
			enterChangeRequestToDB();
			addActivitiesReport();
			//testing
			ArrayList<ChangeRequest> a = queryHandler.getChangeRequestQuerys().getAllChangeRequest();
			ArrayList<User> b = queryHandler.getUserQuerys().getAllUsers();
			ArrayList<ActivitiesReport> c =queryHandler.getActivitiesReportQuerys().getAllActivitiesReports();
			
			queryHandler.getMessagesQuerys().InsertMessags(new Message("system", "admin", "the messege1"));
			queryHandler.getMessagesQuerys().InsertMessags(new Message("system", "admin", "the messege2"));
			queryHandler.getMessagesQuerys().InsertMessags(new Message("system", "admin", "the messege2"));
			 // messeges
			EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
			User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", collegeStatus.informationEngineer, Permissions);
			ArrayList<Message> d = queryHandler.getMessagesQuerys().SelectMessages(newUser);
			
			EnumSet<User.icmPermission> lessPermissions = EnumSet.complementOf(Permissions); 
			lessPermissions.add(icmPermission.estimator);
			//testing 
			ServerTesting tes =new ServerTesting(queryHandler);
			newUser = new User("estimator", "1234", "Mrs.", "Estimator", "estimator@email.com", collegeStatus.informationEngineer, lessPermissions);
			Object[] tt=tes.testifUserIcmPermissionCanBeAdded(newUser, icmPermission.executionLeader,queryHandler.getChangeRequestQuerys().getChangeRequest(3));
			
			System.out.println("New DB ready for use !");
		}
	}

	protected void resetDB() {
		if (checkExistence()) dropDB();
		DBwithExamples();
	} //END of


	private void enterUsersToDB() {
		// creating admin
		EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", collegeStatus.informationEngineer, Permissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		// creating  information Technologies Department Manager
		EnumSet<User.icmPermission> lessPermissions = EnumSet.complementOf(Permissions); //empty enum set
		lessPermissions.add(User.icmPermission.informationTechnologiesDepartmentManager);
		newUser = new User("ITDepartmentManager", "1234", "Mrs.", "IT DeptMngr", "itdept@email.com", collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//creating inspector
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.inspector);
		newUser = new User("inspector", "1234", "Mrs.", "Inspector", "inspector@email.com", collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//creating estimator
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.estimator);
		newUser = new User("estimator", "1234", "Mrs.", "Estimator", "estimator@email.com", collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//creating exeution Leader
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.executionLeader);
		newUser = new User("executionLeader", "1234", "Mr.", "ExecLeader", "executor@email.com", collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//creating examiner
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.icmPermission.examiner);
		lessPermissions.add(User.icmPermission.changeControlCommitteeMember);
		newUser = new User("examiner", "1234", "Mrs.", "Examiner", "examiner@email.com", collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//creating change Control Committee Chairman
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.changeControlCommitteeChairman);
		newUser = new User("changeControlCommitteeChairman", "1234", "Mrs.", "CCCChairman", "chairman@email.com", collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//creating student
		newUser = new User("student", "1234", "Mr.", "Student", "student@email.com", collegeStatus.student, EnumSet.complementOf(Permissions));
		queryHandler.getUserQuerys().insertUser(newUser);

		//creating lecturer
		newUser = new User("lecturer", "1234", "Mrs.", "Lecturer", "lecturer@email.com", collegeStatus.student, EnumSet.complementOf(Permissions));
		queryHandler.getUserQuerys().insertUser(newUser);
		queryHandler.getUserQuerys().updateCollegeStatus(newUser, collegeStatus.lecturer);
		//creating a worker with no Permission
		lessPermissions = EnumSet.complementOf(Permissions);
		newUser = new User("worker", "1234", "Mrs.", "Worker", "worker@email.com",
				collegeStatus.informationEngineer, lessPermissions);
		queryHandler.getUserQuerys().insertUser(newUser);
		//tests
		queryHandler.getUserQuerys().updateIcmPermission(newUser, icmPermission.changeControlCommitteeChairman, 1);
		queryHandler.getUserQuerys().updateIcmPermission(newUser, icmPermission.changeControlCommitteeChairman, 0);
	}// END of  enterUsersToDB()

	private void enterChangeRequestToDB() {
		EnumSet<User.icmPermission> Permissions = EnumSet.allOf(User.icmPermission.class);
		EnumSet<User.icmPermission> lessPermissions; //empty enum set
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", collegeStatus.informationEngineer, Permissions);
		String[] ExtensionExplanation = new String[5];
		Initiator initiator = new Initiator(newUser, null);
		LocalDate start = LocalDate.now();
		ChangeRequest changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange1", null);
		// change request at stage 1
		changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
		queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());

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

		// change request stage 2
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange2", null);
		changeRequest.setStatus(ChangeRequestStatus.suspended);
		LocalDate[][] startEndArray = new LocalDate[5][3];
		int[] WasThereAnExtensionRequest = new int[5];
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
		ProcessStage stager = new ProcessStage(ChargeRequestStages.examinationAndDecision, subStages.supervisorAction, newUser, "test2", "test2", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStage(stager);
		changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
		changeRequest.getProcessStage().setEstimatorReport(estimiatorReoport);
		queryHandler.getChangeRequestQuerys().updateAllChangeRequestFields(changeRequest);
		//creating execution Leader
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.icmPermission.executionLeader);
		newUser = new User("executionLeader", "1234", "FirstName", "LastName", "mail@email.com", collegeStatus.informationEngineer, lessPermissions);
		initiator = new Initiator(newUser, null);

		// change request stage 3
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange3", null);
		stager = new ProcessStage(ChargeRequestStages.execution, subStages.determiningDueTime, newUser, "test3", "test3", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStage(stager);
		changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
		queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
		// updating due date 
		changeRequest.getProcessStage().setDueDate(LocalDate.now());
		changeRequest.getProcessStage().setDueDateExtension(LocalDate.of(2100, 9, 22));
		//addin estimators report
		changeRequest.getProcessStage().setEstimatorReport(estimiatorReoport);
		changeRequest.getProcessStage().setFlagExtensionRequestHandled();
		queryHandler.getChangeRequestQuerys().updateAllChangeRequestFields(changeRequest);
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
		//test
		//queryHandler.updateAllProcessStageFields(changeRequest.getProcessStage());
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
		changeRequest.setStatus(ChangeRequestStatus.suspended);
		changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
		queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());

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
		queryHandler.getChangeRequestQuerys().updateAllChangeRequestFields(changeRequest);

		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}

		// change request stage 5
		changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
		stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
		changeRequest.setStatus(ChangeRequestStatus.closed);
		changeRequest.setStage(stager);
		changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
		queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
		ServerTesting tester = new ServerTesting(queryHandler);
		whatHappened wh = tester.testIfRequestIsFrozen(estimator, changeRequest);
		// testing***********
		Object[] arr = tester.testIfUserIcmPermissionCanBeRemoved(estimator, icmPermission.estimator);
		// creating examiner
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(icmPermission.examiner);
		lessPermissions.add(User.icmPermission.changeControlCommitteeMember);
		newUser = new User("examiner", "1234", "FirstName", "LastName", "mail@email.com",
				collegeStatus.informationEngineer, lessPermissions);
		Object[] arr2 = tester.testIfUserIcmPermissionCanBeRemoved(newUser, icmPermission.examiner);
		/// for report testing
		
		
		start= LocalDate.of(2020, 2, 3);
		startEndArray[4][2] =LocalDate.of(2020, 12, 25);
		// change request stage 5
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 3, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 4, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 5, 13);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 5, 26);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 11, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 5, 8);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 8, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 10, 3);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
				// change request stage 5
				start= LocalDate.of(2020, 10, 27);
				changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", "baseforChange5", null);
				stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", startEndArray, WasThereAnExtensionRequest, ExtensionExplanation);
				changeRequest.setStatus(ChangeRequestStatus.closed);
				changeRequest.setStage(stager);
				changeRequest.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(changeRequest));
				changeRequest.updateInitiatorRequest();
				changeRequest.updateStage();
				queryHandler.getInitiatorQuerys().insertInitiator(changeRequest.getInitiator());
				queryHandler.getProccesStageQuerys().InsertProcessStage(changeRequest, changeRequest.getProcessStage());
	}// END of enterChangeRequestToDB
	
	private void addActivitiesReport() {
		ReportController cont = new ReportController(queryHandler);
		LocalDate start = LocalDate.of(2020, 1, 1);
		LocalDate end = LocalDate.of(2021, 1, 1);
		ActivitiesReport c =cont.creatActivitiesReport(start,end,reportScope.months);
		queryHandler.getActivitiesReportQuerys().InsertActivitiesReport(c);
		ActivitiesReport c1 =cont.creatActivitiesReport(start,end,reportScope.dayOfmonth);
		queryHandler.getActivitiesReportQuerys().InsertActivitiesReport(c1);
		ActivitiesReport c2 =cont.creatActivitiesReport(start,end,reportScope.dayOfweek);
		queryHandler.getActivitiesReportQuerys().InsertActivitiesReport(c2);
		 start = LocalDate.of(2020, 3, 1);
		ActivitiesReport d =cont.creatActivitiesReport(start,end,reportScope.months);
		queryHandler.getActivitiesReportQuerys().InsertActivitiesReport(d);
	} //END of  addActivitiesReport
}//END of mysqlConnection

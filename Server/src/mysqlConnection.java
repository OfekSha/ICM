import java.sql.*;

public class mysqlConnection {
	private static Connection conn;

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

	public static void buildDB() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.execute("CREATE SCHEMA icm;"); // create schema
			// @building by yonathan - changes are coming !, maybe...
			stmt.execute("CREATE TABLE icm.user (" + 
					"userName VARCHAR(45) NOT NULL," + 	 										//[1]
					"password VARCHAR(45) NOT NULL," + 											//[2]
					"firstName VARCHAR(45) NULL," + 											//[3]
					"lastName VARCHAR(45) NULL," + 												//[4]
					"job VARCHAR(45) NULL," + 													//[6]
					"email VARCHAR(45) NULL," + 												//[7]
					"informationTechnologiesDepartmentMangerPermission TINYINT(1) NOT NULL,"+	//[8]
					"inspectorPermission TINYINT(1) NOT NULL,"+									//[9]
					"estimatorPermission TINYINT(1) NOT NULL,"+									//[10]
					"executionLeaderPermission TINYINT(1) NOT NULL,"+							//[11]
					"examinerPermission TINYINT(1) NOT NULL," + 								//[12]
					"changeControlCommitteeChairman TINYINT(1) NOT NULL," +						//[13]
					"changeControlCommitteeMember TINYINT(1) NOT NULL," +						//[14]
					"PRIMARY KEY (userName));"); // create user table
			//
			stmt.execute("CREATE TABLE icm.initiator (" +
					"RequestID VARCHAR(45) NOT NULL," +		
					"userName VARCHAR(45) NOT NULL," +
					"PRIMARY KEY (RequestID, userName));");
			stmt.execute("CREATE TABLE icm.stage (" +
					"RequestID VARCHAR(45) NOT NULL, " +						//1
					"currentStage ENUM ('meaningEvaluation'," +					//2
										"'examinationAndDecision'," +
										"'execution'," +
										"'examination'," +
										"'closure')," +
					
					"StageSupervisor VARCHAR(45) NULL, " +						//3
					"EstimatorReport VARCHAR(45) NULL, " +						//4
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
					"stage5extension TINYINT(1) NOT NULL, "+						//25
					"currentSubStage ENUM ('supervisorAllocation'," +				//26
					"'determiningDueTime'," +										
					"'supervisorAction','ApprovingDueTime')," +										
					"stage1ExtensionExplanation TEXT NULL, " +						//27
					"stage2ExtensionExplanation TEXT NULL, " +						//28
					"stage3ExtensionExplanation TEXT NULL, " +						//29
					"stage4ExtensionExplanation TEXT NULL, " +						//30
					"stage5ExtensionExplanation TEXT NULL, " +						//32
					"PRIMARY KEY (RequestID, currentStage));");
			stmt.execute("CREATE TABLE icm.changerequest (" +
					"RequestID VARCHAR(45) NOT NULL," +
					"startDate VARCHAR(45) NULL," +
					"`system` VARCHAR(45) NULL," +
					"problemDescription TEXT NULL," +
					"changeReason TEXT NULL," +
					"comment VARCHAR(45)," +
					"status VARCHAR(45) NULL," +
					"PRIMARY KEY (RequestID));");
			stmt.execute("CREATE TABLE icm.docs (\n" +
					"FileID VARCHAR(45) NOT NULL ,\n"+
					"RequestID VARCHAR(45) NOT NULL," +
					"uploadedFile MEDIUMBLOB,\n" +		
					"PRIMARY KEY (FileID)) ;\n");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return amount of schemas with name icm
	 */
	public static boolean checkExistence() {
		String result = "0";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("SELECT count(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'icm';");
			while(rs1.next()) {
				result = rs1.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result.equals("1");
	}
}

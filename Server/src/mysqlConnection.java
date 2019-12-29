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
			stmt.execute("CREATE TABLE icm.requirement (" +
					"Initiator VARCHAR(45) NOT NULL," +
					"RequestID INT NOT NULL," +
					"CurrentSituationDetails LONGTEXT NULL," +
					"RequestDetails LONGTEXT NULL," +
					"StageSupervisor VARCHAR(45) NULL," +
					"Status ENUM('ongoing', 'suspended', 'closed') NOT NULL DEFAULT 'ongoing',\n" +
					"PRIMARY KEY (RequestID));"); // create requirement table

			// @building by yonathan - changes are coming !, maybe...
			stmt.execute("CREATE TABLE icm.user (" + 
					"userName VARCHAR(45) NOT NULL," + 	 										//[1]
					"password VARCHAR(45) NOT NULL," + 											//[2]
					"firstName VARCHAR(45) NULL," + 											//[3]
					"lastName VARCHAR(45) NULL," + 												//[4]
					"login TINYINT(1) NOT NULL," + 												//[5]
					"job VARCHAR(45) NULL," + 													//[6]
					"email VARCHAR(45) NULL," + 												//[7]
					"informationTechnologiesDepartmentMangerPermission TINYINT(1) NOT NULL,"+	//[8]
					"inspectorPermission TINYINT(1) NOT NULL,"+									//[9]
					"estimatorPermission TINYINT(1) NOT NULL,"+									//[10]
					"executionLeaderPermission TINYINT(1) NOT NULL,"+							//[11]
					"examinerPermission TINYINT(1) NOT NULL," + 								//[12]
					"changeControlCommitteeChairman TINYINT(1) NOT NULL," +						//[13]
					"PRIMARY KEY (userName));"); // create user table
			//
			stmt.execute("CREATE TABLE icm.initiator (" +
					"RequestID VARCHAR(45) NOT NULL," +
					"userName VARCHAR(45) NOT NULL," +
					"PRIMARY KEY (RequestID, userName));");
			stmt.execute("CREATE TABLE icm.stage (" +
					"RequestID VARCHAR(45) NOT NULL," +
					"currentStage VARCHAR(45) NOT NULL," +
					"StageSupervisor VARCHAR(45) NULL," +
					"EstimatorReport VARCHAR(45) NULL," +
					"ExaminerFailReport VARCHAR(45) NULL," +
					"inspectorDocument VARCHAR(45) NULL," +				//6
					"meaningEvaluationStartDate VARCHAR(45) NULL," + //[7]
					"meaningEvaluationDueDate VARCHAR(45) NULL," +
					"meaningEvaluationEndDate VARCHAR(45) NULL," +
					"examinationAndDecisionStartDate VARCHAR(45) NULL," +
					"stageColExaminationAndDecisionDueDate VARCHAR(45) NULL," +
					"examinationAndDecisionEndDate VARCHAR(45) NULL," +
					"ExecutionStartDate VARCHAR(45) NULL," +
					"ExecutionDueDate VARCHAR(45) NULL," +
					"ExecutionEndDate VARCHAR(45) NULL," +
					"examinationStartDate VARCHAR(45) NULL," +
					"examinationDueDate VARCHAR(45) NULL," +
					"examinationEndDate VARCHAR(45) NULL," +
					"closureStarDate VARCHAR(45) NULL," +
					"closureEndDate VARCHAR(45) NULL," +
					"stage1extension TINYINT(1) NOT NULL,"+
					"stage2extension TINYINT(1) NOT NULL,"+
					"stage3extension TINYINT(1) NOT NULL,"+
					"stage4extension TINYINT(1) NOT NULL,"+
					"stage5extension TINYINT(1) NOT NULL,"+
					"currentSubStage VARCHAR(45) NULL," +
					"PRIMARY KEY (RequestID, currentStage));");
			stmt.execute("CREATE TABLE icm.changerequest (" +
					"RequestID VARCHAR(45) NOT NULL," +
					"startDate VARCHAR(45) NULL," +
					"`system` VARCHAR(45) NULL," +
					"problemDescription TEXT NULL," +
					"whyChange TEXT NULL," +
					"comment VARCHAR(45)," +
					"status VARCHAR(45) NULL," +
					"PRIMARY KEY (RequestID));");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkExistence() {
		try {
			String result = "0";
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("SELECT count(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'icm';");
			while(rs1.next()) {
				result = rs1.getString(1);
			}
			return result.equals("1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

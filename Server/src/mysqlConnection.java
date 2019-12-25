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
			stmt.execute("CREATE TABLE icm.requirement (\n" +
					"\tInitiator VARCHAR(45) NOT NULL,\n" +
					"\tRequestID INT NOT NULL,\n" +
					"\tCurrentSituationDetails LONGTEXT NULL,\n" +
					"\tRequestDetails LONGTEXT NULL,\n" +
					"\tStageSupervisor VARCHAR(45) NULL,\n" +
					"\tStatus ENUM('ongoing', 'suspended', 'closed') NOT NULL DEFAULT 'ongoing',\n" +
					"\tPRIMARY KEY (RequestID));"); // create requirement table
			
			
			// @building by yonathan - changes are coming !, maybe...
			stmt.execute("CREATE TABLE icm.user (" + 
					"\tuserName VARCHAR(45) NOT NULL," +  									//[1]
					"\tpassword VARCHAR(45) NOT NULL," + 										//[2]
					"\tfirstName VARCHAR(45) NULL," + 										//[3]
					"\tlastName VARCHAR(45) NULL," + 											//[4]
					"\tlogin TINYINT(1) NOT NULL," + 											//[5]
					"\tjob VARCHAR(45) NULL," + 												//[6]
					"\temail VARCHAR(45) NULL," + 											//[7]
					"\tinformationTecnologiesDeparmentMangerPermission TINYINT(1) NOT NULL,"+	//[8]
					"\tinspectorPermission TINYINT(1) NOT NULL,"+								//[9]			
					"\testimatorPermission TINYINT(1) NOT NULL,"+								//[10]
					"\texeutionLeaderPermission TINYINT(1) NOT NULL,"+							//[11]
					"\texaminerPermission TINYINT(1) NOT NULL," + 								//[12]
					"\tchangeControlCommitteeChairmant TINYINT(1) NOT NULL," +					//[13]
					"\tPRIMARY KEY (userName));"); // create user table
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	public void closeConnection() {
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
			stmt.execute("CREATE TABLE icm.requirement (\n" +
					"\tInitiator VARCHAR(45) NOT NULL,\n" +
					"\tNum INT NOT NULL,\n" +
					"\tCurrentSituationDetails LONGTEXT NULL,\n" +
					"\tRequestDetails LONGTEXT NULL,\n" +
					"\tStageSupervisor VARCHAR(45) NULL,\n" +
					"\tStatus ENUM('ongoing', 'suspended', 'closed') NOT NULL DEFAULT 'ongoing',\n" +
					"\tPRIMARY KEY (Num, Initiator));"); // create table
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * for prototype.
	 * update status to some id in requirement table.
	 * 
	 * @param id
	 * @param status
	 * @return ArrayList<Object> or null
	 */
	public ArrayList<Object> updateQuery(int id, String status) { 
		PreparedStatement UpdateStmnt;
		try {
			UpdateStmnt = conn.prepareStatement("UPDATE icm.requirement SET status=? WHERE ID=? ");
			UpdateStmnt.setString(1, status);
			UpdateStmnt.setInt(2, id);
			UpdateStmnt.execute();
			UpdateStmnt.close();
			return readFromDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<Object> readFromDB() {
		Statement stmt;
		ResultSet re;
		ArrayList<Object> list = new ArrayList<>();
		try {
			stmt = conn.createStatement();
			re = stmt.executeQuery("SELECT * FROM icm.requirement;");
			while (re.next()) {
				list.add(re.getNString(1));
				list.add(re.getInt(2));
				list.add(re.getNString(3));
				list.add(re.getNString(4));
				list.add(re.getNString(5));
				list.add(re.getNString(6));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insertRequirement(String Initiator, String CurrentSituationDetails, String RequestDetails, String StageSupervisor, String status) { // send the use details.
		PreparedStatement stmt;
		Statement numTest;
		ResultSet re;
		int num = 0;
		try {
			numTest = conn.createStatement();
			re = numTest.executeQuery("SELECT requirement.num FROM icm.requirement;");// get all numbers submissions.
			while (re.next()) { // generate number for submission.
				num = re.getInt(1) + 1;
			}
			stmt = conn.prepareStatement("INSERT INTO icm.requirement " +
					"(Initiator," +
					"Num," +
					"CurrentSituationDetails," +
					"RequestDetails," +
					"StageSupervisor) " +
					"VALUES(?, ?, ?, ?, ?);");
			stmt.setNString(1, Initiator);
			stmt.setInt(2, num);
			stmt.setNString(3, CurrentSituationDetails);
			stmt.setNString(4, RequestDetails);
			stmt.setNString(5, StageSupervisor);
			//stmt.setNString(6, status);
			stmt.execute(); // insert new row to requirement table.
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkExistence() {
		try {
			String result = "0";
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery("SELECT count(*) AS result FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'icm';");
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

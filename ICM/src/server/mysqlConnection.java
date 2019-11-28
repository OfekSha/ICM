package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class mysqlConnection {
	private static Connection conn;
	private static String name="root";
	private static String password="Aa123456";
	public static void connect(){ 
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        	 }
        
        try 
        {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/?serverTimezone=IST",name,password);
            System.out.println("SQL connection succeed");
            
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}
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
		stmt.execute("CREATE SCHEMA `icm` ;"); // create schema
		stmt.execute("CREATE TABLE `icm`.`requirement` (\r\n" + 
				"  `Initiator` VARCHAR(45) NOT NULL,\r\n" + 
				"  `Num` INT NOT NULL,\r\n" + 
				"  `CurrentSituationDetails` LONGTEXT NULL,\r\n" + 
				"  `RequestDetails` LONGTEXT NULL,\r\n" + 
				"  `StageSupervisor` VARCHAR(45) NULL,\r\n" + 
				"  PRIMARY KEY (`Num`, `Initiator`));"); // create table
		
	}catch (SQLException e) {	e.printStackTrace();}
		
	}
public static void readFromDB(String where,String value){
	PreparedStatement stmt;
	ResultSet re;
	try {
		stmt = conn.prepareStatement("SELECT * FROM `icm`.`requirement` WHERE ?=?;");
		stmt.setString(1, where);
		stmt.setString(2, value);
		re=stmt.executeQuery();
		while(re.next()) {
			System.out.println("1: "+re.getNString(1));
			//System.out.println("2: "+re.getInt(2));
			//System.out.println("3: "+re.getNString(3));
			//System.out.println("4: "+re.getNString(4));
			//System.out.println("5: "+re.getNString(5));
		}
 	
	} catch (SQLException e) {	e.printStackTrace();}
	 		
}
public static void readFromDB(){
	Statement stmt;
	ResultSet re;
	try {
		stmt = conn.createStatement();
		re=stmt.executeQuery("SELECT * FROM `icm`.`requirement`;");
		while(re.next()) {
			System.out.println("1: "+re.getNString(1));
			System.out.println("2: "+re.getInt(2));
			System.out.println("3: "+re.getNString(3));
			System.out.println("4: "+re.getNString(4));
			System.out.println("5: "+re.getNString(5));
		}
 	
	} catch (SQLException e) {	e.printStackTrace();}
	 		
}
public static void readFromDB(String where,int value){
	PreparedStatement stmt;
	ResultSet re;
	try {
		stmt = conn.prepareStatement("SELECT `requirement`.* FROM `icm`.`requirement` WHERE ?=?;");
		stmt.setString(1, where);
		stmt.setInt(2, value);
		re=stmt.executeQuery();
		while(re.next()) {
			System.out.println(re.toString());
			System.out.println("1: "+re.getNString(1));
			System.out.println("2: "+re.getInt(2));
			System.out.println("3: "+re.getNString(3));
			System.out.println("4: "+re.getNString(4));
			System.out.println("5: "+re.getNString(5));
		}
 	
	} catch (SQLException e) {	e.printStackTrace();}
	 		
}	
	public static void insertRequirement(String Initiator,String CurrentSituationDetails,String RequestDetails,String StageSupervisor){ // send the use details.
		PreparedStatement stmt;
		Statement numTest;
		ResultSet re;
		int num=0;
		try {
			numTest=conn.createStatement();
			re=numTest.executeQuery("SELECT `requirement`.`num` FROM `icm`.`requirement` ;");// get all numbers submissions.
			while (re.next()) { // generate number for submission.
				num=re.getInt(1)+1;
			}
			stmt = conn.prepareStatement("INSERT INTO `icm`.`requirement` VALUES(?,?,?,?,?);");
			stmt.setNString(1, Initiator);
			stmt.setInt(2, num);
			stmt.setNString(3, CurrentSituationDetails);
			stmt.setNString(4, RequestDetails);
			stmt.setNString(5, StageSupervisor);
			stmt.execute(); // insert new row to requirement table.
	 	
		} catch (SQLException e) {	e.printStackTrace();}
		 		
	}
	
	
	
}



package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class mysqlConnection {
	private static Connection conn;
	private  String name;
	private  String password;
	private String ip;
	public mysqlConnection(String name,String password,String ip) {
		this.name=name;
		this.ip=ip;
		this.password=password;
	}
	public mysqlConnection(){
		this("root","Aa123456","localhost");
	}
	public void connect(){ 
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
            conn = DriverManager.getConnection("jdbc:mysql://"+ip+"/?serverTimezone=IST",name,password);
            System.out.println("SQL connection succeed");
            
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }
   	}
}
public  void closeConnection() {
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
		stmt.execute("CREATE SCHEMA `icm` ;"); // create schema
		stmt.execute("CREATE TABLE `icm`.`requirement` (\r\n" + 
				"  `Initiator` VARCHAR(45) NOT NULL,\r\n" + 
				"  `Num` INT NOT NULL,\r\n" + 
				"  `CurrentSituationDetails` LONGTEXT NULL,\r\n" + 
				"  `RequestDetails` LONGTEXT NULL,\r\n" + 
				"  `StageSupervisor` VARCHAR(45) NULL,\r\n" +
				"  `Status` ENUM('ongoing','suspended','closed') NOT NULL DEFAULT 'ongoing',\r\n" +
				"  PRIMARY KEY (`Num`, `Initiator`));"); // create table
		stmt.close();
	}catch (SQLException e) {	e.printStackTrace();}
		
	}
public void updateQuerry(String sqlQuerry){
	PreparedStatement UpdateStmnt;
	
	try {
		UpdateStmnt = conn.prepareStatement(sqlQuerry);
		UpdateStmnt.execute();
		readFromDB();
		UpdateStmnt.close();
	} catch (SQLException e) {	e.printStackTrace();}
	 		
}
public List<Object> readFromDB(){
	Statement stmt;
	ResultSet re=null;
	List<Object> list = new ArrayList<Object>();
	try {
		stmt = conn.createStatement();
		re=stmt.executeQuery("SELECT * FROM `icm`.`requirement`;");
		while(re.next()) {
			list.add(re.getNString(1));
			list.add(re.getInt(2));
			list.add(re.getNString(3));
			list.add(re.getNString(4));
			list.add(re.getNString(5));
			list.add(re.getNString(6));
		}
		stmt.close();
		
	} catch (SQLException e) {	e.printStackTrace();}
	 	return list;	
}
	public  void insertRequirement(String Initiator,String CurrentSituationDetails,String RequestDetails,String StageSupervisor,String status){ // send the use details.
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
			stmt = conn.prepareStatement("INSERT INTO `icm`.`requirement`(Initiator,num,CurrentSituationDetails,RequestDetails,StageSupervisor) VALUES(?,?,?,?,?);");
			stmt.setNString(1, Initiator);
			stmt.setInt(2, num);
			stmt.setNString(3, CurrentSituationDetails);
			stmt.setNString(4, RequestDetails);
			stmt.setNString(5, StageSupervisor);
			//stmt.setNString(6, status);
			stmt.execute(); // insert new row to requirement table.
			stmt.close();
		} catch (SQLException e) {	e.printStackTrace();}
		 		
	}
	
	
	
}



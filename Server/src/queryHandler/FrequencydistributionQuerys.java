package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Entity.EstimatorReport;

/**
 * concentrates  all querys  for the tables:  frequencydistribution -<?>
 *
 */
public class FrequencydistributionQuerys {

	
	private final QueryHandler queryHandler;
	
	 public FrequencydistributionQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	 
	 
	 // inserts******************************
	 
	public int InsertfrequencydistributionMonths(int [] frequencydistribution) {
		int count = 1;
		 if(frequencydistribution!=null) {
			 	
				//
				try {
					Statement numbTest = queryHandler.getmysqlConn().getConn().createStatement();
					ResultSet re = numbTest.executeQuery("SELECT Max(id) FROM `icm`.`frequencydistribution-months`");																																																
				while(	re.next()) // generate number for
				{
						count = re.getInt(1);		
				}
				count ++;
				} catch (SQLException e) {
					System.out.println("Database is empty, or no schema for ICM - insertRequirement");
					count = 1;
				}
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`frequencydistribution-months`\r\n" + 
	                    "(`id`,\r\n" + 
	                    "`1`,\r\n" + 
	                    "`2`,\r\n" + 
	                    "`3`,\r\n" + 
	                    "`4`,\r\n" + 
	                    "`5`,\r\n" + 
	                    "`6`,\r\n" + 
	                    "`7`,\r\n" + 
	                    "`8`,\r\n" + 
	                    "`9`,\r\n" + 
	                    "`10`,\r\n" + 
	                    "`11`,\r\n" + 
	                    "`12`)\r\n" + 
	                    "VALUES\r\n" + 
	                    "(?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?);\r\n" + 
	                    "");
	            stmt.setInt(1, count);
	            
	            for (int i=0 ;i<12 ;i++)
	            	  stmt.setInt(i+2, frequencydistribution[i]);

	            stmt.execute(); 
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	
		 }
		 return count;
	    } // end of InsertfrequencydistributionMonths()
	
	
	public int InsertfrequencydistributionDayInWeek(int [] frequencydistribution) {
		int count = 1;
		 if(frequencydistribution!=null) {
			 	
				//
				try {
					Statement numbTest = queryHandler.getmysqlConn().getConn().createStatement();
					ResultSet re = numbTest.executeQuery("SELECT Max(id) FROM `icm`.`frequencydistribution-daysinweek`");																																																
				while(	re.next()) // generate number for
				{
						count = re.getInt(1);		
				}
				count ++;
				} catch (SQLException e) {
					System.out.println("Database is empty, or no schema for ICM - insertRequirement");
					count = 1;
				}
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`frequencydistribution-daysinweek`\r\n" + 
	                    "(`id`,\r\n" + 
	                    "`1`,\r\n" + 
	                    "`2`,\r\n" + 
	                    "`3`,\r\n" + 
	                    "`4`,\r\n" + 
	                    "`5`,\r\n" + 
	                    "`6`,\r\n" + 
	                    "`7`)\r\n" + 
	                    "VALUES\r\n" + 
	                    "(?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?);\r\n" + 
	                    "");
	            stmt.setInt(1, count);
	            
	            for (int i=0 ;i<7 ;i++)
	            	  stmt.setInt(i+2, frequencydistribution[i]);

	            stmt.execute(); 
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	
		 }
		 return count;
	    } // end of InsertfrequencydistributionDayInWeek()
	
	
	public int InsertfrequencydistributionDayInMounth(int [] frequencydistribution) {
		int count =1;
		 if(frequencydistribution!=null) {
			 	
				//
				try {
					Statement numbTest = queryHandler.getmysqlConn().getConn().createStatement();
					ResultSet re = numbTest.executeQuery("SELECT Max(id) FROM `icm`.`frequencydistribution-daysinmonth`");																																																
				while(	re.next()) // generate number for
				{
						count = re.getInt(1);		
				}
				count ++;
				} catch (SQLException e) {
					System.out.println("Database is empty, or no schema for ICM - insertRequirement");
					count = 1;
				}
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`frequencydistribution-daysinmonth`\r\n" + 
	                    "(`id`,\r\n" + 
	                    "`1`,\r\n" + 
	                    "`2`,\r\n" + 
	                    "`3`,\r\n" + 
	                    "`4`,\r\n" + 
	                    "`5`,\r\n" + 
	                    "`6`,\r\n" + 
	                    "`7`,\r\n" + 
	                    "`8`,\r\n" + 
	                    "`9`,\r\n" + 
	                    "`10`,\r\n" + 
	                    "`11`,\r\n" + 
	                    "`12`,\r\n" + 
	                    "`13`,\r\n" + 
	                    "`14`,\r\n" + 
	                    "`15`,\r\n" + 
	                    "`16`,\r\n" + 
	                    "`17`,\r\n" + 
	                    "`18`,\r\n" + 
	                    "`19`,\r\n" + 
	                    "`20`,\r\n" + 
	                    "`21`,\r\n" + 
	                    "`22`,\r\n" + 
	                    "`23`,\r\n" + 
	                    "`24`,\r\n" + 
	                    "`25`,\r\n" + 
	                    "`26`,\r\n" + 
	                    "`27`,\r\n" + 
	                    "`28`,\r\n" + 
	                    "`29`,\r\n" + 
	                    "`30`,\r\n" + 
	                    "`31`)\r\n" + 
	                    "VALUES\r\n" + 
	                    "(?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?);\r\n" + 
	                    "");
	            stmt.setInt(1, count);
	            
	            for (int i=0 ;i<31 ;i++)
	            	  stmt.setInt(i+2, frequencydistribution[i]);

	            stmt.execute(); 
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	
		 }
		 return count;
	    } // end of InsertfrequencydistributionDayInMounth()
	
	
	//selects ***********************************************************
	
	
	
	public int[] SelectfrequencydistributionMonths( int ID) {
		int[] toReturn = new int[12];
    	try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "SELECT * FROM `icm`.`frequencydistribution-months` WHERE id = ? ;");
            stmt.setInt(1,ID);
            ResultSet re =  stmt.executeQuery(); 
            
            while (re.next()) {
            	for(int i=0;i<12;i++)
            	toReturn[i]=re.getInt(i+1);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return toReturn;
    } // END of  SelectfrequencydistributionMonths()
	
	public int[] SelectfrequencydistributionDayInWeek( int ID) {
		int[] toReturn = new int[7];
    	try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "SELECT * FROM `icm`.`frequencydistribution-daysinweek` WHERE id = ? ;");
            stmt.setInt(1,ID);
            ResultSet re =  stmt.executeQuery(); 
            
            while (re.next()) {
            	for(int i=0;i<7;i++)
            	toReturn[i]=re.getInt(i+1);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return toReturn;
    } // END of  SelectfrequencydistributionDayInWeek()
	
	
	public int[] SelectfrequencydistributionDayInMounth( int ID) {
		int[] toReturn = new int[31];
    	try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "SELECT * FROM `icm`.`frequencydistribution-daysinmonth` WHERE id = ? ;");
            stmt.setInt(1,ID);
            ResultSet re =  stmt.executeQuery(); 
            
            while (re.next()) {
            	for(int i=0;i<31;i++)
            	toReturn[i]=re.getInt(i+1);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return toReturn;
    } // END of  SelectfrequencydistributionDayInMounth()
	
} // END of class frequencydistributionQuerys

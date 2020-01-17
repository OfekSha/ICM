package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import Entity.Message;
import Entity.User;

public class MessagesQuerys {
	private final QueryHandler queryHandler;
	
	 public MessagesQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	 public int InsertMessags (Message message) {
		 if(message!=null) {
			 	int count = 0;
				//
				
				try {
					Statement numbTest = queryHandler.getmysqlConn().getConn().createStatement();
					ResultSet re = numbTest.executeQuery("SELECT Max(id) FROM icm.messages");																																																
				while(	re.next()) 
				{
						count = re.getInt(1);		
				}
				count ++;
				} catch (SQLException e) {
					System.out.println("Database is empty, or no schema for ICM - messages");
					count = 1;
				}
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`messages`\r\n" + 
	                    "(`id`,\r\n" + 
	                    "`from`,\r\n" + 
	                    "`to`,\r\n" + 
	                    "`messege`)\r\n" + 
	                    "VALUES\r\n" + 
	                    "(?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?);\r\n" + 
	                    "");
	            stmt.setInt(1, count);
	            stmt.setString(2, message.getFrom());
	            stmt.setString(3, message.getTo());
	            stmt.setString(4, message.getMessege());
	            
	            stmt.execute(); // insert new row to requirement table
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return count;
		 }
		 return 0;
	    } // end of InsertMessags()
	 
	 

		public ArrayList<Message> SelectMessages(User name) {
			ArrayList<Message> toReturn = new ArrayList<Message>();
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "SELECT * FROM icm.messages where `to` = ? ;");
	            stmt.setString(1,name.getUserName());
	            ResultSet re =  stmt.executeQuery(); 
	            
	            while (re.next()) {
	            	toReturn.add(new Message(re.getInt(1),re.getString(2),re.getString(3),re.getString(4)));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return toReturn;
	    } // ENd of SelectMessages
	 
	 

		public ArrayList<Message> DeleteMessages(Message  message) {
			ArrayList<Message> toReturn = new ArrayList<Message>();
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "DELETE FROM `icm`.`messages` WHERE (`id` = ?);");
	            stmt.setInt(1,message.getId());
	            stmt.executeQuery(); 
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return toReturn;
	    } // ENd of SelectMessages
} // END of MessagesQuerys class

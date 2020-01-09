package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.Initiator;
import Entity.ProcessStage;
import Entity.User;
import Entity.InspectorUpdateDescription;
import Entity.InspectorUpdateDescription.inspectorUpdateKind;
import Entity.ChangeRequest.ChangeRequestStatus;

/**
 * concentrates  all querys  for the table inspectorupdates 
 *
 */
public class InspectorUpdatesQuerys {

	private final QueryHandler queryHandler;

	public InspectorUpdatesQuerys(QueryHandler queryHandler) {
		this.queryHandler = queryHandler;
	}
	
	 /** insets an inspectorUpdateDescription to the DB
	 * @param newUpdate - the incoming update 
	 * @return - the new updates id
	 * @see InspectorUpdateDescription
	 */
	public String InsertInspectorUpdates(InspectorUpdateDescription newUpdate) {
	    	int count = 0;
	        try {
	            Statement numTest = queryHandler.getmysqlConn().getConn().createStatement();
	            ResultSet re = numTest.executeQuery("SELECT updateID FROM icm.inspectorupdates");// get all numbers submissions.
	            while (re.next()) { // generate number for submission.
	                count++;
	            }
	            } catch (SQLException e) {
	            count = 0;
	        }
	        count++;
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`inspectorupdates`\r\n" + 
	                    "(`updateID`,\r\n" + 
	                    "`RequestID`,\r\n" + 
	                    "`inspector`,\r\n" + 
	                    "`updatDescription`,\r\n" + 
	                    "`updateDate`,\r\n" + 
	                    "`updateKind`)\r\n" + 
	                    "VALUES\r\n" + 
	                    "(?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?);\r\n" + 
	                    "");
	            stmt.setNString(1, String.valueOf(count));
	            setinspectorUpdateDescriptionFieldsStmnt(newUpdate, stmt);
	            stmt.execute(); // insert new row to requirement table
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return String.valueOf(count);
	    } // end of InsertInspectorUpdates()
	 
	  /** sets inspectorUpdateDescription in a statement 
	 * @param newUpdate - the incoming update
	 * @param stmt	- the statement
	 * @throws SQLException
	 * 
	 * @see InspectorUpdateDescription
	 * @see PreparedStatement
	 */
	private void setinspectorUpdateDescriptionFieldsStmnt(InspectorUpdateDescription newUpdate, PreparedStatement stmt) throws SQLException {
	        stmt.setNString(2, newUpdate.getReferencedChangeRequest().getRequestID());
	        stmt.setNString(3, newUpdate.getInspector().getUserName());
	        stmt.setNString(4, newUpdate.getUpdateDescription());
	        stmt.setNString(5, newUpdate.getUpdateDate().toString());
	        stmt.setNString(6, newUpdate.getinspectorUpdateKind().name());

	    }// end of setinspectorUpdateDescriptionFieldsStmnt()
	
	/**  Updates a InspectorUpdateDescription in the DB
	 * @param newUpdate - the InspectorUpdateDescription to be updated in DB
	 */
	public void UpdateInspectorUpdates(InspectorUpdateDescription newUpdate) {
    	try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "UPDATE `icm`.`inspectorupdates`\r\n" + 
                    "SET\r\n" + 
                    "`updateID` = ?,\r\n" + 
                    "`RequestID` = ?,\r\n" + 
                    "`inspector` = ?,\r\n" + 
                    "`updatDescription` = ?,\r\n" + 
                    "`updateDate` = ?,\r\n" + 
                    "`updateKind` = ?\r\n" + 
                    "WHERE `updateID` = ?;\r\n" + 
                    "");
            stmt.setNString(1, newUpdate.getUpdateID());
            stmt.setNString(7, newUpdate.getUpdateID());
            setinspectorUpdateDescriptionFieldsStmnt(newUpdate, stmt);
            stmt.execute(); 
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return;
    } // end of UpdateInspectorUpdates()
	
	
	
    /** 
     * @param re - a result statement from a full query on the  inspectorupdates table
     * @return the inspector update  the result statement  held
     */
    private InspectorUpdateDescription getinspectorUpdateDescriptionFromRes(ResultSet re) {
    	InspectorUpdateDescription toPut =null;
        try {
        	String ID =re.getString(1);
        	User user =queryHandler.getUserQuerys().selectUser(re.getString(3));
        	String dec =re.getString(4);
        	LocalDate date =LocalDate.parse(re.getString(5));
        	inspectorUpdateKind kind =inspectorUpdateKind.valueOf(re.getString(6));
        	toPut=new InspectorUpdateDescription(user, dec, date, kind);
        	toPut.setUpdateID(ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toPut;
    } //END of getinspectorUpdateDescriptionFromRes()
    
    
	/**  
	 * @param RequestID - the request witch updates the method will get
	 * @return  all the of change request's inspector updates
	 */
	public ArrayList<InspectorUpdateDescription> SelectInspectorUpdates( String RequestID) {
		ArrayList<InspectorUpdateDescription> toReturn = new ArrayList<>();
    	try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "SELECT `inspectorupdates`.`updateID`,\r\n" + 
                    "    `inspectorupdates`.`RequestID`,\r\n" + 
                    "    `inspectorupdates`.`inspector`,\r\n" + 
                    "    `inspectorupdates`.`updatDescription`,\r\n" + 
                    "    `inspectorupdates`.`updateDate`,\r\n" + 
                    "    `inspectorupdates`.`updateKind`\r\n" + 
                    "FROM `icm`.`inspectorupdates` WHERE RequestID = ? ;\r\n" + 
                    "");
            stmt.setNString(1,RequestID);
            ResultSet re =  stmt.executeQuery(); 
            
            
            while (re.next()) {
            	toReturn.add(getinspectorUpdateDescriptionFromRes(re));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return toReturn;
    } // end of SelectInspectorUpdates()
    
	/** chooses whether updates need to be inserted to DB or updated 
	 * @param newUpdates  - the updates  
	 * @param RequestID -  the related request 
	 */
	public void UpdateOrInsertInspectorUpdates(ArrayList<InspectorUpdateDescription>  newUpdates,String RequestID) {
		if (newUpdates!=null) {
			ArrayList<InspectorUpdateDescription>  oldUpdates =SelectInspectorUpdates(RequestID);
	    	for (InspectorUpdateDescription e: newUpdates) {
	    		if(oldUpdates.contains(e)) UpdateInspectorUpdates(e);
	    		else InsertInspectorUpdates(e);
	    	}
	    	}
	}//END of UpdateOrInsertInspectorUpdates
	
    
	
	 
	 
}// END inspectorUpdatesQuerys

package queryHandler;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entity.Initiator;
import Entity.User;
import theServer.mysqlConnection;

/**
 * concentrates  all querys  for the table initiator
 *
 */
public class InitiatorQuerys {

	
	 private final QueryHandler queryHandler;
		
	 public InitiatorQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	    
	    
	    
	    /**Inserting Initiator in to DB
	     * @param initiator ?
	     */
	    public void insertInitiator(Initiator initiator) {
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO icm.initiator " +
	            		"(RequestID, userName) VALUES(?, ?);");
	            setAllInitiatorFieldsStatement(initiator,stmt);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }// END insertInitiator()
	    
	    /**sets up all of the  Initiator fields in to a Prepared Statement
	     * mainly used to support insets and updates
	     * @param initiator ?
	     * @param stmt ?
	     * @throws SQLException ?
	     */
	    private void setAllInitiatorFieldsStatement(Initiator initiator ,PreparedStatement stmt) throws SQLException {
	    	 stmt.setNString(1, initiator.getrequest().getRequestID());
	         stmt.setNString(2, initiator.getTheInitiator().getUserName());
	         stmt.execute(); // insert new row 
	         stmt.close();
	    }//END setAllInitiatorFieldsStatement();
	    
	    public void updateAllInitiatorFields(Initiator initiator) {
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "UPDATE icm.initiator " +
	            		"SET RequestID = ?, userName = ? " +
	            		"WHERE requestID = ?;");
	            stmt.setNString(3,initiator.getrequest().getRequestID());
	            setAllInitiatorFieldsStatement(initiator,stmt);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    } //End updateAllInitiator()

	    
	    /** get the  Initiator without change request
	     * its purpose is to support getAllChangeRequest() 
	     * @param RequestID ?
	     * @return ?
	    */
	    public Initiator getInitiator(String RequestID) {
	        Initiator returnInitiator = null;
	        String Username = null;
	        try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "SELECT * FROM icm.initiator WHERE initiator.RequestID = ?;");
	            stmt.setNString(1, RequestID);
	            ResultSet re = stmt.executeQuery();
	            while (re.next()) {
	                Username = re.getString(2);
	            }
	            User user = queryHandler.getUserQuerys().selectUser(Username);
	            returnInitiator = new Initiator(user, null);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return returnInitiator;
	    }

}// END of InitiatorQuerys class

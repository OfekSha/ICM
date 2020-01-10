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
import Entity.InspectorUpdateDescription;
import Entity.ProcessStage;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import theServer.mysqlConnection;

/** concentrates  all querys  for the table changeRequest
 * 
 *
 */
public class ChangeRequestQuerys {
	

	 private final QueryHandler queryHandler;
		
	 public ChangeRequestQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	    
	    
	    /**Inserting ChangeRequest
	     * @param newRequest ?
	     * @return string  - given id of the request
	     */
	    public String InsertChangeRequest(ChangeRequest newRequest) {
	    	int count = 0;
	        try {
	            Statement numTest = queryHandler.getmysqlConn().getConn().createStatement();
	            ResultSet re = numTest.executeQuery("SELECT RequestID FROM icm.changeRequest");// get all numbers submissions.
	            while (re.next()) { // generate number for submission.
	                count++;
	            }
	        } catch (SQLException e) {
	            System.out.println("Database is empty, or no schema for ICM - InsertChangeRequest");
	            count = 0;
	        }
	        count++;
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO icm.changerequest " +
	            		"(RequestID, " +
	            		"startDate, " +
	            		"`system`, " +
	            		"problemDescription, " +
	            		"changeReason, " +
	                    "comment, " +
	            		"status,"
	            		+ "baseforChange"
	            		+ ")" +
	            		"VALUES(?, ?, ?, ?, ?, ?, ?,?);");
	            stmt.setNString(1, String.valueOf(count));
	            setChangeRequestFieldsStmnt(newRequest, stmt);
	            stmt.execute(); // insert new row to requirement table
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	queryHandler.getInspectorUpdatesQuerys().UpdateOrInsertInspectorUpdates(newRequest.getInspectorUpdateDescription(),newRequest.getRequestID());
	    	return String.valueOf(count);
	    } // end of InsertChangeRequest()

	    public void updateAllChangeRequestFields(ChangeRequest changeRequest) {
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "UPDATE icm.changerequest SET "
	            		+ "RequestID = ?,"
	            		+ "startDate = ?,"
	            		+ "`system` = ?,"
	            		+ "problemDescription = ?,"
	            		+ "changeReason = ?,"
	            		+ "comment = ?,"
	            		+ "status = ?,"
	            		+ "baseforChange=?"
	            		+ " WHERE (RequestID = ?);");
	            stmt.setNString(1, changeRequest.getRequestID());
	            setChangeRequestFieldsStmnt(changeRequest, stmt);
	            stmt.setNString(9, changeRequest.getRequestID());

	            stmt.execute(); // insert new row to requirement table
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	queryHandler.getProccesStageQuerys().updateAllProcessStageFields(changeRequest.getProcessStage());
	    	queryHandler.getInitiatorQuerys().updateAllInitiatorFields(changeRequest.getInitiator());
	    	queryHandler.getInspectorUpdatesQuerys().UpdateOrInsertInspectorUpdates(changeRequest.getInspectorUpdateDescription(),changeRequest.getRequestID());

		    	
		    	
	    }// END updateChangeRequest()

	    private void setChangeRequestFieldsStmnt(ChangeRequest newRequest, PreparedStatement stmt) throws SQLException {
	        stmt.setNString(2, newRequest.getStartDate().toString());
	        stmt.setNString(3, newRequest.getSystem() );
	        stmt.setNString(4, newRequest.getProblemDescription() );
	        stmt.setNString(5, newRequest.getChangeReason());
	        stmt.setNString(6, newRequest.getComment());
	        stmt.setNString(7, newRequest.getStatus().name());
	        stmt.setNString(8, newRequest.getBaseforChange());
	    }

	    
	    /** gets all of the change requests in DB
	     * @return -ArrayList<ChangeRequest>
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequest() {
	        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
	        Statement stmt;
	        ResultSet re;
	        try {
	            stmt = queryHandler.getmysqlConn().getConn().createStatement();
	            re = stmt.executeQuery("SELECT * FROM icm.changerequest;");
	            while (re.next()) {
	                toReturn.add(getChangeRequestsFromRes(re));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        
	        return toReturn;
	    } // END of getAllChangeRequest();
	    
	    /**getting all requests with the specified status 
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatus(ChangeRequestStatus stat) {
	        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
	    
	        try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement("SELECT * FROM icm.changerequest where status = ?;");
	            stmt.setNString(1,stat.name());
	            ResultSet re = stmt.executeQuery();
	           while (re.next()) {
	                toReturn.add(getChangeRequestsFromRes(re));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return toReturn;
	    } // END of getAllChangeRequest();
	    
	    /**get all requests in a specified:  stage AND substage AND state  
	     * @param currentStage ?
	     * @param currentSubStage ?
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndStage(ChargeRequestStages currentStage ,subStages currentSubStage, ChangeRequestStatus stat) {
	        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
	    
	        try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "SELECT K.RequestID, startDate, `system`, problemDescription, changeReason, comment, status, baseforChange " +
	            		"from (SELECT icm.stage.RequestID " +
	            		"FROM icm.stage " +
	            		"WHERE currentStage = ? And currentSubStage = ?) as T " +
	            		"INNER JOIN (SELECT * FROM icm.changerequest WHERE status = ?) as K " +
	            		"ON T.RequestID = K.RequestID");
	            stmt.setNString(1, currentStage.name());
	            stmt.setNString(2, currentSubStage.name());
	            stmt.setNString(3, stat.name());
	            ResultSet re = stmt.executeQuery();
	           while (re.next()) {
	                toReturn.add(getChangeRequestsFromRes(re));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return toReturn;
	    } // END of getAllChangeRequestWithStatusAndStage();
	    
	    
	    /** get all requests in a specified:  sub stage  AND state AND StageSupervisor
	     * @param currentStage ?
	     * @param currentSubStage ?
	     * @param stat ?
	     * @param username ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndStageAndSupervisor(ChargeRequestStages currentStage ,subStages currentSubStage,
	                                                                                       ChangeRequestStatus stat , String username) {
	        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
	    
	        try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "select K.RequestID, startDate, `system`, problemDescription, changeReason, comment, status, baseforChange " +
	            		"from (SELECT icm.stage.RequestID " +
	            		"FROM icm.stage " +
	            		"WHERE currentStage = ? And currentSubStage = ? AND StageSupervisor = ?) as T " +
	            		"INNER JOIN (SELECT * FROM icm.changerequest WHERE status = ?) as K " +
	            		"on T.RequestID = K.RequestID");
	            stmt.setNString(1,currentStage.name());
	            stmt.setNString(2,currentSubStage.name());
	            stmt.setNString(3,username);
	            stmt.setNString(4,stat.name());
	            ResultSet re = stmt.executeQuery();
	           while (re.next()) {
	                toReturn.add(getChangeRequestsFromRes(re));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return toReturn;
	    } // END of getAllChangeRequestWithStatusAndStageAndSupervisor();
	    
	    /**   get all requests in a specified:  sub stage  AND state
	     * @param currentSubStage ?
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndSubStageOnly(subStages currentSubStage, ChangeRequestStatus stat) {
	        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
	    
	        try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "SELECT K.RequestID, startDate, `system`, problemDescription, changeReason, comment, status, baseforChange " +
	            		"FROM (SELECT icm.stage.RequestID " +
	            		"FROM icm.stage " +
	            		"WHERE currentSubStage = ?) as T " +
	            		"INNER JOIN (SELECT * FROM icm.changerequest WHERE status = ?) as K " +
	            		"on T.RequestID = K.RequestID");
	            stmt.setNString(1,currentSubStage.name());
	            stmt.setNString(2,stat.name());
	            ResultSet re = stmt.executeQuery();
	           while (re.next()) {
	                toReturn.add(getChangeRequestsFromRes(re));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return toReturn;
	    } // END of getAllChangeRequestWithStatusAndSubStageOnly();
	    
	    /** get all requests in a specified:  stage  AND state
	     * @param currentStage ?
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndStageOnly(ChargeRequestStages currentStage , ChangeRequestStatus stat) {
	        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
	        try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "SELECT K.RequestID, startDate,`system`, problemDescription, changeReason, comment, status, baseforChange " +
	                    "FROM (SELECT icm.stage.RequestID " +
	                    "FROM icm.stage " +
	                    "WHERE currentStage = ?) as T " +
	                    "INNER JOIN (SELECT * FROM icm.changerequest WHERE status = ?) as K " +
	                    "ON T.RequestID = K.RequestID");
	            stmt.setNString(1, currentStage.name());
	            stmt.setNString(2, stat.name());
	            ResultSet re = stmt.executeQuery();
	            while (re.next()) {
	                toReturn.add(getChangeRequestsFromRes(re));
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return toReturn;
	    } // END of getAllChangeRequestWithStatusAndStageOnly();
	    
	    private ChangeRequest getChangeRequestsFromRes(ResultSet re) {
	        ChangeRequest toPut;
	        try {
	            String RequestID = re.getString(1);
	            LocalDate startDate;
	            if (re.getString(2) != null) {
	                startDate = LocalDate.parse(re.getString(2));
	            } else startDate = null;
	            String system = re.getString(3);
	            String description = re.getString(4);
	            String reason = re.getString(5);
	            String comment = re.getString(6);
	            String status = re.getString(7);
	            String baseforChange = re.getString(8);
	            Initiator theInitiator =queryHandler.getInitiatorQuerys().getInitiator(RequestID);
	            // TODO:
	            Document doc = null;
	            //
	            ProcessStage stage = queryHandler.getProccesStageQuerys().getProcessStage(RequestID);//<-----

	            ChangeRequestStatus statusEnum = ChangeRequest.ChangeRequestStatus.valueOf(status);

	            toPut = new ChangeRequest(theInitiator, startDate, system, description, reason, comment, baseforChange,null);
	            toPut.setStatus(statusEnum);
	            toPut.setRequestID(RequestID);
	            toPut.setStage(stage);
	            toPut.updateStage();
	            toPut.updateInitiatorRequest();
	            toPut.setInspectorUpdateDescription(queryHandler.getInspectorUpdatesQuerys().SelectInspectorUpdates(toPut.getRequestID()));
	            //toPut.updateStage();
	            return toPut;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return null;
	    } //END of getChangeRequestsFromRes()
}// END of ChangeRequestQuerys

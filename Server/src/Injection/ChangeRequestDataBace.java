package Injection;

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
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import queryHandler.QueryHandler;

public interface ChangeRequestDataBace {

	public  void setQueryHandler( QueryHandler queryHandler);
	    
	    /**Inserting ChangeRequest
	     * @param newRequest ?
	     * @return string  - given id of the request
	     */
	    public int InsertChangeRequest(ChangeRequest newRequest) ;

	    /** updated a change request in DB
	     * @param changeRequest - the change request witch will be updated in the DB
	     */
	    public void updateAllChangeRequestFields(ChangeRequest changeRequest);
	    
	    /** gets all of the change requests in DB
	     * @return -ArrayList<ChangeRequest>
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequest() ;
	    
	    /**getting all requests with the specified status 
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatus(ChangeRequestStatus stat);
	    
	    /**get all requests in a specified:  stage AND substage AND state  
	     * @param currentStage ?
	     * @param currentSubStage ?
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndStage(ChargeRequestStages currentStage ,subStages currentSubStage, ChangeRequestStatus stat);
	    
	    
	    /** get all requests in a specified:  sub stage  AND state AND StageSupervisor
	     * @param currentStage ?
	     * @param currentSubStage ?
	     * @param stat ?
	     * @param username ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndStageAndSupervisor(ChargeRequestStages currentStage ,subStages currentSubStage,
	                                                                                       ChangeRequestStatus stat , String username) ;
	    
	    /**   get all requests in a specified:  sub stage  AND state
	     * @param currentSubStage ?
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndSubStageOnly(subStages currentSubStage, ChangeRequestStatus stat) ;
	    
	    /** get all requests in a specified:  stage  AND state
	     * @param currentStage ?
	     * @param stat ?
	     * @return ?
	     */
	    public ArrayList<ChangeRequest> getAllChangeRequestWithStatusAndStageOnly(ChargeRequestStages currentStage , ChangeRequestStatus stat);
	    
  
	    public ChangeRequest getChangeRequest(int ID);
	
}

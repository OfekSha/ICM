package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import Entity.EstimatorReport;
import Entity.InspectorUpdateDescription;
import Entity.User;
import Entity.InspectorUpdateDescription.inspectorUpdateKind;

/**
 * concentrates  all querys  for the table estimatorreports 
 *
 */
public class EstimatorReportQuerys {
	private final QueryHandler queryHandler;
	
	 public EstimatorReportQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	 
	 /** Inserts new reports to DB
	 * @param newReport -the newly added report
	 * @return the reports id in the DB
	 */
	public String InsertEstimatorReport (EstimatorReport newReport) {
		 if(newReport!=null) {
	    	int count = 0;
	        try {
	            Statement numTest = queryHandler.getmysqlConn().getConn().createStatement();
	            ResultSet re = numTest.executeQuery("SELECT estimatorReportID FROM icm.estimatorreports");// get all numbers submissions.
	            while (re.next()) { // generate number for submission.
	                count++;
	            }
	            } catch (SQLException e) {
	            count = 0;
	        }
	        count++;
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`estimatorreports`\r\n" + 
	                    "(`estimatorReportID`,\r\n" + 
	                    "`referencedRequestID`,\r\n" + 
	                    "`estimatorUsername`,\r\n" + 
	                    "`location`,\r\n" + 
	                    "`changeDescription`,\r\n" + 
	                    "`resultingResult`,\r\n" + 
	                    "`constraints`,\r\n" + 
	                    "`timeEstimate`,"
	                    + "`risks`)\r\n" + 
	                    "VALUES\r\n" + 
	                    "(?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,?,\r\n" + 
	                    "?);\r\n" + 
	                    "");
	            stmt.setNString(1, String.valueOf(count));
	            setEstimatorReportFieldsStmnt(newReport, stmt);
	            stmt.execute(); // insert new row to requirement table
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return String.valueOf(count);
		 }
		 return null;
	    } // end of InsertEstimatorReport()
	 
	 /** sets all EstimatorReportFields besides the primary key
	 * @param newReport - the report that is beeng set
	 * @param stmt	- the statement  beeng set to
	 * @throws SQLException
	 */
	private void setEstimatorReportFieldsStmnt(EstimatorReport newReport, PreparedStatement stmt) throws SQLException {
		 
	        stmt.setNString(2, newReport.getReferencedRequest().getRequestID());
	        stmt.setNString(3, newReport.getEstimator().getUserName());
	        stmt.setNString(4, newReport.getlocation());
	        stmt.setNString(5, newReport.getChangeDescription());
	        stmt.setNString(6, newReport.getResultingResult());
	        stmt.setNString(7, newReport.getConstraints());
	        stmt.setNString(8, newReport.getTimeEstimate().toString());
	        stmt.setNString(9, newReport.getRisks());


	    }// end of setEstimatorReportFieldsStmnt()
	 
	 
	 /** updates Estimator report in DB
	 * @param newReport - the new report
	 */
	public void UpdateEstimatorReport (EstimatorReport  newReport) {
		 if (newReport!=null) {
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "UPDATE `icm`.`estimatorreports`\r\n" + 
	                    "SET\r\n" + 
	                    "`estimatorReportID` = ?,\r\n" + 
	                    "`referencedRequestID` = ?,\r\n" + 
	                    "`estimatorUsername` = ?,\r\n" + 
	                    "`location` = ?,\r\n" + 
	                    "`changeDescription` = ?,\r\n" + 
	                    "`resultingResult` = ?,\r\n" + 
	                    "`constraints` = ?,\r\n" + 
	                    "`timeEstimate` = ?,\r\n" +
	                    "`risks` = ?\r\n" + 
	                    "WHERE `estimatorReportID` = ?;\r\n" + 
	                    "");
	            
	            stmt.setNString(1, newReport.getEstimatorReportID());
	            stmt.setNString(10, newReport.getEstimatorReportID());
	            setEstimatorReportFieldsStmnt(newReport, stmt);
	            stmt.execute(); 
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return;
		 }
	    } // end of UpdateEstimatorReport()
	 
	   /** gets Estimator data form a ResultSet to EstimatorReport
	 * @param re -the ResultSet
	 * @return -EstimatorReport
	 */
	private EstimatorReport getEstimatorReportFromRes(ResultSet re) {
		   EstimatorReport toPut =null;
	        try {
	        	String ID =re.getString(1);
	        	User user =queryHandler.getUserQuerys().selectUser(re.getString(3));
	        	String loc =re.getString(4);
	        	String changeDescription =re.getString(5);
	        	String resultingResult =re.getString(6);
	        	String constraints =re.getString(7);
	        	LocalDate date =LocalDate.parse(re.getString(8));
	        	String risks =re.getString(9);
	        	toPut=new EstimatorReport(user, loc, changeDescription, resultingResult,constraints,risks,date);
	        	toPut.setEstimatorReportID(ID);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return toPut;
	    } //END of getinspectorUpdateDescriptionFromRes()
	 
	 
	   
		/**  
		 * @param RequestID - the request withch the report is linked to 
		 * @return  the linked to the request EstimatorReport
		 */
		public EstimatorReport SelectEstimatorreports( String RequestID) {
			EstimatorReport toReturn = null;
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "SELECT * FROM icm.estimatorreports WHERE referencedRequestID = ? ;");
	            stmt.setNString(1,RequestID);
	            ResultSet re =  stmt.executeQuery(); 
	            
	            while (re.next()) {
	            	toReturn=getEstimatorReportFromRes(re);
	            }
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return toReturn;
	    }
		
		
		/** choses whether the report needs to be inserted or updated
		 * @param newUpdates
		 * @param RequestID
		 */
		public void UpdateOrInsertEstimatorReport(EstimatorReport newUpdates,String RequestID) {
			if (newUpdates!=null) {
				EstimatorReport  old =SelectEstimatorreports(RequestID);
		    		if(old!=null) UpdateEstimatorReport(newUpdates);
		    		else InsertEstimatorReport(newUpdates);
		    	}
		}//END of UpdateOrInsertInspectorUpdates
		
	 
	 
}// END of  EstimatorReportQuerys

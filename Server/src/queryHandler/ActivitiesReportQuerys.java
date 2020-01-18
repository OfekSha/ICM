package queryHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.mysql.cj.x.protobuf.MysqlxNotice.Frame.Scope;

import Entity.ActivitiesReport;
import Entity.EstimatorReport;
import Entity.User;
import reporting.ReportController.reportScope;

/** * concentrates  all querys  for the tables:  activitiesreport 

 * 
 *
 */
public class ActivitiesReportQuerys {
	private final QueryHandler queryHandler;
	
	 public ActivitiesReportQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	
	 
	 
	 public int InsertActivitiesReport (ActivitiesReport newReport) {
		 if(newReport!=null) {
			 	int count = 0;
				//
				
				try {
					Statement numbTest = queryHandler.getmysqlConn().getConn().createStatement();
					ResultSet re = numbTest.executeQuery("SELECT Max(reportID) FROM `icm`.`activitiesreport`");																																																
				while(	re.next()) // generate number for
				{
						count = re.getInt(1);		
				}
				count ++;
				} catch (SQLException e) {
					System.out.println("Database is empty, or no schema for ICM ");
					count = 1;
				}
	    	try {
	            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
	                    "INSERT INTO `icm`.`activitiesreport`\r\n" + 
	                    "(`reportID`,\r\n" +  // 1
	                    "`reportScope`,\r\n" + 
	                    "`startDate`,\r\n" + 
	                    "`endDate`,\r\n" + 
	                    "`creationDate`,\r\n" + 
	                    "`ongoingRequestsMedian`,\r\n" + 
	                    "`ongoingRequestsStandardDeviation`,\r\n" + 
	                    "`ongoingRequests`,\r\n" + 
	                    "`ongoingRequestsFrequencyDistribution`,\r\n" + 
	                    "` suspendedRequestsMedian`,\r\n" + //10
	                    "` suspendedRequestsStandardDeviation`,\r\n" + 
	                    "` suspendedRequests`,\r\n" + 
	                    "` suspendedRequestsFrequencyDistribution`,\r\n" + 
	                    "`closedRequestsMedian`,\r\n" + 
	                    "`closedRequestsStandardDeviation`,\r\n" + 
	                    "`closedRequests`,\r\n" + 
	                    "`closedRequestsFrequencyDistribution`,\r\n" + 
	                    "`rejectedRequestsMedian`,\r\n" + 
	                    "`rejectedRequestsStandardDeviation`,\r\n" + 
	                    "`rejectedRequests`,\r\n" + //10
	                    "`rejectedRequestsFrequencyDistribution`,\r\n" + 
	                    "`treatmentDaysRequestsMedian`,\r\n" + 
	                    "`treatmentDaysStandardDeviation`,\r\n" + 
	                    "`treatmentDays`,\r\n" + 
	                    "`treatmentDaysFrequencyDistribution`)\r\n" + //5
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
	                    "?,\r\n" + //10
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + //10
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?,\r\n" + 
	                    "?);\r\n" + //5
	                    "");
	            stmt.setInt(1, count);
	            stmt.setNString(2,newReport.getScope().name());
	            stmt.setNString(3,newReport.getStart().toString());
	            stmt.setNString(4,newReport.getEnd().toString());
	            stmt.setNString(5,newReport.getCreationDate().toString());
	            //ongoing
	            stmt.setDouble(6,newReport.getOngoingRequests()[0]);
	            stmt.setDouble(7,newReport.getOngoingRequests()[1]);
	            stmt.setDouble(8,newReport.getOngoingRequests()[2]);
	            //suspended
	            stmt.setDouble(10,newReport.getSuspendedRequests()[0]);
	            stmt.setDouble(11,newReport.getSuspendedRequests()[1]);
	            stmt.setDouble(12,newReport.getSuspendedRequests()[2]);
	        
	            //Closed
	            stmt.setDouble(14,newReport.getClosedRequests()[0]);
	            stmt.setDouble(15,newReport.getClosedRequests()[1]);
	            stmt.setDouble(16,newReport.getClosedRequests()[2]);
	            //Rejected
	            stmt.setDouble(18,newReport.getRejectedRequests()[0]);
	            stmt.setDouble(19,newReport.getRejectedRequests()[1]);
	            stmt.setDouble(20,newReport.getRejectedRequests()[2]);
	            //TreatmentDays
	            stmt.setDouble(22,newReport.getTreatmentDays()[0]);
	            stmt.setDouble(23,newReport.getTreatmentDays()[1]);
	            stmt.setDouble(24,newReport.getTreatmentDays()[2]);
	            
	            
				switch (newReport.getScope()) {
				case dayOfmonth:
					// ongoing
					stmt.setInt(9, queryHandler.getFrequencydistributionQuerys().InsertfrequencydistributionDayInMounth(
							newReport.getOngoingRequestsFrequencyDistribution()));
					// suspended
					stmt.setInt(13,
							queryHandler.getFrequencydistributionQuerys().InsertfrequencydistributionDayInMounth(
									newReport.getSuspendedRequestsFrequencyDistribution()));
					// Closed
					stmt.setInt(17,
							queryHandler.getFrequencydistributionQuerys().InsertfrequencydistributionDayInMounth(
									newReport.getClosedRequestsFrequencyDistribution()));
					// Rejected
					stmt.setInt(21,
							queryHandler.getFrequencydistributionQuerys().InsertfrequencydistributionDayInMounth(
									newReport.getRejectedRequestsFrequencyDistribution()));
					// TreatmentDays
					stmt.setInt(25, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionDayInMounth(newReport.getTreatmentDaysFrequencyDistribution()));
					break;
				case dayOfweek:
					// ongoing
					stmt.setInt(9, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionDayInWeek(newReport.getOngoingRequestsFrequencyDistribution()));
					// suspended
					stmt.setInt(13, queryHandler.getFrequencydistributionQuerys().InsertfrequencydistributionDayInWeek(
							newReport.getSuspendedRequestsFrequencyDistribution()));
					// Closed
					stmt.setInt(17, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionDayInWeek(newReport.getClosedRequestsFrequencyDistribution()));
					// Rejected
					stmt.setInt(21, queryHandler.getFrequencydistributionQuerys().InsertfrequencydistributionDayInWeek(
							newReport.getRejectedRequestsFrequencyDistribution()));
					// TreatmentDays
					stmt.setInt(25, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionDayInWeek(newReport.getTreatmentDaysFrequencyDistribution()));
					break;
				case months:
					// ongoing
					stmt.setInt(9, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionMonths(newReport.getOngoingRequestsFrequencyDistribution()));
					// suspended
					stmt.setInt(13, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionMonths(newReport.getSuspendedRequestsFrequencyDistribution()));
					// Closed
					stmt.setInt(17, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionMonths(newReport.getClosedRequestsFrequencyDistribution()));
					// Rejected
					stmt.setInt(21, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionMonths(newReport.getRejectedRequestsFrequencyDistribution()));
					// TreatmentDays
					stmt.setInt(25, queryHandler.getFrequencydistributionQuerys()
							.InsertfrequencydistributionMonths(newReport.getTreatmentDaysFrequencyDistribution()));
					break;
				default:
					break;

				}
	            stmt.execute(); // insert new row to requirement table
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	return count;
		 }
		 return -1;
	    } // end of InsertEstimatorReport()

	 
	public ArrayList<ActivitiesReport> getAllActivitiesReports() {
		ArrayList<ActivitiesReport> toRetern = new ArrayList<ActivitiesReport>();
		try {
			Statement stmt = queryHandler.getmysqlConn().getConn().createStatement();
			ResultSet re = stmt.executeQuery("SELECT * FROM icm.activitiesreport;");
			while (re.next()) {
				toRetern.add(getActivitiesReportFromRes(re));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return toRetern;
	}// getAllActivitiesReports()
	 
	 
	 private ActivitiesReport  getActivitiesReportFromRes(ResultSet re) {
		 ActivitiesReport toPut =new ActivitiesReport();
		 double[] doubleArray = new  double[3];
	        try {
	        	toPut.setID(re.getInt(1)); ;    	
	        	toPut.setScope(reportScope.valueOf(re.getString(2)));
	        	toPut.setStart(LocalDate.parse(re.getString(3)));
	        	toPut.setEnd(LocalDate.parse(re.getString(4)));
	        	toPut.setCreationDate(LocalDate.parse(re.getString(5)));
	        	// ongoing
	        	doubleArray[0] =re.getDouble(6);
	        	doubleArray[1] =re.getDouble(7);
	        	doubleArray[2] =re.getDouble(8);
	        	toPut.setOngoingRequests(Arrays.copyOf(doubleArray,doubleArray.length));
	        	//9
				// suspended
	        	doubleArray[0] =re.getDouble(10);
	        	doubleArray[1] =re.getDouble(11);
	        	doubleArray[2] =re.getDouble(12);
	        	toPut.setSuspendedRequests(Arrays.copyOf(doubleArray,doubleArray.length));
	        	//13
				// Closed
	           	doubleArray[0] =re.getDouble(14);
	        	doubleArray[1] =re.getDouble(15);
	        	doubleArray[2] =re.getDouble(16);
	        	toPut.setClosedRequests(Arrays.copyOf(doubleArray,doubleArray.length));
	        	//17
				// Rejected
	        	doubleArray[0] =re.getDouble(18);
	        	doubleArray[1] =re.getDouble(19);
	        	doubleArray[2] =re.getDouble(20);
	        	toPut.setRejectedRequests(Arrays.copyOf(doubleArray,doubleArray.length));
	        	//21
				// TreatmentDays
	        	doubleArray[0] =re.getDouble(22);
	        	doubleArray[1] =re.getDouble(23);
	        	doubleArray[2] =re.getDouble(24);
	        	toPut.setTreatmentDays(Arrays.copyOf(doubleArray,doubleArray.length));
	        	//25
	        	
			switch(toPut.getScope()) {
			case dayOfmonth:
	        	// ongoing
	        	toPut.setOngoingRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInMounth(re.getInt(9)));
				// suspended 13
	        	toPut.setSuspendedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInMounth(re.getInt(13)));
				// Closed 17
	        	toPut.setClosedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInMounth(re.getInt(17)));
	        	// Rejected 21
	        	toPut.setRejectedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInMounth(re.getInt(21)));
	        	// TreatmentDays 25
	        	toPut.setTreatmentDaysFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInMounth(re.getInt(25)));
				break;
			case dayOfweek:
				
				// ongoing
	        	toPut.setOngoingRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInWeek(re.getInt(9)));
				// suspended 13
	        	toPut.setSuspendedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInWeek(re.getInt(13)));
				// Closed 17
	        	toPut.setClosedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInWeek(re.getInt(17)));
	        	// Rejected 21
	        	toPut.setRejectedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInWeek(re.getInt(21)));
	        	// TreatmentDays 25
	        	toPut.setTreatmentDaysFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionDayInWeek(re.getInt(25)));
				break;
			case months:
				// ongoing
	        	toPut.setOngoingRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionMonths(re.getInt(9)));
				// suspended 13
	        	toPut.setSuspendedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionMonths(re.getInt(13)));
				// Closed 17
	        	toPut.setClosedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionMonths(re.getInt(17)));
	        	// Rejected 21
	        	toPut.setRejectedRequestsFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionMonths(re.getInt(21)));
	        	// TreatmentDays 25
	        	toPut.setTreatmentDaysFrequencyDistribution(queryHandler.getFrequencydistributionQuerys().SelectfrequencydistributionMonths(re.getInt(25)));
				break;

			}
			
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return toPut;
	 }
} // END of ActivitiesReportQuerys class

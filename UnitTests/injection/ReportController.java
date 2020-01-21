package injection;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;


import Entity.ActivitiesReport;
import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.DelayReport;
import queryHandler.QueryHandler;

public class ReportController {

	
	public enum reportScope{
		
		months,
		dayOfweek, dayOfmonth
	}
	private final QueryHandler queryHandler;

	public ReportController(QueryHandler queryHandler) {
		this.queryHandler = queryHandler;
	}

	// assisting methods

	/**
	 * calculates Median
	 * 
	 * @param numbersArray -an array of of the values witch Median would be
	 *                     calculated to
	 * @return the median for parameter
	 */
	double calculateTheMedian(int[] numbersArray) {
		Arrays.sort(numbersArray);
		double median;
		if (numbersArray.length % 2 == 0)
			median = ((double) numbersArray[numbersArray.length / 2]
					+ (double) numbersArray[numbersArray.length / 2 - 1]) / 2;
		else
			median = (double) numbersArray[numbersArray.length / 2];
		return median;
	} // end of calculateTheMedian()

	/**
	 * calculates standard deviation
	 * 
	 * @param intArray - an array of of the values witch standard deviation would be
	 *                 calculated to
	 * @return the standard deviation for parameter
	 */
	public double calculatestandardDeviation(int[] intArray) {
		double[] doubleArray = new double[intArray.length];
		for (int i = 0; i < intArray.length; i++)
			doubleArray[i] = intArray[i];

		double sum = 0.0, standardDeviation = 0.0;
		int numAmount = doubleArray.length;
		for (double num : doubleArray)
			sum += num;
		double avrege = sum / numAmount;
		for (double num : doubleArray) {
			standardDeviation += Math.pow(num - avrege, 2);
		}
		return Math.sqrt(standardDeviation / numAmount);
	}// End of calculatestandardDeviation()

	
	
	
	public int[] newFrequencyDistribution(ArrayList<ChangeRequest> requests, reportScope chosenRange,FrequencyDistributionKind fdk) {
		int[] frequencyDistribution = null;
		switch (chosenRange) {
		case dayOfmonth:
			frequencyDistribution = new int[31];
			for (ChangeRequest e : requests) {
				int range = (e.getStartDate()).getDayOfMonth();
				frequencyDistribution[range - 1]+=fdk.addToDistribution(e);
			}
			break;
		case months:
			frequencyDistribution = new int[12];
			for (ChangeRequest e : requests) {
				int range = (e.getStartDate()).getMonthValue();
				frequencyDistribution[range - 1]+=fdk.addToDistribution(e);
			}
			break;
		// note that day of the week array is [1] Monday ..... [7] Sunday
		case dayOfweek:
			frequencyDistribution = new int[7];
			for (ChangeRequest e : requests) {
				int range = ((e.getStartDate()).getDayOfWeek()).getValue();
				frequencyDistribution[range - 1]+=fdk.addToDistribution(e);
			}
			break;
		}

		return frequencyDistribution;
	}

	

	/**
	 * removes requests which haven started in between start and end dates
	 * 
	 * @param requests
	 * @param start
	 * @param end
	 * @return
	 */
	public ArrayList<ChangeRequest> isInDateRange(ArrayList<ChangeRequest> requests, LocalDate start, LocalDate end) {
		ArrayList<ChangeRequest> toBeRemoved =new ArrayList();
		for (ChangeRequest e : requests) {
			if (!e.getStartDate().isAfter(start) || !e.getStartDate().isBefore(end))
				toBeRemoved.add(e);
		}
		for (ChangeRequest e : toBeRemoved) {
			requests.remove(e);
		}
		return requests;
	}// END isInRange()

	
	/** create a filed for a report <p>
	 * a filed composes of the values described in the return 
	 * @param requests
	 * @param chosenRange
	 * @param fdk - how you want to calculate the FrequencyDistribution
	 * @return
	 *         <p>
	 *         [0] frequencyDistribution
	 *         <p>
	 *         [1] Median
	 *         <p>
	 *         [2] standard Deviation
	 *         <p>
	 *         [3] request amount
	 * 
	 */
		public Object[] newReportFiled(ArrayList<ChangeRequest> requests, reportScope chosenScope,FrequencyDistributionKind fdk) {
			Object[] ret = new Object[4];
			ret[0] = newFrequencyDistribution(requests, chosenScope,fdk);
			int len=((int[])ret[0]).length;
			int[] send=(int[])ret[0];
			ret[1] = calculateTheMedian(Arrays.copyOf(send,len));
			ret[2] = calculatestandardDeviation(Arrays.copyOf(send,len));	
			ret[3] = arraySum(send); // the arry is filed with the valus we care about - the sum is the total thing we cate about
			return ret;
		}
		
		
		/** Calculates the sum of an array 
		 * @param arr 
		 * @return
		 */
		private  int arraySum(int[] arr) {
			int sum =0;
			for(int e : arr)
				sum+=e;
			return sum;
		}

	

	// specific methods

	/** creates an  ActivitiesReport int the range of star - end , with the  chosenScope
	 * @param start - the date the report coverage  begins in 
	 * @param end - the day the report coverage ends
	 * @param chosenScope - @see reportScope
	 * @return a full  ActivitiesReport
	 * @see ActivitiesReport
	 * @see reportScope
	 */
	public ActivitiesReport creatActivitiesReport(LocalDate start, LocalDate end, reportScope chosenScope) {

		ActivitiesReport report = new ActivitiesReport();
		report.setScope(chosenScope);
		report.setStart(start);
		report.setEnd(end);
		report.setCreationDate(LocalDate.now());
		Object[] filed = new Object[4];
		double[] medianAndStandardDeviation = new double[3];
		// ongoing filed
		ArrayList<ChangeRequest> ongoing = queryHandler.getChangeRequestQuerys()
				.getAllChangeRequestWithStatus(ChangeRequestStatus.ongoing);
		if (ongoing!=null) {
		ongoing = isInDateRange(ongoing, start, end);
		//filed = reportFiled(ongoing, chosenScope); //@deprecated
		filed =newReportFiled(ongoing, chosenScope, new Count());
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setOngoingRequests(medianAndStandardDeviation);
		report.setOngoingRequestsFrequencyDistribution((int[]) filed[0]);
		}
		// suspended filed
		medianAndStandardDeviation= new double[3];
		filed = new Object[4];
		ArrayList<ChangeRequest> suspended = queryHandler.getChangeRequestQuerys()
				.getAllChangeRequestWithStatus(ChangeRequestStatus.suspended);
		if (suspended!=null) {
		suspended = isInDateRange(suspended, start, end);
		//filed = reportFiled(suspended, chosenScope); //@deprecated
		filed =newReportFiled(suspended, chosenScope, new Count());
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setSuspendedRequests(medianAndStandardDeviation);
		report.setSuspendedRequestsFrequencyDistribution((int[]) filed[0]);
		}
		// closed filed
		filed = new Object[4];
		medianAndStandardDeviation= new double[3];
		ArrayList<ChangeRequest> closed = queryHandler.getChangeRequestQuerys()
				.getAllChangeRequestWithStatus(ChangeRequestStatus.closed);
		if (closed!=null) {
		closed = isInDateRange(closed, start, end);
		//filed = reportFiled(closed, chosenScope); //@deprecated
		filed =newReportFiled(closed, chosenScope, new Count());
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setClosedRequests(medianAndStandardDeviation);
		report.setClosedRequestsFrequencyDistribution((int[]) filed[0]);
		}
		// Rejected filed
		filed = new Object[4];
		medianAndStandardDeviation= new double[3];
		ArrayList<ChangeRequest> rejected = queryHandler.getChangeRequestQuerys()
				.getAllChangeRequestWithStatus(ChangeRequestStatus.rejected);
		if (rejected!=null) {
		rejected = isInDateRange(rejected, start, end);
		//filed = reportFiled(rejected, chosenScope);//@deprecated
		filed =newReportFiled(rejected, chosenScope, new Count());

		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setRejectedRequests(medianAndStandardDeviation);
		report.setRejectedRequestsFrequencyDistribution((int[]) filed[0]);
		}
		// Treatment filed 
		filed = new Object[4];
		medianAndStandardDeviation= new double[3];
		ArrayList<ChangeRequest> all = queryHandler.getChangeRequestQuerys().getAllChangeRequest();
		if  (all!=null) {
		all = isInDateRange(all, start, end);
		//filed = reportFiledForDaysTreated(all, chosenScope);//@deprecated
		filed =newReportFiled(all, chosenScope, new ActiveDays());
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setTreatmentDays(medianAndStandardDeviation);
		report.setTreatmentDaysFrequencyDistribution((int[]) filed[0]);
		}
		return report;
	}// End of creatActivitiesReport()
	
	
	/**  Creates a delay report in the chosen Scope
	 * @param chosenScope @see reportScope
	 * @return a full DelayReport
	 * @see DelayReport 
	 */
	public DelayReport createDelayReport(reportScope chosenScope) {
		Object[] filed = new Object[4];
		double[] medianAndStandardDeviation = new double[3];
		DelayReport report = new DelayReport();
		report.setChosenScope(chosenScope);
		// length of late submissions
		ArrayList<ChangeRequest> all = queryHandler.getChangeRequestQuerys().getAllChangeRequest();
		all =testForLateStagesInAll(all);
		filed =newReportFiled(all, chosenScope, new DaysLate());
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setLengthofLateSubmissionFrequencyDistribution((int[]) filed[0]);
		report.setLengthofLateSubmission(medianAndStandardDeviation);
		// numOfLateSubmission
		filed =newReportFiled(all, chosenScope, new Count());
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setLateSubmissionFrequencyDistribution((int[]) filed[0]);
		report.setNumOfLateSubmission(medianAndStandardDeviation);
		return report;
	}//EndOf createDelayRepot 

	/** removes any request witch has no late stages 
	 * @param requests  - an array list of requests
	 * @return the array list without requests witch have no late stages 
	 */
	private ArrayList<ChangeRequest>testForLateStagesInAll( ArrayList<ChangeRequest> requests){
		ArrayList<ChangeRequest> toBeRemoved = new ArrayList<ChangeRequest>(); 
		for (ChangeRequest r :requests ) 
			if(!testForLateStagesInRequest(r)) toBeRemoved.add(r);
		for (ChangeRequest r :toBeRemoved ) 
			requests.remove(r);		
		return requests;
	}// END of testForLateStages
	
	/** testing whether one of the stages of the requests was completes late 
	 * @param request - the request we are testing
	 * @return true when there was  a stage completed later then expected  
	 */
	private boolean testForLateStagesInRequest(ChangeRequest request) {
		LocalDate[][] dates = request.getProcessStage().getDates();
		for (int i = 0; i < 5; i++) {
			if(dates[i][1]==null || dates[i][2] == null) return false; // meaning we haven't done the stage yet  -> if it wasn't late until now it can't be 
			if(dates[i][1].isBefore(dates[i][2]))  return true;	// if due date is before the ending time then it was late
		}
		return false;
	} // END of testForLateStagesInRequest()
	
	
	
	////////////////////////////////@deprecated
	
	
	/**@deprecated - use  newReportFiled() insted
	 * @param requests
	 * @param chosenRange
	 * @return
	 *         <p>
	 *         [0] frequencyDistribution
	 *         <p>
	 *         [1] Median
	 *         <p>
	 *         [2] standard Deviation
	 *         <p>
	 *         [3] request amount
	 * 
	 */
	public Object[] reportFiled(ArrayList<ChangeRequest> requests, reportScope chosenScope) {
		Object[] ret = new Object[4];
		ret[0] = frequencyDistribution(requests, chosenScope);
		int len=((int[])ret[0]).length;
		int[] send=(int[])ret[0];
		ret[1] = calculateTheMedian(Arrays.copyOf(send,len));
		ret[2] = calculatestandardDeviation(Arrays.copyOf(send,len));
		ret[3] = requests.size();
		return ret;
	}

	/**@deprecated - use  newReportFiled() insted
	 * @param requests
	 * @param chosenScope
	 * @return
	 * 
	 */
	public Object[] reportFiledForDaysTreated(ArrayList<ChangeRequest> requests, reportScope chosenScope) {
		Object[] ret = new Object[4];

		ret[0] = frequencyDistributionForDaysTreated(requests, chosenScope);
		int len=((int[])ret[0]).length;
		int[] send=(int[])ret[0];
		ret[1] = calculateTheMedian(Arrays.copyOf(send,len));
		ret[2] = calculatestandardDeviation(Arrays.copyOf(send,len));
		ret[3] = requests.size();
		return ret;
	}

	/**@deprecated use ActiveDays and ActiveDays  insted
	 * @see ActiveDays
	 * @see newReportFiled
	 * @param report
	 * @return
	 */
	public int daysReportActive(ChangeRequest report) {
		int days = 0;
		LocalDate start = report.getStartDate();
		LocalDate end = (report.getProcessStage().getDates())[4][2];
		if (end == null)
			end = LocalDate.now();
		days = (int) start.until(end, ChronoUnit.DAYS);
		return days;
	}

	/**@deprecated
	 * creates frequency Distribution
	 * 
	 * @param requests - an array list of the requests already sorted by the value
	 *                 and date reange requested
	 * @return [month-1] - amount of requests in that month
	 */
	public int[] frequencyDistribution(ArrayList<ChangeRequest> requests, reportScope chosenRange) {
		int[] frequencyDistribution = null;
		switch (chosenRange) {
		case dayOfmonth:
			frequencyDistribution = new int[31];
			for (ChangeRequest e : requests) {
				int range = (e.getStartDate()).getDayOfMonth();
				frequencyDistribution[range - 1]++;
			}
			break;
		case months:
			frequencyDistribution = new int[12];
			for (ChangeRequest e : requests) {
				int range = (e.getStartDate()).getMonthValue();
				frequencyDistribution[range - 1]++;
			}
			break;
		// note that day of the week array is [1] Monday ..... [7] Sunday
		case dayOfweek:
			frequencyDistribution = new int[7];
			for (ChangeRequest e : requests) {
				int range = ((e.getStartDate()).getDayOfWeek()).getValue();
				frequencyDistribution[range - 1]++;
			}
			break;
		}

		return frequencyDistribution;
	}

	/**@deprecated
	 * creates frequency Distribution
	 * 
	 * @param requests - an array list of the requests already sorted by the value
	 *                 and date reange requested
	 * @return [month-1] - amount of requests in that month
	 */
	public int[] frequencyDistributionForDaysTreated(ArrayList<ChangeRequest> requests,
			reportScope chosenRange) {
		int[] frequencyDistribution = null;
		switch (chosenRange) {
		case dayOfmonth:
			frequencyDistribution = new int[31];
			for (ChangeRequest e : requests) {
				int range = (e.getStartDate()).getDayOfMonth();
				frequencyDistribution[range - 1] += daysReportActive(e);
			}
			break;
		case months:
			frequencyDistribution = new int[12];
			for (ChangeRequest e : requests) {
				int range = (e.getStartDate()).getMonthValue();
				frequencyDistribution[range - 1] += daysReportActive(e);
			}
			break;
		// note that day of the week array is [1] Monday ..... [7] Sunday
		case dayOfweek:
			frequencyDistribution = new int[7];
			for (ChangeRequest e : requests) {
				int range = ((e.getStartDate()).getDayOfWeek()).getValue();
				frequencyDistribution[range - 1] += daysReportActive(e);
			}
			break;
		}

		return frequencyDistribution;
	} // ENd of frequencyDistributionForDaysTreated()
	
	/** @deprecated use DaysLate insted 
	 * @see DaysLate
	 * Calculates how meny days was the request late 
	 * @param request
	 * @return
	 */
	private int amountOfDaysLate(ChangeRequest request) {
		LocalDate[][] dates = request.getProcessStage().getDates();
		int days =0;
		for (int i = 0; i < 5; i++) {
			if(dates[i][1]==null || dates[i][2] == null) return days; // meaning we haven't done the stage yet  -> if it wasn't late until now it can't be 
			days =+ (int) dates[i][1].until( dates[i][2], ChronoUnit.DAYS);
		}
		return days;
	}//amountOfDaysLate()
	
}// END of ReportController()

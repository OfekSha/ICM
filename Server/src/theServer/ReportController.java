package theServer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import Entity.ActivitiesReport;
import Entity.ActivitiesReport.reportScope;
import Entity.ChangeRequest;
import Entity.ChangeRequest.ChangeRequestStatus;
import queryHandler.QueryHandler;

public class ReportController {

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

	/**
	 * creates frequency Distribution
	 * 
	 * @param requests - an array list of the requests already sorted by the value
	 *                 and date reange requested
	 * @return [month-1] - amount of requests in that month
	 */
	public int[] frequencyDistribution(ArrayList<ChangeRequest> requests, ActivitiesReport.reportScope chosenRange) {
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

	/**
	 * creates frequency Distribution
	 * 
	 * @param requests - an array list of the requests already sorted by the value
	 *                 and date reange requested
	 * @return [month-1] - amount of requests in that month
	 */
	public int[] frequencyDistributionForDaysTreated(ArrayList<ChangeRequest> requests,
			ActivitiesReport.reportScope chosenRange) {
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

	/**
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

	public int daysReportActive(ChangeRequest report) {
		int days = 0;
		LocalDate start = report.getStartDate();
		LocalDate end = (report.getProcessStage().getDates())[4][2];
		if (end == null)
			end = LocalDate.now();
		days = (int) start.until(end, ChronoUnit.DAYS);
		return days;
	}

	// specific methods

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
		filed = reportFiled(ongoing, chosenScope);

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
		filed = reportFiled(suspended, chosenScope);

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
		filed = reportFiled(closed, chosenScope);

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
		filed = reportFiled(rejected, chosenScope);

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
		filed = reportFiledForDaysTreated(all, chosenScope);
		medianAndStandardDeviation[0] = (double) filed[1];
		medianAndStandardDeviation[1] = (double) filed[2];
		medianAndStandardDeviation[2] = (int) filed[3];
		report.setTreatmentDays(medianAndStandardDeviation);
		report.setTreatmentDaysFrequencyDistribution((int[]) filed[0]);
		}
		return report;
	}// End of creatActivitiesReport()

}// END of ReportController()

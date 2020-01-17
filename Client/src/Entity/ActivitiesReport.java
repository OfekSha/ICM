package Entity;

import java.io.Serializable;
import java.time.LocalDate;

public class ActivitiesReport implements Serializable {
public enum reportScope{
	
	months,
	dayOfweek, dayOfmonth
}


private int ID ;
private reportScope chosenScope ;
	/**the start and end date the report covers
	 * 
	 */
	private LocalDate startDate ,endDate ,creationDate;


	/**
	 * [0] the median of the requests which are ongoing
	 * <p> [1] the Standard Deviation
	 * of the requests which are ongoing
	 * <p>[2] the number of ongoing requests
	 */
	private double[] ongoingRequests = new double[3];
	private int[] ongoingRequestsFrequencyDistribution;
	/**
	 * [0] the median of the suspended requests <p>[1] the Standard Deviation of the
	 * suspended requests<p>[2] the number of suspended requests
	 */
	private double[] suspendedRequests = new double[3];
	private int[] suspendedRequestsFrequencyDistribution;

	/**
	 * [0] the median of the closed requests <p>[1] the Standard Deviation of the
	 * closed requests<p>[2] the number of closed requests
	 */
	private double[] closedRequests = new double[3];
	private int[] closedRequestsFrequencyDistribution;

	/**
	 * [0] the median of the Rejected requests <p>[1] the Standard Deviation of the
	 * Rejected requests<p>[2] the number of Rejected requests
	 */
	private double[] rejectedRequests = new double[3];
	private int[] rejectedRequestsFrequencyDistribution;

	/**
	 * [0] the median of the Days it took to finish the request <p>[1] the Standard
	 * Deviation of the Days it took to finish the request<p>[2] the number of  days it took to finish
	 */
	private double[] treatmentDays = new double[3];
	private int[] treatmentDaysFrequencyDistribution;

	
	
	// input output
	public double[] getOngoingRequests() {
		return ongoingRequests;
	}

	public void setOngoingRequests(double[] ongoingRequests) {
		this.ongoingRequests = ongoingRequests;
	}

	public int[] getOngoingRequestsFrequencyDistribution() {
		return ongoingRequestsFrequencyDistribution;
	}

	public void setOngoingRequestsFrequencyDistribution(int[] ongoingRequestsFrequencyDistribution) {
		this.ongoingRequestsFrequencyDistribution = ongoingRequestsFrequencyDistribution;
	}

	public double[] getSuspendedRequests() {
		return suspendedRequests;
	}

	public void setSuspendedRequests(double[] suspendedRequests) {
		this.suspendedRequests = suspendedRequests;
	}

	public int[] getSuspendedRequestsFrequencyDistribution() {
		return suspendedRequestsFrequencyDistribution;
	}

	public void setSuspendedRequestsFrequencyDistribution(int[] suspendedRequestsFrequencyDistribution) {
		this.suspendedRequestsFrequencyDistribution = suspendedRequestsFrequencyDistribution;
	}

	public double[] getClosedRequests() {
		return closedRequests;
	}

	public void setClosedRequests(double[] closedRequests) {
		this.closedRequests = closedRequests;
	}

	public int[] getClosedRequestsFrequencyDistribution() {
		return closedRequestsFrequencyDistribution;
	}

	public void setClosedRequestsFrequencyDistribution(int[] closedRequestsFrequencyDistribution) {
		this.closedRequestsFrequencyDistribution = closedRequestsFrequencyDistribution;
	}

	public double[] getRejectedRequests() {
		return rejectedRequests;
	}

	public void setRejectedRequests(double[] rejectedRequests) {
		this.rejectedRequests = rejectedRequests;
	}

	public int[] getRejectedRequestsFrequencyDistribution() {
		return rejectedRequestsFrequencyDistribution;
	}

	public void setRejectedRequestsFrequencyDistribution(int[] rejectedRequestsFrequencyDistribution) {
		this.rejectedRequestsFrequencyDistribution = rejectedRequestsFrequencyDistribution;
	}

	public double[] getTreatmentDays() {
		return treatmentDays;
	}

	public void setTreatmentDays(double[] treatmentDays) {
		this.treatmentDays = treatmentDays;
	}

	public int[] getTreatmentDaysFrequencyDistribution() {
		return treatmentDaysFrequencyDistribution;
	}

	public void setTreatmentDaysFrequencyDistribution(int[] treatmentDaysFrequencyDistribution) {
		this.treatmentDaysFrequencyDistribution = treatmentDaysFrequencyDistribution;
	}
	public LocalDate getStart() {
		return startDate;
	}

	public void setStart(LocalDate start) {
		this.startDate = start;
	}

	public LocalDate getEnd() {
		return endDate;
	}

	public void setEnd(LocalDate end) {
		this.endDate = end;
	}

	public reportScope getScope() {
		return chosenScope;
	}

	public void setScope(reportScope chosenScope) {
		this.chosenScope = chosenScope;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	
}// END of ActivitiesReport

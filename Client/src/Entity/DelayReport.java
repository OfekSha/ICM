package Entity;

import reporting.ReportController.reportScope;

public class DelayReport     {

	
	/** 
	 * 
	 */
	private reportScope chosenScope ;
	/**
	 * [0] the median of the all Late Submissions
	 * <p> [1] the Standard Deviation
	 * of the all Late Submissions
	 * <p>[2] the number of the all Late Submissions
	 */
	private double[] numOfLateSubmission ;
	/**  Frequency Distribution  for amount of late submissions extensions<p>
	 * depending on the report Scope chosen:<p>
	 *  <li>dayOfmonth : [0]-[30] - each containing the  amount of late submissions extensions that were created at [day-1]  </li>
	 *  <li>dayOfweek:[0]-[6] - each containing the amount of late submissions extensions that were created at [day of the week-1] </li>
	 *  <li>months:[0]-[11] - containing the amount of late submissions extensions that were created at [month-1] </li>
	 */
	private int[] LateSubmissionFrequencyDistribution ;
	
	/**
	 * [0] the median of the all lengths of late submissions
	 * <p> [1] the Standard Deviation
	 * of the all lengths of late submissions
	 * <p>[2] the number of the all lengths of late submissions
	 */
	private double[] lengthofLateSubmission ;
	/**  Frequency Distribution  for  the time added from extensions<p>
	 * depending on the report Scope chosen:<p>
	 *  <li>dayOfmonth : [0]-[30] - each containing the amount time added from extensions that were created at [day-1]  </li>
	 *  <li>dayOfweek:[0]-[6] - each containing the amount time added from extensions that were created at [day of the week-1] </li>
	 *  <li>months:[0]-[11] - containing the amount time added from extensions that were created at [month-1] </li>
	 */
	private int[] lengthofLateSubmissionFrequencyDistribution ;
	
	
	
	public double[] getNumOfLateSubmission() {
		return numOfLateSubmission;
	}
	public void setNumOfLateSubmission(double[] numOfLateSubmission) {
		this.numOfLateSubmission = numOfLateSubmission;
	}
	public int[] getLateSubmissionFrequencyDistribution() {
		return LateSubmissionFrequencyDistribution;
	}
	public void setLateSubmissionFrequencyDistribution(int[] lateSubmissionFrequencyDistribution) {
		LateSubmissionFrequencyDistribution = lateSubmissionFrequencyDistribution;
	}
	public double[] getLengthofLateSubmission() {
		return lengthofLateSubmission;
	}
	public void setLengthofLateSubmission(double[] lengthofLateSubmission) {
		this.lengthofLateSubmission = lengthofLateSubmission;
	}
	public int[] getLengthofLateSubmissionFrequencyDistribution() {
		return lengthofLateSubmissionFrequencyDistribution;
	}
	public void setLengthofLateSubmissionFrequencyDistribution(int[] lengthofLateSubmissionFrequencyDistribution) {
		this.lengthofLateSubmissionFrequencyDistribution = lengthofLateSubmissionFrequencyDistribution;
	}
	public reportScope getChosenScope() {
		return chosenScope;
	}
	public void setChosenScope(reportScope chosenScope) {
		this.chosenScope = chosenScope;
	}
	
	
	
	
} //END of DelayReport class

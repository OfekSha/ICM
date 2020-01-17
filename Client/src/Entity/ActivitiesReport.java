package Entity;

public class ActivitiesReport   {

/**[0] the median of the  requests which are ongoing 
 * [1] the Standard Deviation of the requests which are ongoing
 */
private Integer[] numOfOngoingRequests = new  Integer[2];

/**[0] the median of the suspended requests 
 * [1] the Standard Deviation of the suspended requests 
 */
private Integer[] numOfSuspendedRequests = new  Integer[2];

/**[0] the median of the closed requests 
 * [1] the Standard Deviation of the closed requests
 */
private Integer[]  numOfClosedRequests	= new  Integer[2];

/**[0] the median of the Rejected requests
 * [1] the Standard Deviation of the Rejected requests
 */
private Integer[]  numOfRejectedRequests = new  Integer[2];

/**[0] the median of the Days it took to finish the request
 * [1] the Standard Deviation of the Days it took to finish the request
 */
private Integer[]  numOfTreatmentDays	= new  Integer[2];


	
	
	

}// END of ActivitiesReport

package Entity;

public class ActivitiesReport extends StatisticalReport implements IReport {

	//  class variables  
	private int numRequests;
	private int numDenied;
	private int requestsTimeMap;
	
	
	@Override
	public void createReport() {
		// TODO Auto-generated method stub

	}
	
	// get class variables
	// if changes are to be made make sure to update the DB creation 

	public int getNumRequests(){
		return numRequests;
	}
	
	public int  getNumDenied() {
		return numDenied;
	}
	
	
	public int getRequestsTimeMap() {
		return requestsTimeMap;
	}
	

}

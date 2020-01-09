package Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import Entity.User.ICMPermissions;

/**
 * represents a estimators report
 *
 */
public class EstimatorReport implements Serializable {

	private User estimator;
	private String estimatorReportID;
	private ChangeRequest referencedRequest;

	private String location;
	private String changeDescription;
	private String resultingResult;
	private String constraints;
	private LocalDate timeEstimate;
	
	public EstimatorReport(User estimator, String location, String changeDescription, String resultingResult,
			String constraints, LocalDate timeEstimate) {
		setEstimator(estimator);
		setLocation(location);
		setChangeDescription(changeDescription);
		setResultingResult(resultingResult);
		setConstraints(constraints);
		setTimeEstimate(timeEstimate);
	}

	// input
	public void setEstimator(User estimator) {
		if(!(estimator.getICMPermissions().contains(ICMPermissions.estimator))) throw new IllegalArgumentException("only an estimator can make an estimator report ");
		this.estimator = estimator;
	}

	public void setEstimatorReportID(String reportID) {
		this.estimatorReportID = reportID;
	}

	public void setReferencedRequest(ChangeRequest referencedRequest) {
		this.referencedRequest = referencedRequest;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setChangeDescription(String changeDescription) {
		this.changeDescription = changeDescription;
	}

	public void setResultingResult(String resultingResult) {
		this.resultingResult = resultingResult;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public void setTimeEstimate(LocalDate timeEstimate) {
		this.timeEstimate = timeEstimate;
	}

	// output
	
	public User getEstimator() {
		return estimator;
	}
	
	public String getEstimatorReportID() {
		return estimatorReportID;
	}
	public ChangeRequest getReferencedRequest() {
		return referencedRequest;
	}
	
	
	public String getlocation() {
		return location;
	}
	
	public String getChangeDescription() {
		return changeDescription;
	}
	
	public String getResultingResult() {
		return resultingResult;
	}
	
	
	public String getConstraints() {
		return constraints;
	}
	public  LocalDate getTimeEstimate() {
		return  timeEstimate;
	}
	
	 

}// END of EstimatorReport class

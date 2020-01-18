package reporting;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import Entity.ChangeRequest;

/**
 *  used when there is aneed for the amount of days the request was late  in a Distribution filed
 *
 */
public class DaysLate implements FrequencyDistributionKind {

	/**returns for Distribution filed :the request  how many days was the request late
	 *@param request- the request we are interested in
	 *@return  the amount of days the request was late 
	 */
	@Override
	public int addToDistribution(ChangeRequest request) {
		LocalDate[][] dates = request.getProcessStage().getDates();
		int days =0;
		for (int i = 0; i < 5; i++) {
			if(dates[i][1]==null || dates[i][2] == null) return days; // meaning we haven't done the stage yet  -> if it wasn't late until now it can't be 
			days =+ (int) dates[i][1].until( dates[i][2], ChronoUnit.DAYS);
		}
		return days;
	}

}

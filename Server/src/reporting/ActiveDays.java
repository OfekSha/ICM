package reporting;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import Entity.ChangeRequest;

/** 
 *  used when wee need the days request was active in a Distribution filed
 */
public class ActiveDays implements FrequencyDistributionKind {

	/** * returns for Distribution filed :the amount of days it took to complete a request or if not compete the time form its inception to now 
	 *	@param  request - the report which  active time we want to know
	 */
	@Override
	public int addToDistribution(ChangeRequest request) {
		int days = 0;
		LocalDate[][] dates = request.getProcessStage().getDates();
		for (int i = 0; i < 5; i++) {
			if(dates[i][1]==null) continue;
			if(dates[i][2] != null) 
			days =+ (int) dates[i][1].until( dates[i][2], ChronoUnit.DAYS);
			else  days =+ (int) dates[i][1].until( LocalDate.now(), ChronoUnit.DAYS);
		}		
		return days;
	}

}

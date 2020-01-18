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
		LocalDate start = request.getStartDate();
		LocalDate end = (request.getProcessStage().getDates())[4][2];
		if (end == null)
			end = LocalDate.now();
		days = (int) start.until(end, ChronoUnit.DAYS);
		return days;
	}

}

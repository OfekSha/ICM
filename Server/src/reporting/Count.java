package reporting;

import Entity.ChangeRequest;
/**
 *  used when counting the amount of request for a Distribution filed
 */
public class Count implements FrequencyDistributionKind {
	
	/**returns for Distribution filed :the request  count
	 *
	 */
	@Override
	public int addToDistribution(ChangeRequest request) {
		return 1;
	}

}

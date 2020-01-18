package reporting;

import java.util.ArrayList;

import Entity.ChangeRequest;

/** 
 * how to build the   Frequency Distribution table
 *
 */
public interface FrequencyDistributionKind {
 /** what to add to a Frequency Distribution filed 
 * @param request
 * @return
 */
public int addToDistribution(ChangeRequest request); 

}

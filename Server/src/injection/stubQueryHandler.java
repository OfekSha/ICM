package injection;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.User;
import Entity.ChangeRequest.ChangeRequestStatus;
import queryHandler.ChangeRequestQuerys;
import queryHandler.QueryHandler;

/**
 * Used when DB dependency is unwanted
 *
 */
public class stubQueryHandler implements ModelQueryHandler {
	/** Replace the ChangeRequest DB 
	 * 
	 */
	public ArrayList<ChangeRequest> fakeList;

	public stubQueryHandler(ArrayList<ChangeRequest> fakeList) {
		this.fakeList = fakeList;
	}

	public void setFakeList(ArrayList<ChangeRequest> fakeList) {
		this.fakeList = fakeList;
	}

	QueryHandler query = new QueryHandler(null) {
		/** @return a  ChangeRequestQuerys  which deals from our fakeList 
		 *	@see ChangeRequestQuerys
		 */
		public ChangeRequestQuerys getChangeRequestQuerys() {
			return new ChangeRequestQuerys(query) {
				/** Replacing the getAllChangeRequestWithStatus() method  were getting form  ChangeRequestQuerys to return fake list <p>
				 * note that if you were to test more methods were getting form  ChangeRequestQuerys you wolde need to sort corrctly <p>
				 * @see ChangeRequestQuerys
				 * @param the status you want the requests returned to be in 
				 * @return fakeList
				 */
				public ArrayList<ChangeRequest> getAllChangeRequestWithStatus(ChangeRequestStatus ongoingStatus) {
					return fakeList;

				}
			};
		}
	};

	@Override
	public QueryHandler getQuerry() {
		// TODO Auto-generated method stub
		return query;
	}

}

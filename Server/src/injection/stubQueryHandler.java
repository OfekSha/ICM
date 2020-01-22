package injection;

import java.lang.reflect.Method;
import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.User;
import Entity.ChangeRequest.ChangeRequestStatus;
import queryHandler.ChangeRequestQuerys;
import queryHandler.QueryHandler;

public class stubQueryHandler implements ModelQueryHandler {
	public ArrayList<ChangeRequest> fakeList;

	public stubQueryHandler(ArrayList<ChangeRequest> fakeList) {
		this.fakeList = fakeList;
	}

	public void setFakeList(ArrayList<ChangeRequest> fakeList) {
		this.fakeList = fakeList;
	}

	QueryHandler query = new QueryHandler(null) {
		public ChangeRequestQuerys getChangeRequestQuerys() {
			return new ChangeRequestQuerys(query) {
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

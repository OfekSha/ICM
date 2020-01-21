package test;

import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.User;
import Entity.ChangeRequest.ChangeRequestStatus;
import queryHandler.ChangeRequestQuerys;
import queryHandler.QueryHandler;

public class stubQueryHandler implements ModelQueryHandler{
	public ArrayList<ChangeRequest> fakeList;
	Initiator testInitiator=new Initiator(new User("test", null, null, null, null, null, null),null);
	QueryHandler query=new QueryHandler(null) {
		public ChangeRequestQuerys getChangeRequestQuerys() {
			return new ChangeRequestQuerys(query) {
				public ArrayList<ChangeRequest> getAllChangeRequestWithStatus(ChangeRequestStatus ongoingStatus){
					fakeList.add(new ChangeRequest(testInitiator,null,null, null, null, null, null, null));
					return fakeList ;
					
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


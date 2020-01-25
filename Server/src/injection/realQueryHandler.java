package injection;

import queryHandler.QueryHandler;

/**
 * Used when  dealing with DB 
 *
 */
public class realQueryHandler implements ModelQueryHandler{
	QueryHandler querry;
	@Override
	public QueryHandler getQuerry() {
		// TODO Auto-generated method stub
		return querry;
	}
	public void setQueryHandler(QueryHandler querry) {
		this.querry=querry;
	}

}

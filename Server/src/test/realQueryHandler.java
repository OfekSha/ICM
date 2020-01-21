package test;

import queryHandler.QueryHandler;

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

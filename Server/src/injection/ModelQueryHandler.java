package injection;

import queryHandler.QueryHandler;

/**
 *  Designed to to replace the dependence on DB  with any data source we wish 
 *
 */
public interface ModelQueryHandler {
	public QueryHandler getQuerry();
}

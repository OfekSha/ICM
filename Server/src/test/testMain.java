package test;

import queryHandler.QueryHandler;
import theServer.mysqlConnection;

public class testMain {
	static stubQueryHandler stub=new stubQueryHandler();
public static void main(String[] args) {
	
	ReportController reportTest=new ReportController(new stubQueryHandler());
	reportTest.createOngoingFiled(null, null, null, null);
	QueryHandler realQry=new QueryHandler(new mysqlConnection());
	reportTest=new ReportController(realQry);
	reportTest.createOngoingFiled(null, null, null, null);
}
}

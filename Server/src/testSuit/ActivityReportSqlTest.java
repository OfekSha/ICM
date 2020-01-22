package testSuit;

import static org.junit.Assert.fail;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.corba.se.impl.orb.ParserTable.TestContactInfoListFactory;

import Entity.ActivitiesReport;
import queryHandler.QueryHandler;
import reporting.ReportController;
import reporting.ReportController.reportScope;
import testSuit.Status.statusEnum;
import theServer.mysqlConnection;

class ActivityReportSqlTest {
	ReportController reportController;
	mysqlConnection mySQL;
	QueryHandler qryHndlr;
	
	@BeforeEach
	 void initilaize() {
		mySQL=new mysqlConnection(); // local
		qryHndlr= new QueryHandler(mySQL);
		reportController= new ReportController(qryHndlr);
		mysqlConnection.dropDB(); // drop schema
		mySQL.buildDB(); // build new schema with the relevent tables to icm.
	}
	// TODO: add test to client send to server reports in 
	@Test
	 void insertReportToDBTest() {
		int id;
		String reportScope;
		LocalDate start,end,created;
		Status ongoingResults=new Status(statusEnum.ongoing);
		ActivitiesReport report =reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5), reportScope.dayOfweek);
		qryHndlr.getActivitiesReportQuerys().InsertActivitiesReport(report);
		fail();
		
	}

}
class Status{
	enum statusEnum{
		ongoing,suspended,close,rejected,treatment
	}
	statusEnum status;
	float meidan,deviation,frequency;
	int requests;
	Status(statusEnum status){
		this.status=status;
		meidan=deviation=frequency=0;
		requests=0;
	}
}

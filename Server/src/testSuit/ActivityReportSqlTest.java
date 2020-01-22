package testSuit;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.ActivitiesReport;
import GUI.PopUpWindows.ActivityReport;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import queryHandler.QueryHandler;
import reporting.ReportController;
import reporting.ReportController.reportScope;
import theServer.mysqlConnection;

class ActivityReportSqlTest {
	ReportController reportController;
	mysqlConnection mySQL;
	QueryHandler qryHndlr;
	// number of each column:
	final int ID = 1;
	final int SCOPE = 2;
	final int START = 3;
	final int END = 4;
	final int CREATED = 5;
	final int MEIDAN = 6;
	final int DEVIATION = 7;
	final int REQUESTS = 8;
	final int DISTRIBUTION = 9;
	// end numbering.
	//Activity report details (only ongoing):
	int id, requests;
	String scope;
	float meidan, deviation, distribution;
	LocalDate start, end, created;
	//end details.
	@BeforeEach
	void initilaize() {
		id = requests = 0;
		scope = null;
		meidan = deviation = distribution = 0;
		start = end = created = null;
		mySQL = new mysqlConnection(); // local
		qryHndlr = new QueryHandler(mySQL);
		reportController = new ReportController(qryHndlr);
		mysqlConnection.dropDB(); // drop schema
		mySQL.buildDB(); // build new schema with the relevent tables to icm.
	}

	/**
	 * Insert activity report from the server with the class QueryHandler.
	 * with 0 requests.
	 */
	@Test
	void serverCreateReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfweek);
		qryHndlr.getActivitiesReportQuerys().InsertActivitiesReport(report);
		getOngoingReport();
		assertEquals(id, 1);
		assertEquals(scope, reportScope.dayOfweek.toString());
		assertEquals(start, LocalDate.now());
		assertEquals(end, LocalDate.now().plusMonths(5));
		assertEquals(created, LocalDate.now());
		assertEquals(meidan, 0);
		assertEquals(deviation, 0);
		assertEquals(requests, 0);
		assertEquals(distribution, 0);

	}
	/**
	 * Insert activity report from server and then get it from server
	 */
	@Test
	void serverCreateAndGetReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfweek);
		qryHndlr.getActivitiesReportQuerys().InsertActivitiesReport(report);
		getOngoingReport();
		ArrayList<ActivitiesReport> reports = qryHndlr.getActivitiesReportQuerys().getAllActivitiesReports();
		assertEquals(id, reports.get(0).getID());
		assertEquals(scope,reports.get(0).getScope().toString());
		assertEquals(start, reports.get(0).getStart());
		assertEquals(end, reports.get(0).getEnd());
		assertEquals(created, reports.get(0).getCreationDate());
		assertEquals(meidan, reports.get(0).getOngoingRequests()[0]);
		assertEquals(deviation,  reports.get(0).getOngoingRequests()[1]);
		assertEquals(requests,  reports.get(0).getOngoingRequests()[2]);
		assertEquals(distribution, 0);
	}
	/**
	 * Server didn't create report but still try to get (no reports in the DATABASE)
	 */
	@Test
	void serverGetEmptyReportTest() {
		getOngoingReport();
		ArrayList<ActivitiesReport> reports = qryHndlr.getActivitiesReportQuerys().getAllActivitiesReports();
		
	}
	/*@Test // not need to test client-server.
	void client0RequestsTest() {
		ActivityReport clientSendReport=new ActivityReport();
		clientSendReport.getReports(reportScope.dayOfweek, LocalDate.now(), LocalDate.now().plusMonths(5));
		getOngoingReport();
		assertEquals(id, 1);
		assertEquals(scope, reportScope.dayOfweek.toString());
		assertEquals(start, LocalDate.now());
		assertEquals(end, LocalDate.now().plusMonths(5));
		assertEquals(created, LocalDate.now());
		assertEquals(meidan, 0);
		assertEquals(deviation, 0);
		assertEquals(requests, 0);
		assertEquals(distribution, 0);
	}*/

	private void getOngoingReport() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=IST",
					"root", "Aa123456");
			Statement stmnt = conn.createStatement();
			stmnt.execute("SELECT * FROM icm.activitiesreport");
			ResultSet res = stmnt.getResultSet();
			while (res.next()) {
				id = res.getInt(ID);
				scope = res.getNString(SCOPE);
				start = LocalDate.parse(res.getNString(START));
				end = LocalDate.parse(res.getNString(END));
				created = LocalDate.parse(res.getNString(CREATED));
				meidan = res.getFloat(MEIDAN);
				deviation = res.getFloat(DEVIATION);
				requests = res.getInt(REQUESTS);
				distribution = res.getFloat(DISTRIBUTION);
			}
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e);
		}

	}
}

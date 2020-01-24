package testSuit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.ActivitiesReport;
import queryHandler.ActivitiesReportQuerys;
import queryHandler.QueryHandler;
import reporting.ReportController;
import reporting.ReportController.reportScope;
import theServer.mysqlConnection;

/**
 * this test suit is for testing database of ongoing requests in the activity
 * report. don't run it in the real local database.(it will corrupt the
 * database) this is use for testing only!
 */
class ActivityReportSqlTest {
	// number of each column:
	final int ID = 1; // Activity id column.
	final int SCOPE = 2; // id column.
	final int START = 3; // Start date column.
	final int END = 4; // End date column.
	final int CREATED = 5; // Created date column.
	final int MEDIAN = 6; // Median column for ongoing.
	final int DEVIATION = 7; // id column for ongoing.
	final int REQUESTS = 8; // Number of requests column for ongoing.
	final int DISTRIBUTION = 9; // Distribution column for ongoing.
	// end numbering.
	private Connection conn; // connection to mysql database.
	private ReportController reportController; // the report controller to create activity report before testing the
												// database.
	private mysqlConnection mySQL; // need this connection class for tests.
	private ActivitiesReportQuerys activityQry; // this is the class with the methods that need to tests.
	ArrayList<ExpectedActivityReport> DbReports; // Activity reports from the database.

	/**
	 * This class is for save information from the database and then compare for the
	 * testing functions.
	 *
	 */
	private class ExpectedActivityReport {
		int id, distribution;
		String scope; // this is for the status (test only ongoing)
		double meidan, deviation, requests;
		LocalDate start, end, created;
		int[] monthDays = new int[31]; // requests per day in every day of a month.
		int[] weekDays = new int[7]; // requests per day in every day of a week.
		int[] yearMonths = new int[12];// requests per month in every month of year.

		public ExpectedActivityReport(int id, double requests, String scope, double meidan, double deviation,
				int distribution, LocalDate start, LocalDate end, LocalDate created, int[] monthDays, int[] weekDays,
				int[] yearMonths) {
			this.id = id;
			this.requests = requests;
			this.scope = scope;
			this.meidan = meidan;
			this.deviation = deviation;
			this.distribution = distribution;
			this.start = start;
			this.end = end;
			this.created = created;
			this.monthDays = monthDays;
			this.weekDays = weekDays;
			this.yearMonths = yearMonths;
		}
	}

	@BeforeEach
	void initilaize() {
		connectMysql();
		DbReports = new ArrayList<ExpectedActivityReport>();
		mySQL = new mysqlConnection(); // local
		activityQry = new QueryHandler(mySQL).getActivitiesReportQuerys();
		reportController = new ReportController(new QueryHandler(mySQL));
		mysqlConnection.dropDB(); // drop schema
		mySQL.buildDB(); // build new schema with the relevant tables to icm.
		createRequests();
	}

	/**
	 * Insert activity report from the server by activityQry. test days per month
	 */
	@Test
	void serverCreateDayOfMonthReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfmonth); // make new activity report
		activityQry.InsertActivitiesReport(report); // this is the tested method.
		getOngoingReport(); // call for method to see the database and compare for testing.
		assertEquals(1, DbReports.get(0).id);
		assertEquals(report.getScope().toString(), DbReports.get(0).scope);
		assertEquals(report.getStart(), DbReports.get(0).start);
		assertEquals(report.getEnd(), DbReports.get(0).end);
		assertEquals(report.getCreationDate(), DbReports.get(0).created);
		assertEquals(report.getOngoingRequests()[0], DbReports.get(0).meidan);
		assertEquals(report.getOngoingRequests()[1], DbReports.get(0).deviation);
		assertEquals(report.getOngoingRequests()[2], DbReports.get(0).requests);
		assertArrayEquals(report.getOngoingRequestsFrequencyDistribution(), DbReports.get(0).monthDays);
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Insert activity report from the server by activityQry. test days per week
	 */
	@Test
	void serverCreateDayOfWeekReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfweek);
		activityQry.InsertActivitiesReport(report);
		getOngoingReport();
		assertEquals(1, DbReports.get(0).id);
		assertEquals(report.getScope().toString(), DbReports.get(0).scope);
		assertEquals(report.getStart(), DbReports.get(0).start);
		assertEquals(report.getEnd(), DbReports.get(0).end);
		assertEquals(report.getCreationDate(), DbReports.get(0).created);
		assertEquals(report.getOngoingRequests()[0], DbReports.get(0).meidan);
		assertEquals(report.getOngoingRequests()[1], DbReports.get(0).deviation);
		assertEquals(report.getOngoingRequests()[2], DbReports.get(0).requests);
		assertArrayEquals(report.getOngoingRequestsFrequencyDistribution(), DbReports.get(0).weekDays);
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Insert activity report from the server by activityQry. test months per year
	 */
	@Test
	void serverCreateMonthsReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.months);
		activityQry.InsertActivitiesReport(report);
		getOngoingReport();
		assertEquals(1, DbReports.get(0).id);
		assertEquals(report.getScope().toString(), DbReports.get(0).scope);
		assertEquals(report.getStart(), DbReports.get(0).start);
		assertEquals(report.getEnd(), DbReports.get(0).end);
		assertEquals(report.getCreationDate(), DbReports.get(0).created);
		assertEquals(report.getOngoingRequests()[0], DbReports.get(0).meidan);
		assertEquals(report.getOngoingRequests()[1], DbReports.get(0).deviation);
		assertEquals(report.getOngoingRequests()[2], DbReports.get(0).requests);
		assertArrayEquals(report.getOngoingRequestsFrequencyDistribution(), DbReports.get(0).yearMonths);
	}

	/**
	 * Insert activity report from server and then get it from server by method
	 * getAllActivitiesReports test days per month
	 */
	@Test
	void serverCreateDayOfMonthAndGetReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfmonth);
		activityQry.InsertActivitiesReport(report);
		getOngoingReport();
		ArrayList<ActivitiesReport> reports = activityQry.getAllActivitiesReports();
		assertEquals(reports.get(0).getID(), DbReports.get(0).id);
		assertEquals(reports.get(0).getScope().toString(), DbReports.get(0).scope);
		assertEquals(reports.get(0).getStart(), DbReports.get(0).start);
		assertEquals(reports.get(0).getEnd(), DbReports.get(0).end);
		assertEquals(reports.get(0).getCreationDate(), DbReports.get(0).created);
		assertEquals(reports.get(0).getOngoingRequests()[0], DbReports.get(0).meidan);
		assertEquals(reports.get(0).getOngoingRequests()[1], DbReports.get(0).deviation);
		assertEquals(reports.get(0).getOngoingRequests()[2], DbReports.get(0).requests);
		assertArrayEquals(reports.get(0).getOngoingRequestsFrequencyDistribution(), DbReports.get(0).monthDays);
	}

	/**
	 * Insert activity report from server and then get it from server by method
	 * getAllActivitiesReports test days per week.
	 */
	@Test
	void serverCreateDayOfWeekAndGetReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfweek);
		activityQry.InsertActivitiesReport(report);
		getOngoingReport();
		ArrayList<ActivitiesReport> reports = activityQry.getAllActivitiesReports();
		assertEquals(reports.get(0).getID(), DbReports.get(0).id);
		assertEquals(reports.get(0).getScope().toString(), DbReports.get(0).scope);
		assertEquals(reports.get(0).getStart(), DbReports.get(0).start);
		assertEquals(reports.get(0).getEnd(), DbReports.get(0).end);
		assertEquals(reports.get(0).getCreationDate(), DbReports.get(0).created);
		assertEquals(reports.get(0).getOngoingRequests()[0], DbReports.get(0).meidan);
		assertEquals(reports.get(0).getOngoingRequests()[1], DbReports.get(0).deviation);
		assertEquals(reports.get(0).getOngoingRequests()[2], DbReports.get(0).requests);
		assertArrayEquals(reports.get(0).getOngoingRequestsFrequencyDistribution(), DbReports.get(0).weekDays);
	}

	/**
	 * Insert activity report from server and then get it from server by method
	 * getAllActivitiesReports test months per year.
	 */
	@Test
	void serverCreateMonthsAndGetReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.months);
		activityQry.InsertActivitiesReport(report);
		getOngoingReport();
		ArrayList<ActivitiesReport> reports = activityQry.getAllActivitiesReports();
		assertEquals(reports.get(0).getID(), DbReports.get(0).id);
		assertEquals(reports.get(0).getScope().toString(), DbReports.get(0).scope);
		assertEquals(reports.get(0).getStart(), DbReports.get(0).start);
		assertEquals(reports.get(0).getEnd(), DbReports.get(0).end);
		assertEquals(reports.get(0).getCreationDate(), DbReports.get(0).created);
		assertEquals(reports.get(0).getOngoingRequests()[0], DbReports.get(0).meidan);
		assertEquals(reports.get(0).getOngoingRequests()[1], DbReports.get(0).deviation);
		assertEquals(reports.get(0).getOngoingRequests()[2], DbReports.get(0).requests);
		assertArrayEquals(reports.get(0).getOngoingRequestsFrequencyDistribution(), DbReports.get(0).yearMonths);
	}

	/**
	 * Server didn't create report but still try to get (no reports in the DATABASE)
	 */
	@Test
	void serverGetEmptyReportTest() {
		ArrayList<ActivitiesReport> reports = activityQry.getAllActivitiesReports();
		assertTrue(reports.isEmpty());
	}

	/**
	 * make all kinds of reports.
	 */
	@Test
	void serverCreateAllReportTest() {
		ArrayList<ActivitiesReport> reports = new ArrayList<ActivitiesReport>();
		//months per year:
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(10),
				reportScope.months);
		reports.add(report);
		//days per week:
		activityQry.InsertActivitiesReport(report);
		report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfweek);
		reports.add(report);
		//days per month:
		activityQry.InsertActivitiesReport(report);
		report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(2),
				reportScope.dayOfmonth);
		reports.add(report);
		activityQry.InsertActivitiesReport(report);
		getOngoingReport(); // save data
		for (int i = 0; i < 3; i++) { // test for each report:
			assertEquals(i+1, DbReports.get(i).id);
			assertEquals(reports.get(i).getScope().toString(), DbReports.get(i).scope);
			assertEquals(reports.get(i).getStart(), DbReports.get(i).start);
			assertEquals(reports.get(i).getEnd(), DbReports.get(i).end);
			assertEquals(reports.get(i).getCreationDate(), DbReports.get(i).created);
			assertEquals(reports.get(i).getOngoingRequests()[0], DbReports.get(i).meidan);
			assertEquals(reports.get(i).getOngoingRequests()[1], DbReports.get(i).deviation);
			assertEquals(reports.get(i).getOngoingRequests()[2], DbReports.get(i).requests);
			switch (reports.get(i).getScope()) {
			case months:
				assertArrayEquals(reports.get(i).getOngoingRequestsFrequencyDistribution(),
						DbReports.get(i).yearMonths);
				break;
			case dayOfweek:
				assertArrayEquals(reports.get(i).getOngoingRequestsFrequencyDistribution(), DbReports.get(i).weekDays);
				break;
			case dayOfmonth:
				assertArrayEquals(reports.get(i).getOngoingRequestsFrequencyDistribution(), DbReports.get(i).monthDays);
				break;
			}
		}
	}

	/**
	 * Make a test connection to connect to the database
	 */
	private void connectMysql() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// connect to local
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=IST", "root", "Aa123456");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e);
		}

	}

	/**
	 * This method create requests for testing.
	 */
	private void createRequests() {
		int j; // sort the dates
		try {
			PreparedStatement addRequestsStmt;
			for (int i = 0; i <= 30; i++) {
				String qry = "INSERT INTO `icm`.`changerequest` (`RequestID`,`startDate`, `status`) VALUES (?, ?, 'ongoing');";
				addRequestsStmt = conn.prepareStatement(qry);
				addRequestsStmt.setInt(1, i);
				if (i < 10)
					j = i; // requests in 10 days since now.
				else if (i < 15)
					j = i * 7; // 5 weeks have request in same day from now.
				else
					j = i * 31; // 15 months have different requests from now.
				addRequestsStmt.setNString(2, LocalDate.now().plusDays(j).toString());
				addRequestsStmt.execute();
				addRequestsStmt.close();
				qry = "INSERT INTO `icm`.`stage` (`RequestID`, `currentStage`, currentSubStage,`stage1extension`, `stage2extension`, `stage3extension`, `stage4extension`) VALUES (?, 'meaningEvaluation', 'ApprovingDueTime' , '0', '0', '0', '0');";
				addRequestsStmt = conn.prepareStatement(qry);
				addRequestsStmt.setInt(1, i);
				addRequestsStmt.execute();// Creates stages for every requests needed for testing methods.
				addRequestsStmt.close();
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

	/**
	 * get from the Database report and compare or test it against the server
	 * methods.<br>
	 * save data to DbReports - list of activity reports.
	 */
	private void getOngoingReport() {
		try {
			Statement stmntActivity = conn.createStatement();
			stmntActivity.execute("SELECT * FROM icm.activitiesreport"); // activity report.
			ResultSet resActivity = stmntActivity.getResultSet(); // result for activity
			PreparedStatement stmntWeekDays = conn
					.prepareStatement("SELECT * FROM icm.`frequencydistribution-daysinweek` WHERE id=?"); // days per
																											// week.
			PreparedStatement stmntMonthDays = conn
					.prepareStatement("SELECT * FROM icm.`frequencydistribution-daysinmonth` WHERE id=?"); // days per
																											// month.
			PreparedStatement stmntYearMonths = conn
					.prepareStatement("SELECT * FROM icm.`frequencydistribution-months` WHERE id=?"); // months per
																										// year.

			while (resActivity.next()) {

				int id = resActivity.getInt(ID);
				String scope = resActivity.getNString(SCOPE);
				LocalDate start = LocalDate.parse(resActivity.getNString(START));
				LocalDate end = LocalDate.parse(resActivity.getNString(END));
				LocalDate created = LocalDate.parse(resActivity.getNString(CREATED));
				Double meidan = resActivity.getDouble(MEDIAN);
				Double deviation = resActivity.getDouble(DEVIATION);
				Double requests = resActivity.getDouble(REQUESTS);
				int distribution = resActivity.getInt(DISTRIBUTION);
				// days per week:
				stmntWeekDays.setInt(1, distribution);
				stmntWeekDays.execute();
				ResultSet resWeekDays = stmntWeekDays.getResultSet();
				int[] weekDays = null;
				while (resWeekDays.next()) {
					weekDays = new int[7];
					for (int i = 2; i <= 8; i++)
						weekDays[i - 2] = resWeekDays.getInt(i);
				}
				// days per month:
				stmntMonthDays.setInt(1, distribution);
				stmntMonthDays.execute();
				ResultSet resMonthDays = stmntMonthDays.getResultSet();
				int[] monthDays = null;
				while (resMonthDays.next()) {
					monthDays = new int[31];
					for (int i = 2; i <= 32; i++)
						monthDays[i - 2] = resMonthDays.getInt(i);
				}
				// months per year:
				stmntYearMonths.setInt(1, distribution);
				stmntYearMonths.execute();
				ResultSet resYearMonths = stmntYearMonths.getResultSet();
				int[] yearMonths = null;
				while (resYearMonths.next()) {
					yearMonths = new int[12];
					for (int i = 2; i <= 13; i++)
						yearMonths[i - 2] = resYearMonths.getInt(i);
				}
				// save the data:
				DbReports.add(new ExpectedActivityReport(id, requests, scope, meidan, deviation, distribution, start,
						end, created, monthDays, weekDays, yearMonths));
			}
		} catch (SQLException e) {
			System.out.println(e);
			fail("Can't reach to the activity tables");
		}

	}
}

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
import java.util.EnumSet;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.ActivitiesReport;
import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.User;
import Entity.User.collegeStatus;
import Entity.User.icmPermission;
import queryHandler.ActivitiesReportQuerys;
import queryHandler.FrequencydistributionQuerys;
import queryHandler.QueryHandler;
import reporting.ReportController;
import reporting.ReportController.reportScope;
import theServer.mysqlConnection;

class ActivityReportSqlTest {
	Connection conn;
	ReportController reportController;
	mysqlConnection mySQL;
	ActivitiesReportQuerys activityQry;
	FrequencydistributionQuerys distributionQry;
	// number of each column:
	final int ID = 1; // Activity id column
	final int SCOPE = 2; // id column
	final int START = 3; // Start date column
	final int END = 4; // End date column
	final int CREATED = 5; // Created date column
	final int MEDIAN = 6; // Median column
	final int DEVIATION = 7; // id column
	final int REQUESTS = 8; // Number of requests column
	final int DISTRIBUTION = 9; // Distribution column
	// end numbering.
	// Activity report details (only ongoing):

	private class ExpectedActivityReport {
		int id, distribution;
		String scope;
		double meidan, deviation, requests;
		LocalDate start, end, created;
		int[] monthDays = new int[31];
		int[] weekDays = new int[7];
		int[] yearMonths = new int[12];

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

	ArrayList<ExpectedActivityReport> DbReports;

	// end details.
	
	@BeforeEach
	void initilaize() {
		connectMysql();
		DbReports = new ArrayList<ExpectedActivityReport>();
		mySQL = new mysqlConnection(); // local
		activityQry = new QueryHandler(mySQL).getActivitiesReportQuerys();
		distributionQry = new QueryHandler(mySQL).getFrequencydistributionQuerys();
		reportController = new ReportController(new QueryHandler(mySQL));
		mysqlConnection.dropDB(); // drop schema
		mySQL.buildDB(); // build new schema with the relevent tables to icm.
		createRequests();
		

	}
	private void connectMysql() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=IST", "root",
					"Aa123456");
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println(e);
		}
			
	}
	private void createRequests() {
		int j;
		try {
			PreparedStatement addRequestsStmt;
			for (int i =0;i<=30;i++) {
				String qry="INSERT INTO `icm`.`changerequest` (`RequestID`,`startDate`, `status`) VALUES (?, ?, 'ongoing');";
				addRequestsStmt=conn.prepareStatement(qry);
				addRequestsStmt.setInt(1, i);
				if (i<10) j=i;
				else if(i<15) j=i*7; // 5 weeks have request in same day
				else j=i*31; // 15 months have diffrent requests.
				addRequestsStmt.setNString(2, LocalDate.now().plusDays(j).toString());
				addRequestsStmt.execute();
				addRequestsStmt.close();
				qry="INSERT INTO `icm`.`stage` (`RequestID`, `currentStage`, currentSubStage,`stage1extension`, `stage2extension`, `stage3extension`, `stage4extension`) VALUES (?, 'meaningEvaluation', 'ApprovingDueTime' , '0', '0', '0', '0');";
				addRequestsStmt=conn.prepareStatement(qry);
				addRequestsStmt.setInt(1, i);
				addRequestsStmt.execute();
				addRequestsStmt.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
	}

	/**
	 * Insert activity report from the server with the class QueryHandler. with 0
	 * requests.
	 */
	@Test
	void serverCreateDayOfMonthReportTest() {
		ActivitiesReport report = reportController.creatActivitiesReport(LocalDate.now(), LocalDate.now().plusMonths(5),
				reportScope.dayOfmonth);
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
		assertArrayEquals(report.getOngoingRequestsFrequencyDistribution(), DbReports.get(0).monthDays);
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Insert activity report from the server with the class QueryHandler. with 0
	 * requests.
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
	 * Insert activity report from the server with the class QueryHandler. with 0
	 * requests.
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
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Insert activity report from server and then get it from server
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
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Insert activity report from server and then get it from server
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
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Insert activity report from server and then get it from server
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
		assertEquals(1, DbReports.get(0).distribution);
	}

	/**
	 * Server didn't create report but still try to get (no reports in the DATABASE)
	 */
	@Test
	void serverGetEmptyReportTest() {
		ArrayList<ActivitiesReport> reports = activityQry.getAllActivitiesReports();
		assertTrue(reports.isEmpty());
	}

	private void getOngoingReport() {
		try {
			Statement stmntActivity = conn.createStatement();
			stmntActivity.execute("SELECT * FROM icm.activitiesreport");
			ResultSet resActivity = stmntActivity.getResultSet();
			PreparedStatement stmntWeekDays = conn
					.prepareStatement("SELECT * FROM icm.`frequencydistribution-daysinweek` WHERE id=?");
			PreparedStatement stmntMonthDays = conn
					.prepareStatement("SELECT * FROM icm.`frequencydistribution-daysinmonth` WHERE id=?");
			PreparedStatement stmntYearMonths = conn
					.prepareStatement("SELECT * FROM icm.`frequencydistribution-months` WHERE id=?");

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
				// WeekDays
				stmntWeekDays.setInt(1, distribution);
				stmntWeekDays.execute();
				ResultSet resWeekDays = stmntWeekDays.getResultSet();
				int[] weekDays = null;
				while (resWeekDays.next()) {
					weekDays = new int[7];
					for (int i = 2; i <= 8; i++)
						weekDays[i - 2] = resWeekDays.getInt(i);
				}
				// MonthDays
				stmntMonthDays.setInt(1, distribution);
				stmntMonthDays.execute();
				ResultSet resMonthDays = stmntMonthDays.getResultSet();
				int[] monthDays = null;
				while (resMonthDays.next()) {
					monthDays = new int[31];
					for (int i = 2; i <= 32; i++)
						monthDays[i - 2] = resMonthDays.getInt(i);
				}
				// YearMonths
				stmntYearMonths.setInt(1, distribution);
				stmntYearMonths.execute();
				ResultSet resYearMonths = stmntYearMonths.getResultSet();
				int[] yearMonths = null;
				while (resYearMonths.next()) {
					yearMonths = new int[12];
					for (int i = 2; i <= 13; i++)
						yearMonths[i - 2] = resYearMonths.getInt(i);
				}
				DbReports.add(new ExpectedActivityReport(id, requests, scope, meidan, deviation, distribution, start,
						end, created, monthDays, weekDays, yearMonths));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}

	}
}

package queryHandler;

import theServer.mysqlConnection;

/**
 * concentrates all existing query Classes
 * <p>
 * 
 * @see FilesQuerys,
 * @see ChangeRequestQuerys
 * @see ProccesStageQuerys,
 * @see InitiatorQuerys
 * @see UserQuerys
 * @see InspectorUpdatesQuerys
 * @see EstimatorReportQuerys
 * @see FrequencydistributionQuerys
 * @see ActivitiesReportQuerys
 *
 */
public class QueryHandler {

	private mysqlConnection mysqlConn;
	private FilesQuerys filesQuerys;

	private ChangeRequestQuerys changeRequestQuerys;
	private ProccesStageQuerys proccesStageQuerys;

	private InitiatorQuerys initiatorQuerys;
	private UserQuerys userQuerys;
	private InspectorUpdatesQuerys inspectorUpdatesQuerys;
	private EstimatorReportQuerys estimatorReportQuerys;
	private FrequencydistributionQuerys frequencydistributionQuerys;
	private ActivitiesReportQuerys activitiesReportQuerys ;
	private MessagesQuerys messagesQuerys;

	public QueryHandler(mysqlConnection mysqlConn) {
		this.mysqlConn = mysqlConn;
		this.filesQuerys = new FilesQuerys(this);
		this.changeRequestQuerys = new ChangeRequestQuerys(this);
		this.proccesStageQuerys = new ProccesStageQuerys(this);
		this.initiatorQuerys = new InitiatorQuerys(this);
		this.userQuerys = new UserQuerys(this);
		this.inspectorUpdatesQuerys = new InspectorUpdatesQuerys(this);
		this.estimatorReportQuerys = new EstimatorReportQuerys(this);
		this.frequencydistributionQuerys= new FrequencydistributionQuerys(this);
		this.activitiesReportQuerys= new ActivitiesReportQuerys(this);
		this.messagesQuerys=  new MessagesQuerys(this);
		}

	public mysqlConnection getmysqlConn() {
		return mysqlConn;
	}

	public FilesQuerys getFilesQuerys() {
		return filesQuerys;
	}

	public ChangeRequestQuerys getChangeRequestQuerys() {
		return changeRequestQuerys;
	}

	public ProccesStageQuerys getProccesStageQuerys() {
		return proccesStageQuerys;

	}

	public InitiatorQuerys getInitiatorQuerys() {
		return initiatorQuerys;
	}

	public UserQuerys getUserQuerys() {
		return userQuerys;
	}

	public InspectorUpdatesQuerys getInspectorUpdatesQuerys() {
		return inspectorUpdatesQuerys;
	}

	public EstimatorReportQuerys getEstimatorReportQuerys() {
		return estimatorReportQuerys;
	}

	public FrequencydistributionQuerys getFrequencydistributionQuerys() {
		return frequencydistributionQuerys;
	}

	public ActivitiesReportQuerys getActivitiesReportQuerys() {
		return activitiesReportQuerys;
	}

	public MessagesQuerys getMessagesQuerys() {
		return messagesQuerys;
	}
	
	
	

	// all old prototype methods
	// **********************************************************************************

	/*
	 * * for prototype. update status to some id in requirement table.
	 *
	 * @param id id of Request going to Update as a Num in DataBase
	 * 
	 * @param status current status going to Update as a Status in DataBase
	 *//*
		 * public void updateStatus(int id, String status) throws
		 * IllegalArgumentException { PreparedStatement updStatus; try { updStatus =
		 * mysqlConn.getConn().prepareStatement(
		 * "UPDATE icm.requirement SET Status = ? WHERE RequestID = ?;");
		 * updStatus.setNString(1, status); updStatus.setInt(2, id);
		 * updStatus.execute(); updStatus.close(); } catch (SQLException e) {
		 * e.printStackTrace(); } }
		 * 
		 *//*
			 * for prototype. insert new requirement in icm.requirement.
			 * 
			 * @param reqInitiator Initiator of request
			 * 
			 * @param currentSituationDetails Details of current situation
			 * 
			 * @param requestDetails Details of request
			 * 
			 * @param stageSupervisor Supervisor of request
			 *//*
				 * 
				 * public void insertRequirement(String reqInitiator, String
				 * currentSituationDetails, String requestDetails, String stageSupervisor,
				 * statusOptions status) { // send the use details. try { int count = 0;
				 * Statement numTest = mysqlConn.getConn().createStatement(); try { ResultSet re
				 * = numTest.
				 * executeQuery("SELECT MAX(RequestID) FROM icm.requirement WHERE RequestID;");/
				 * / get all numbers submissions. while (re.next()) { // generate number for
				 * submission. count = re.getInt(1); } } catch (SQLException e) { System.out.
				 * println("Database is empty, or no schema for ICM - insertRequirement"); count
				 * = 0; } PreparedStatement stmt =
				 * mysqlConn.getConn().prepareStatement("INSERT INTO icm.requirement " +
				 * "(Initiator, " + "RequestID, " + "CurrentSituationDetails, " +
				 * "RequestDetails, " + "StageSupervisor, " + "Status) " +
				 * "VALUES(?, ?, ?, ?, ?,?);"); stmt.setNString(1, reqInitiator); stmt.setInt(2,
				 * count + 1); stmt.setNString(3, currentSituationDetails); stmt.setNString(4,
				 * requestDetails); stmt.setNString(5, stageSupervisor); stmt.setNString(6,
				 * status.name()); stmt.execute(); // insert new row to requirement table.
				 * 
				 * stmt.close(); numTest.close(); } catch (SQLException e) {
				 * e.printStackTrace(); } }
				 * 
				 *//*
					 *
					 * for prototype. get all details of one requirement by it's ID
					 * 
					 * @param reqID input request ID
					 * 
					 * @return list
					 *//*
						 * public String[] selectRequirement(String reqID) { String[] toReturn = null;
						 * try { PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
						 * "SELECT * FROM icm.requirement WHERE RequestID = ?"); stmt.setNString(1,
						 * reqID); ResultSet re = stmt.executeQuery(); while (re.next()) { toReturn =
						 * new String[]{ re.getNString(1), re.getNString(2), re.getNString(3),
						 * re.getNString(4), re.getNString(5), re.getNString(6) }; } stmt.close(); }
						 * catch (SQLException e) { e.printStackTrace(); } return toReturn; }
						 * 
						 *//*
							 *
							 * get all requirement data. send it to osf.server as ArrayList of array of
							 * strings.
							 *
							 * the array: place 1: Initiator place 2: RequestID place 3:
							 * CurrentSituationDetails place 4: RequestDetails place 5: StageSupervisor
							 * place 6: Status
							 *
							 * @return toReturn ArrayList<String[]>
							 *//*
								 * public ArrayList<String[]> selectAll() { ArrayList<String[]> toReturn = new
								 * ArrayList<>(); String[] toPut; Statement stmt; ResultSet re; try { stmt =
								 * mysqlConn.getConn().createStatement(); re =
								 * stmt.executeQuery("SELECT * FROM icm.requirement;"); while (re.next()) {
								 * toPut = new String[6]; for (int i = 0; i < 6; i++) { toPut[i] =
								 * (re.getObject(i + 1)).toString(); } toReturn.add(toPut); } stmt.close(); }
								 * catch (SQLException e) { e.printStackTrace(); } return toReturn; }
								 */
	
	
	
	
   	
	// old counting rows methode
	
/*
 * try { Statement numTest =
 * queryHandler.getmysqlConn().getConn().createStatement(); ResultSet re =
 * numTest.executeQuery("SELECT RequestID FROM icm.changeRequest");// get all
 * numbers submissions. while (re.next()) { // generate number for submission.
 * count++; } } catch (SQLException e) { System.out.
 * println("Database is empty, or no schema for ICM - InsertChangeRequest");
 * count = 0; } count++;
 */
}
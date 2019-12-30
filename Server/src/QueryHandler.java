import Entity.*;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.User.ICMPermissions;
import Entity.User.Job;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;

import static Entity.Requirement.statusOptions;
import static Entity.ProcessStage.ChargeRequestStages;
import static Entity.ProcessStage.subStages;

public class QueryHandler {

    private final mysqlConnection mysqlConn;

    public QueryHandler(mysqlConnection mysqlConn) {
        this.mysqlConn = mysqlConn;
    }


    /** Inserts a full user entity in to the DB
     * @param user ?
     */
    public void insertUser(User user) { // send the use details.
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "INSERT INTO icm.user " +
                    "(userName," +
                    "password," +
                    "firstName," +
                    "lastName," +
                    "login," +
                    "job," +
                    "email," +
                    "informationTechnologiesDepartmentMangerPermission," +
                    "inspectorPermission," +
                    "estimatorPermission," +
                    "executionLeaderPermission," +
                    "examinerPermission," +
                    "changeControlCommitteeChairman)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            setAllUserFieldsStatement(user, stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //END of insertUser()

    /** sets up all of the User class fields in to a Prepared Statement
     * 	mainly used to support insets and updates
     * @param user ?
     * @param stmt ?
     * @throws SQLException ?
     */
    private void setAllUserFieldsStatement(User user, PreparedStatement stmt) throws SQLException {
        stmt.setNString(1, user.getUserName());
        stmt.setNString(2, user.getPassword());
        stmt.setNString(3, user.getFirstName());
        stmt.setNString(4, user.getLastName());
        if (user.getLoggedIn()) {
            stmt.setInt(5, 1);
        }
        else stmt.setInt(5, 0);
        stmt.setNString(6, user.getJob().name());
        stmt.setNString(7, user.getEmail());
        stmt.setInt(8, 0);
        stmt.setInt(9, 0);
        stmt.setInt(10, 0);
        stmt.setInt(11, 0);
        stmt.setInt(12, 0);
        stmt.setInt(13, 0);
        EnumSet<ICMPermissions> Permissions = user.getICMPermissions();
        if (Permissions != null) {
            for (ICMPermissions e : Permissions) {
                switch (e) {
                    case informationTechnologiesDepartmentManager:
                        stmt.setInt(8, 1);
                        break;
                    case inspector:
                        stmt.setInt(9, 1);
                        break;
                    case estimator:
                        stmt.setInt(10, 1);
                        break;
                    case executionLeader:
                        stmt.setInt(11, 1);
                        break;
                    case examiner:
                        stmt.setInt(12, 1);
                        break;
                    case changeControlCommitteeChairman:
                        stmt.setInt(13, 1);
                        break;
                }
            }
        }
        stmt.execute(); // insert new row to requirement table
        stmt.close();
    }

    /**Inserting Initiator in to DB
     * @param initiator ?
     */
    public void insertInitiator(Initiator initiator) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "INSERT INTO icm.initiator " +
            		"(RequestID, userName) VALUES(?, ?);");
            setAllInitiatorFieldsStatement(initiator,stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// END insertInitiator()
    
    /**sets up all of the  Initiator fields in to a Prepared Statement
     * mainly used to support insets and updates
     * @param initiator ?
     * @param stmt ?
     * @throws SQLException ?
     */
    private void setAllInitiatorFieldsStatement(Initiator initiator ,PreparedStatement stmt) throws SQLException {
    	 stmt.setNString(1, initiator.getrequest().getRequestID());
         stmt.setNString(2, initiator.getTheInitiator().getUserName());
         stmt.execute(); // insert new row 
         stmt.close();
    }//END setAllInitiatorFieldsStatement();
    
    public void updateAllInitiatorFields(Initiator initiator) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "UPDATE icm.initiator " +
            		"SET RequestID = ?, userName = ? " +
            		"WHERE requestID = ?;");
            stmt.setNString(3,initiator.getrequest().getRequestID());
            setAllInitiatorFieldsStatement(initiator,stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //End updateAllInitiator()

    /** Inserting a ProcessStage in to DB
     * @param newStage ?
     */
    public void InsertProcessStage(ProcessStage newStage) {
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "INSERT INTO icm.stage " +
                    "(RequestID," +                            //[1]
                    "currentStage," +                            //[2]
                    "StageSupervisor," +                        //[3]
                    "EstimatorReport," +                        //[4]
                    "ExaminerFailReport," +                    //[5]
                    "inspectorDocument," +                        //[6]
                    "meaningEvaluationStartDate," +            //[7]
                    "meaningEvaluationDueDate," +                //[8]
                    "meaningEvaluationEndDate," +                //[9]
                    "examinationAndDecisionStartDate," +        //[10]
                    "stageColExaminationAndDecisionDueDate," +  //[11]
                    "examinationAndDecisionEndDate," +            //[12]
                    "ExecutionStartDate," +                        //[13]
                    "ExecutionDueDate," +                        //[14]
                    "ExecutionEndDate," +                        //[15]
                    "examinationStartDate," +                    //[16]
                    "examinationDueDate," +                    //[17]
                    "examinationEndDate," +                    //[18]
                    "closureStarDate," +                        //[19]
                    "closureEndDate," +                         //[20]
                    "stage1extension," +                      //[21]
                    "stage2extension," +                      //[22]
                    "stage3extension," +                      //[23]
                    "stage4extension," +                      //[24]
                    "stage5extension," +                      //[25]
                    "currentSubStage)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            setAllProcessStageStatement( newStage , stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //END of InsertProcessStage();
    
    /**sets up all of the ProcessStage  fields in to a Prepared Statement
     * 
     * ,mainly used to support insets and updates
     * 
     * @param newStage ?
     * @param stmt ?
     * @throws SQLException ?
     */
    private void setAllProcessStageStatement(ProcessStage newStage ,
                                             PreparedStatement stmt) throws SQLException {
        stmt.setNString(1, newStage.getRequest().getRequestID());
        stmt.setNString(2, newStage.getCurrentStage().name());
        if (newStage.getStageSupervisor() == null) {
            stmt.setNString(3, null);
        } else {
            stmt.setNString(3, newStage.getStageSupervisor().getUserName());
        }
        stmt.setNString(4, newStage.getEstimatorReport());
        stmt.setNString(5, newStage.getExaminerFailReport());
        stmt.setNString(6, newStage.getInspectorDocumentation());
        LocalDate[][] date = newStage.getDates();
        int u = 7;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if (date[i][j] == null) {
                    stmt.setNString(u, null);
                } else {
                    stmt.setNString(u, date[i][j].toString());
                }
                u++;
            }
        }
        if (date[4][0] == null) {
            stmt.setNString(19, null);
        } else {
            stmt.setNString(19, date[4][0].toString());
        }
        if (date[4][2] == null) {
            stmt.setNString(20, null);
        } else {
            stmt.setNString(20, date[4][2].toString());
        }
        boolean[] bool = newStage.getWasThereAnExtensionRequest();
        int v = 21;
        for (int j = 0; j < 5; j++) {
            if (bool[j]) {
                stmt.setInt(v, 1);
            } else {
                stmt.setInt(v, 0);
            }
            v++;
        }
        stmt.setString(26, newStage.getCurrentSubStage().name());
        stmt.execute();
        stmt.close();
    }//END setAllProcessStageStatement()

    /** Updateds all existing ProcessStage fields
     * @param newStage ?
    */
    public void updateAllProcessStageFields(ProcessStage newStage) {
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "UPDATE icm.stage "
                    + "SET RequestID = ?,"
                    + "currentStage = ?,"
                    + "StageSupervisor = ?,"
                    + "EstimatorReport = ?,"
                    + "ExaminerFailReport = ?,"
                    + "inspectorDocument = ?,"
                    + "meaningEvaluationStartDate = ?,"
                    + "meaningEvaluationDueDate = ?,"
                    + "meaningEvaluationEndDate = ?,"
                    + "examinationAndDecisionStartDate = ?,"
                    + "stageColExaminationAndDecisionDueDate = ?,"
                    + "examinationAndDecisionEndDate = ?,"
                    + "ExecutionStartDate = ?,"
                    + "ExecutionDueDate = ?,"
                    + "ExecutionEndDate = ?,"
                    + "examinationStartDate = ?,"
                    + "examinationDueDate = ?,"
                    + "examinationEndDate = ?,"
                    + "closureStarDate = ?,"
                    + "closureEndDate = ?,"
                    + "stage1extension = ?,"
                    + "stage2extension = ?,"
                    + "stage3extension = ?,"
                    + "stage4extension = ?,"
                    + "stage5extension = ?,"
                    + "currentSubStage = ?"
                    + "WHERE (RequestID = ?) and (currentStage = ?);");
            stmt.setNString(27, newStage.getRequest().getRequestID());
            stmt.setNString(28, newStage.getCurrentStage().name());
            setAllProcessStageStatement(newStage, stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }// END updateAllProcessStageStatement
    }

    /**Inserting ChangeRequest
     * @param newRequest ?
     * @return string  - given id of the request
     */
    public String InsertChangeRequest(ChangeRequest newRequest) {
    	int count = 0;
        try {
            Statement numTest = mysqlConn.getConn().createStatement();
            ResultSet re = numTest.executeQuery("SELECT RequestID FROM icm.changeRequest");// get all numbers submissions.
            while (re.next()) { // generate number for submission.
                count++;
            }
        } catch (SQLException e) {
            System.out.println("Database is empty, or no schema for ICM - InsertChangeRequest");
            count = 0;
        }
        count++;
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "INSERT INTO icm.changerequest " +
            		"(RequestID, " +
            		"startDate, " +
            		"`system`, " +
            		"problemDescription, " +
            		"whyChange, " +
                    "comment, " +
            		"status)" +
            		"VALUES(?, ?, ?, ?, ?, ?, ?);");
            stmt.setNString(1, String.valueOf(count));
            setChangeRequestFieldsStmnt(newRequest, stmt);
            stmt.execute(); // insert new row to requirement table
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return String.valueOf(count);
    } // end of InsertChangeRequest()

    public void updateAllChangeRequestFields(ChangeRequest newRequest) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "UPDATE icm.changerequest SET "
            		+ "RequestID = ?,"
            		+ "startDate = ?,"
            		+ "`system` = ?,"
            		+ "problemDescription = ?,"
            		+ "whyChange = ?,"
            		+ "comment = ?,"
            		+ "status = ?"
            		+ " WHERE (RequestID = ?);");
            stmt.setNString(1, newRequest.getRequestID());
            setChangeRequestFieldsStmnt(newRequest, stmt);
            stmt.setNString(8, newRequest.getRequestID());

            stmt.execute(); // insert new row to requirement table
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	updateAllProcessStageFields(newRequest.getProcessStage());
    	updateAllInitiatorFields(newRequest.getInitiator());
    }// END updateChangeRequest()

    private void setChangeRequestFieldsStmnt(ChangeRequest newRequest, PreparedStatement stmt) throws SQLException {
        stmt.setNString(2, newRequest.getStartDate().toString());
        stmt.setNString(3, newRequest.getSystem() );
        stmt.setNString(4, newRequest.getProblemDescription() );
        stmt.setNString(5, newRequest.getWhyChange());
        stmt.setNString(6, newRequest.getComment());
        stmt.setNString(7, newRequest.getStatus().name());
    }

    /** updates all user fields in the DB
     *
     * @param user ?
     * 
     */
    public void updateAllUserFields(User user) {
        PreparedStatement updStatus;
        try {
            updStatus = mysqlConn.getConn().prepareStatement(
                    "UPDATE icm.user " +
                            "SET userName = ?," +
                            "password = ?," +
                            "firstName = ?," +
                            "lastName = ?," +
                            "login = ?," +
                            "job = ?," +
                            "email = ?," +
                            "informationTechnologiesDepartmentMangerPermission = ?," +
                            "inspectorPermission = ?," +
                            "estimatorPermission = ?," +
                            "executionLeaderPermission = ?," +
                            "examinerPermission = ?," +
                            "changeControlCommitteeChairman = ?" +
                            "WHERE userName = ?;");
            updStatus.setNString(14, user.getUserName());
            setAllUserFieldsStatement(user, updStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** returns a user form db  by username
     * @param username ?
     * @return User ?
    */
    public User selectUser(String username) { // @building by yonathan not finished.
        User toReturn = null;
        try {
            //TODO: is PreparedStatement needed ?> yes
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("SELECT * FROM icm.user where user.userName = ?;");
            stmt.setNString(1, username);

            ResultSet re = stmt.executeQuery();
            while (re.next()) {
            	toReturn=userStamentgets(re);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    }//END of selectUser

     /** takes result set with all the user fields  and converts it to a user
     * @param re ?
     * @return ?
     */
    private  User userStamentgets(ResultSet re) {
    	 User toReturn;
         String userName = null;
         String password = null;
         String firstName = null;
         String lastName = null;
         boolean login = false;
         String jobString = null;
         String email = null;
         int informationTechnologiesDepartmentManagerPermission = 0;
         int inspectorPermission = 0;
         int estimatorPermission = 0;
         int executionLeaderPermission = 0;
         int examinerPermission = 0;
         int changeControlCommitteeChairman = 0;
         try {

             userName = re.getNString(1);
             password = re.getNString(2);
             firstName = re.getNString(3);
             lastName = re.getNString(4);
             login = re.getBoolean(5);
             jobString = re.getNString(6);
             email = re.getNString(7);
             informationTechnologiesDepartmentManagerPermission = re.getInt(8);
             inspectorPermission = re.getInt(9);
             estimatorPermission = re.getInt(10);
             executionLeaderPermission = re.getInt(11);
             examinerPermission = re.getInt(12);
             changeControlCommitteeChairman = re.getInt(13);

     } catch (SQLException e) {
         e.printStackTrace();
     }

     // convering job to enum
     EnumSet<Job> Jobs = EnumSet.allOf(User.Job.class);
     Job job = null;
     for (Job e : Jobs) {
         if (jobString != null && jobString.equals(e.name())) job = e;
     }
     // converting to premissions set
     EnumSet<ICMPermissions> all = EnumSet.allOf(User.ICMPermissions.class);
     EnumSet<ICMPermissions> Permissions = EnumSet.complementOf(all);
     if (informationTechnologiesDepartmentManagerPermission == 1) {
         Permissions.add(ICMPermissions.informationTechnologiesDepartmentManager);
     }
     if (inspectorPermission == 1) {
         Permissions.add(ICMPermissions.inspector);
     }
     if (estimatorPermission == 1) {
         Permissions.add(ICMPermissions.estimator);
     }
     if (executionLeaderPermission == 1) {
         Permissions.add(ICMPermissions.executionLeader);
     }
     if (examinerPermission == 1) {
         Permissions.add(ICMPermissions.examiner);
     }
     if (changeControlCommitteeChairman == 1) {
         Permissions.add(ICMPermissions.changeControlCommitteeChairman);
     }
     toReturn = new User(userName, password, firstName, lastName, email, job, Permissions, login);
     return toReturn;

     } // end of userStamentgets

    /** get all the users in the DB
     * @return ?
     */
    public ArrayList<User> getAllUsers(){
    	ArrayList<User> toReturn = new ArrayList<>();
        Statement stmt;
        ResultSet re;
        try {
            stmt = mysqlConn.getConn().createStatement();
            re = stmt.executeQuery("SELECT * FROM icm.user;");

            while (re.next()) {
                toReturn.add(userStamentgets(re));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    } // END of getAllUsers
    
    /**  getting all users with the specified job
     * @param job
     * @return
     */
    public ArrayList<User> getAllUsersByJob(Job job){
    	ArrayList<User> toReturn = new ArrayList<>();
    	 try {
             PreparedStatement stmt = mysqlConn.getConn().prepareStatement("SELECT * FROM icm.user where job=?;");
             stmt.setNString(1,job.name());
             ResultSet re = stmt.executeQuery();
            while (re.next()) {

                 toReturn.add(userStamentgets(re));
             }
             stmt.close();
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return toReturn;
    } // END of getAllUsers
    
    /** Getting all users with the  specified ICMPermission
     * @param prem
     * @return
     */
    public ArrayList<User> getAllUsersWithICMPermissions(ICMPermissions prem){
    	ArrayList<User> toReturn = new ArrayList<>();
    	Statement stmt;
        ResultSet re = null ;
        try {
            stmt = mysqlConn.getConn().createStatement();

    	switch (prem) {
    	case informationTechnologiesDepartmentManager :
            re = stmt.executeQuery("SELECT * FROM icm.user where informationTechnologiesDepartmentMangerPermission=1;");
    		break;
    	case inspector:
            re = stmt.executeQuery("SELECT * FROM icm.user where inspectorPermission=1;");
    		break;
    	case estimator:
            re = stmt.executeQuery("SELECT * FROM icm.user where estimatorPermission=1;");
    		break;
    	case executionLeader:
            re = stmt.executeQuery("SELECT * FROM icm.user where executionLeaderPermission=1;");
    		break;
    	case examiner:
            re = stmt.executeQuery("SELECT * FROM icm.user where examinerPermission=1;");
    		break;
    	case changeControlCommitteeChairman :
            re = stmt.executeQuery("SELECT * FROM icm.user where changeControlCommitteeChairman=1;");
    		break;
    	}
            while (re.next()) {
             toReturn.add(userStamentgets(re));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    } // END of getAllUsersWithRule
    /** gets all of the change requests in DB
     * @return -ArrayList<ChangeRequest>
     */
    public ArrayList<ChangeRequest> getAllChangeRequest() {
        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
        Statement stmt;
        ResultSet re;
        try {
            stmt = mysqlConn.getConn().createStatement();
            re = stmt.executeQuery("SELECT * FROM icm.changerequest;");

            while (re.next()) {

                toReturn.add(getChangeRequestsFromRes(re));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    } // END of getAllChangeRequest();
    
    /**getting all requests with the specified status 
     * @param stat
     * @return
     */
    public ArrayList<ChangeRequest> getAllChangeRequestWithStatus(ChangeRequestStatus stat) {
        ArrayList<ChangeRequest> toReturn = new ArrayList<>();
    
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("SELECT * FROM icm.changerequest where status=?;");
            stmt.setNString(1,stat.name());
            ResultSet re = stmt.executeQuery();
           while (re.next()) {

                toReturn.add(getChangeRequestsFromRes(re));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
    } // END of getAllChangeRequest();
    
    
    private ChangeRequest getChangeRequestsFromRes( ResultSet re) throws SQLException {
        ChangeRequest toPut;
        String RequestID = re.getString(1);
        LocalDate startDate;
        if (re.getString(2) != null) {
            startDate = LocalDate.parse(re.getString(2));
        } else startDate = null;
        String system = re.getString(3);
        String problemDescriptionString = re.getString(4);
        String whyChange = re.getString(5);
        String comment = re.getString(6);
        String statusString = re.getString(7);
        Initiator theInitiator = getInitiator(RequestID);
        // TODO:
        Document doc = null;
        //
        ProcessStage stage = getProcessStage(RequestID);

//THIS WAS REPLACED
/*       if (statusString.equals(ongoing.name())) {
            status = ongoing;
        }
        if (statusString.equals(ChangeRequestStatus.suspended.name())) {
            status = ChangeRequestStatus.suspended;
        }
        if (statusString.equals(ChangeRequestStatus.closed.name())) {
            status = ChangeRequestStatus.closed;
        }
*/
//BY THIS:
        ChangeRequestStatus status = ChangeRequest.ChangeRequestStatus.valueOf(statusString);

        toPut = new ChangeRequest(theInitiator, startDate, system, problemDescriptionString, whyChange, comment, doc);
        toPut.setStatus(status);
        toPut.setRequestID(RequestID);
        toPut.updateInitiatorRequest();
        toPut.updateStage();
        return toPut;
    } //END of getChangeRequestsFromRes()

	/** gets processStage without the change request
	 * 	its purpose is to support getAllChangeRequest() 
	 * @param RequestID ?
	 * @return ?
	 */
	public ProcessStage getProcessStage(String RequestID) {
		ProcessStage returnProcessStage = null;
		try {
			PreparedStatement stmt = mysqlConn.getConn()
					.prepareStatement("SELECT * FROM icm.stage where stage.RequestID = ?;");
			stmt.setNString(1, RequestID);
			ResultSet re = stmt.executeQuery();
			while (re.next()) {

				String currentStageString = re.getNString(2);
                ChargeRequestStages currentStage = ChargeRequestStages.valueOf(currentStageString);

				String currentSubStageString = re.getNString(26);
                subStages currentSubStage = subStages.valueOf(currentSubStageString);

                User StageSupervisor = selectUser(re.getString(3));
				String EstimatorReport = re.getString(4);
				String ExaminerFailReport = re.getString(5);
				String inspectorDocumentation = re.getString(6);

				LocalDate[][] startEndArray = new LocalDate[5][3];
				int u = 7;
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 3; j++) {
						if(re.getString(u) != null) {
						    startEndArray[i][j] = LocalDate.parse(re.getString(u));
                        }
						else startEndArray[i][j] = null;
						u++;
					}
				}
				if(re.getString(u) != null) {
				    startEndArray[4][0] = LocalDate.parse(re.getString(19));
                }
				else startEndArray[4][0] = null;

				if(re.getString(u) != null) {
				    startEndArray[4][2] = LocalDate.parse(re.getString(20));
                }
				else startEndArray[4][2] = null;

				boolean[] WasThereAnExtensionRequest = new boolean[5];
				u = 21;
				for (int i = 0; i < 5; i++) {
                    WasThereAnExtensionRequest[i] = re.getInt(u) == 1;
					u++;
				}
				returnProcessStage = new ProcessStage(currentStage, currentSubStage,
                        StageSupervisor, EstimatorReport, ExaminerFailReport,
                        inspectorDocumentation, startEndArray, WasThereAnExtensionRequest);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnProcessStage;
	}// END getProcessStage()

    /** get the  Initiator without change request
     * its purpose is to support getAllChangeRequest() 
     * @param RequestID ?
     * @return ?
    */
    public Initiator getInitiator(String RequestID) {
        Initiator returnInitiator = null;
        String Username = null;
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "SELECT * FROM icm.initiator WHERE initiator.RequestID = ?;");
            stmt.setNString(1, RequestID);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                Username = re.getString(2);
            }
            User user = selectUser(Username);
            returnInitiator = new Initiator(user, null);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnInitiator;
    }

    // all old prototype methods **********************************************************************************

    /**
    *
    * for prototype.
    * update status to some id in requirement table.
    *
    * @param id id of Request going to Update as a Num in DataBase
    * @param status current status  going to Update as a Status in DataBase
    */
   public void updateStatus(int id, String status) throws IllegalArgumentException {
       PreparedStatement updStatus;
       try {
           updStatus = mysqlConn.getConn().prepareStatement(
                   "UPDATE icm.requirement SET Status = ? WHERE RequestID = ?;");
           updStatus.setNString(1, status);
           updStatus.setInt(2, id);
           updStatus.execute();
           updStatus.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
    
    /**
     * for prototype.
     * insert new requirement in icm.requirement.
     * @param reqInitiator              Initiator of request
     * @param currentSituationDetails   Details of current situation
     * @param requestDetails            Details of request
     * @param stageSupervisor           Supervisor of request
     */

    public void insertRequirement(String reqInitiator, String currentSituationDetails,
                                  String requestDetails, String stageSupervisor,
                                  statusOptions status) { // send the use details.
        try {
            int count = 0;
            Statement numTest = mysqlConn.getConn().createStatement();
            try {
                ResultSet re = numTest.executeQuery("SELECT MAX(RequestID) FROM icm.requirement WHERE RequestID;");// get all numbers submissions.
                while (re.next()) { // generate number for submission.
                    count = re.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println("Database is empty, or no schema for ICM - insertRequirement");
                count = 0;
            }
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO icm.requirement " +
                    "(Initiator, " +
                    "RequestID, " +
                    "CurrentSituationDetails, " +
                    "RequestDetails, " +
                    "StageSupervisor, " +
                    "Status) " +
                    "VALUES(?, ?, ?, ?, ?,?);");
            stmt.setNString(1, reqInitiator);
            stmt.setInt(2, count + 1);
            stmt.setNString(3, currentSituationDetails);
            stmt.setNString(4, requestDetails);
            stmt.setNString(5, stageSupervisor);
            stmt.setNString(6, status.name());
            stmt.execute(); // insert new row to requirement table.

            stmt.close();
            numTest.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *for prototype.
     *get all details of one requirement by it's ID
     * @param reqID input request ID
     * @return list
     */
    public String[] selectRequirement(String reqID) {
        String[] toReturn = null;
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement(
                    "SELECT * FROM icm.requirement WHERE RequestID = ?");
            stmt.setNString(1, reqID);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                toReturn = new String[]{
                        re.getNString(1),
                        re.getNString(2),
                        re.getNString(3),
                        re.getNString(4),
                        re.getNString(5),
                        re.getNString(6)
                };
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     *
     * get all requirement data.
     * send it to osf.server as ArrayList of array of strings.
     *
     * the array:
     * place 1: Initiator
     * place 2: RequestID
     * place 3: CurrentSituationDetails
     * place 4: RequestDetails
     * place 5: StageSupervisor
     * place 6: Status
     *
     * @return toReturn ArrayList<String[]>
     */
    public ArrayList<String[]> selectAll() {
        ArrayList<String[]> toReturn = new ArrayList<>();
        String[] toPut;
        Statement stmt;
        ResultSet re;
        try {
            stmt = mysqlConn.getConn().createStatement();
            re = stmt.executeQuery("SELECT * FROM icm.requirement;");
            while (re.next()) {
                toPut = new String[6];
                for (int i = 0; i < 6; i++) {
                    toPut[i] = (re.getObject(i + 1)).toString();
                }
                toReturn.add(toPut);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
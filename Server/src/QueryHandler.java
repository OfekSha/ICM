import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.ProcessStage;
import Entity.User;
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
public class QueryHandler {
    private final mysqlConnection mysqlConn;

    public QueryHandler(mysqlConnection mysqlConn) {
        this.mysqlConn = mysqlConn;
    }

    /**
     * for prototype.
     * insert new requirement in icm.requirement.
     * @param reqInitiator              Initiator of request
     * @param currentSituationDetails   Details of current situation
     * @param requestDetails            Details of request
     * @param stageSupervisor           Supervisor of request
     */

    public void insertRequirement(String reqInitiator, String currentSituationDetails, String requestDetails, String stageSupervisor, statusOptions status) { // send the use details.
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
     * @param user ?
     */
    public void insertUser(User user) { // send the use details.
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO icm.user " +
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
                    "VALUES(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?);");
            setUserStatement(user, stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //END of insertUser()

    private void setUserStatement(User user, PreparedStatement stmt) throws SQLException {
        stmt.setNString(1, user.getUserName());
        stmt.setNString(2, user.getPassword());
        stmt.setNString(3, user.getFirstName());
        stmt.setNString(4, user.getLastName());
        if (user.getLoggedIn()) {
            stmt.setInt(5, 1);
        }
        else {
            stmt.setInt(5, 0);
        }
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

    public void insertInitiator(Initiator initiator) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO icm.initiator " +
            		"(RequestID, userName) VALUES(?, ?);");
            stmt.setNString(1, initiator.getrequest().getRequestID());
            stmt.setNString(2, initiator.getTheInitiator().getUserName());
            stmt.execute(); // insert new row to requirement table
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }// END insertInitiator()


    public void InsertProcessStage(ProcessStage newStage) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO icm.stage " +
            		"(RequestID," + 							//[1]
            		"currentStage," + 							//[2]
            		"StageSupervisor," + 						//[3]
            		"EstimatorReport," + 						//[4]
            		"ExaminerFailReport," + 					//[5]
            		"inspectorDocument," + 					    //[6]
            		"meaningEvaluationStartDate," + 			//[7]
            		"meaningEvaluationDueDate," + 				//[8]
            		"meaningEvaluationEndDate," +          		//[9]
            		"examinationAndDecisionStartDate," + 		//[10]
            		"stageColExaminationAndDecisionDueDate," +  //[11]
            		"examinationAndDecisionEndDate," + 			//[12]
            		"ExecutionStartDate," +						//[13]
            		"ExecutionDueDate," + 						//[14]
            		"ExecutionEndDate," + 						//[15]
            		"examinationStartDate," + 					//[16]
            		"examinationDueDate," + 					//[17]
            		"examinationEndDate," + 					//[18]
            		"closureStarDate," + 						//[19]
            		"closureEndDate,"							//[20]
            		+ "stage1extention,"
            		+ "stage2extention,"
            		+ "stage3extention,"
            		+ "stage4extention,"
            		+ "stage5extention,"
            		+ "currentSubStage)" + 						
            		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setNString(1, newStage.getRequest().getRequestID());
            stmt.setNString(2, newStage.getCurrentStage().name());
            if (newStage.getStageSupervisor() == null) {
                stmt.setNString(3, null);
            }
            else  {
                stmt.setNString(3, newStage.getStageSupervisor().getUserName());
            }
            stmt.setNString(4, newStage.getEstimatorReport());
            stmt.setNString(5, newStage.getExeminorFailReport());
            stmt.setNString(6, newStage.getInspectorDocumention());
            LocalDate [][] date = newStage.getDates();
            int u = 7;
            for (int i = 0 ; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    if (date[i][j] == null) {
                        stmt.setNString(u, null);
                    }
                    else {
                        stmt.setNString(u, date[i][j].toString());
                    }
                    u++;
                }
            }
            if (date[4][0] == null) {
                stmt.setNString(19, null);
            }
            else {
                stmt.setNString(19, date[4][0].toString());
            }
            if(date[4][2] == null) {
                stmt.setNString(20, null);
            }
            else {
                stmt.setNString(20, date[4][2].toString());
            }
            boolean[] bool =newStage.getWasThereAnExtentionRequest();
            int v= 21;
			for (int j = 0; j < 5; j++) {
				if (bool[j] == true)
					stmt.setInt(v, 1);
				else
					stmt.setInt(v, 0);
				v++;
			}
			stmt.setString(26, newStage.getCurrentSubStage().name());
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
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
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO icm.changerequest " +
            		"(RequestID, " +
            		"startDate, " +
            		"`system`, " +
            		"problemDescription, " +
            		"whyChange, " +
                    "comment, " +
            		"status)" +
            		"VALUES(?, ?, ?, ?, ?, ?, ?);");
            stmt.setNString(1, String.valueOf(count));
            stmt.setNString(2, newRequest.getStartDate().toString());
            stmt.setNString(3, newRequest.getSystem() );
            stmt.setNString(4, newRequest.getProblemDescription() );
            stmt.setNString(5, newRequest.getWhyChange());
            stmt.setNString(6, newRequest.getComment());
            stmt.setNString(7, newRequest.getStatus().name());
            stmt.execute(); // insert new row to requirement table
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return String.valueOf(count);
    } // end of InsertChangeRequest()
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
    public void updateAllUserFields(User user) throws IllegalArgumentException {
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
            setUserStatement(user, updStatus);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
/** returns a user form db  by username
 * @param username ?
 * @return ?
 */
    public User selectUser(String username) { // @building by yonathan not finished.
        User toReturn;
        String userName = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        int intLogin = 0;
        String jobString = null;
        String email = null;
        int informationTechnologiesDepartmentManagerPermission = 0;
        int inspectorPermission = 0;
        int estimatorPermission = 0;
        int executionLeaderPermission = 0;
        int examinerPermission = 0;
        int changeControlCommitteeChairman = 0;
        try {
            //TODO: is PreparedStatement needed ?> yes
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("SELECT * FROM icm.user where user.userName = ?;");
            stmt.setNString(1, username);

            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                userName = re.getNString(1);
                password = re.getNString(2);
                firstName = re.getNString(3);
                lastName = re.getNString(4);
                intLogin = re.getInt(5);
                jobString = re.getNString(6);
                email = re.getNString(7);
                informationTechnologiesDepartmentManagerPermission = re.getInt(8);
                inspectorPermission = re.getInt(9);
                estimatorPermission = re.getInt(10);
                executionLeaderPermission = re.getInt(11);
                examinerPermission = re.getInt(12);
                changeControlCommitteeChairman = re.getInt(13);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // convertin log in to bloolean vulue
        boolean logIn = false;
        if (intLogin == 1) {
            logIn = true;
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
        toReturn = new User(userName, password, firstName, lastName, email, job, Permissions, logIn);
        return toReturn;
    }//END of selectUser



    /**
     *
     *for prototype.
     *get all details of one requirement by it's ID
     *
     * @param reqID input request ID
     * @return list
     */
    public String[] selectRequirement(int reqID) {
        String[] toReturn = null;
        try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("SELECT * FROM icm.requirement WHERE RequestID = ?");
            stmt.setInt(1, reqID);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                toReturn = new String[] {
                        re.getNString(1),
                        re.getInt(2) + "",
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

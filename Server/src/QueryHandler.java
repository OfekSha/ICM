import java.sql.*;
import java.util.ArrayList;
import java.util.EnumSet;

import Entity.User.ICMPermissions;
import Entity.User.Job;

import static Entity.Requirement.*;
import Entity.Requirement;
import Entity.User;
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
                System.out.println("Database is empty, or no schema for ICM");
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
    
    /**insert new user in icm.user.
     * @param userName
     * @param password
     * @param firstName
     * @param lastName
     * @param email
     * @param Permissions
     * @param job
     * @param logedIn
     */
    public void insertUser(String userName, String password, String firstName, String lastName,String email ,EnumSet<ICMPermissions> Permissions,Job job,boolean logedIn) { // send the use details.
        try {
            int count = 0;
            Statement numTest = mysqlConn.getConn().createStatement();
            try {
                ResultSet re = numTest.executeQuery("SELECT(user) FROM icm.user;");// get all numbers submissions.
                while (re.next()) { // generate number for submission.
                    count = re.getInt(1);
                }
            } catch (SQLException e) {
                System.out.println("Database is empty, or no schema for ICM");
                count = 0;
            }
            
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO `icm`.`user`\n" + 
            		"(`userName`,\n" + 
            		"`password`,\n" + 
            		"`firstName`,\n" + 
            		"`lastName`,\n" + 
            		"`login`,\n" + 
            		"`job`,\n" + 
            		"`email`,\n" + 
            		"`informationTecnologiesDeparmentMangerPermission`,\n" + 
            		"`inspectorPermission`,\n" + 
            		"`estimatorPermission`,\n" + 
            		"`exeutionLeaderPermission`,\n" + 
            		"`examinerPermission`,\n" + 
            		"`changeControlCommitteeChairmant`)"+
                    "VALUES(?, ?, ?, ?, ?,?,?, ?, ?, ?, ?,?,?);");
            stmt.setNString(1, userName);
            stmt.setNString(2, password);
            stmt.setNString(3, firstName);
            stmt.setNString(4, lastName);
            stmt.setBoolean(5, logedIn);
            stmt.setNString(6, job.name());
            stmt.setNString(7, email);  
            stmt.setInt(8, 0);
            stmt.setInt(9, 0);
            stmt.setInt(10, 0);
            stmt.setInt(11, 0);
            stmt.setInt(12, 0);
            stmt.setInt(13, 0);
           
            for(ICMPermissions e: Permissions) {
            	switch(e) {
            	case informationTecnologiesDeparmentManger:
            	     stmt.setInt(8, 1);
            		break;
            	case inspector:
            		 stmt.setInt(9, 1);
            		break;
            	case estimator:
            		  stmt.setInt(10, 1);
            		break;
            	case exeutionLeader:
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
           
            stmt.execute(); // insert new row to requirement table.

            stmt.close();
            numTest.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //END of insertUser()

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
    
    
/** returns a user form db  by username
 * @param username
 * @return
 */
public User selectUser(String username) { // @building by yonathan not finished.
	User toReturn = null;
	String userName = null ;
	  String password = null;
	  String firstName = null;
	  String lastName = null;
	  int intLogin = 0 ;
	  String jobString = null;
	  String email = null ;
	  int informationTecnologiesDeparmentMangerPermission = 0;
	  int inspectorPermission = 0 ;
	  int estimatorPermission = 0 ;
	  int exeutionLeaderPermission = 0 ;
	  int examinerPermission = 0 ;
	  int changeControlCommitteeChairmant = 0 ;  
     try { 
    	 //TODO: is PreparedStatement needed ?>
         PreparedStatement stmt = mysqlConn.getConn().prepareStatement("SELECT * FROM icm.user where userName = \""+username+"\";" );
        // stmt.setString(1, username);
         ResultSet re = stmt.executeQuery();
         while (re.next()) {
        	   userName =re.getNString(1);
        	   password=re.getNString(2);
        	   firstName=re.getNString(3);
        	   lastName=re.getNString(4);
        	   intLogin = re.getInt(5);
        	   jobString=re.getNString(6);
        	   email =re.getNString(7);
        	   informationTecnologiesDeparmentMangerPermission = re.getInt(8);
        	   inspectorPermission = re.getInt(9);
        	   estimatorPermission = re.getInt(10);
        	   exeutionLeaderPermission = re.getInt(11);
        	   examinerPermission = re.getInt(12);
        	   changeControlCommitteeChairmant = re.getInt(13);  
         } 
         stmt.close();
     } catch (SQLException e) {
         e.printStackTrace();
     }
     
  // convertin log in to bloolean vulue
     boolean logIn=false;
     if(intLogin == 1) logIn=true;
     // convering job to enum 
     EnumSet<Job> Jobs =EnumSet.allOf(User.Job.class);
     Job job =null ;
     for(Job e :Jobs) {
    	 if ( jobString.equals(e.name()))  job=e;
     }
     // converting to premissions set
   ArrayList<ICMPermissions> PermissionsArrayList =new ArrayList<>() ;
     if(informationTecnologiesDeparmentMangerPermission==1)  PermissionsArrayList.add(ICMPermissions.informationTecnologiesDeparmentManger);
     if(inspectorPermission==1)  PermissionsArrayList.add(ICMPermissions.inspector);
     if(estimatorPermission==1)  PermissionsArrayList.add(ICMPermissions.estimator);
     if(exeutionLeaderPermission==1)  PermissionsArrayList.add(ICMPermissions.exeutionLeader);
     if(examinerPermission==1)  PermissionsArrayList.add(ICMPermissions.examiner);
     if(changeControlCommitteeChairmant==1)  PermissionsArrayList.add(ICMPermissions.changeControlCommitteeChairman);
    EnumSet<ICMPermissions> Permissions=EnumSet.copyOf(PermissionsArrayList); 

    
    toReturn = new User(userName, password, firstName, lastName, email, job, Permissions, logIn);
     return toReturn;
}//END ofselectUser



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

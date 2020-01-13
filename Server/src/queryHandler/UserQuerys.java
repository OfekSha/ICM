package queryHandler;
import Entity.User;
import Entity.User.collegeStatus;
import Entity.User.icmPermission;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * concentrates  all querys  for the table user
 *
 */
public class UserQuerys {

	 private final QueryHandler queryHandler;
		
	 public UserQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
    
    
    
    /** Inserts a full user entity in to the DB
     * @param user ?
     */
    public void insertUser(User user) { // send the use details.
        try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "INSERT INTO icm.user " +
                    "(userName," +
                    "password," +
                    "firstName," +
                    "lastName," +
                    "job," +
                    "email," +
                    "informationTechnologiesDepartmentMangerPermission," +
                    "inspectorPermission," +
                    "estimatorPermission," +
                    "executionLeaderPermission," +
                    "examinerPermission," +
                    "changeControlCommitteeChairman," +
                    "changeControlCommitteeMember)" +
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
        stmt.setNString(5, user.getCollegeStatus().name());
        stmt.setNString(6, user.getEmail());
        stmt.setInt(7, 0);
        stmt.setInt(8, 0);
        stmt.setInt(9, 0);
        stmt.setInt(10, 0);
        stmt.setInt(11, 0);
        stmt.setInt(12, 0);
        stmt.setInt(13, 0);
        EnumSet<icmPermission> Permissions = user.getICMPermissions();
        if (Permissions != null) {
            for (icmPermission e : Permissions) {
                switch (e) {
                    case informationTechnologiesDepartmentManager:
                        stmt.setInt(7, 1);
                        break;
                    case inspector:
                        stmt.setInt(8, 1);
                        break;
                    case estimator:
                        stmt.setInt(9, 1);
                        break;
                    case executionLeader:
                        stmt.setInt(10, 1);
                        break;
                    case examiner:
                        stmt.setInt(11, 1);
                        break;
                    case changeControlCommitteeChairman:
                        stmt.setInt(12, 1);
                        break;
                    case changeControlCommitteeMember:
                        stmt.setInt(13, 1);
                        break;
                }
            }
        }
        stmt.execute(); // insert new row to requirement table
        stmt.close();
    }

    
    /** updates all user fields in the DB
    *
    * @param user ?
    * 
    */
   public void updateAllUserFields(User user) {
       PreparedStatement updStatus;
       try {
           updStatus = queryHandler.getmysqlConn().getConn().prepareStatement(
           		" UPDATE icm.user SET"
                           + " userName = ?,"
                           + "password = ?,"
                           + "firstName = ?,"
                           + "lastName = ?,"
                           + "job = ?,"
                           + "email = ?,"
                           + "informationTechnologiesDepartmentMangerPermission = ?,"
                           + "inspectorPermission = ?,"
                           + "estimatorPermission = ?,"
                           + "executionLeaderPermission = ?,"
                           + "examinerPermission = ?,"
                           + "changeControlCommitteeChairman = ?,"
                           + "changeControlCommitteeMember = ? "
                           + "WHERE userName = ?;");
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
           PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement("SELECT * FROM icm.user where user.userName = ?;");
           stmt.setNString(1, username);

           ResultSet re = stmt.executeQuery();
           while (re.next()) {
           	toReturn= getUserQuery(re);
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
   private User getUserQuery(ResultSet re) {
   	 User toReturn;
        String userName = null;
        String password = null;
        String firstName = null;
        String lastName = null;
        String jobString = null;
        String email = null;
        int informationTechnologiesDepartmentManagerPermission = 0;
        int inspectorPermission = 0;
        int estimatorPermission = 0;
        int executionLeaderPermission = 0;
        int examinerPermission = 0;
        int changeControlCommitteeChairman = 0;
        int changeControlCommitteeMember =0;
        try {
            userName = re.getNString(1);
            password = re.getNString(2);
            firstName = re.getNString(3);
            lastName = re.getNString(4);
            jobString = re.getNString(5);
            email = re.getNString(6);
            informationTechnologiesDepartmentManagerPermission = re.getInt(7);
            inspectorPermission = re.getInt(8);
            estimatorPermission = re.getInt(9);
            executionLeaderPermission = re.getInt(10);
            examinerPermission = re.getInt(11);
            changeControlCommitteeChairman = re.getInt(12);
            changeControlCommitteeMember = re.getInt(13);
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // converting collegeStatus to enum
    EnumSet<collegeStatus> collegeJobs = EnumSet.allOf(collegeStatus.class);
    User.collegeStatus collegeStatus = null;
    for (collegeStatus e : collegeJobs) {
        if (jobString != null && jobString.equals(e.name())) {
            collegeStatus = e;
        }
    }
    // converting to permissions set
    EnumSet<User.icmPermission> all = EnumSet.allOf(icmPermission.class);
    EnumSet<icmPermission> Permissions = EnumSet.complementOf(all);
    if (informationTechnologiesDepartmentManagerPermission == 1) {
        Permissions.add(User.icmPermission.informationTechnologiesDepartmentManager);
    }
    if (inspectorPermission == 1) {
        Permissions.add(User.icmPermission.inspector);
    }
    if (estimatorPermission == 1) {
        Permissions.add(User.icmPermission.estimator);
    }
    if (executionLeaderPermission == 1) {
        Permissions.add(User.icmPermission.executionLeader);
    }
    if (examinerPermission == 1) {
        Permissions.add(User.icmPermission.examiner);
    }
    if (changeControlCommitteeChairman == 1) {
        Permissions.add(User.icmPermission.changeControlCommitteeChairman);
    }
    if (changeControlCommitteeMember == 1) {
        Permissions.add(icmPermission.changeControlCommitteeMember);
    }
    toReturn = new User(userName, password, firstName, lastName, email, collegeStatus, Permissions);
    return toReturn;

    } // end of userStamentGets

   /** get all the users in the DB
    * @return ?
    */
   public ArrayList<User> getAllUsers(){
   	ArrayList<User> toReturn = new ArrayList<>();
       Statement stmt;
       ResultSet re;
       try {
           stmt = queryHandler.getmysqlConn().getConn().createStatement();
           re = stmt.executeQuery("SELECT * FROM icm.user;");

           while (re.next()) {
               toReturn.add(getUserQuery(re));
           }
           stmt.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return toReturn;
   } // END of getAllUsers
   
   /**  getting all users with the specified collegeStatus
    * @param collegeStatus ?
    * @return ?
    */
   public ArrayList<User> getAllUsersByJob(collegeStatus collegeStatus){
   	ArrayList<User> toReturn = new ArrayList<>();
   	 try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement("SELECT * FROM icm.user where job = ?;");
            stmt.setNString(1, collegeStatus.name());
            ResultSet re = stmt.executeQuery();
           while (re.next()) {

                toReturn.add(getUserQuery(re));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return toReturn;
   } // END of getAllUsersByJob
   
   /** Getting all users with the  specified ICMPermission
    * @param prem ?
    * @return ?
    */
   public ArrayList<User> getAllUsersWithICMPermissions(User.icmPermission prem) {
       ArrayList<User> toReturn = new ArrayList<>();
       Statement stmt;
       ResultSet re = null;
       try {
           stmt = queryHandler.getmysqlConn().getConn().createStatement();

           switch (prem) {
               case informationTechnologiesDepartmentManager:
                   re = stmt.executeQuery("SELECT * FROM icm.user where informationTechnologiesDepartmentMangerPermission = 1;");
                   break;
               case inspector:
                   re = stmt.executeQuery("SELECT * FROM icm.user where inspectorPermission = 1;");
                   break;
               case estimator:
                   re = stmt.executeQuery("SELECT * FROM icm.user where estimatorPermission = 1;");
                   break;
               case executionLeader:
                   re = stmt.executeQuery("SELECT * FROM icm.user where executionLeaderPermission = 1;");
                   break;
               case examiner:
                   re = stmt.executeQuery("SELECT * FROM icm.user where examinerPermission = 1;");
                   break;
               case changeControlCommitteeChairman:
                   re = stmt.executeQuery("SELECT * FROM icm.user where changeControlCommitteeChairman = 1;");
                   break;
               case changeControlCommitteeMember:
                   re = stmt.executeQuery("SELECT * FROM icm.user where changeControlCommitteeMember = 1;");
           }
           while (re.next()) {
               toReturn.add(getUserQuery(re));
           }
           stmt.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }

       return toReturn;
   } // END of getAllUsersWithRule
    
    
    
    
}//END of setAllUserFieldsStatement()

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;

import Entity.User.ICMPermissions;
import Entity.User.Job;

import static Entity.Requirement.*;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.Requirement;
import Entity.User;
import Entity.ProcessStage;
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
    public void insertUser(User user) { // send the use details.
        try {
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
            stmt.setNString(1, user.getUserName());
            stmt.setNString(2, user.getPassword());
            stmt.setNString(3, user.getFirstName());
            stmt.setNString(4, user.getLastName());
            if( user.getLoggedIn())  stmt.setInt(5, 1);
            else stmt.setInt(5, 0);
            stmt.setNString(6, user.getJob().name());
            stmt.setNString(7, user.getEmail());  
            stmt.setInt(8, 0);
            stmt.setInt(9, 0);
            stmt.setInt(10, 0);
            stmt.setInt(11, 0);
            stmt.setInt(12, 0);
            stmt.setInt(13, 0);
            EnumSet<ICMPermissions> Permissions =user.getICMPermissions();
           if(Permissions!=null) {
            for(ICMPermissions e: Permissions) {
            	switch(e) {
            	case informationTechnologiesDepartmentManger:
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //END of insertUser()
    
    public void insertInitiator(Initiator initiator) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO `icm`.`initiator`\n" + 
            		"(`requestID`,\n" + 
            		"`userName`)\n" + 
            		"VALUES\n" + 
            		"(?,\n" + 
            		"?);\n" + 
            		"");
            stmt.setNString(1, initiator.getrequest().getRequestID());  
            stmt.setNString(2, initiator.getTheInitiator().getUserName());  

            stmt.execute(); // insert new row to requirement table
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    }// END insertInitiator()
    
    
    public void InsertProcessStage(ProcessStage newStag) {
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO `icm`.`stage`\n" + 
            		"(`requestID`,\n" + 									//[1]
            		"`currentStage`,\n" + 									//[2]
            		"`StageSupervisor`,\n" + 								//[3]
            		"`EstimatorReport`,\n" + 								//[4]
            		"`ExeminorFailReport`,\n" + 							//[5]
            		"`inspectorDocumention`,\n" + 							//[6]
            		"`meaningEvaluationStartDate`,\n" + 					//[7]
            		"`meaningEvaluationDueDate`,\n" + 						//[8]
            		"`meaningEvaluationEndDate`,\n" +          				//[9]
            		"`examinationAndDecisionStartDate`,\n" + 				//[10]
            		"`stagecolexaminationAndDecisionDueDate`,\n" + 			//[11]
            		"`examinationAndDecisionEndDate`,\n" + 					//[12]
            		"`ExecutionStartDate`,\n" +								//[13] 
            		"`ExecutionDueDate`,\n" + 								//[14]
            		"`ExecutionEndtDate`,\n" + 								//[15]
            		"`examinationStartDate`,\n" + 							//[16]
            		"`examinationDueDate`,\n" + 							//[17]
            		"`examinationEndDate`,\n" + 							//[18]
            		"`closureStarDate`,\n" + 								//[19]
            		"`closureEndDate`)\n" + 								//[20]
            		"VALUES\n" + 											
            		"(?,\n" + 								
            		"?,\n" + 								
            		"?,\n" + 							
            		"?,\n" + 							
            		"?,\n" + 						
            		"?,\n" + 						
            		"?,\n" + 							
            		"?,\n" + 					
            		"?,\n" + 					
            		"?,\n" + 			
            		"?,\n" + 		
            		"?,\n" + 				
            		"?,\n" + 						
            		"?,\n" + 							
            		"?,\n" + 							
            		"?,\n" + 						
            		"?,\n" + 						
            		"?,\n" + 						
            		"?,\n" + 							
            		"?);\n" + 							//[41]
            		"");								
            stmt.setNString(1, newStag.getRequest().getRequestID());  
            stmt.setNString(2, newStag.getCurrentStage().name()); 
            if (newStag.getStageSupervisor()== null)stmt.setNString(3, null);  
            else stmt.setNString(3, newStag.getStageSupervisor().getUserName());  
            stmt.setNString(4, newStag.getEstimatorReport());  
            stmt.setNString(5, newStag.getExeminorFailReport());  
            stmt.setNString(6, newStag.getInspectorDocumention()); 
            LocalDate [][] date=newStag.getDates();
            int u =7;
            for(int i =0 ;i<4; i++)
            {
            	for (int j=0 ;j<3;j++) {
            		if( date[i][j]==null)stmt.setNString(u, null);
            		else stmt.setNString(u, date[i][j].toString());
            		u++;
            	}
            }
            if( date[4][0]==null) stmt.setNString(19, null);
            else stmt.setNString(19, date[4][0].toString());
            if( date[4][2]==null) stmt.setNString(20, null);
            else stmt.setNString(20, date[4][2].toString());         
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
            ResultSet re = numTest.executeQuery("SELECT `requestID` FROM `icm`.`changerequest`;\n" + 
            		"");// get all numbers submissions.
            while (re.next()) { // generate number for submission.
                count++;
            }
        } catch (SQLException e) {
            System.out.println("Database is empty, or no schema for ICM - InsertChangeRequest");
            count = 0;
        }
        count ++;
    	try {
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO `icm`.`changerequest`\n" + 
            		"(`requestID`,\n" + 
            		"`startDate`,\n" + 
            		"`system`,\n" + 
            		"`problomeDescription`,\n" + 
            		"`whyChange`,\n"+
            		"`comment`,\n" + 
            		"`status`)\n" + 
            		"VALUES\n" + 
            		"(?,\n" + 
            		"?,\n" + 
            		"?,\n"+
            		"?,\n" + 
            		"?,\n" + 
            		"?,\n" + 
            		"?);\n" + 
            		"");

            stmt.setNString(1, String.valueOf(count));  
            stmt.setNString(2, newRequest.getStarDate().toString());
            stmt.setNString(3,newRequest.getSystem() );  
            stmt.setNString(4,newRequest.getProblomeDescription() );  
            stmt.setNString(5,newRequest.getWhyChange());  
            stmt.setNString(6,newRequest.getComment()); 
            stmt.setNString(7,newRequest.getStatus().name());     
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
    public void updateAllUserFileds(User user) throws IllegalArgumentException {
        PreparedStatement updStatus;
        try {
            updStatus = mysqlConn.getConn().prepareStatement(
                    "UPDATE `icm`.`user`\n" + 
                    "SET\n" + 
                    "`userName` = ?,\n" + 
                    "`password` = ?,\n" + 
                    "`firstName` = ?,\n" + 
                    "`lastName` = ?,\n" + 
                    "`login` = ?,\n" + 
                    "`job` = ?,\n" + 
                    "`email` = ?,\n" + 
                    "`informationTecnologiesDeparmentMangerPermission` = ?,\n" + 
                    "`inspectorPermission` = ?,\n" + 
                    "`estimatorPermission` = ?,\n" + 
                    "`exeutionLeaderPermission` = ?,\n" + 
                    "`examinerPermission` = ?,\n" + 
                    "`changeControlCommitteeChairmant` = ?\n" + 
                    "WHERE `userName` = ?;\n" + 
                    "");
            updStatus.setNString(14, user.getUserName());
            updStatus.setNString(1, user.getUserName());
            updStatus.setNString(2, user.getPassword());
            updStatus.setNString(3, user.getFirstName());
            updStatus.setNString(4, user.getLastName());
            if( user.getLoggedIn())  updStatus.setInt(5, 1);
            else updStatus.setInt(5, 0);
            updStatus.setNString(6, user.getJob().name());
            updStatus.setNString(7, user.getEmail());  
            updStatus.setInt(8, 0);
            updStatus.setInt(9, 0);
            updStatus.setInt(10, 0);
            updStatus.setInt(11, 0);
            updStatus.setInt(12, 0);
            updStatus.setInt(13, 0);
            EnumSet<ICMPermissions> Permissions =user.getICMPermissions();
           if(Permissions!=null) {
            for(ICMPermissions e: Permissions) {
            	switch(e) {
            	case informationTechnologiesDepartmentManger:
            		updStatus.setInt(8, 1);
            		break;
            	case inspector:
            		updStatus.setInt(9, 1);
            		break;
            	case estimator:
            		updStatus.setInt(10, 1);
            		break;
            	case executionLeader:
            		updStatus.setInt(11, 1);
            		break;
            	case examiner:
            		updStatus.setInt(12, 1);
            		break;
            	case changeControlCommitteeChairman:
            		updStatus.setInt(13, 1);
            	break;
            	}
            }
           }
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
   EnumSet<ICMPermissions> all =EnumSet.allOf(User.ICMPermissions.class);
   EnumSet<ICMPermissions> Permissions =EnumSet.complementOf(all);
     if(informationTecnologiesDeparmentMangerPermission==1)  Permissions.add(ICMPermissions.informationTechnologiesDepartmentManger);
     if(inspectorPermission==1)  Permissions.add(ICMPermissions.inspector);
     if(estimatorPermission==1)  Permissions.add(ICMPermissions.estimator);
     if(exeutionLeaderPermission==1)  Permissions.add(ICMPermissions.executionLeader);
     if(examinerPermission==1)  Permissions.add(ICMPermissions.examiner);
     if(changeControlCommitteeChairmant==1)  Permissions.add(ICMPermissions.changeControlCommitteeChairman); 
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

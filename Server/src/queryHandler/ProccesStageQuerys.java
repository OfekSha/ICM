package queryHandler;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import Entity.ChangeRequest;
import Entity.ProcessStage;
import Entity.User;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import theServer.mysqlConnection;

/**
 * concentrates  all querys  for the table stage
 *
 */
public class ProccesStageQuerys {
	
	 private final QueryHandler queryHandler;
		
	 public ProccesStageQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }

	
	/** Inserting a ProcessStage in to DB
     * @param processStage ?
     */
    public void InsertProcessStage(ChangeRequest changeRequest, ProcessStage processStage) {
        try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "INSERT INTO icm.stage " +
                    "(RequestID," +                             //[1]
                    "currentStage," +                           //[2]
                    "StageSupervisor," +                        //[3]
                    "EstimatorReport," +                        //[4]
                    "ExaminerFailReport," +                     //[5]
                    "inspectorDocumentation," +                 //[6]
                    "meaningEvaluationStartDate," +             //[7]
                    "meaningEvaluationDueDate," +               //[8]
                    "meaningEvaluationEndDate," +               //[9]
                    "examinationAndDecisionStartDate," +        //[10]
                    "stageColExaminationAndDecisionDueDate," +  //[11]
                    "examinationAndDecisionEndDate," +          //[12]
                    "executionStartDate," +                     //[13]
                    "executionDueDate," +                       //[14]
                    "executionEndDate," +                       //[15]
                    "examinationStartDate," +                   //[16]
                    "examinationDueDate," +                     //[17]
                    "examinationEndDate," +                     //[18]
                    "closureStarDate," +                        //[19]
                    "closureEndDate," +                         //[20]
                    "stage1extension," +                        //[21]
                    "stage2extension," +                        //[22]
                    "stage3extension," +                        //[23]
                    "stage4extension," +                        //[24]
                    "stage5extension," +                        //[25]
                    "currentSubStage," +                        //[26]
                    "stage1ExtensionExplanation, " +//27
					"stage2ExtensionExplanation, " +//28
					"stage3ExtensionExplanation, " +//29
					"stage4ExtensionExplanation, " +//30
					"stage5ExtensionExplanation) " +//31
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            setAllProcessStageStatement(changeRequest, processStage , stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //END of InsertProcessStage();
    
    
    /**sets up all of the ProcessStage  fields in to a Prepared Statement
     * 
     * ,mainly used to support insets and updates
     * 
     * @param processStage ?
     * @param stmt ?
     * @throws SQLException ?
     */
    private void setAllProcessStageStatement(ChangeRequest changeRequest, ProcessStage processStage,
                                             PreparedStatement stmt) throws SQLException {
        stmt.setNString(1, changeRequest.getRequestID());
        stmt.setNString(2, processStage.getCurrentStage().name());
        if (processStage.getStageSupervisor() == null) {
            stmt.setNString(3, null);
        } else {
            stmt.setNString(3, processStage.getStageSupervisor().getUserName());
        }
        stmt.setNString(4, processStage.getEstimatorReport());
        stmt.setNString(5, processStage.getExaminerFailReport());
        stmt.setNString(6, processStage.getInspectorDocumentation());
        LocalDate[][] date = processStage.getDates();
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
        int[] bool = processStage.getWasThereAnExtensionRequest();
        int v = 21;
        for (int j = 0; j < 5; j++) {
            if (bool[j] == 2) {
                stmt.setInt(v, 2);
            } 
            if (bool[j] == 1){
            	stmt.setInt(v, 1);
            } else {
                stmt.setInt(v, 0);
            }
            v++;
        }
        stmt.setString(26, processStage.getCurrentSubStage().name());
        String[] s =processStage.getAllExtensionExplanation();
        
        stmt.setString(27, s[0]);
        stmt.setString(28, s[1]);
        stmt.setString(29, s[2]);
        stmt.setString(30, s[3]);
        stmt.setString(31, s[4]);
        
        
        stmt.execute();
        stmt.close();
    }//END setAllProcessStageStatement()

    /** Updateds all existing ProcessStage fields
     * @param processStage ?
    */
    public void updateAllProcessStageFields( ProcessStage processStage) {
        try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "UPDATE `icm`.`stage`\n" + 
                    "SET\n" + 
                    "`RequestID` = ?,\n" + 
                    "`currentStage` = ?,\n" + 
                    "`StageSupervisor` = ?,\n" + 
                    "`EstimatorReport` = ?,\n" + 
                    "`ExaminerFailReport` = ?,\n" + 
                    "`inspectorDocumentation` = ?,\n" + 
                    "`meaningEvaluationStartDate` = ?,\n" + 
                    "`meaningEvaluationDueDate` = ?,\n" + 
                    "`meaningEvaluationEndDate` = ?,\n" + 
                    "`examinationAndDecisionStartDate` = ?,\n" + 
                    "`stageColExaminationAndDecisionDueDate` = ?,\n" + 
                    "`examinationAndDecisionEndDate` = ?,\n" + 
                    "`executionStartDate` = ?,\n" + 
                    "`executionDueDate` = ?,\n" + 
                    "`executionEndDate` = ?,\n" + 
                    "`examinationStartDate` = ?,\n" + 
                    "`examinationDueDate` = ?,\n" + 
                    "`examinationEndDate` = ?,\n" + 
                    "`closureStarDate` = ?,\n" + 
                    "`closureEndDate` = ?,\n" + 
                    "`stage1extension` = ?,\n" + 
                    "`stage2extension` = ?,\n" + 
                    "`stage3extension` = ?,\n" + 
                    "`stage4extension` = ?,\n" + 
                    "`stage5extension` = ?,\n" + 
                    "`currentSubStage` = ?,\n" + 
                    "`stage1ExtensionExplanation` = ?,\n" + 
                    "`stage2ExtensionExplanation` = ?,\n" + 
                    "`stage3ExtensionExplanation` = ?,\n" + 
                    "`stage4ExtensionExplanation` = ?,\n" + 
                    "`stage5ExtensionExplanation` = ?\n" + 
                    "WHERE `RequestID` = ? AND `currentStage` = ?;\n" + 
                    ""); //32 33
            stmt.setNString(32, processStage.getRequest().getRequestID());
            stmt.setNString(33, processStage.getCurrentStage().name());
            setAllProcessStageStatement(processStage.getRequest(), processStage, stmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }// END updateAllProcessStageStatement
    }
    
    /** gets processStage without the change request
	 * 	its purpose is to support getAllChangeRequest() 
	 * @param RequestID ?
	 * @return ?
	 */
	public ProcessStage getProcessStage(String RequestID) {
		ProcessStage returnProcessStage = null;
		try {
			PreparedStatement stmt = queryHandler.getmysqlConn().getConn()
					.prepareStatement("SELECT * FROM icm.stage where stage.RequestID = ?;");
			stmt.setNString(1, RequestID);
			ResultSet re = stmt.executeQuery();
			while (re.next()) {
				String currentStageString = re.getNString(2);
                ChargeRequestStages currentStage = ChargeRequestStages.valueOf(currentStageString);

				
                User StageSupervisor = queryHandler.getUserQuerys().selectUser(re.getString(3));
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

				int[] WasThereAnExtensionRequest = new int[5];
				u = 21;
				for (int i = 0; i < 5; i++) {
                    WasThereAnExtensionRequest[i] = re.getInt(u) ;
					u++;
				}
				String currentSubStageString = re.getString(26);
                subStages currentSubStage = subStages.valueOf(currentSubStageString);
                String[] s =new String[5];
                s[0] =re.getNString(27);
                s[1] =re.getNString(28);
                s[2] =re.getNString(29);
                s[3] =re.getNString(30);
                s[4] =re.getNString(31);

				returnProcessStage = new ProcessStage(currentStage, currentSubStage,
                        StageSupervisor, EstimatorReport, ExaminerFailReport,
                        inspectorDocumentation, startEndArray, WasThereAnExtensionRequest,s);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnProcessStage;
	}// END getProcessStage()
}// END of ProccesStageQuerys()

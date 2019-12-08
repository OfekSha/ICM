import Entity.Requirement;

import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Entity.Requirement.*;

public class QueryHandler {
    private mysqlConnection mysqlConn;

    public QueryHandler() {
        mysqlConn = new mysqlConnection();
    }

    /**
     * for prototype.
     * insert new requirement in icm.requirement.
     * @param reqInitiator Initiator of request
     * @param currentSituationDetails details of current situation
     * @param requestDetails details of request
     * @param stageSupervisor Supervisor of request
     */

    public void insertRequirment(String reqInitiator, String currentSituationDetails, String requestDetails, String stageSupervisor,statusOptions status) { // send the use details.
        int count = 0;
        try {
            Statement numTest = mysqlConn.getConn().createStatement();
            try {
            ResultSet re = numTest.executeQuery("SELECT count(*) FROM icm.requirement;");// get all numbers submissions.
            numTest.close();
            while (re.next()) { // generate number for submission.
                count = re.getInt(1) + 1;
            }
            }catch(SQLException e){
            	e.printStackTrace();
            	System.out.println("empty list");
            	count=0;
            }
            PreparedStatement stmt = mysqlConn.getConn().prepareStatement("INSERT INTO icm.requirement " +
                    "(Initiator, " +
                    "RequestID, " +
                    "CurrentSituationDetails, " +
                    "RequestDetails, " +
                    "StageSupervisor, " +
                    "Status) " +
                    "VALUES(?, ?, ?, ?, ?,?) ; ");
            stmt.setNString(1, reqInitiator);
            stmt.setInt(2, count);
            stmt.setNString(3, currentSituationDetails);
            stmt.setNString(4, requestDetails);
            stmt.setNString(5, stageSupervisor);
            stmt.setNString(6, status.name());
            stmt.execute(); // insert new row to requirement table.
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

    }

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
      //  int ordinalStatus = statusOptions.valueOf(status).ordinal() + 1;
       // if (ordinalStatus < 1 || ordinalStatus > 3) throw new IllegalArgumentException();
        try {
            updStatus = mysqlConn.getConn().prepareStatement(
                    "UPDATE `icm`.`requirement` "
                    + "SET `Status` = ? " +
                            "WHERE `RequestID` = ?;");
           // updStatus.setInt(1, ordinalStatus);
            updStatus.setNString(1, status);
            updStatus.setInt(2, id);
            updStatus.execute();
            updStatus.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
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
                /*toReturn[0] = re.getNString(1);
                toReturn[1] = re.getInt(2) + "";
                toReturn[2] = re.getNString(3);
                toReturn[3] = re.getNString(4);
                toReturn[4] = re.getNString(5);
                toReturn[5] = re.getNString(6);*/
                /*toReturn.add(re.getNString(1));
                toReturn.add(re.getInt(2));
                toReturn.add(re.getNString(3));
                toReturn.add(re.getNString(4));
                toReturn.add(re.getNString(5));
                toReturn.add(re.getNString(6));*/
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
     * send it to server as ArrayList of array of strings.
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

import java.sql.*;
import java.util.ArrayList;

public class QueryHandler {
    private mysqlConnection mysqlConn;

    public QueryHandler() {
        mysqlConn = new mysqlConnection();
    }

    /**
     * for prototype.
     * insert new requirement in requirement.
     * @param reqInitiator
     * @param currentSituationDetails
     * @param requestDetails
     * @param stageSupervisor
     */

    public void insertRequirment(String reqInitiator, String currentSituationDetails, String requestDetails, String stageSupervisor) { // send the use details.
        int count = 0;
        try {
            Statement numTest = mysqlConnection.conn.createStatement();
            ResultSet re = numTest.executeQuery("SELECT count(*) FROM icm.requirement;");// get all numbers submissions.
            numTest.close();
            while (re.next()) { // generate number for submission.
                count = re.getInt(1) + 1;
            }
            PreparedStatement stmt = mysqlConnection.conn.prepareStatement("INSERT INTO icm.requirement " +
                    "(Initiator," +
                    "RequestID," +
                    "CurrentSituationDetails," +
                    "RequestDetails," +
                    "StageSupervisor)" +
                    "VALUES(?, ?, ?, ?, ?);");
            stmt.setNString(1, reqInitiator);
            stmt.setInt(2, count);
            stmt.setNString(3, currentSituationDetails);
            stmt.setNString(4, requestDetails);
            stmt.setNString(5, stageSupervisor);
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
     * @param id
     * @param status
     * @return ArrayList<Object> or null
     */
    public ArrayList<Object> updateStatus(int id, int status) {
        PreparedStatement UpdateStmnt;
        try {
            UpdateStmnt = mysqlConnection.conn.prepareStatement(
                    "UPDATE icm.requirement " +
                            "SET Status = ? " +
                            "WHERE RequestID = ?");
            UpdateStmnt.setInt(1, status);
            UpdateStmnt.setInt(2, id);
            UpdateStmnt.execute();
            UpdateStmnt.close();
            return selectRequirement(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Object> selectRequirement(int reqID) {
        ArrayList<Object> toReturn = new ArrayList<>();
        try {
            PreparedStatement stmt = mysqlConnection.conn.prepareStatement("SELECT * FROM icm.requirement WHERE RequestID = ?");
            stmt.setInt(1, reqID);
            ResultSet re = stmt.executeQuery();
            stmt.close();
            while (re.next()) {
                toReturn.add(re.getNString(1));
                toReturn.add(re.getInt(2));
                toReturn.add(re.getNString(3));
                toReturn.add(re.getNString(4));
                toReturn.add(re.getNString(5));
                toReturn.add(re.getNString(6));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }


    public ArrayList<Object[]> selectAll() {
        ArrayList<Object[]> toReturn = new ArrayList<>();
        Object[] toPut;
        try {
            Statement stmt = mysqlConnection.conn.createStatement();
            ResultSet re = stmt.executeQuery("SELECT * FROM icm.requirement;");
            stmt.close();
            while (re.next()) {
                toPut = new Object[6];
                for (int i = 0; i < 6; i++) {
                    toPut[i] = re.getObject(i + 1);
                }
                toReturn.add(new Object[6]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}

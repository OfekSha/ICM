package queryHandler;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Entity.Document;
import theServer.mysqlConnection;
import theServer.*;;
/**
 * concentrates  all querys  for the table docs
 *
 */
public class FilesQuerys {
	 private final QueryHandler queryHandler;
	
	 public FilesQuerys( QueryHandler queryHandler) {
	        this.queryHandler = queryHandler;
	    }
	public  void InsertFile(  ArrayList<Document> uploadedDocs) {
    	int count=0;
    	try {
    	Statement numTest = queryHandler.getmysqlConn().getConn().createStatement();
        ResultSet re = numTest.executeQuery("SELECT FileID FROM icm.docs");// get all numbers submissions.
        while (re.next()) { // generate number for submission.
            count++;
        }
    	}catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		}
    	
    	for(Document each : uploadedDocs) {
			 try {
				 count++; 
			PreparedStatement stmt = queryHandler.getmysqlConn().getConn()
					.prepareStatement("INSERT INTO `icm`.`docs`\n" + 
							"(`FileID`,\n" + 
							"`RequestID`,\n" + 
							"`uploadedFile`)\n" + 
							"VALUES\n" + 
							"(?,\n" + 
							"?,\n" + 
							"?);\n" + 
							"");
			setFile(stmt,each,Integer.toString(count));
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
    	}
		
    }//END of updateFile
    
	public void setFile(PreparedStatement stmt, Document doc, String ID) throws SQLException {
		InputStream is = new ByteArrayInputStream(doc.getByteArr());
		stmt.setNString(1,ID );
		stmt.setNString(2, doc.getChangeRequestID());
		stmt.setBlob(3, is);
		stmt.executeUpdate();
	}// end of setFile()
    
}

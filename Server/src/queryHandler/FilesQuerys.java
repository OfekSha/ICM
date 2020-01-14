package queryHandler;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Entity.Document;
import Entity.InspectorUpdateDescription;
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
							"`fileName`,\n" +
							"`uploadedFile`,size)\n" + 
							"VALUES\n" + 
							"(?,\n" + 
							"?,?,?,\n" + 
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
		stmt.setInt(2, doc.getChangeRequestID());
		stmt.setNString(3, doc.getFileName());
		stmt.setBlob(4, is);
		stmt.setInt(5, doc.getSize());

		stmt.executeUpdate();
	}// end of setFile()
	
	public ArrayList<Document> selectDocWithotFile(int RequestID ) {
		ArrayList<Document> toReturn = new ArrayList<>();
    	try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "SELECT `docs`.`FileID`,\r\n" + 
                    "    `docs`.`fileName`,`docs`.`size`\r\n" + 
                    "FROM `icm`.`docs`\r\n" + 
                    " WHERE RequestID = ? ;\r\n" + 
                    "");
            stmt.setInt(1,RequestID);
            ResultSet re =  stmt.executeQuery(); 
            
            
            while (re.next()) {
            	Document doc = new Document(re.getNString(2));
            	doc.setFileID(re.getString(1));
            	doc.setSize(re.getInt(3));
            	toReturn.add(doc);
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return toReturn;
	}//END selectDocWithotFile
	
	
	/**
	 * @param doc - the document withc file we want
	 * @return
	 */
	public Document selectDocWithFile (Document doc) {
		try {
            PreparedStatement stmt = queryHandler.getmysqlConn().getConn().prepareStatement(
                    "SELECT `docs`.`uploadedFile`,`docs`.`size` FROM `icm`.`docs`\r\n" + 
                    " WHERE FileID = ? ;\r\n" + 
                    "");
            stmt.setNString(1,doc.getFileID());
            ResultSet re =  stmt.executeQuery(); 
            
            
            while (re.next()) {
            	Blob blob = re.getBlob(1);     	
            	byte[] blobAsBytes = blob.getBytes(1, doc.getSize());
            	doc.setSize(blobAsBytes.length);
            	doc.setMybytearray(blobAsBytes);
            	//release the blob and free up memory. 
            	blob.free();
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return doc ;
	}// END selectDocWithFile
    
}

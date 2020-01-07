package Controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import Controllers.InspectorController.requirmentForTable;
import Entity.ChangeRequest;
import Entity.Document;
import Entity.Initiator;
import Entity.ProcessStage;
import Entity.User;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.clientRequestFromServer.requestOptions;
import GUI.SubmitRequestForm;
import WindowApp.ClientLauncher;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SubmitRequestController {
	
	/** all of the users uploaded docs for  a request
	 * 
	 */
	 private ArrayList<Document> uploadedDocs =new ArrayList<>();
	
	/** sends the change request to the server <p>
	 * 
	 * @param requestDetails
	 * @param requestreason
	 * @param requestComment
	 * @param sys
	 * @param user
	 * @return - true if the request was sent
	 */
	public boolean getSubmition(String requestDetails, String requestreason, String requestComment, String sys,
			User user) {
		if (requestDetails.equals("") || requestreason.equals("") || sys.equals("")) {
			return false;
		} else {
			Initiator init = new Initiator(user, null);
			LocalDate start = LocalDate.now();
			// TODO: add doc
			ChangeRequest change = new ChangeRequest(init, start, sys, requestDetails, requestreason, requestComment,
					uploadedDocs);
			Object msg = new clientRequestFromServer(requestOptions.addRequest, change);//
			ClientLauncher.client.handleMessageFromClientUI(msg);
			// cleaning 
			uploadedDocs =new ArrayList<>();
			return true;
		}
		
	}// END of getSubmition()
	


	/** appends the empty Strings to one  
	 * @param requestDetails
	 * @param requestreasonString
	 * @param sys
	 * @return
	 */
	public String AppendEmpty(String requestDetails, String requestreasonString,String sys) {
		String appended ="";
		if(requestDetails.equals("")) appended=appended+"\nChange request details ";
		if(requestreasonString.equals("")) appended=appended+"\nChange request reason ";
		if(sys.equals("")) appended=appended+"\nInformation System ";
		return appended;
	}
	
	/** adding the file to the array list if under 16mb
	 * @param newFile - the file to be added 
	 * @return true when the file has been attached 
	 * @throws IOException
	 */
	public boolean AddThefile(File newFile) throws IOException {
		
		Document doc = new Document(newFile.getName());	
	      byte [] mybytearray  = new byte [(int)newFile.length()];
	      if( 16777215>mybytearray.length) {
	      FileInputStream fis = new FileInputStream(newFile);
	      BufferedInputStream bis = new BufferedInputStream(fis);			    
	      doc.initArray(mybytearray.length);
	      doc.setSize(mybytearray.length); 
	      bis.read(doc.getMybytearray(),0,mybytearray.length);
	      uploadedDocs.add(doc);
	      return true;
	      }
	      return false;
	  
		
	}//ENF of AddThefile()
	
	
	/** class made for representing the change requests attached documents 
	 * 
	 *
	 */
	public static class DocumentForTable {
		private SimpleStringProperty name;
		private SimpleStringProperty size;
		private Document theDoc;
		public String getName() {
			return name.get();
		}

		public String getSize() {
			return size.get();
		}
		/**
		 * @return The document we are representing in the table raw
		 */
		public Document gettheDoc() {
			return theDoc;
		}


		public DocumentForTable(Document doc) {
			name  =new SimpleStringProperty(doc.getFileName());
			size =  new SimpleStringProperty( Double.toString((double)doc.getSize()/1E6));
			theDoc =doc;
		}

	}
	
	
	/** creates the list of documents witch are attached to the  change request for the table 
	 * @return  DocumentForTable array list for table
	 * @see DocumentForTable
	 */
	public  ArrayList<DocumentForTable> DocumentForTableList() {
		ArrayList<DocumentForTable> newList = new ArrayList<>();
		for (Document doc : uploadedDocs)
			newList.add(new DocumentForTable(doc));
		return newList;
	}
	
	/** removing the document from the requests documents 
	 * @param doc the document to be removed 
	 */
	public void removeDoc(Document doc) {
		uploadedDocs.remove(doc);
	}

	
}//END of  calss SubmitRequestController 

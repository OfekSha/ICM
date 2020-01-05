package Controllers;

import java.time.LocalDate;

import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.User;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import GUI.SubmitRequestForm;
import WindowApp.ClientLauncher;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SubmitRequestController {
	

	
	/** sends the change request to the server <p>
	 * 
	 * @param requestDetails
	 * @param requestreason
	 * @param requestComment
	 * @param sys
	 * @param user
	 * @return - true if the request was sent
	 */
	public  boolean getSubmition(String requestDetails, String requestreason,String requestComment,String sys ,User user) {	
		if (requestDetails.equals("") || requestreason.equals("") || sys.equals("")) {
			return false;
		} else {
			Initiator init = new Initiator(user, null);
			LocalDate start = LocalDate.now();
			//TODO: add doc
			ChangeRequest change = new ChangeRequest(init, start, sys, requestDetails, requestreason, requestComment, null);
			Object msg = new clientRequestFromServer(requestOptions.addRequest, change);
			ClientLauncher.client.handleMessageFromClientUI(msg);
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
	
}

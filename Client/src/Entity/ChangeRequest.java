package Entity;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;


/**
 * @author Yonathan
 * in proggress
 */
public class ChangeRequest implements Serializable {

	
	public enum ChangeRequestStatus { //
		ongoing,
		suspended,
		closed
	}
	
	
	
	private Initiator initiator;
	private  Date date;
	private  String system;
	private String problomeDescription;
	private String whyChange;
	private ChangeRequestStatus status;
	private Document doc;
	private Stage stage;
	
	public ChangeRequest(Initiator initiator, Date date,String problomeDescription,String whyChange ,Document doc) {
		
	}
	
	
	

	
	//moved to requirement.
	//private int dueTime;
	//private int closingDocumentation;
	//private int[] timeDeviations;
	//private requestStatus_enum status; 



}
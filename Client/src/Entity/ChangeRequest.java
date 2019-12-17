package Entity;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;


public class ChangeRequest {
	private class Initiator {
		private int id;
		private String name;
		private String lastName;
		//private userStatus_enum Status;
		private String Email;

	}

	public class Document {

		private int OwnerID;
		private String filename;
		private int FileID;
		File file;

	}
	private Initiator initiator;
	

	private  Date date;
	private int informationSystem;

	private String currentSituationDetails;

	private String expectedResult;

	private String explanation;

	private String notes;
	private ArrayList<Document> documents;
	private int requestID;
	
	//moved to requirement.
	//private int dueTime;
	//private int closingDocumentation;
	//private int[] timeDeviations;
	//private requestStatus_enum status; 



}
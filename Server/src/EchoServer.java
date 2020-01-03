import Entity.*;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User.ICMPermissions;
import Entity.User.Job;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the osf.server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	private QueryHandler queryHandler;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo osf.server.
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the ocf.client.
	 * @param msg    The message received from the ocf.client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		clientRequestFromServer request = (clientRequestFromServer) msg; // request from ocf.client
		System.out.println(LocalTime.now() + ": Message received [" + request.getName() + "] of\n" + request.getObject() + "\t" + " from " + client.getInetAddress());
		//ArrayList<Requirement> ReqListForClient = new ArrayList<>();
		Object sendBackObject = null;
		Object[] objectArray;
		Object[] returningObjectArray;
		boolean iWantResponse = true;
		try {
			//	Requirement reqReceived;
			switch (request.getRequest()) {
				case changeInLogIn:
					queryHandler.updateAllUserFields((User) request.getObject());
					break;

				case updateProcessStage: // change stage
					ChangeRequest cR = (ChangeRequest) request.getObject();
					ProcessStage pS = cR.getProcessStage();
					queryHandler.updateAllProcessStageFields(cR, pS);
					break;

				case updateChangeRequest:
					queryHandler.updateAllChangeRequestFields((ChangeRequest) request.getObject());
					break;

				case addRequest:
					ChangeRequest change = (ChangeRequest) request.getObject();
					change.setRequestID(queryHandler.InsertChangeRequest(change));
					change.updateInitiatorRequest();
					change.updateStage();
					queryHandler.insertInitiator(change.getInitiator());
					queryHandler.InsertProcessStage(change, change.getProcessStage());
					break;
			}
			if (request.getRequest().ordinal() < 4) iWantResponse = false;
			else {

				switch (request.getRequest()) {
					// read all ChangeRequest data
					case getAll:
						sendBackObject = queryHandler.getAllChangeRequest();
						break;

					case getUser:
						sendBackObject = queryHandler.selectUser(((String) request.getObject()));
						break;

					case updateUser:
						queryHandler.updateAllUserFields((User) request.getObject());
						break;

					case getAllUsers:
						sendBackObject = queryHandler.getAllUsers();
						break;
					case getChangeRequestByStatus:
						objectArray = new Object[2];
						objectArray[0] = queryHandler.getAllChangeRequestWithStatus((ChangeRequestStatus) request.getObject());
						objectArray[1] = request.getObject();
						sendBackObject = objectArray;
						break;
					case getUsersByICMPermissions:
						objectArray = new Object[2];
						objectArray[0] = queryHandler.getAllUsersWithICMPermissions((ICMPermissions) request.getObject());
						objectArray[1] = request.getObject();
						sendBackObject = objectArray;
						break;
					case getAllUsersByJob:
						objectArray = new Object[2];
						objectArray[0] = queryHandler.getAllUsersByJob((Job) request.getObject());
						objectArray[1] = request.getObject();
						sendBackObject = objectArray;
						break;
					case getAllChangeRequestWithStatusAndStage:
						objectArray = (Object[]) request.getObject();
						returningObjectArray = new Object[4];
						returningObjectArray[1] = objectArray[0];
						returningObjectArray[2] = objectArray[1];
						returningObjectArray[3] = objectArray[2];
						returningObjectArray[0] = queryHandler.getAllChangeRequestWithStatusAndStage(
								(ChargeRequestStages)objectArray[0],
								(subStages)objectArray[1],
								(ChangeRequestStatus)objectArray[2]);
						sendBackObject = returningObjectArray;
						break;
					case getAllChangeRequestWithStatusAndStageOnly:
						objectArray = (Object[]) request.getObject();
						returningObjectArray = new Object[3];
						returningObjectArray[1] = objectArray[0];
						returningObjectArray[2] = objectArray[1];
						returningObjectArray[0] = queryHandler.getAllChangeRequestWithStatusAndStageOnly(
								(ChargeRequestStages)objectArray[0],
								(ChangeRequestStatus)objectArray[1]);
						sendBackObject = returningObjectArray;
						break;
					case getAllChangeRequestWithStatusAndSubStageOnly:
						objectArray = (Object[]) request.getObject();
						returningObjectArray = new Object[3];
						returningObjectArray[1] = objectArray[0];
						returningObjectArray[2] = objectArray[1];
						returningObjectArray[0] = queryHandler.getAllChangeRequestWithStatusAndSubStageOnly(
								(subStages)objectArray[0],
								(ChangeRequestStatus)objectArray[1]);
						sendBackObject = returningObjectArray;
						break;
					case getAllChangeRequestWithStatusAndStageAndSupervisor:
						objectArray= (Object[]) request.getObject();
						returningObjectArray = new Object[5];
						returningObjectArray[1] = objectArray[0];
						returningObjectArray[2] = objectArray[1];
						returningObjectArray[3] = objectArray[2];
						returningObjectArray[4] = objectArray[3];
						returningObjectArray[0] = queryHandler.
								getAllChangeRequestWithStatusAndStageAndSupervisor(
										(ChargeRequestStages)objectArray[0],
										(subStages)objectArray[1],
										(ChangeRequestStatus)objectArray[2],
										(String)objectArray[3]);
						sendBackObject = returningObjectArray;
						break;
					default:
						throw new IllegalArgumentException("the request " + request + " not implemented in the osf.server.");
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		Object answer = new clientRequestFromServer(request.getRequest(), sendBackObject); // answer to the ocf.client
		try {
			if (iWantResponse) client.sendToClient(answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	private void selectRequirement(ArrayList<Requirement> reqListForClient, ChangeRequest reqReceived) {
//		String[] result;
//		result = queryHandler.selectRequirement(reqReceived.getRequestID());
//		reqListForClient.add(new Requirement(result));
//	}
//
//	private void getAllRequest(ArrayList<Requirement> reqListForClient) {
//		queryHandler.selectAll().forEach(arr -> reqListForClient.add(new Requirement(arr)));
//	}
	
	private void enterUsersToDB() {
		// creating admin
		EnumSet<ICMPermissions> Permissions = EnumSet.allOf(User.ICMPermissions.class);
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", Job.informationEngineer, Permissions, false);
		queryHandler.insertUser(newUser);
		// creating  information Technologies Department Manager
		EnumSet<ICMPermissions> lessPermissions = EnumSet.complementOf(Permissions); //empty enum set
		lessPermissions.add(User.ICMPermissions.informationTechnologiesDepartmentManager);
		newUser = new User("informationTechnologiesDepartmentManager", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating inspector
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.inspector);
		newUser = new User("inspector", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating estimator
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.estimator);
		newUser = new User("estimator", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating exeution Leader
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.executionLeader);
		newUser = new User("executionLeader", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating examiner
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.examiner);
		lessPermissions.add(User.ICMPermissions.changeControlCommitteeMember);
		newUser = new User("examiner", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating change Control Committee Chairman
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.changeControlCommitteeChairman);
		newUser = new User("changeControlCommitteeChairman", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating student
		newUser = new User("student", "1234", "FirstName", "LastName", "mail@email.com", Job.student, null, false);
		queryHandler.insertUser(newUser);
	}// END of  enterUsersToDB()

	private void enterChangeRequestToDB() {
		EnumSet<ICMPermissions> Permissions = EnumSet.allOf(User.ICMPermissions.class);
		EnumSet<ICMPermissions> lessPermissions; //empty enum set
		User newUser = new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", Job.informationEngineer, Permissions, false);
		Initiator initiator = new Initiator(newUser, null);
		LocalDate start = LocalDate.now();
		ChangeRequest changeRequest = new ChangeRequest(initiator, start, "TheSystem", "test", "test", "test", null);
		// change request at satge 1
		changeRequest.setRequestID(queryHandler.InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.insertInitiator(changeRequest.getInitiator());
		queryHandler.InsertProcessStage(changeRequest, changeRequest.getProcessStage());

		//creating change Control Committee Chairman
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.changeControlCommitteeChairman);
		newUser = new User("changeControlCommitteeChairman", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		initiator = new Initiator(newUser, null);
		// change request satge 2
		changeRequest = new ChangeRequest(initiator, start, "TheSystme", "test", "test", "test", null);
		changeRequest.setStatus(ChangeRequestStatus.suspended);
		LocalDate[][] startEndArray = new LocalDate[5][3];
		int[] WasThereAnExtensionRequest = new int[5];
		for (int i = 0; i < 5; i++) {
			WasThereAnExtensionRequest[i] = 0;
		}
		ProcessStage stager = new ProcessStage(ChargeRequestStages.examinationAndDecision, subStages.supervisorAction, newUser, "test2", "test2", "test2", startEndArray, WasThereAnExtensionRequest);
		changeRequest.setStage(stager);
		changeRequest.setRequestID(queryHandler.InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.InsertProcessStage(changeRequest, changeRequest.getProcessStage());
		//creating exeution Leader
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.executionLeader);
		newUser = new User("executionLeader", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		initiator = new Initiator(newUser, null);
		// change request stage 3
		changeRequest = new ChangeRequest(initiator, start, "TheSystme", "test", "test", "test", null);
		stager = new ProcessStage(ChargeRequestStages.execution, subStages.determiningDueTime, newUser, "test3", "test3", "test3", startEndArray, WasThereAnExtensionRequest);
		changeRequest.setStage(stager);
		changeRequest.setRequestID(queryHandler.InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.insertInitiator(changeRequest.getInitiator());
		queryHandler.InsertProcessStage(changeRequest, changeRequest.getProcessStage());
		//creating examiner
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.examiner);
		lessPermissions.add(User.ICMPermissions.changeControlCommitteeMember);
		newUser = new User("examiner", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		initiator = new Initiator(newUser, null);
		// change request stage 4
		changeRequest = new ChangeRequest(initiator, start, "TheSystme", "test", "test", "test", null);
		stager = new ProcessStage(ChargeRequestStages.examination, subStages.supervisorAction, newUser, "test4", "test4", "test4", startEndArray, WasThereAnExtensionRequest);
		changeRequest.setStage(stager);
		changeRequest.setStatus(ChangeRequestStatus.suspended);
		changeRequest.setRequestID(queryHandler.InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.insertInitiator(changeRequest.getInitiator());
		queryHandler.InsertProcessStage(changeRequest, changeRequest.getProcessStage());

		//creating inspector
		lessPermissions = EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.inspector);
		newUser = new User("inspector", "1234", "FirstName", "LastName", "mail@email.com", Job.informationEngineer, lessPermissions, false);
		initiator = new Initiator(newUser, null);
		// change request stage 5
		changeRequest = new ChangeRequest(initiator, start, "TheSystme", "test", "test", "test", null);
		stager = new ProcessStage(ChargeRequestStages.closure, subStages.supervisorAction, newUser, "test5", "test5", "test5", startEndArray, WasThereAnExtensionRequest);
		changeRequest.setStatus(ChangeRequestStatus.closed);
		changeRequest.setStage(stager);
		changeRequest.setRequestID(queryHandler.InsertChangeRequest(changeRequest));
		changeRequest.updateInitiatorRequest();
		changeRequest.updateStage();
		queryHandler.insertInitiator(changeRequest.getInitiator());
		queryHandler.InsertProcessStage(changeRequest, changeRequest.getProcessStage());


	}// END of enterChangeRequestToDB

	/**
	 * This method is responsible for the creation of the osf.server instance (there is
	 * no UI in this phase).
	 * @param args The port number to listen on. Defaults to 5555 if no argument is entered.
	 **/
	public static void main(String[] args) {
		int port; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the osf.server
	 * starts listening for connections.
	 */
	protected void serverStarted() throws UnknownHostException {
		System.out.println("Server listening for connections on host " + InetAddress.getLocalHost().getHostAddress() + ':' + getPort());
		mysqlConnection mysqlConn = new mysqlConnection();
		queryHandler = new QueryHandler(mysqlConn);
		if (!mysqlConnection.checkExistence()) {
			mysqlConnection.buildDB();
			/*queryHandler.insertRequirement("Bob", "Cataclysm", "Fix it!", "Johny", closed);
			queryHandler.insertRequirement("Or", "Joy", "Enjoy", "Ilia", ongoing);
			queryHandler.insertRequirement("Abu Ali", "Playful", "to play", "Marak", suspended);*/
			enterUsersToDB();
			enterChangeRequestToDB();
			//testing
			ArrayList<ChangeRequest> a = queryHandler.getAllChangeRequestWithStatus(ChangeRequestStatus.suspended);
			//ArrayList<ChangeRequest> b= queryHandler.getAllChangeRequestWithStatusAndStageOnly(ChangeRequestStatus.ongoing);
//
			System.out.println("New DB ready for use");
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the osf.server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		mysqlConnection.closeConnection();
		System.out.println("Server has stopped listening for connections.");
	}
}
//End of EchoServer class
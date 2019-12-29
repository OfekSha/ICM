
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

import Entity.User.ICMPermissions;
import Entity.User.Job;
import Entity.ChangeRequest;
import Entity.Initiator;
import Entity.ProcessStage;
import Entity.Requirement;
import Entity.User;
import Entity.clientRequestFromServer;
import static Entity.Requirement.statusOptions.*;
import static Entity.Requirement.statusOptions.suspended;


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
	 *
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
		clientRequestFromServer request = (clientRequestFromServer)msg; // request from ocf.client
		System.out.println(LocalTime.now() + ": Message received [" + request.getName() + "] of\n" + request.getObject() + "\t" + " from " + client.getInetAddress());
		ArrayList<Requirement> ReqListForClient = new ArrayList<>();
		Object sendBackobject =null;
		Boolean iWantResponce =true;
		try {
		
			Requirement reqReceived;
			switch (request.getRequest()) {
				// read all requirement data
				case getAll: // get all requirements need to change!!!!!!!!!
					getAllRequest(ReqListForClient);
					sendBackobject=ReqListForClient;
					break;
				// read data from some id in requirement
				case updateStatus: // change status of one requirement.
					//reqReceived = request.getObject().get(0);
					//queryHandler.updateStatus(reqReceived.getID(), reqReceived.getStatus().name());
					//(ReqListForClient, reqReceived);
					sendBackobject=ReqListForClient;
					break;
				case getRequirement:
					//reqReceived = request.getObject().get(0); // get the requirement id
					//selectRequirement(ReqListForClient, reqReceived);
					sendBackobject=ReqListForClient;
					break;
				case getUser:	
					sendBackobject=queryHandler.selectUser(((String)request.getObject()));
					break;
				case updateUser:	
					queryHandler.updateAllUserFileds((User)request.getObject());
					break;
				case changeInLogIn:	
					iWantResponce =false;
					queryHandler.updateAllUserFileds((User)request.getObject());
					break;
				case addRequest:	
					ChangeRequest change =(ChangeRequest)request.getObject();
					change.changeRequestID(queryHandler.InsertChangeRequest(change));
					change.updateInitiatorRequest();
					change.updateStage();
					queryHandler.insertInitiator(change.getInitiator());
					queryHandler.InsertProcessStage(change.stage);
					iWantResponce =false;
					break;
				default:
					throw new IllegalArgumentException("the request " + request + " not implemented in the osf.server.");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		Object answer = new clientRequestFromServer(request.getRequest(), sendBackobject); // answer to the ocf.client
		try {
			if (iWantResponce)client.sendToClient(answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void selectRequirement(ArrayList<Requirement> reqListForClient, Requirement reqReceived) {
		String[] result;
		result = queryHandler.selectRequirement(reqReceived.getID());
		reqListForClient.add(new Requirement(result));
	}

	private void getAllRequest(ArrayList<Requirement> reqListForClient) {
		for (String[] arr : queryHandler.selectAll()) {
			reqListForClient.add(new Requirement(arr));
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
			queryHandler.insertRequirement("Bob", "Cataclysm", "Fix it!", "Johny", closed);
			queryHandler.insertRequirement("Or", "Joy", "Enjoy", "Ilia", ongoing);
			queryHandler.insertRequirement("Abu Ali", "Playful", "to play", "Marak", suspended);
			enterUsersToDB();	
			enterChangeRequestToDB();
			System.out.println("\nNew DB ready for use");
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
	
	private void enterUsersToDB() {
		// creating admin
		EnumSet<ICMPermissions> Permissions =EnumSet.allOf(User.ICMPermissions.class);
		User newUser =new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", User.Job.informationEngineer, Permissions, false);
		queryHandler.insertUser(newUser);
		// creating  information Tecnologies DeparmentManger
		EnumSet<ICMPermissions> lessPermissions =EnumSet.complementOf(Permissions);		//empty enum set
		lessPermissions.add(User.ICMPermissions.changeControlCommitteeChairman);
		newUser= new User("informationTecnologiesDeparmentManger", "1234", "FirstName", "LastName", "mail@email.com", User.Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating inspector
		lessPermissions=EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.inspector);
		newUser= new User("inspector", "1234", "FirstName", "LastName", "mail@email.com", User.Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating estimator
		lessPermissions=EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.estimator);
		newUser= new User("estimator", "1234", "FirstName", "LastName", "mail@email.com", User.Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating exeution Leader
		lessPermissions=EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.executionLeader);
		newUser= new User("exeutionLeader", "1234", "FirstName", "LastName", "mail@email.com", User.Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating examiner
		lessPermissions=EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.examiner);
		newUser= new User("examiner", "1234", "FirstName", "LastName", "mail@email.com", User.Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating change Control Committee Chairman
		lessPermissions=EnumSet.complementOf(Permissions);
		lessPermissions.add(User.ICMPermissions.changeControlCommitteeChairman);
		newUser= new User("changeControlCommitteeChairman", "1234", "FirstName", "LastName", "mail@email.com", User.Job.informationEngineer, lessPermissions, false);
		queryHandler.insertUser(newUser);
		//creating student
		newUser= new User("student", "1234", "FirstName", "LastName", "mail@email.com", User.Job.student, null, false);
		queryHandler.insertUser(newUser);
	}// END of  enterUsersToDB() 
	
	private void enterChangeRequestToDB() {
		EnumSet<ICMPermissions> Permissions =EnumSet.allOf(User.ICMPermissions.class);
		User newUser =new User("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", User.Job.informationEngineer, Permissions, false);
		Initiator init = new Initiator(newUser,null);
		LocalDate start =  LocalDate.now();
		ChangeRequest chang = new ChangeRequest(init, start,"TheSystme" ,"test", "test", "test",null);
		chang.changeRequestID(queryHandler.InsertChangeRequest(chang));
		init.setrequest(chang);
		queryHandler.insertInitiator(init);
		queryHandler.InsertProcessStage(chang.stage);
		
	}// END of enterChangeRequestToDB
	
/**
	 * This method is responsible for the creation of the osf.server instance (there is
	 * no UI in this phase).
	 *
	 * @param args The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
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
}
//End of EchoServer class

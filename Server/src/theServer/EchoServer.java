package theServer;
import Entity.*;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import Entity.User.collegeStatus;
import Entity.User.icmPermission;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import queryHandler.QueryHandler;
import reporting.ReportController;
import reporting.ReportController.reportScope;
import theServer.ServerTesting.whatHappened;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;

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
	private mysqlConnection mysqlConn;
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
	 *
	 * @param msg    The message received from the ocf.client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		clientRequestFromServer request = (clientRequestFromServer) msg; // request from ocf.client
		System.out.println(LocalTime.now() + ": Message received [" + request.getName() + "] of\n" + request.getObject() + "\t" + " from " + client.getInetAddress());
		Object sendBackObject = null;
		Object[] objectArray;
		Object[] returningObjectArray;
		ServerTesting tester = new ServerTesting(queryHandler);
		ReportController reportController = new ReportController(queryHandler);
		whatHappened varWhatHappened;

		//---------------------------------------
		boolean iWantResponse = true;

		try {

			switch (request.getRequest()) {
				case changeInLogIn:
					queryHandler.getUserQuerys().updateAllUserFields((User) request.getObject());
					iWantResponse = false;
					break;

				case updateProcessStage:
					varWhatHappened = tester.testIfRequestIsFrozen(client.getConnectedUser(), ((ProcessStage) request.getObject()).getRequest());
					sendBackObject = varWhatHappened;
					if (whatHappened.success == varWhatHappened) {
						queryHandler.getProccesStageQuerys().updateAllProcessStageFields((ProcessStage) request.getObject());
					}
					break;

				case updateChangeRequest:
					varWhatHappened = tester.testIfRequestIsFrozen(client.getConnectedUser(), (ChangeRequest) request.getObject());
					sendBackObject = varWhatHappened;
					if (whatHappened.success == varWhatHappened) {
						queryHandler.getChangeRequestQuerys().updateAllChangeRequestFields((ChangeRequest) request.getObject());
					}
					break;

				case addRequest:
					ChangeRequest change = (ChangeRequest) request.getObject();
					change.setRequestID(queryHandler.getChangeRequestQuerys().InsertChangeRequest(change));
					change.updateInitiatorRequest();
					change.updateStage();
					queryHandler.getInitiatorQuerys().insertInitiator(change.getInitiator());
					queryHandler.getProccesStageQuerys().InsertProcessStage(change, change.getProcessStage());
					change.updateDocs();
					queryHandler.getFilesQuerys().InsertFile(change.getDoc());
					iWantResponse = false;
					break;

				case updateUser:

					//varWhatHappend =tester.testUpstingUserCanUpdateUsers(client.getConnectedUser());
					//sendBackObject =varWhatHappend;
					sendBackObject =whatHappened.success;
					//if (whatHappend.success==varWhatHappend) 			
					queryHandler.getUserQuerys().updateAllUserFields((User) request.getObject());

					break;

				case updateUserCollegeStatus:
					varWhatHappened = tester.testUpstingUserCanUpdateUsers(client.getConnectedUser());
					sendBackObject = varWhatHappened;
					if (whatHappened.success == varWhatHappened) {
						returningObjectArray = (Object[]) request.getObject();
						queryHandler.getUserQuerys().updateCollegeStatus((User) returningObjectArray[0],
								(collegeStatus) returningObjectArray[1]);
					}
					break;
				// read all ChangeRequest data
				case getAll:
					sendBackObject = queryHandler.getChangeRequestQuerys().getAllChangeRequest();
					break;

				case getUser:
					sendBackObject = queryHandler.getUserQuerys().selectUser(((String) request.getObject()));
					break;

				case getAllUsers:
					sendBackObject = queryHandler.getUserQuerys().getAllUsers();
					break;

				case getChangeRequestByStatus:
					objectArray = new Object[2];
					objectArray[0] = queryHandler.getChangeRequestQuerys().getAllChangeRequestWithStatus((ChangeRequestStatus) request.getObject());
					objectArray[1] = request.getObject();
					sendBackObject = objectArray;
					break;
				case getUsersByICMPermissions:
					objectArray = new Object[2];
					objectArray[0] = queryHandler.getUserQuerys().getAllUsersWithICMPermissions((icmPermission) request.getObject());
					objectArray[1] = request.getObject();
					sendBackObject = objectArray;
					break;
				case getAllUsersByJob:
					objectArray = new Object[2];
					objectArray[0] = queryHandler.getUserQuerys().getAllUsersByJob((collegeStatus) request.getObject());
					objectArray[1] = request.getObject();
					sendBackObject = objectArray;
					break;
				case getAllChangeRequestWithStatusAndStage:
					objectArray = (Object[]) request.getObject();
					returningObjectArray = new Object[4];
					returningObjectArray[1] = objectArray[0];
					returningObjectArray[2] = objectArray[1];
					returningObjectArray[3] = objectArray[2];
					returningObjectArray[0] = queryHandler.getChangeRequestQuerys().getAllChangeRequestWithStatusAndStage(
							(ChargeRequestStages) objectArray[0],
							(subStages) objectArray[1],
							(ChangeRequestStatus) objectArray[2]);
					sendBackObject = returningObjectArray;
					break;
				case getAllChangeRequestWithStatusAndStageOnly:
					objectArray = (Object[]) request.getObject();
					returningObjectArray = new Object[3];
					returningObjectArray[1] = objectArray[0];
					returningObjectArray[2] = objectArray[1];
					returningObjectArray[0] = queryHandler.getChangeRequestQuerys().getAllChangeRequestWithStatusAndStageOnly(
							(ChargeRequestStages) objectArray[0],
							(ChangeRequestStatus) objectArray[1]);
					sendBackObject = returningObjectArray;
					break;
				case getAllChangeRequestWithStatusAndSubStageOnly:
					objectArray = (Object[]) request.getObject();
					returningObjectArray = new Object[3];
					returningObjectArray[1] = objectArray[0];
					returningObjectArray[2] = objectArray[1];
					returningObjectArray[0] = queryHandler.getChangeRequestQuerys().getAllChangeRequestWithStatusAndSubStageOnly(
							(subStages) objectArray[0],
							(ChangeRequestStatus) objectArray[1]);
					sendBackObject = returningObjectArray;
					break;
				case getAllChangeRequestWithStatusAndStageAndSupervisor:
					objectArray = (Object[]) request.getObject();
					returningObjectArray = new Object[5];
					returningObjectArray[1] = objectArray[0];
					returningObjectArray[2] = objectArray[1];
					returningObjectArray[3] = objectArray[2];
					returningObjectArray[4] = objectArray[3];
					returningObjectArray[0] = queryHandler.getChangeRequestQuerys()
							.getAllChangeRequestWithStatusAndStageAndSupervisor(
									(ChargeRequestStages) objectArray[0],
									(subStages) objectArray[1],
									(ChangeRequestStatus) objectArray[2],
									(String) objectArray[3]);
					sendBackObject = returningObjectArray;
					break;

				case LogIN:
					Object[] returning = new Object[2];
					User tryingToLogInUser = (User) request.getObject();
					User UserInDB = queryHandler.getUserQuerys().selectUser(tryingToLogInUser.getUserName());
					returning[0] = UserInDB;
					if (UserInDB == null)
						returning[1] = false;
					else {
						if (testAllClientsForUser(UserInDB))
							returning[1] = false;
						else returning[1] = UserInDB.getPassword().equals(tryingToLogInUser.getPassword());
					}
					sendBackObject = returning;

					break;
				case successfulLogInOut:
					client.setConnectedUser((User) request.getObject());
					iWantResponse = false;
					break;
				case getDoc:
					sendBackObject = queryHandler.getFilesQuerys().selectDocWithFile((Document) request.getObject());
					break;
				case removeUserIcmPermission:
					objectArray = (Object[]) request.getObject();
					sendBackObject = tester.testIfUserIcmPermissionCanBeRemoved((User) objectArray[0], (icmPermission) objectArray[1]);
					if(((Object[])sendBackObject)[0] ==whatHappened.success)
						queryHandler.getUserQuerys().updateIcmPermission((User) objectArray[0], (icmPermission) objectArray[1], 0);


					break ;
				case createNewActivitiesReport:
					objectArray = (Object[]) request.getObject();
					LocalDate start= (LocalDate) objectArray[0];
					LocalDate end= (LocalDate) objectArray[1];
					reportScope scope =(reportScope) objectArray[2];
					ActivitiesReport tosend =reportController.creatActivitiesReport(start, end, scope);
					queryHandler.getActivitiesReportQuerys().InsertActivitiesReport(tosend);
					sendBackObject =tosend;
					break;
				case getAllActivitiesReports:
					sendBackObject = queryHandler.getActivitiesReportQuerys().getAllActivitiesReports();
					break;
				case alertClient:
					Message mesg =(Message) request.getObject();
					queryHandler.getMessagesQuerys().InsertMessags(mesg);
					User reciver=	queryHandler.getUserQuerys().selectUser(mesg.getTo());
					ConnectionToClient otherClient =searchForConnectedClient(reciver);
					if (otherClient!=null) {
						try {otherClient.sendToClient(new clientRequestFromServer(request.getRequest(), request.getObject()));
						} catch (IOException e) {e.printStackTrace();}
						sendBackObject =whatHappened.success;
					}
					else  sendBackObject =whatHappened.failedNoSuchUser;	
					break;
				case getAllMessges:
					sendBackObject = queryHandler.getMessagesQuerys().SelectMessages(client.getConnectedUser());
					break;
				case addUserIcmPermission:
					objectArray = (Object[]) request.getObject();
					sendBackObject = tester.testifUserIcmPermissionCanBeAdded((User) objectArray[0], (icmPermission) objectArray[1],(ChangeRequest)objectArray[2]);
					if(((Object[])sendBackObject)[0] ==whatHappened.success)
						queryHandler.getUserQuerys().updateIcmPermission((User) objectArray[0], (icmPermission) objectArray[1], 1);
					break;
				case getDelayReport: 
					sendBackObject=reportController.createDelayReport( (reportScope) request.getObject());
					break;
				default:
					throw new IllegalArgumentException("the request " + request + " not implemented in the osf.server.");
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


	/**
	 * This method is responsible for the creation of the osf.server instance (there is
	 * no UI in this phase).
	 *
	 * @param args The port number to listen on. Defaults to 5555 if no argument is entered.
	 **/
	public static void StartOcfServer(String[] args) {
		int port; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);
		ServerMenuForm.echo = sv;
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!\n  please test for other server instance\n");
		}

	}

	/**
	 * This method overrides the one in the superclass. Called when the osf.server
	 * starts listening for connections.
	 */
	protected void serverStarted() throws UnknownHostException {
		System.out.println("Server listening for connections on host " + InetAddress.getLocalHost().getHostAddress() + ':' + getPort());
		mysqlConn = new mysqlConnection();
		queryHandler = new QueryHandler(mysqlConn);
		mysqlConn.DBwithExamples();
	}

	/**
	 * This method overrides the one in the superclass. Called when the osf.server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		mysqlConnection.closeConnection();
		System.out.println("Server has stopped listening for connections.");
	}

	/**
	 * tests all connections if the user trying  to connect is connected
	 *
	 * @param tryingToConnect ?
	 * @return true if the user is already connected
	 */
	protected boolean testAllClientsForUser(User tryingToConnect) {
		Thread[] clientThreadList = getClientConnections();

		for (Thread thread : clientThreadList) {
			try {
				User u = ((ConnectionToClient) thread).getConnectedUser();

				if (tryingToConnect.equals(u)) {
					return true;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return false;
	}// END of testAllClientsForUser
	
	protected ConnectionToClient searchForConnectedClient(User lookingFor) {
		Thread[] clientThreadList = getClientConnections();

		for (Thread thread : clientThreadList) {
			try {
				User u = ((ConnectionToClient) thread).getConnectedUser();

				if (lookingFor.equals(u)) {
					return (ConnectionToClient) thread;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}// END of searchForConnectedClient
	
	public mysqlConnection getmysqlConnection() {
		return mysqlConn;
	}

	/**
	 * once the server is closed the clients will be notified
	 */
	@Override
	public void serverClosed() {

	}
}
//End of EchoServer class

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;

import Entity.User.ICMPermissions;
import Entity.User.Job;
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
		System.out.println(LocalTime.now() + ": Message received [" + request.getName() + "] of\n" + request.getObj() + "\t" + " from " + client.getInetAddress());
		ArrayList<Requirement> ReqListForClient = new ArrayList<>();
		Object sendBackobject =null;
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
					reqReceived = request.getObj().get(0);
					queryHandler.updateStatus(reqReceived.getID(), reqReceived.getStatus().name());
					selectRequirement(ReqListForClient, reqReceived);
					sendBackobject=ReqListForClient;
					break;
				case getRequirement:
					reqReceived = request.getObj().get(0); // get the requirement id
					selectRequirement(ReqListForClient, reqReceived);
					sendBackobject=ReqListForClient;
					break;
					//getUser not implemented.
				case getUser:	
					sendBackobject=queryHandler.selectUser(((String)request.getObject()));
					break;
				default:
					throw new IllegalArgumentException("the request " + request + " not implemented in the osf.server.");
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		Object answer = new clientRequestFromServer(request.getRequest(), sendBackobject); // answer to the ocf.client
		try {
			client.sendToClient(answer);
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
			EnumSet<ICMPermissions> Permissions =EnumSet.allOf(User.ICMPermissions.class);
			queryHandler.insertUser("admin", "admin", "adminFirstName", "adiminLastName", "admin@email.com", Permissions, User.Job.informationEngineer, false);
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

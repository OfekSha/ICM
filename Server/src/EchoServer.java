
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.ArrayList;

import Entity.Requirement;
import Entity.Requirement.statusOptions;
import Entity.clientRequestFromServer;

import server.AbstractServer;
import server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
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
	private mysqlConnection mysqlConn;
	private QueryHandler queryHandler;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		clientRequestFromServer request = (clientRequestFromServer)msg; // request from client
		System.out.println(LocalTime.now() + ": Message received [" + msg + "] of\n" + request.getObj() + "\t" + " from " + client.getInetAddress());
		try {
			ArrayList<Requirement> ReqListForClient = new ArrayList<>();
			Requirement reqReceived;
			switch (request.getRequest()) {
				// read all requirement data
				case getAll:
					getAllRequest(ReqListForClient);
					break;
				// read data from some id in requirement
				case updateStatus:
					reqReceived = request.getObj().get(0);
					queryHandler.updateStatus(reqReceived.getID(), reqReceived.getStatus().name());
					selectRequirement(ReqListForClient, reqReceived);
					break;
				case getRequirement:
					reqReceived = request.getObj().get(0);
					selectRequirement(ReqListForClient, reqReceived);
					break;
				default:
					throw new IllegalArgumentException("the request " + request + " not implemented in the server.");
			}
			clientRequestFromServer answer = new clientRequestFromServer(request.getRequest(), ReqListForClient); // answer to the client
			client.sendToClient(answer);
		} catch (IOException e) {
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
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() throws UnknownHostException {
		System.out.println("Server listening for connections on host " + InetAddress.getLocalHost().getHostAddress() + ':' + getPort());
		mysqlConn = new mysqlConnection();
		queryHandler = new QueryHandler(mysqlConn);
		if (!mysqlConnection.checkExistence()) {
			mysqlConnection.buildDB();
			queryHandler.insertRequirement("Bob", "Cataclysm", "Fix it!", "Johny", statusOptions.closed);
			queryHandler.insertRequirement("Or", "Joy", "Enjoy", "Ilia", statusOptions.ongoing);
			queryHandler.insertRequirement("Abu Ali", "Playful", "to play", "Marak", statusOptions.suspended);
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		mysqlConnection.closeConnection();
		System.out.println("Server has stopped listening for connections.");
	}
/**
	 * This method is responsible for the creation of the server instance (there is
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

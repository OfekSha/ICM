import server.*;
import java.io.*;
import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

import Entity.Requirement;
import Entity.Requirement.statusOptions;
import Entity.clientRequestFromServer;

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
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object[] msg, ConnectionToClient client) {
		QueryHandler query = new QueryHandler();
		System.out.println("Message received: " + msg[0] + msg[1] + " from " + client);
		try {
			if (!mysqlConnection.checkExistence()) {
				mysqlConnection.buildDB();
				query.insertRequirment("Bob", "Cataclysm", "Fix it!", "Johny");
			}
			else {
				clientRequestFromServer request = new clientRequestFromServer((String)msg[0]); // msg is array of objects first is from where
				switch(request.getRequest()) {
						// read all requirement data
					case getRequirement: ArrayList<String[]> reqList = query.selectAll() ;
					ArrayList<Requirement> ReqListForClient = new ArrayList<>();
						for (String[] arr : reqList)
							try {
								ReqListForClient.add(packageRequirement(arr));
							} catch (InvalidAttributesException e) {
								e.printStackTrace();
							}
						Object[] o = new Object[2];
						o[0] = request;
						o[1] = ReqListForClient;
						client.sendToClient(o);
						break; //TODO select * from icm.requirement
						// read data from some id in requirement
					case updateStatus: // client.sendToClient(query.selectRequirement(Integer.parseInt(msg.toString())));
						break;
					// insert new line to requirement
					/*case 3: query.insertRequirment("Bob", "Cataclysm", "Fix it!", "Johny");//TODO insert
							client.sendToClient(query.selectAll());
						break;
						//Update status in requirement.
					case 4:
						break;*/
				default: throw new IllegalArgumentException("the request "+request+" not implemented in the server.");
				}
			}
			//client.sendToClient((query.selectAll()).toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mysqlConnection.closeConnection();
		sendToAllClients(msg, client);
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
/**
 * 
 * package list of strings to requirement
 * 
 * @author Ofek
 * @param reqLine ?????
 * @return newReq
 * @throws InvalidAttributesException ????
 */

private Requirement packageRequirement (String[] reqLine) throws InvalidAttributesException {
	int id = Integer.parseInt(reqLine[1]);
	String reqInitiator = reqLine[0],
			currentSituationDetails = reqLine[2],
			requestDetails = reqLine[3],
			stageSupervisor = reqLine[4];
	statusOptions status;
	switch (reqLine[5]) {
		case "ongoing":
			status = statusOptions.ongoing;
			break;
		case "suspended":
			status = statusOptions.suspended;
			break;
		case "closed":
			status = statusOptions.closed;
			break;
		default:
			throw new InvalidAttributesException("Invalid status");
	}
	return new Requirement(reqInitiator, currentSituationDetails, requestDetails, stageSupervisor, status, id);
}
	// Class methods ***************************************************
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


import java.io.*;
import java.util.ArrayList;

import javax.naming.directory.InvalidAttributesException;

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
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		QueryHandler query = new QueryHandler();
		//String asStr = String.valueOf(msg); // kostya
		//String[] splittedMsg = asStr.split(" "); //kostya
		//clientRequestFromServer request = new clientRequestFromServer(splittedMsg[0]); // kostya
		clientRequestFromServer request = (clientRequestFromServer)msg; // request from client
		//System.out.println("Message received: " + splittedMsg[0] + " = [" + request.getRequest() + ']' +
		//		" from " + client);
		try {
			if (!mysqlConnection.checkExistence()) {
				mysqlConnection.buildDB();
				query.insertRequirment("Bob", "Cataclysm", "Fix it!", "Johny");
			}
			else {
				
				ArrayList<Requirement> ReqListForClient = new ArrayList<>();
				switch (request.getRequest()) {
					// read all requirement data
					case getAll:
						ArrayList<String[]> reqList = query.selectAll();
						for (String[] arr : reqList) {
							ReqListForClient.add(packageRequirement(arr));
						}
						break;
					// read data from some id in requirement
					case updateStatus:
						/*ID = Integer.parseInt(splittedMsg[1]);
						String status = splittedMsg[2];
						query.updateStatus(ID, status);*/
					// no break because we want to see the changes!
						Requirement updateStatus=(Requirement) request.getObj();
						query.updateStatus(updateStatus.getID(), updateStatus.getStatus().name());
						reqList = query.selectAll();
						for (String[] arr : reqList) {
							ReqListForClient.add(packageRequirement(arr));
						}
						
					case getRequirement:
						//kostya
						/*int ID = Integer.parseInt(splittedMsg[1]);
						String[] getReq = query.selectRequirement(ID);
							try {
								ReqListForClient.add(packageRequirement(getReq));
							} catch (InvalidAttributesException e) {
								e.printStackTrace();
							}*/

						break;
					// insert new line to requirement
					/*case 3: query.insertRequirment("Bob", "Cataclysm", "Fix it!", "Johny");//TODO insert
							client.sendToClient(query.selectAll());
						break;
						//Update status in requirement.
					case 4:
						break;*/
					default:
						throw new IllegalArgumentException("the request " + request + " not implemented in the server.");
				}
				//Object[] answer = new Object[] { request, ReqListForClient }; // kostya
				clientRequestFromServer answer = new clientRequestFromServer(request.getRequest(),ReqListForClient); // answer to the client
				client.sendToClient(answer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		mysqlConnection.closeConnection();
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

private Requirement packageRequirement (String[] reqLine) {
	return new Requirement(reqLine[0],
			reqLine[2],
			reqLine[3],
			reqLine[4],
			statusOptions.valueOf(reqLine[5]),
			Integer.parseInt(reqLine[1]));
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

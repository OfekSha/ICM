package WindowApp;

import client.*;
import java.io.*;

public class IcmClient extends AbstractClient {

	IcmForm clientUI; // the UI currently in serves

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	public IcmClient(String host, int port, IcmForm clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
		openConnection();
	}


	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {

		clientUI.getFromServer((Object[]) msg);
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {//TODO: display some messge ?
			//clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}


	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}// end of ICMclient class

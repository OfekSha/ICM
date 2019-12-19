package WindowApp;

import GUI.MainForm;
import ocsf.client.AbstractClient;

import java.io.*;

public class IcmClient extends AbstractClient {

	public MainForm clientUI; // the UI currently in serves

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the chat ocf.client.
	 *
	 * @param host     The osf.server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	public IcmClient(String host, int port, MainForm clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
		openConnection();
	}

	/**
	 * This method handles all data that comes in from the osf.server.
	 *
	 * @param msg The message from the osf.server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.getFromServer(msg);
	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {//TODO: display some message ?
			//clientUI.display("Could not send message to osf.server.  Terminating ocf.client.");
			quit();
		}
	}

	/**
	 * This method terminates the ocf.client.
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

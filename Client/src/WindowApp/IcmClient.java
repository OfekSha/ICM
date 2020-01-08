package WindowApp;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ocsf.client.AbstractClient;

import java.io.IOException;
import java.time.LocalTime;

public class IcmClient extends AbstractClient {

	public IcmForm clientUI; // the UI currently in serves

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
	 * @param clientUI receives as object new Scene
	 *                 to understand where send answers from server
	 */
	public void setClientUI(IcmForm clientUI) {
		this.clientUI = clientUI;
	}

	/**
	 * This method handles all data that comes in from the server.
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.getFromServer(msg);
	}

	/**
	 * This method handles all data coming from the UI
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			System.out.print("\n["
					+ LocalTime.now()
					//+ ZonedDateTime.now().toString()
					//.split("T")[1]
					//.split("\\+")[0]
					+ "]: Message to server <- " + message);
			sendToServer(message);
		} catch (IOException e) {
			System.out.println("Could not send message to server.\tTerminating client.");
			e.printStackTrace();
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
	/**@author Yonathan 
	 *	if we lose the connection  we need organized message to the client
	 */
	@Override
	  public void sendToServer(Object msg) throws IOException
	  {
	    if (clientSocket == null || output == null) {
	    	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("no connection");
			alert.setHeaderText("You have lost you connection to the server");
			alert.setContentText("Things you can do:\n1) Make sure the server is running\n");
			alert.showAndWait();
			// TODO : re lunch the client insted
			System.exit(0);		
	    }

	    else  output.writeObject(msg);
	  }
}
package defaultPackage;

import client.*;
import java.io.*;

import Entity.clientRequestFromServer;

public class ICMclient  extends AbstractClient {

	
	ICMform clientUI; // the UI currently in serves 
	  
	//Constructors ****************************************************
	  
	  /**
	   * Constructs an instance of the chat client.
	   *
	   * @param host The server to connect to.
	   * @param port The port number to connect on.
	   * @param clientUI The interface type variable.
	   */
	public ICMclient(String host, int port,ICMform clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
	    openConnection();
	}
	
	
		//TODO: is needed? 
	/**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	  public void handleMessageFromServer(Object msg) 
	  {
		  clientRequestFromServer request = (clientRequestFromServer)(((Object[])msg)[0]); // msg is array of objects first is from where
		  switch (request) {
		  case getRequirement:
			  break;
		  default: throw new IllegalArgumentException("the request "+request+" not implemented in the client.");
		  }
	    clientUI.getFromServer(msg);
	  }
	  
	  /**
	   * This method handles all data coming from the UI            
	   *
	   * @param message The message from the UI.    
	   */
	  public void handleMessageFromClientUI(Object message)  
	  {
	    try
	    {
	    	sendToServer(message);
	    }
	    catch(IOException e)
	    {//TODO: display some messge ?
	      //clientUI.display("Could not send message to server.  Terminating client.");
	      quit();
	    }
	  }
	  
	  
	  /**
	   * This method terminates the client.
	   */
	  public void quit()
	  {
	    try
	    {
	      closeConnection();
	    }
	    catch(IOException e) {
	      e.printStackTrace();
	    }
	    System.exit(0);
	  }
	


}// end of ICMclient class 

package defaultPackage;
import java.io.*;
import java.io.IOException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.stage.Stage;

import GUI.FrameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main   extends Application   {
	

	//Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  FrameController aFrame ;
	  
	  //Instance variables **********************************************
	  
	  /**
	   * The instance of the client that created this ConsoleChat.
	   */
	  ChatClient client; 
	  
	  
	  public Main (String host, int port) {
		  
		  
		   			aFrame = new FrameController(); // create the frame
			    try 
			    {
			      client = new ChatClient(host, port, aFrame);
			      System.out.println("Connection established!\n" //to be removed/changed
			              + "Welcome to ICM.\n");
			    } 
			    catch(IOException exception) 
			    {
			      System.out.println("Error: Can't setup connection!" // to be removed/changed
			                + " Terminating client.");
			      System.exit(1);
			    }
	  }
	  
	  
	public static void main(String[] args) 
	  {
	    
	    int port = 0;  //The port number
	     String host;
	    try
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      host = "localhost";
	    }
	    
	    Main chat= new Main(host, DEFAULT_PORT);
	    launch(args);	  //Wait for  data
	    
	  }


	@Override
	public void start(Stage arg0) throws Exception {
		aFrame.start(arg0);
		
	}

	
	
}

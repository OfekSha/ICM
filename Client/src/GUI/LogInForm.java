package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import Entity.Requirement;
import WindowApp.ClientLauncher;
import WindowApp.IcmClient;
import WindowApp.IcmForm;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;






public class LogInForm implements Initializable, IcmForm {
	
	//Variables
		private static ArrayList<Requirement> ReqListForClient = null;
		ObservableList<String> listFor_cmbRequests;
		ObservableList<String> listFor_cmbStatus;
		 MainMenuForm TheMainMenuForm2 ;
		// text fields
		@FXML
		private TextField IP;
		@FXML
		private TextField UserName;
		@FXML
		private TextField Password;
		
		// buttons
		@FXML
		private Button btnExit;
		@FXML
		private Button btnLogin;
		
		//UNDECORATED
		private double xOffset = 0;
		private double yOffset = 0;

	public void start(Stage primaryStage) throws Exception  {
		
			// scene
			Parent root = FXMLLoader.load(getClass().getResource("/GUI/LogInForm.fxml"));
			primaryStage.initStyle(StageStyle.UNDECORATED);
			root.setOnMousePressed(event -> {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
			});
			root.setOnMouseDragged(event ->{
					primaryStage.setX(event.getScreenX() - xOffset);
					primaryStage.setY(event.getScreenY() - yOffset);
			});
			Scene scene = new Scene(root);
			primaryStage.setTitle("Log in");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		// TODO Auto-generated method stub
		
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	//TODO: the following  methods are from the class diagram:  
	
	 public void getInput(ActionEvent event)throws Exception {
			
		 String host;// = "localhost";
			
			
			// START ----insert server ip to connect from client console----
			System.out.println("Insert Server IP: ");
			host = IP.getText();
			System.out.println(host);
			// END ---- insert server ip to connect from client console----
			host ="localhost";
			try {
				ClientLauncher.client = new IcmClient(host, ClientLauncher.DEFAULT_PORT, this);
				System.out.println("Connection established!\n" //to be removed/changed
						+ "Welcome to ICM.");
			} catch (IOException exception) {
				System.out.println("Error: Can't setup connection!" // to be removed/changed
						+ " Terminating client.");
				System.exit(1);
			}
			
			
			// lunching main 
			
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			///-----------------------------------------------
			Stage stage = new Stage();
			Parent root;
		    root = (Parent) FXMLLoader.load(getClass().getResource("/GUI/MainMenu.fxml"));
		    Scene scene = new Scene(root);
		    scene.setRoot(root);
		    FXMLLoader fxmlLoader = new FXMLLoader();
		    //        FooController fooController = (FooController) fxmlLoader.getController();
		    MainMenuForm MainMenuFormController = (MainMenuForm) fxmlLoader.getController();
		    stage.setScene(scene);
		    stage.show();
			///---------------------------------------------------
			
			// TO DO : request DB clients 
						//getRequests();
			
			//TODO: test password / user name  is correct	 
	 }
	 
	 public void ExitBtn() {
			System.exit(0);			
		}
	 
	
	
	
	}// End of LogInForm



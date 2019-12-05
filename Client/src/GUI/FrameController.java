package GUI;

import defaultPackage.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class FrameController implements Initializable , IForm{
	//text filds
	@FXML
	private TextField txtInitiator;
	@FXML
	private TextField txtCurrentSituationDetails;
	@FXML
	private TextField txtRequestDetails ;
	@FXML
	private TextField txtStageSupervisor;
	@FXML
	private TextField txtStatus;
	//buttons
	@FXML
	private Button btnExit;
	@FXML
	private Button btnUpdateStatus;
	
	@FXML
	private ComboBox cmbRequests;	
	
	
	  ChatClient client;
	  
	  
	  /** 
	 * @param primaryStage
	 * @throws Exception  
	 */
	public void start(Stage primaryStage) throws Exception {	
			Parent root = FXMLLoader.load(getClass().getResource("/GUI/Form.fxml"));
					
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/GUI/Form.css").toExternalForm());
			primaryStage.setTitle("Update Tool");
			primaryStage.setScene(scene);
			primaryStage.show();		
		}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	 //for the ComboBox?
		//setFacultyComboBox();		
	}
	
	
	//from IForm
	/*
	 * server answer
	 * 
	 * 
	 */
	@Override
	public void display(String message) {	

		
	}
	
	
	
}

package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainMenuForm implements Initializable, IcmForm{

	// buttons
			@FXML
			private Button Log_out;
			@FXML
			private Button WatchRequest;
			@FXML
			private Button MakeAChangeEequest;
			@FXML
			private Button InformationTechnologiesDepartmentManager;
			@FXML
			private Button Inspector;
			@FXML
			private Button Estimator;
			@FXML
			private Button ExecutionLeader;
			@FXML
			private Button Examiner;
			@FXML
			private Button ChangeControlCommitteeChairman;
	

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void  LogOutButton(ActionEvent event) throws Exception  {
		
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/GUI/LogInForm.fxml").openStream());
		Scene scene = new Scene(root);			
		primaryStage.setScene(scene);		
		primaryStage.show();
		
	}
	
	public void InspectorMenue() {
		
	}
	
}
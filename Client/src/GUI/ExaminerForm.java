package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.IcmForm;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class ExaminerForm  extends StageSupervisorForm implements IcmForm, Initializable, MainForm {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}
	
	//TODO: the following  methods are from the class diagram:  

	public  void ExecApproved() {}
	public void ExecDesapproved() {}

}

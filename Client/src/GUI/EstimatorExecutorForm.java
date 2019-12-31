package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * @author Yonathan
 * abstract because it should not be a window,
 *   only exists so that  EstimatorForm and  EcecutionLeaderForm Classes  have the same getDueTime 
 */



public abstract class EstimatorExecutorForm extends StageSupervisorForm  {

	 // vars
	@FXML
	public TextArea taRequestDetails;
	@FXML
	public TextArea taRequestReason;
	@FXML
	public TextArea taComment;
	@FXML
	private Button btnSubmit;
	
	
	
	//TODO: the following  methods are from the class diagram:  
	public void getDueTime() {
	}
}

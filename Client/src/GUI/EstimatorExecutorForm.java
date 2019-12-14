package GUI;

import WindowApp.IcmForm;
import javafx.fxml.Initializable;


/**
 * @author Yonathan
 * abstract because it should not be a window,
 *   only exists so that  EstimatorForm and  EcecutionLeaderForm Classes  have the same getDueTime 
 */
public abstract  class EstimatorExecutorForm extends StageSupervisorForm  {

	//TODO: the following  methods are from the class diagram:  
public void getDueTime() {}
}

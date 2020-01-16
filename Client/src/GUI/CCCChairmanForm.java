package GUI;

import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class CCCChairmanForm extends StageSupervisorForm implements IcmForm {

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}

	//TODO: the following  methods are from the class diagram:
	public void getUserID() {
	}

	public void getExecApproved() {
	}

	public void getDisapproved() {
	}

	public void getMoreDetails() {
	}

	public void getCommiteeMemvers() {
	}

	public void getAssessmentReport() {
	}

	@Override
	public StageSupervisorController getController() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IcmForm getIcmForm() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void filterRequests(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRequestClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChangeRequest getSelectedReq() {
		// TODO Auto-generated method stub
		return null;
	}
}

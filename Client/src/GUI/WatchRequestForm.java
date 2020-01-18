package GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controllers.InspectorController;
import Controllers.StageSupervisorController;
import Controllers.WatchRequestController;
import Entity.ChangeRequest;
import Entity.RequestTableView;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import Entity.RequestTableView.requirementForTable;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;

public class WatchRequestForm  extends StageSupervisorForm {
	// fxml vars:

	private ArrayList<ChangeRequest>  requests =new ArrayList<ChangeRequest>();


		@FXML
		private TableColumn<requirementForTable, Object> stageColumn;
		private WatchRequestController controller = new WatchRequestController();
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		table = new RequestTableView(tblView, idColumn, statusColumn, stageColumn, dueTimeColumn, messageColumn);
		clientRequestFromServer toServer = new clientRequestFromServer(requestOptions.getAll, null);
		ClientLauncher.client.handleMessageFromClientUI(toServer);
	}

	@Override
	public StageSupervisorController getController() {
		// TODO Auto-generated method stub
		return controller;
	}

	@Override
	public IcmForm getIcmForm() {
	return this;
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
	public void getFromServer(Object message) {
		clientRequestFromServer toServer =  (clientRequestFromServer) message;
		requests = (ArrayList<ChangeRequest>) toServer.getObject();
		ArrayList<ChangeRequest> toRemove =  new ArrayList<ChangeRequest>();
		for (ChangeRequest e : requests)
			if(!user.equals(e.getInitiator().getTheInitiator()))
				toRemove.add(e);
		for (ChangeRequest e : toRemove)
			requests.remove(e);

		table.setData(requests);
		
	}

	@Override
	public ChangeRequest getSelectedReq() {
		// TODO Auto-generated method stub
		return null;
	}


}
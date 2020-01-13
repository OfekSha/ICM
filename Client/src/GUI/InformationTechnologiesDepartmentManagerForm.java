package GUI;

import Entity.UserTableView.userForTable;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static Entity.clientRequestFromServer.requestOptions.getAllUsers;

public class InformationTechnologiesDepartmentManagerForm extends UserForm {

	@FXML
	public Tab tabCommitteeAccredit;
	public MenuButton mbUserFilter;
	public Button btnInspector;
	public Button btnChairman;
	public Button btnMember1;
	public Button btnMember2;
	public TextField tfInspector;
	public TextField tfChairman;
	public TextField tfMember1;
	public TextField tfMember2;
	public TableView<userForTable> tblViewUsers;
	public TableColumn<userForTable, String> colFirstname;
	public TableColumn<userForTable, String> colLastname;
	public TableColumn<userForTable, String> colEmail;
	public TableColumn<userForTable, Object> colPermissions;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clientRequestFromServer newRequest = new clientRequestFromServer(getAllUsers);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
		btnChairman.setDisable(true);
		btnInspector.setDisable(true);
		btnMember1.setDisable(true);
		btnMember2.setDisable(true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(this::setTableUsers);
	}


	protected void setTableUsers() {
		tblViewUsers.getItems().clear();
		colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
		colLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colPermissions.setCellValueFactory(new PropertyValueFactory<>("permissions"));
		allUsers.forEach(u ->
			tblViewUsers.getItems().add(new userForTable(u)));
	}

	public void getUserId() {}
	public void getRequestId() {}
}

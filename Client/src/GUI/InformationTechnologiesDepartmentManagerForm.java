package GUI;

import Entity.UserTableView;
import Entity.UserTableView.userForTable;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

import static Entity.clientRequestFromServer.requestOptions.getAllUsers;

public class InformationTechnologiesDepartmentManagerForm extends UserForm {

	@FXML
	public MenuButton mbUserFilter;
	public Button btnMember1;
	public Button btnMember2;
	public Button btnChairman;
	public Button btnInspector;
	public TextField tfMember1;
	public TextField tfMember2;
	public TextField tfChairman;
	public TextField tfInspector;
	public Tab tabCommitteeAccredit;
	public TableView<userForTable> tblViewUsers;
	public TableColumn<userForTable, String> colEmail;
	public TableColumn<userForTable, String> colLastName;
	public TableColumn<userForTable, String> colFirstName;
	public TableColumn<userForTable, String> colPermissions;

	public UserTableView userTableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		clientRequestFromServer newRequest = new clientRequestFromServer(getAllUsers);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
		userTableView = new UserTableView(tblViewUsers,
				colFirstName, colLastName, colEmail, colPermissions);
		btnMember1.setDisable(true);
		btnMember2.setDisable(true);
		btnChairman.setDisable(true);
	//	btnInspector.setDisable(true);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(this::setTableUsers);
	}

	protected void setTableUsers() {
		tblViewUsers.getItems().clear();
		userTableView.setData(allUsers);
	}

	public void setInspector(ActionEvent actionEvent) {
		System.out.println(colPermissions);
	}

	public void setChairman(ActionEvent actionEvent) {}
	public void setFirstMember(ActionEvent actionEvent) {}
	public void setSecondMember(ActionEvent actionEvent) {}

	public void getUserId() {}
	public void getRequestId() {}

}

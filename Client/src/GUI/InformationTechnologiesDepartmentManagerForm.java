package GUI;

import Entity.ProcessStage;
import Entity.User;
import Entity.User.icmPermission;
import Entity.UserTableView;
import Entity.UserTableView.userForTable;
import Entity.clientRequestFromServer;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static Entity.ProcessStage.ChargeRequestStages.execution;
import static Entity.User.icmPermission.*;
import static Entity.clientRequestFromServer.requestOptions.getAllUsers;
import static Entity.clientRequestFromServer.requestOptions.updateUser;

public class InformationTechnologiesDepartmentManagerForm extends UserForm {

	@FXML
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
	private User chosenOne;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		new ExceedChecker().start();
		launched = true;
		clientRequestFromServer newRequest = new clientRequestFromServer(getAllUsers);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
		userTableView = new UserTableView(tblViewUsers,	colFirstName,
				colLastName, colEmail, colPermissions);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		allUsers.forEach(u -> {
					if (!u.getUserName().equals("admin")) {
						u.getICMPermissions().forEach(per -> {
							switch (per) {
								case inspector:
									if (tfInspector.getText().isEmpty()) {
										tfInspector.setText(u.getFullName());
									}
									break;
								case changeControlCommitteeChairman:
									if (tfChairman.getText().isEmpty()) {
										tfChairman.setText(u.getFullName());
									}
									break;
								case changeControlCommitteeMember:
									if (tfMember1.getText().isEmpty()) {
										tfMember1.setText(u.getFullName());
									}
									if (tfMember2.getText().isEmpty()) {
										tfMember2.setText(u.getFullName());
									}
									break;
							}
						});
					}
				});
		tblViewUsers.setOnMouseClicked(event -> {
			userForTable userForTable = tblViewUsers.getSelectionModel().getSelectedItem();
			if (userForTable != null) {
				chosenOne = userForTable.getOriginalUser();
				btnMember1.setDisable(false);
				btnMember2.setDisable(false);
				btnChairman.setDisable(false);
				btnInspector.setDisable(false);
			}
		});


		Platform.runLater(() -> userTableView.setData(allUsers));
	}

	public void setInspector() {
		removePermission(tfInspector, changeControlCommitteeChairman);
		tfInspector.setText(chosenOne.getFullName());
		chosenOne.addPermission(inspector);
		sendUpdateRequest();
	}

	public void setChairman() {
		removePermission(tfChairman, changeControlCommitteeChairman);
		tfChairman.setText(chosenOne.getFullName());
		chosenOne.addPermission(changeControlCommitteeChairman);
		sendUpdateRequest();
	}

	public void setFirstMember() {
		removePermission(tfMember1, changeControlCommitteeMember);
		tfMember1.setText(chosenOne.getFullName());
		chosenOne.addPermission(changeControlCommitteeMember);
		sendUpdateRequest();
	}
	public void setSecondMember() {
		removePermission(tfMember2, changeControlCommitteeMember);
		tfMember2.setText(chosenOne.getFullName());
		chosenOne.addPermission(changeControlCommitteeMember);
		sendUpdateRequest();
	}

	private void sendUpdateRequest() {
		clientRequestFromServer newRequest = new clientRequestFromServer(updateUser, user);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
	}

	private void removePermission(TextField chosenField, icmPermission perm) {
		allUsers.forEach(u -> {
			if(u.getFullName().equals(chosenField.getText())) {
				u.removePermission(perm);
			}
		});
	}

	private class ExceedChecker extends Thread {
		public void run() {
			try {
				while (launched) { //IcmClient.clientUI.equals(InformationTechnologiesDepartmentManagerForm.class
					getRequests();
					Thread.sleep(3000);
					changeRequests.forEach(req -> {
						ProcessStage processStage = req.getProcessStage();
						if (processStage.getDueDate() != null
								&& processStage.getDueDate().isAfter(LocalDate.now())
								&& processStage.getCurrentStage().equals(execution)) {
							alertWindowLauncher(Alert.AlertType.ERROR,
									"Requested time is exceed!",
									"Request #" + req.getRequestID() +
											"should've been finished before " + req.getProcessStage().getDueDate(),
									"Due time of request is exceed but it still in process");
						}
					});
				}
			} catch (Exception e) {
				System.out.println("Check exceeding thread is down.");
				e.printStackTrace();
			}
		}
	}
	public void getUserId() {}
	public void getRequestId() {}

}

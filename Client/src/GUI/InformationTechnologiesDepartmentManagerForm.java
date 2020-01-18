package GUI;

import Entity.*;
import Entity.RequestTableView.requirementForTable;
import Entity.User.icmPermission;
import Entity.UserTableView.userForTable;
import WindowApp.ClientLauncher;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static Entity.ProcessStage.ChargeRequestStages.execution;
import static Entity.User.collegeStatus.informationEngineer;
import static Entity.User.icmPermission.*;
import static Entity.clientRequestFromServer.requestOptions.*;

public class InformationTechnologiesDepartmentManagerForm extends UserForm {

	@FXML //Main side
	public Button btnRefresh;
	public Button btnDelayReport;
	public Button btnWatchDetails;
	public TabPane tpITDeptManager;
	public Button btnActivitiesReport;
	public Button btnPerformanceReport;
	@FXML //Tab Committee Accredit
	public Tab tabCommitteeAccredit;
	public TableView<userForTable> tblViewUsers;
	public TableColumn<userForTable, String> colFirstName1, colLastName1, colEmail1, colPermissions1;
	public Button btnInspector, btnChairman, btnMember1, btnMember2;
	public TextField tfInspector, tfChairman, tfMember1, tfMember2;
	@FXML //Tab IT Dept Permissions
	public Tab tabITDeptPermissions;
	public TableView<userForTable> tblViewEngineers;
	public TableColumn<userForTable, String> colFirstName2, colLastName2, colEmail2, colPermissions2;
	public Button btnSetPermissions;
	public CheckBox cbEstimator, cbExecLeader, cbExaminer;
	@FXML //Tab View Requests
	public Tab tabViewRequests;
	public TableView<requirementForTable> tblRequests;
	public TableColumn<requirementForTable, String> colID, colSystem;
	public TableColumn<requirementForTable, Object> colInitiator, colStartDate, colStatus, colStage, colDueTime;

	//Some useful variables
	private UserTableView userTableView;
	private User chosenOne;
	private userForTable userForTable;
	public static requirementForTable requirementForTable;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientLauncher.client.setClientUI(this);
		getUsers();
		new ExceedChecker().start();
		btnWatchDetails.setOnMouseClicked(event -> {
			requirementForTable = tblRequests.getSelectionModel().getSelectedItem();
			LaunchReportPopUp("/GUI/PopUpWindows/RequestDetails.fxml");
		});

		btnActivitiesReport.setOnMouseClicked(event ->
			LaunchReportPopUp("/GUI/PopUpWindows/ActivityReport.fxml"));

		btnDelayReport.setOnMouseClicked(event -> {
			//LaunchReportPopUp("/GUI/PopUpWindows/ActivityReport.fxml");
		});

		btnPerformanceReport.setOnMouseClicked(event -> {
			//LaunchReportPopUp("/GUI/PopUpWindows/ActivityReport.fxml");
		});

		tblRequests.setOnMouseClicked(e -> btnRefresh.setDisable(false));
		activeTab();
	}

	private void LaunchReportPopUp(String s) {
		try {
			popupWindowLauncher(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void activeTab() {
		switch (tpITDeptManager.getSelectionModel().getSelectedItem().getId()) {
			case "tabCommitteeAccredit":
				btnRefresh.setOnMouseClicked(event -> {
					btnWatchDetails.setDisable(true);
					initCommitteeAccredit();
				});
				initCommitteeAccredit();
				break;
			case "tabITDeptPermissions":
				btnRefresh.setOnMouseClicked(event -> {
					btnWatchDetails.setDisable(true);
					initITDeptPermissions();
				});
				initITDeptPermissions();
				break;
			case "tabViewRequests":
				btnRefresh.setOnMouseClicked(event -> {
					btnWatchDetails.setDisable(true);
					initViewRequests();
				});
				initViewRequests();
				break;
		}
	}

	private void initCommitteeAccredit() {
		userTableView = new UserTableView(tblViewUsers, colFirstName1,
				colLastName1, colEmail1, colPermissions1);
		btnInspector.setDisable(true);
		btnChairman.setDisable(true);
		btnMember1.setDisable(true);
		btnMember2.setDisable(true);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Platform.runLater(() ->	userTableView.setData(allUsers));

		allUsers.forEach(u -> {
			if (u.getICMPermissions().size() < 7) {
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
			userForTable = tblViewUsers.getSelectionModel().getSelectedItem();
			if (userForTable != null) {
				btnMember1.setDisable(false);
				btnMember2.setDisable(false);
				btnChairman.setDisable(false);
				btnInspector.setDisable(false);

				chosenOne = userForTable.getOriginalUser();
				chosenOne.getICMPermissions().forEach(p ->
				{
					switch (p) {
						case inspector:
							btnInspector.setDisable(true);
							break;
						case changeControlCommitteeChairman:
							btnChairman.setDisable(true);
							break;
						case changeControlCommitteeMember:
							if (tfMember1.getText().isEmpty()) {
								btnMember2.setDisable(true);
							}
							if (tfMember2.getText().isEmpty()) {
								btnMember1.setDisable(true);
							}
							break;
					}
				});
			}
		});

		btnInspector.setOnMouseClicked(getMouseEventEventHandler(tfInspector, changeControlCommitteeChairman, inspector));
		btnChairman.setOnMouseClicked(getMouseEventEventHandler(tfChairman, changeControlCommitteeChairman, changeControlCommitteeChairman));
		btnMember1.setOnMouseClicked(getMouseEventEventHandler(tfMember1, changeControlCommitteeMember, changeControlCommitteeMember));
		btnMember2.setOnMouseClicked(getMouseEventEventHandler(tfMember2, changeControlCommitteeMember, changeControlCommitteeMember));
	}

	private EventHandler<MouseEvent> getMouseEventEventHandler(TextField tfMember1, icmPermission changeControlCommitteeMember,
															   icmPermission changeControlCommitteeMember2) {
		return event -> {
			removePermission(tfMember1, changeControlCommitteeMember);
			tfMember1.setText(chosenOne.getFullName());
			chosenOne.addPermission(changeControlCommitteeMember2);
			sendUpdateRequest();

			activeTab();
		};
	}

	/**
	 * Tab tabITDeptPermissions;
	 * TableView<userForTable> tblViewEngineers;
	 * TableColumn<userForTable, String> colFirstName2, colLastName2, colEmail2, colPermissions2;
	 * Button btnSetEngineer;
	 * CheckBox cbEstimator, cbExecLeader, cbExaminer;
	 */
	private void initITDeptPermissions() {
		userTableView = new UserTableView(tblViewEngineers, colFirstName2,
				colLastName2, colEmail2, colPermissions2);
		userTableView.setData(allUsers);
		cbExaminer.setSelected(false);
		cbEstimator.setSelected(false);
		cbExecLeader.setSelected(false);
		cbExaminer.setDisable(true);
		cbEstimator.setDisable(true);
		cbExecLeader.setDisable(true);
		btnSetPermissions.setDisable(true);

		tblViewEngineers.setOnMouseClicked(event -> {
			cbExaminer.setDisable(false);
			cbEstimator.setDisable(false);
			cbExecLeader.setDisable(false);
			btnSetPermissions.setDisable(false);
			userForTable = tblViewEngineers.getSelectionModel().getSelectedItem();

			if (userForTable != null) {
				chosenOne = userForTable.getOriginalUser();
				chosenOne.getICMPermissions().forEach(p -> {
					switch (p) {
						case estimator:
							cbEstimator.setSelected(true);
							break;
						case examiner:
							cbExaminer.setSelected(true);
							break;
						case executionLeader:
							cbExecLeader.setSelected(true);
							break;
					}
				});
			}
		});

		btnSetPermissions.setOnMouseClicked(event -> {
			chosenOne.removePermission(estimator);
			chosenOne.removePermission(examiner);
			chosenOne.removePermission(executionLeader);
			if (cbEstimator.isSelected()) {
				chosenOne.addPermission(estimator);
			}
			if (cbExaminer.isSelected()) {
				chosenOne.addPermission(examiner);
			}
			if (cbExecLeader.isSelected()) {
				chosenOne.addPermission(executionLeader);
			}
			sendUpdateRequest();
			activeTab();
		});
	}

	/**
	 * Tab View Requests
	 * Tab tabViewRequests;
	 * TableView<requirementForTable> tblRequests;
	 * TableColumn<requirementForTable, String> colID;
	 * TableColumn<requirementForTable, String> colSystem;
	 * TableColumn<requirementForTable, String> colInitiator;
	 * TableColumn<requirementForTable, String> colStartDate;
	 * TableColumn<requirementForTable, String> colStatus;
	 * TableColumn<requirementForTable, String> colStage;
	 * TableColumn<requirementForTable, String> colDueTime;
	 */
	private void initViewRequests() {
		tblRequests.getItems().clear();
		RequestTableView requestTableView = new RequestTableView(tblRequests, colID, colStatus,
				colStage, colDueTime, colSystem, colInitiator, colStartDate);
		changeRequests.forEach(e ->	tblRequests.getItems().add(new requirementForTable(e)));
	}

	private void removePermission(TextField chosenField, icmPermission perm) {
		allUsers.forEach(u -> {
			if(u.getFullName().equals(chosenField.getText())) {
				u.removePermission(perm);
			}
		});
	}

	private void getUsers() {
		clientRequestFromServer newRequest = new clientRequestFromServer(getAllUsersByJob, informationEngineer);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
	}

	private void sendUpdateRequest() {
		clientRequestFromServer newRequest = new clientRequestFromServer(updateUser, chosenOne);
		ClientLauncher.client.handleMessageFromClientUI(newRequest);
	}

	private class ExceedChecker extends Thread {
		public void run() {
			launched = true;
			try {
				while (launched) {
					getRequests();
					Thread.sleep(10000);
					changeRequests.forEach(req -> {
						ProcessStage processStage = req.getProcessStage();
						if (processStage.getDueDate() != null
								&& LocalDate.now().isAfter(processStage.getDueDate())
								&& processStage.getCurrentStage().equals(execution)) {
							alertWindowLauncher(Alert.AlertType.WARNING,
									"Requested time is exceed!",
									"Request #" + req.getRequestID() +
											"should've been finished before " + req.getProcessStage().getDueDate(),
									"Due time of request is exceed but it still in process");
						}
					});
				}
			} catch (Exception e) {
				System.out.println("Exceed check thread is down!");
				e.printStackTrace();
			}
		}
	}
}

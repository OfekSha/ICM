package GUI.PopUpWindows;

import static Entity.clientRequestFromServer.requestOptions.updateProcessStage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Controllers.ExaminerController;
import Entity.clientRequestFromServer;
import Entity.ChangeRequest.ChangeRequestStatus;
import Entity.ProcessStage.ChargeRequestStages;
import Entity.ProcessStage.subStages;
import WindowApp.ClientLauncher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class failReportForm extends AbstractPopUp  {
	@FXML
	private TextArea taReport;

	@FXML
	private Button btnSend;

	@FXML
	void sendClicked(ActionEvent event) {
		processStage.setExaminerFailReport(taReport.getText());
		processStage.setEndDate(LocalDate.now());
		processStage.setCurrentStage(ChargeRequestStages.execution);
		processStage.setCurrentSubStage(subStages.supervisorAllocation);
		sendUpdateForRequest();
	}
	 public void initialize(URL location, ResourceBundle resources) {
	    	ClientLauncher.client.setClientUI(this);
	    	processStage=ExaminerController.selectedRequest.getProcessStage();
	    }
}

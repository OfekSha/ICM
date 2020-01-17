package GUI;

import Controllers.InspectorController;
import Entity.EstimatorReport;
import Entity.RequestTableView.requirementForTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class tabPaneInspectorForm    implements Initializable{

	@FXML
	private  TabPane tabPane;
	/**
	 * Initiator request information
	 */
	@FXML
	private TextArea baseForChange,requestDetails,requestReason,comment;

	/**
	 * Initiator request information
	 */
	@FXML
	private TextField informationSystem;


	/**
	 * Estimator report information
	 */
	@FXML 
	private TextArea location,changeDescription,desiredResult,constraints,risks;

	/**
	 * Estimator report information 
	 */
	@FXML
	private TextField dueTimeEstimate;

	/**
	 *  start date for each stage
	 */
	@FXML
	private TextField start1,start2,start3,start4,start5;
	/**
	 *  end date for each stage
	 */
	@FXML
	private TextField end1,end2,end3,end4,end5;
	/**
	 * check if was extension for each stage
	 */
	@FXML
	private CheckBox extension1,extension2,extension3,extension4,extension5;
	/**
	 * explain for extension for each stage
	 */
	@FXML
	private TextArea explain1,explain2,explain3,explain4,explain5;

	@FXML
	public DocumentTableForDownloadsForm DocumentTableController; // the document table with download capability

	public void onRequirementClicked(requirementForTable selectedReq) {
		// set details into tab pane:
		baseForChange.setText(InspectorController.selectedRequest.getBaseforChange());
		requestDetails.setText(InspectorController.selectedRequest.getProblemDescription());
		requestReason.setText(InspectorController.selectedRequest.getChangeReason());
		comment.setText(InspectorController.selectedRequest.getComment());
		informationSystem.setText(InspectorController.selectedRequest.getSystem());
		if (InspectorController.selectedRequest.getProcessStage().getEstimatorReport() != null) {
			EstimatorReport estimatorReport = InspectorController.selectedRequest.getProcessStage()
					.getEstimatorReport();
			location.setText(estimatorReport.getlocation());
			changeDescription.setText(estimatorReport.getChangeDescription());
			desiredResult.setText(estimatorReport.getResultingResult());
			constraints.setText(estimatorReport.getConstraints());
			risks.setText(estimatorReport.getRisks());
			dueTimeEstimate.setText(String.valueOf(estimatorReport.getTimeEstimate()));
			//dueTimeEstimate.setText(estimatorReport.getTimeEstimate().toString());
		}
		String[] explanations = selectedReq.getStage().getAllExtensionExplanation();
		LocalDate[][] allDates = selectedReq.getStage().getDates();
		
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[0] == 2) {
			start1.setText(allDates[0][0].toString());
			end1.setText(allDates[0][2].toString());
			explain1.setText(explanations[0]);
			extension1.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[1] == 2) {
			start2.setText(allDates[1][0].toString());
			end2.setText(allDates[1][2].toString());
			explain2.setText(explanations[1]);
			extension2.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[2] == 2) {
			start3.setText(allDates[2][0].toString());
			end3.setText(allDates[2][2].toString());
			explain3.setText(explanations[2]);
			extension3.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[3] == 2) {
			start4.setText(allDates[3][0].toString());
			end4.setText(allDates[3][2].toString());
			explain4.setText(explanations[3]);
			extension4.setSelected(true);
		}
		if (selectedReq.getStage().getWasThereAnExtensionRequest()[4] == 2) {
			start5.setText(allDates[4][0].toString());
			end5.setText(allDates[4][2].toString());
			explain5.setText(explanations[4]);
			extension5.setSelected(true);
		}

		// setting up the document table
		DocumentTableController.onRequirementTableClick(InspectorController.selectedRequest);
	}
	private void resetDetails() {
		DocumentTableController.onRequirementTableClick(null);
		baseForChange.setText(null);
		requestDetails.setText(null);
		requestReason.setText(null);
		comment.setText(null);
		informationSystem.setText(null);
		start1.setText("");
		end1.setText("");
		explain1.setText("");
		extension1.setSelected(false);
		start2.setText("");
		end2.setText("");
		explain2.setText("");
		extension2.setSelected(false);
		start3.setText("");
		end3.setText("");
		explain3.setText("");
		extension3.setSelected(false);
		start4.setText("");
		end4.setText("");
		explain4.setText("");
		extension4.setSelected(false);
		start5.setText("");
		end5.setText("");
		explain5.setText("");
		extension5.setSelected(false);
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}



}

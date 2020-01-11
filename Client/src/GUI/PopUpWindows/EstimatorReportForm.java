package GUI.PopUpWindows;


import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import Controllers.EstimatorController;
import WindowApp.IcmForm;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

public class EstimatorReportForm extends AbstractPopUp implements IcmForm {
	@FXML
	private TextArea location;
	@FXML
	private TextArea changeDescription;
	@FXML
	private TextArea desiredResult;
	@FXML
	private TextArea constraints;
	@FXML
	private TextArea risks;
	@FXML
	private TextField dueTimeEstimate;

	@Override
	public void getFromServer(Object message) {
		// TODO Auto-generated method stub

	}
	@FXML
	private void sendReport() {
		EstimatorController.setReport(EstimatorController.selectedRequest, location.getText(), changeDescription.getText(), desiredResult.getText(), constraints.getText(), risks.getText());
		getCancel();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// set due time text field could be only numbers.
		UnaryOperator<Change> filter = change -> {
		    String text = change.getText();

		    if (text.matches("[0-9]*") && (change.getControlText().length()<3 || text.matches(""))) {
		        return change;
		    }

		    return null;
		};
		TextFormatter<String> textFormatter = new TextFormatter<>(filter);
		dueTimeEstimate.setTextFormatter(textFormatter);
		
	}

}

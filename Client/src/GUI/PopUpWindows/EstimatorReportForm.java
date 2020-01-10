package GUI.PopUpWindows;


import java.net.URL;
import java.util.ResourceBundle;

import WindowApp.IcmForm;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dueTimeEstimate.setTextFormatter(new TextFormatter<Integer>(c->{
			return c;
		}) );
		
	}

}

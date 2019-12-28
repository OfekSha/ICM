package GUI;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import WindowApp.IcmForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class SubmitRequestForm extends UserForm implements Initializable, IcmForm {
	
	ObservableList<String> list;
	@FXML
	public TextArea taRequestDetails;
	@FXML
	public TextArea taRequestReason;
	@FXML
	public TextArea taComment;
	@FXML
	private Button btnSubmit;
	@FXML
	private ComboBox cmbSystems;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		setSystemsComboBox() ;

	}


	@SuppressWarnings("unchecked")
	public void setSystemsComboBox() 
	{
		ArrayList<String> al = new ArrayList<String>();	
	al.add("ICM");
	al.add("Moodle");
	al.add("Information Station");
	list = FXCollections.observableArrayList(al);
	cmbSystems.setItems(list);
	}
	
	//TODO: the following  methods are from the class diagram:  
public void getRequestData() {}
public void getRequestID() {}
	
}



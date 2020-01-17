package GUI;

import Controllers.StageSupervisorController;
import Entity.ChangeRequest;
import Entity.RequestTableView;
import Entity.RequestTableView.requirementForTable;
import GUI.PopUpWindows.DueTimeController;
import GUI.PopUpWindows.GetExtensionController;
import WindowApp.ClientLauncher;
import WindowApp.IcmForm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * 
 * Each stage has form for supervisor.<br>
 * with FXML components:
 * <li>MenuButton - menubtnWatch</li>
 * <li>MenuItem - DueTimeFilter, ActionFilter</li>
 * <li>TableView<requirementForTable> - tblView</li>
 * <li>TableColumn<requirementForTable, String> - idColumn, messageColumn</li>
 * <li>TableColumn<requirementForTable, Object> - statusColumn, dueTimeColumn</li>
 * @author Ofek
 *
 */
public abstract class StageSupervisorForm extends UserForm {
	@FXML
	public MenuButton menubtnWatch; // the filter button
	public StageSupervisorController controller;
	@FXML
	public MenuItem DueTimeFilter, ActionFilter; // for filter requests
	@FXML
	public TableView<requirementForTable> tblView; // table view of requests
	@FXML
	public TableColumn<requirementForTable, String> idColumn, messageColumn; // columns of table
	@FXML
	public TableColumn<requirementForTable, Object> statusColumn, dueTimeColumn; // columns of table
	@FXML
	public Button btnSetDueTime, btnAskForTimeExtension; // the actions for every supervisor.
	public static RequestTableView table; // make request adaptable for table view.
	public static IcmForm icmForm; // use for connections and set up.
	private static Stage popupWindow;

	abstract public StageSupervisorController getController(); // need to setup controller
	abstract public IcmForm getIcmForm(); // need to set up icm form.
	abstract public void initialize(URL location, ResourceBundle resources);

	@FXML
	abstract public void filterRequests(ActionEvent event); // use for filter by MenuItem

	@FXML
	protected abstract void onRequestClicked(MouseEvent event); // save the selected request and enable buttons.

	@Override
	public abstract void getFromServer(Object message); // get messages from server

	public StageSupervisorForm() {
		controller = getController();
		icmForm = getIcmForm();
	}

	/**
	 * This method create pop up window by source and set connection back when closed.
	 *
	 * @param target - the source fxml file.
	 * @throws IOException if the load failed.
	 */
	public void popupWindow(String target) throws IOException {
		if (popupWindow != null) popupWindow.close(); // can't be 2 pop up windows.
		popupWindow = new Stage();
		Parent root = FXMLLoader.load(this.getClass().getResource(target));
		Scene scene = new Scene(root);
		popupWindow.setScene(scene);
		popupWindow.initModality(Modality.APPLICATION_MODAL); // lock previous windows
		popupWindow.show();

		// what happened when close window from out or from stage.close / stage.hide
		// method
		popupWindow.setOnCloseRequest(windowEvent -> // close from out (alt +f4)
				ClientLauncher.client.setClientUI(icmForm));
		// stage.close / stage.hide method:
		popupWindow.setOnHidden(we -> ClientLauncher.client.setClientUI(icmForm));
	}

	/**
	 * This method for button extension and used pop up window method for open GetExtension.fxml
	 *
	 * @param event
	 * @see popupWindow
	 */
	@FXML
	public void askExtensionClicked(ActionEvent event) {
		try {
			// set the stage to the extension controller:
			GetExtensionController.processStage = getSelectedReq().getProcessStage();
			popupWindow("/GUI/PopUpWindows/GetExtension.fxml");

		} catch (IOException e) {
			System.out.println(e.getCause());
		}
	}


	@FXML
	public void setDueTimeClicked(ActionEvent event) {
		try {
			// not work because the fxml work only with execution leader.
			DueTimeController.processStage = getSelectedReq().getProcessStage();
			popupWindow("/GUI/PopUpWindows/DeterminingDueTime.fxml");
		} catch (IOException e) {
			System.out.println(e.getCause());
		}
	}

	abstract public ChangeRequest getSelectedReq(); // get selected request important for abstract functions.
}
package Entity;

import Entity.ChangeRequest.ChangeRequestStatus;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * 
 * Build Table View use the FXML components from this class only.
 * 
 *  make new class with no parametrs and you will get the table ready for use.
 *  
 *  use function onRequirementClicked for get selected row in the table.
 * 
 * @author ooffe
 *
 */
public class RequestTableView {
	@FXML
	public TableView<requirementForTable> tblviewRequests;
	// table colums:
	@FXML
	public TableColumn<requirementForTable, String> columnId;
	@FXML
	public TableColumn<requirementForTable, Object> columnStatus;
	@FXML
	public TableColumn<requirementForTable, Object> columnStage;
	@FXML
	public TableColumn<requirementForTable, Object> columnDueTime;
	@FXML
	public TableColumn<requirementForTable, String> columnMessage;

	@FXML
	public TableColumn<requirementForTable, Object> columninitiator;
	@FXML
	public TableColumn<requirementForTable, Object> columnStartDate;

	@FXML
	public TableColumn<requirementForTable, String> columnSystem;

	@FXML
	public TableColumn<requirementForTable, String> columnProblemDescription;
	@FXML
	public TableColumn<requirementForTable, String> columnWhyChange;
	@FXML
	public TableColumn<requirementForTable, String> columnComment;
	@FXML
	public TableColumn<requirementForTable, Object> columnDoc;

	public RequestTableView() {
		initializeTableView();
	}

	/**
	 * 
	 * This function match the columns for the table(FXML) to the class requirementForTable.
	 * 
	 */
	private void initializeTableView() {
		columnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
		columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		columnStage.setCellValueFactory(new PropertyValueFactory<>("stage"));
		columnDueTime.setCellValueFactory(new PropertyValueFactory<>("dueTime"));

		columninitiator.setCellValueFactory(new PropertyValueFactory<>("initiator"));

		columnStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

		columnSystem.setCellValueFactory(new PropertyValueFactory<>("system"));

		columnProblemDescription
				.setCellValueFactory(new PropertyValueFactory<>("problemDescription"));

		columnWhyChange.setCellValueFactory(new PropertyValueFactory<>("whyChange"));

		columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));

		columnDoc.setCellValueFactory(new PropertyValueFactory<>("doc"));

	}

	/**
	 * 
	 * This function return the selected request in the table.
	 * 
	 * @param event 
	 *  - mouse event (onClick for example)
	 * @return requirementForTable
	 * 
	 *  - this is special class for table for get the object requirementForTable.getObject();
	 */
	public requirementForTable onRequirementClicked(MouseEvent event) {
		return tblviewRequests.getSelectionModel().getSelectedItem();
	}

	/**
	 * 
	 * Special class for Table View.
	 * 
	 * @author Ofek
	 *
	 */
	public static class requirementForTable {
		private SimpleStringProperty message;
		private SimpleStringProperty id;
		private SimpleObjectProperty<ChangeRequestStatus> status;
		private SimpleObjectProperty<ProcessStage> stage;
		private SimpleObjectProperty<LocalDate> dueTime;
		//
		private SimpleObjectProperty<Initiator> initiator;
		private SimpleObjectProperty<LocalDate> startDate;
		private SimpleStringProperty system;

		private SimpleStringProperty problemDescription;
		private SimpleStringProperty whyChange;
		private SimpleStringProperty comment;

		private SimpleObjectProperty<ArrayList<Document>> doc;

		public String getMessage() {
			return message.get();
		}

		public String getId() {
			return id.get();
		}

		public ChangeRequestStatus getStatus() {
			return status.get();
		}

		public ProcessStage getStage() {
			return stage.get();
		}

		public LocalDate getDueTime() {
			return dueTime.get();
		}

		//
		public Initiator getInitiator() {
			return initiator.get();
		}

		public LocalDate getStartDate() {
			return startDate.get();
		}

		public String getSystem() {
			return system.get();
		}

		public String getProblemDescription() {
			return problemDescription.get();
		}

		public String getWhyChange() {
			return whyChange.get();
		}

		public String getComment() {
			return comment.get();
		}

		public ArrayList<Document> getDoc() {
			return doc.get();
		}

		/**
		 * 
		 * constructor for requirementForTable.
		 * use SimpleStringProperty and SimpleObjectProperty
		 * very important.
		 * 
		 * @param req ?
		 */
		public requirementForTable(ChangeRequest req) {
			int stageNumber = req.getProcessStage().getCurrentStage().ordinal();
			id = new SimpleStringProperty(req.getRequestID());
			status = new SimpleObjectProperty<>(req.getStatus());
			message = new SimpleStringProperty("test");
			stage = new SimpleObjectProperty<>(req.getProcessStage());
			dueTime = new SimpleObjectProperty<>(req.getProcessStage().getDates()[stageNumber][1]);
			//
			initiator = new SimpleObjectProperty<>(req.getInitiator());
			startDate = new SimpleObjectProperty<>(req.getStartDate());
			system = new SimpleStringProperty(req.getSystem());
			problemDescription = new SimpleStringProperty(req.getProblemDescription());
			whyChange = new SimpleStringProperty(req.getChangeReason());
			comment = new SimpleStringProperty(req.getComment());
			doc = new SimpleObjectProperty<>(req.getDoc());
		}
	}
}

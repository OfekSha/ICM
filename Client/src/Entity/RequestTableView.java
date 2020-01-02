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

/**
 * 
 * Build Table View use the FXML components from this class only.
 * 
 *  make new class with no parametrs and you will get the table ready for use.
 *  
 *  use function onRequirmentClicked for get selected row in the table.
 * 
 * @author ooffe
 *
 */
public class RequestTableView {
	@FXML
	public TableView<requirmentForTable> tblviewRequests;
	// table colums:
	@FXML
	public TableColumn<requirmentForTable, String> columnId;
	@FXML
	public TableColumn<requirmentForTable, Object> columnStatus;
	@FXML
	public TableColumn<requirmentForTable, Object> columnStage;
	@FXML
	public TableColumn<requirmentForTable, Object> columnDueTime;
	@FXML
	public TableColumn<requirmentForTable, String> columnMessage;

	@FXML
	public TableColumn<requirmentForTable, Object> columninitiator;
	@FXML
	public TableColumn<requirmentForTable, Object> columnStartDate;

	@FXML
	public TableColumn<requirmentForTable, String> columnSystem;

	@FXML
	public TableColumn<requirmentForTable, String> columnProblemDescription;
	@FXML
	public TableColumn<requirmentForTable, String> columnWhyChange;
	@FXML
	public TableColumn<requirmentForTable, String> columnComment;
	@FXML
	public TableColumn<requirmentForTable, Object> columnDoc;

	public RequestTableView() {
		initializeTableView();

	}

	/**
	 * 
	 * This function match the columns for the table(FXML) to the class requirmentForTable.
	 * 
	 */
	private void initializeTableView() {
		columnMessage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("message"));
		columnId.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("id"));
		columnStatus.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("status"));
		columnStage.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("stage"));
		columnDueTime.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("dueTime"));

		columninitiator.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("initiator"));

		columnStartDate.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("startDate"));

		columnSystem.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("system"));

		columnProblemDescription
				.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("problemDescription"));

		columnWhyChange.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("whyChange"));

		columnComment.setCellValueFactory(new PropertyValueFactory<requirmentForTable, String>("comment"));

		columnDoc.setCellValueFactory(new PropertyValueFactory<requirmentForTable, Object>("doc"));

	}

	/**
	 * 
	 * This function return the selected request in the table.
	 * 
	 * @param event 
	 *  - mouse event (onClick for example)
	 * @return requirementForTable
	 * 
	 *  - this is special class for table for get the object requirmentForTable.getObject();
	 * @throws Exception
	 */
	public requirmentForTable onRequirmentClicked(MouseEvent event) throws Exception {
		requirmentForTable selectedReq = tblviewRequests.getSelectionModel().getSelectedItem();
		return selectedReq;
	}

	/**
	 * 
	 * Special class for Table View.
	 * 
	 * @author Ofek
	 *
	 */
	public static class requirmentForTable {
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

		private SimpleObjectProperty<Document> doc;

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

		public Document getDoc() {
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
		public requirmentForTable(ChangeRequest req) {
			int stageNumber = req.getProcessStage().getCurrentStage().ordinal();
			id = new SimpleStringProperty(req.getRequestID());
			status = new SimpleObjectProperty<ChangeRequestStatus>(req.getStatus());
			message = new SimpleStringProperty("test");
			stage = new SimpleObjectProperty<ProcessStage>(req.getProcessStage());
			dueTime = new SimpleObjectProperty<LocalDate>(req.getProcessStage().getDates()[stageNumber][1]);
			//
			initiator = new SimpleObjectProperty<Initiator>(req.getInitiator());
			startDate = new SimpleObjectProperty<LocalDate>(req.getStartDate());
			system = new SimpleStringProperty(req.getSystem());
			problemDescription = new SimpleStringProperty(req.getProblemDescription());
			whyChange = new SimpleStringProperty(req.getChangeReason());
			comment = new SimpleStringProperty(req.getComment());
			doc = new SimpleObjectProperty<Document>(req.getDoc());
		}

	}

}

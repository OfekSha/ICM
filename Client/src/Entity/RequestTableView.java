package Entity;

import Entity.ChangeRequest.ChangeRequestStatus;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * 
 * Build Table View use the FXML components from this class only.<br>
 *  make new class with no parameters and you will get the table ready for use.<br>
 *  use function onRequirementClicked for get selected row in the table.<br>
 *  list of fxml can use:
 *  <li> tblViewRequests - the table</li>
 * 	<li>columnId</li>
 *  <li>columnStatus</li>
 * 	<li>columnStage</li>
 *  <li>columnDueTime</li>
 *  <li>columnMessage</li>
 *  <li>columnInitiator</li>
 *  <li>columnStartDate</li>
 *  <li>columnSystem</li>
 *  <li>columnProblemDescription</li>
 *  <li>columnWhyChange</li>
 *  <li>columnComment</li>
 *  <li>columnDoc</li>
 *
 *
 */
public class RequestTableView {
	public TableView<requirementForTable> tblViewRequests;
	// table columns:
	public TableColumn<requirementForTable, String> columnId;
	public TableColumn<requirementForTable, String> columnMessage;
	public TableColumn<requirementForTable, String> columnSystem;
	public TableColumn<requirementForTable, String> columnProblemDescription;
	public TableColumn<requirementForTable, String> columnWhyChange;
	public TableColumn<requirementForTable, String> columnComment;

	public TableColumn<requirementForTable, Object> columnStatus;
	public TableColumn<requirementForTable, Object> columnStage;
	public TableColumn<requirementForTable, Object> columnDueTime;
	public TableColumn<requirementForTable, Object> columnInitiator;
	public TableColumn<requirementForTable, Object> columnStartDate;
	public TableColumn<requirementForTable, Object> columnDoc;

	public RequestTableView(TableView<requirementForTable> tblViewRequests,
							TableColumn<requirementForTable, String> columnId,
							TableColumn<requirementForTable, Object> columnStatus,
							TableColumn<requirementForTable, Object> columnStage,
							TableColumn<requirementForTable, Object> columnDueTime,
							TableColumn<requirementForTable, String> columnSystem,
							TableColumn<requirementForTable, Object> columnInitiator,
							TableColumn<requirementForTable, Object> columnStartDate) {
		this.tblViewRequests = tblViewRequests;
		this.columnId = columnId;
		this.columnSystem = columnSystem;
		this.columnStatus = columnStatus;
		this.columnStage = columnStage;
		this.columnDueTime = columnDueTime;
		this.columnInitiator = columnInitiator;
		this.columnStartDate = columnStartDate;
		initializeTableView();
	}

	public RequestTableView(TableView<requirementForTable> tblViewRequests,
							TableColumn<requirementForTable, String> columnId,
							TableColumn<requirementForTable, Object> columnStatus,
							TableColumn<requirementForTable, Object> columnDueTime) {
		this.tblViewRequests = tblViewRequests;
		this.columnId = columnId;
		this.columnStatus = columnStatus;
		this.columnDueTime = columnDueTime;
		initializeTableView();
	}

	/**
	 * Initialize the table and the properties of the columns.<br>
	 * To insert data use method setData.
	 */
	public RequestTableView() {
		initializeTableView();
	}
	/**
	 * Initialize the table and the properties of the columns.<br>
	 * If you don't use one of the column send null.<br>
	 * To insert data use method setData.
	 * @param table - fxml table view
	 * @param id - id fxml column.
	 * @param status -status fxml column.
	 * @param stage - stage fxml column.
	 * @param dueTime - due time fxml column.
	 * @param message - message fxml column.
	 */
	public RequestTableView(TableView<requirementForTable> table,
							TableColumn<requirementForTable, String> id,
							TableColumn<requirementForTable, Object> status,
							TableColumn<requirementForTable, Object> stage,
							TableColumn<requirementForTable, Object> dueTime,
							TableColumn<requirementForTable, String> message) {
		tblViewRequests = table;
		columnId = id;
		columnStatus = status;
		columnStage = stage;
		columnDueTime = dueTime;
		columnMessage = message;
		initializeTableView();
	}
	/**
	 * This method set data into the table.
	 * @param requests - arrayList of requests (changeRequest)
	 */
	public void setData(ArrayList <ChangeRequest> requests) {
		ObservableList<requirementForTable> tableData;
		tableData = FXCollections.observableArrayList(requirementForTableList(requests));
		tblViewRequests.setItems(tableData);
	}
	/**
	 * This method build new array list from requests that adapt to table view.
	 * @param reqList - arrayList of all requests (changeRequest)
	 * @return newList - arrayList of requirementForTable
	 */
	private static ArrayList<requirementForTable> requirementForTableList(ArrayList<ChangeRequest> reqList) {
		ArrayList<requirementForTable> newList = new ArrayList<>();
		reqList.forEach(req -> newList.add(new requirementForTable(req)));
		return newList;
	}

	/**
	 * 
	 * This function match the columns for the table(FXML) to the class requirementForTable.
	 * 
	 */
	private void initializeTableView() {
		if (columnMessage != null) {
			columnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
		}
		if (columnId != null) {
			columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		}
		if (columnStatus != null) {
			columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		}
		if (columnStage != null) {
			columnStage.setCellValueFactory(new PropertyValueFactory<>("stage"));
		}
		if (columnDueTime != null) {
			columnDueTime.setCellValueFactory(new PropertyValueFactory<>("dueTime"));
		}
		if (columnInitiator != null) {
			columnInitiator.setCellValueFactory(new PropertyValueFactory<>("initiator"));
		}
		if (columnStartDate != null) {
			columnStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		}
		if (columnSystem != null) {
			columnSystem.setCellValueFactory(new PropertyValueFactory<>("system"));
		}
		if (columnProblemDescription != null) {
			columnProblemDescription.setCellValueFactory(new PropertyValueFactory<>("problemDescription"));
		}
		if (columnWhyChange != null) {
			columnWhyChange.setCellValueFactory(new PropertyValueFactory<>("whyChange"));
		}
		if (columnComment != null) {
			columnComment.setCellValueFactory(new PropertyValueFactory<>("comment"));
		}
		if (columnDoc != null) {
			columnDoc.setCellValueFactory(new PropertyValueFactory<>("doc"));
		}
	}

	/**
	 * 
	 * This function return the selected request in the table.
	 * 
	 * @param event - mouse event (onClick for example)
	 * @return requirementForTable - this is special class for table for get the object requirementForTable.getObject();
	 */
	public requirementForTable onRequirementClicked(MouseEvent event) {
		return tblViewRequests.getSelectionModel().getSelectedItem();
	}

	/**
	 * 
	 * Special class for Table View.
	 */
	public static class requirementForTable {
		private ChangeRequest originalRequest; // this is the original request before make it adaptable to table view
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

		/**
		 * 
		 * constructor for requirementForTable.
		 * use SimpleStringProperty and SimpleObjectProperty
		 * very important.
		 * 
		 * @param req - ChangeRequest
		 */
		public requirementForTable(ChangeRequest req) {
			originalRequest = req;
			int stageNumber = req.getProcessStage().getCurrentStage().ordinal();
			id = new SimpleStringProperty(String.valueOf(req.getRequestID()));
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

		public ChangeRequest getOriginalRequest() {
			return originalRequest;
		}

		public void setOriginalRequest(ChangeRequest originalRequest) {
			this.originalRequest = originalRequest;
		}

		public String getMessage() {
			return message.get();
		}

		public SimpleStringProperty messageProperty() {
			return message;
		}

		public void setMessage(String message) {
			this.message.set(message);
		}

		public String getId() {
			return id.get();
		}

		public SimpleStringProperty idProperty() {
			return id;
		}

		public void setId(String id) {
			this.id.set(id);
		}

		public ChangeRequestStatus getStatus() {
			return status.get();
		}

		public SimpleObjectProperty<ChangeRequestStatus> statusProperty() {
			return status;
		}

		public void setStatus(ChangeRequestStatus status) {
			this.status.set(status);
		}

		public ProcessStage getStage() {
			return stage.get();
		}

		public SimpleObjectProperty<ProcessStage> stageProperty() {
			return stage;
		}

		public void setStage(ProcessStage stage) {
			this.stage.set(stage);
		}

		public LocalDate getDueTime() {
			return dueTime.get();
		}

		public SimpleObjectProperty<LocalDate> dueTimeProperty() {
			return dueTime;
		}

		public void setDueTime(LocalDate dueTime) {
			this.dueTime.set(dueTime);
		}

		public Initiator getInitiator() {
			return initiator.get();
		}

		public SimpleObjectProperty<Initiator> initiatorProperty() {
			return initiator;
		}

		public void setInitiator(Initiator initiator) {
			this.initiator.set(initiator);
		}

		public LocalDate getStartDate() {
			return startDate.get();
		}

		public SimpleObjectProperty<LocalDate> startDateProperty() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate.set(startDate);
		}

		public String getSystem() {
			return system.get();
		}

		public SimpleStringProperty systemProperty() {
			return system;
		}

		public void setSystem(String system) {
			this.system.set(system);
		}

		public String getProblemDescription() {
			return problemDescription.get();
		}

		public SimpleStringProperty problemDescriptionProperty() {
			return problemDescription;
		}

		public void setProblemDescription(String problemDescription) {
			this.problemDescription.set(problemDescription);
		}

		public String getWhyChange() {
			return whyChange.get();
		}

		public SimpleStringProperty whyChangeProperty() {
			return whyChange;
		}

		public void setWhyChange(String whyChange) {
			this.whyChange.set(whyChange);
		}

		public String getComment() {
			return comment.get();
		}

		public SimpleStringProperty commentProperty() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment.set(comment);
		}

		public ArrayList<Document> getDoc() {
			return doc.get();
		}

		public SimpleObjectProperty<ArrayList<Document>> docProperty() {
			return doc;
		}

		public void setDoc(ArrayList<Document> doc) {
			this.doc.set(doc);
		}
	}
}

package GUI;

import Controllers.DocmentTableForDownloadsController;
import Entity.ChangeRequest;
import Entity.DocumentForTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DocumentTableForDownloadsForm {

	@FXML
	public TableView<DocumentForTable> tblViewDocuments;

	@FXML
	public TableColumn<DocumentForTable, String> columnFileName;

	@FXML
	public TableColumn<DocumentForTable, String> columnFileSize;

	@FXML
	public Button btnDownload;

	public DocmentTableForDownloadsController controller = new DocmentTableForDownloadsController();

	public void onRequirementTableClick(ChangeRequest selectedRequest) {
		ObservableList<DocumentForTable> documentTableData = FXCollections
				.observableArrayList(controller.DocumentForTableList(selectedRequest));
		tblViewDocuments.setItems(documentTableData);
	}// END of onRequirementTableClick()

	/**
	 * setting up the document table columns
	 */
	public void initializeDocumentTableView() {

		columnFileName.setCellValueFactory(new PropertyValueFactory<>("name")); // set values for id
		columnFileSize.setCellValueFactory(new PropertyValueFactory<>("size")); // set values

	} //END of initializeDocumentTableView()

	public void pressedDownload() {
		DocumentForTable selectedDoc = tblViewDocuments.getSelectionModel().getSelectedItem();
		if (selectedDoc != null) {
			controller.askForDownload(selectedDoc.gettheDoc());
		}

	} //END of pressedDownload()
}

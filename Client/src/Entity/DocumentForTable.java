package Entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

/** class made for representing the change requests attached documents 
 * 
 *
 */

public class DocumentForTable {
	private SimpleStringProperty name;
	private SimpleStringProperty size;
	private Document theDoc;

	public String getName() {
		return name.get();
	}

	public String getSize() {
		return size.get();
	}

	/**
	 * @return The document we are representing in the table raw
	 */
	public Document gettheDoc() {
		return theDoc;
	}


	public DocumentForTable(Document doc) {
		name = new SimpleStringProperty(doc.getFileName());
		size = new SimpleStringProperty(Double.toString((double) doc.getSize() / 1E6));
		theDoc = doc;
	}

	public static void setTableProperties(TableColumn<DocumentForTable, String> nameColumn, TableColumn<DocumentForTable, String> sizeColumn) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<DocumentForTable, String>("name"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<DocumentForTable, String>("size"));
	}

	public static ArrayList<DocumentForTable> createDocForTableArrayList(ArrayList<Document> docs) {
		ArrayList<DocumentForTable> newList = new ArrayList<DocumentForTable>();
		for (Document doc : docs) {
			newList.add(new DocumentForTable(doc));
		}
		return newList;
	}
}
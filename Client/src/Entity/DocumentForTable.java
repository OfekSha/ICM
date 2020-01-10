package Entity;

import javafx.beans.property.SimpleStringProperty;

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
		name  =new SimpleStringProperty(doc.getFileName());
		size =  new SimpleStringProperty( Double.toString((double)doc.getSize()/1E6));
		theDoc =doc;
	}

}
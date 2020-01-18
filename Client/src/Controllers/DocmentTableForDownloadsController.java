package Controllers;

import Entity.ChangeRequest;
import Entity.Document;
import Entity.DocumentForTable;
import Entity.clientRequestFromServer;
import Entity.clientRequestFromServer.requestOptions;
import WindowApp.ClientLauncher;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DocmentTableForDownloadsController {
	public static Thread inspetor;
	public static Document downloded;

	/**
	 * Saves the the lunched thread and puts it to  sleep
	 * <p>
	 * - saves it so  wakeUpLunchedThread would be able to wake it up
	 */
	public void putLunchedThreadToSleep() {
		inspetor = Thread.currentThread();
		try {
			inspetor.sleep(9999999);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Wakes up the lunched thread
	 */
	public static void wakeUpLunchedThread() {
		try {
			inspetor.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * creates the list of documents witch are attached to the  change request for the table
	 *
	 * @return DocumentForTable array list for table
	 * @see DocumentForTable
	 */
	public ArrayList<DocumentForTable> DocumentForTableList(ChangeRequest selectedRequest) {
		if (selectedRequest == null) return null;
		ArrayList<DocumentForTable> newList = new ArrayList<>();
		for (Document doc : selectedRequest.getDoc())
			newList.add(new DocumentForTable(doc));
		return newList;
	} //END  of DocumentForTableList() 

	public void askForDownload(Document doc) {
		ClientLauncher.client.handleMessageFromClientUI(new clientRequestFromServer(requestOptions.getDoc, doc));
		putLunchedThreadToSleep();
		Download();
	} //END of askForDownload()

	public void Download() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName(downloded.getFileName());
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			try {
				OutputStream os = new FileOutputStream(file);
				// Starts writing the bytes in it
				os.write(downloded.mybytearray);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} //END of Download()
}

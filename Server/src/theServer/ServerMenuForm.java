package theServer;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerMenuForm implements Initializable {

	@FXML
	private Button btnStartTheServer;

	@FXML
	private Button btnCloseTheServer;

	@FXML
	private Button btnResetDB;

	@FXML
	private Button btnExit;

	@FXML
	private TextArea Messages;

	@FXML
	private Button btnRefreshMessges;

	// vars
	public PrintStream ps;
	public PrintStream old;
	public static EchoServer   echo;
	
	private static double xOffset = 0;
	private static double yOffset = 0;

	public void start(Stage primaryStage) throws Exception {
		// scene
		Parent root = FXMLLoader.load(getClass().getResource("/theServer/ServerMenu.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server menu");
		primaryStage.setScene(scene);
//set style
		primaryStage.initStyle(StageStyle.UNDECORATED);
		root.setOnMousePressed(event -> {
			xOffset = event.getSceneX();
			yOffset = event.getSceneY();
		});
		root.setOnMouseDragged(event -> {
			primaryStage.setX(event.getScreenX() - xOffset);
			primaryStage.setY(event.getScreenY() - yOffset);
		});
		primaryStage.show();

	}

	/** moving the output to the messages text box and setting it up .
	 *@see MessgesOutputStream
	 */
	public void initialize(URL location, ResourceBundle resources) {
		
		// we dont want its size to change only scrolling
		ChangeListener <String> arg0 = (observable, oldValue, newValue) -> Messages.setPrefWidth(Messages.getText().length() * 7);
		Messages.textProperty().addListener(arg0);
		
		// taking over the consul
		ps = new PrintStream(new MessgesOutputStream(Messages));
		old = System.out;
		System.setOut(ps);
		// making sure echo is empty for the servers opening 
		echo =null;
		
		//btnCloseTheServer.setDisable(true); 
		}

	/** ending the server gui and the server 
	 * @param event ?
	 */
	@FXML
	void ExitBtn(ActionEvent event) {
		// resetting the console
		
		System.out.flush();
		System.setOut(old);	
		stopeServer(event);
		System.exit(0);
	}

	/** starts the server
	 * @param event ?
	 */
	@FXML
	void startServer(ActionEvent event) {
		if(echo != null) { 
			System.out.print("there is a echo instans  close it \n");
			return;
		}
	
			EchoServer.StartOcfServer(ServerMenuController.theArgs);

	//	btnStartTheServer.setDisable(true);
	//	btnCloseTheServer.setDisable(false);
	}

	/** turning the server off 
	 * @param event ?
	 */
	@FXML
	void stopeServer(ActionEvent event) {
		if(echo == null) { 
			System.out.print("there is no echo instans \n");
			return;}
		try {
			echo.close();
			echo=null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//btnStartTheServer.setDisable(false);
		//btnCloseTheServer.setDisable(true);
	}

	
	/**cleans the messeges text box
	 * @param event ?
	 */
	@FXML
	void RefreshMessages(ActionEvent event) {
		refreshMessages();
	}

	/** assisting : RefreshMessages,
	 * 
	 * //@see RefreshMessages
	 * */
	public void refreshMessages() {
		Messages.setText("");
	}
	/** Rebuilds the DB with examples 
	 * @param event ?
	 * @see mysqlConnection
	 */
	@FXML
	public void reSetDB(ActionEvent event) {
		mysqlConnection mysqlConn;
		if (echo == null)
			mysqlConn = new mysqlConnection();
		else
		{
			//TODO : add are you sure you want to reset during running server  the run
			mysqlConn = echo.getmysqlConnection();

		}

		mysqlConn.resetDB();
	} //End of reSetDB
	

}

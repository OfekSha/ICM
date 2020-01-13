package theServer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	/** moving the output to the messges text box
	 *
	 */
	public void initialize(URL location, ResourceBundle resources) {
		/*
		ps = new PrintStream(new MessgesOutputStream(Messages));
		old = System.out;
		System.setOut(ps);
		*/
		echo =null;
		
		//btnCloseTheServer.setDisable(true); 
		}

	@FXML
	void ExitBtn(ActionEvent event) {
		// resetting the console
		/*
		System.out.flush();
		System.setOut(old);
		*/
		
		stopeServer(event);
		System.exit(0);
	}

	@FXML
	void startServer(ActionEvent event) {
		if(echo != null) { 
			System.out.print("there is a echo instans  close it \n");
			return;
		}
	
			EchoServer.StartOcfServer(ServerMenuControler.theArgs);

	//	btnStartTheServer.setDisable(true);
	//	btnCloseTheServer.setDisable(false);
	}

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

	@FXML
	void RefreshMessges(ActionEvent event) {
		refreshMessages();
	}

	/** cleaning the messages screan
	 * 
	 */
	public void refreshMessages() {
		Messages.setText("");
	}
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

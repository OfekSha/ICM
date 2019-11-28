
public class Main {
public static void main(String[] args) {
	mysqlConnection.connect();
	//mysqlConnection.buildDB();
	//mysqlConnection.insertRequirement("Ofek1","after sending message there is button","button dosen't work","");
	mysqlConnection.readFromDB();
	//mysqlConnection.closeConnection();
	
}
}

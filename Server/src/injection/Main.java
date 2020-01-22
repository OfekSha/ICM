package injection;

import java.util.ArrayList;

import Entity.ChangeRequest;
import reporting.ReportController;

public class Main {

	public static void main(String[] args) {
		ArrayList<ChangeRequest> fakeList = null;
		ModelQueryHandler stubQueryHandler=new stubQueryHandler(fakeList);
		ReportController report=new ReportController(stubQueryHandler);
		
	}

}

package Controllers;

import java.util.ArrayList;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import GUI.EstimatorForm;
import GUI.InspectorForm;

public class EstimatorContorller {

	public static void messageFromServer(Object message,EstimatorForm estimatorForm) {
		// @@ need to add testing for message
		clientRequestFromServer respone = (clientRequestFromServer) message;
		Object[] objectArray;
		switch (respone.getRequest()) {
		case getChangeRequestBystatus:
			objectArray =(Object[]) respone.getObject();
			estimatorForm.setArrayList((ArrayList<ChangeRequest>) objectArray[0]);
			break;
		default:
			break;
		}
	}
	
	
}

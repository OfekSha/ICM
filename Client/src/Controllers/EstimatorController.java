package Controllers;

import Entity.ChangeRequest;
import Entity.clientRequestFromServer;
import GUI.EstimatorForm;

import java.util.ArrayList;

public class EstimatorController {

	@SuppressWarnings("unchecked")
	public static void messageFromServer(Object message, EstimatorForm estimatorForm) {
		// @@ need to add testing for message
		clientRequestFromServer response = (clientRequestFromServer) message;
		Object[] objectArray;
		switch (response.getRequest()) {
			case getChangeRequestByStatus:
				objectArray = (Object[]) response.getObject();
				estimatorForm.setArrayList((ArrayList<ChangeRequest>) objectArray[0]);
				break;
			default:
				break;
		}
	}
	
	
}

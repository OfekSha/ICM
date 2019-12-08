package Entity;
import java.io.Serializable;
import java.util.ArrayList;

public class clientRequestFromServer implements Serializable {
    public enum requestOptions {
        getAll,
        updateStatus,
        getRequirement
    }
    private ArrayList<Requirement> obj;	// some object that transfer to client or to server.
    private requestOptions request; // request

    public clientRequestFromServer(requestOptions request) {
    	this.obj = null;
    	this.request = request;
    }
    public clientRequestFromServer(requestOptions request, ArrayList<Requirement> obj) {
    	this.obj = obj;
    	this.request = request;
    }
    public requestOptions getRequest() {
        return request;
    }

    @Override
    public String toString() {
        return request.name();
    }
	public ArrayList<Requirement> getObj() {
		return obj;
	}
}

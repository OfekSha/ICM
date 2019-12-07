package Entity;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

public class clientRequestFromServer implements Serializable {
    public enum requestOptions {
        getAll,
        getRequirement,
        updateStatus,
    }
    private Object obj;	// some object that transfer to client or to server.
    private requestOptions request; // request

    public clientRequestFromServer(String request) {
        switch (request) {
            case "0": 
                this.request = requestOptions.getAll;
                break;
            case "1":
                this.request = requestOptions.getRequirement;
                break;
            case "2":
                this.request = requestOptions.updateStatus;
                break;
            default:
                throw new NotImplementedException();
        }
    }
    public clientRequestFromServer(requestOptions request) {
    	this.obj=null;
    	this.request=request;
    }
    public clientRequestFromServer(requestOptions request,Object obj) {
    	this.obj=obj;
    	this.request=request;
    }
    public requestOptions getRequest() {
        return request;
    }

    public void setRequest(requestOptions request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "Client requested = " + request;
    }
	public Object getObj() {
		return obj;
	}
}

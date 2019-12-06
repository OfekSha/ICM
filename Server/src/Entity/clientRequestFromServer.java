package Entity;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;

public class clientRequestFromServer implements Serializable {
    public enum requestOptions {
        getAll,
        getRequirement,
        updateStatus,
    }

    private requestOptions request;

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
}

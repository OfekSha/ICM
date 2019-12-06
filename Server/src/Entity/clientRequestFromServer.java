package Entity;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class clientRequestFromServer {
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
}

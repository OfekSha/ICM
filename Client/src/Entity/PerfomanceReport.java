package Entity;

public class PerfomanceReport extends IReport {

    int daysExtensionsApproved;
    int daysDisapprovedRequests;

    /*public PerfomanceReport() {
        this.daysExtensionsApproved = 0;
        this.daysDisapprovedRequests = 0;
    }*/

    public int getDaysExtensionsApproved() {
        return daysExtensionsApproved;
    }

    public int getDaysDisapprovedRequests() {
        return daysDisapprovedRequests;
    }

    public void setDaysExtensionsApproved(int daysExtensionsApproved) {
        this.daysExtensionsApproved = daysExtensionsApproved;
    }

    public void setDaysDisapprovedRequests(int daysDisapprovedRequests) {
        this.daysDisapprovedRequests = daysDisapprovedRequests;
    }

    @Override
    public void createReport() {

    }
}

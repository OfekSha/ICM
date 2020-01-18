package Entity;

/**
 * Represents a performance report in the system 
 *
 */
public class PerfomanceReport {

    /** the amount of days added to the requests diffrent stages in addition to the set do time 
     * 
     */
    int daysExtensionsApproved;
    /**
     * 
     */
    int daysDisapprovedRequests;


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

}// end of PerfomanceReport class

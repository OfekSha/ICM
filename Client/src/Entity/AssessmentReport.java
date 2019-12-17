package Entity;

import java.io.File;

public class AssessmentReport extends IReport {

    String place;
    File requestDescription;
    File requestResult;
    String constraints;
    String risks;
    String dueTime;

    public AssessmentReport(String place, File requestDescription, File requestResult, String constraints, String risks, String dueTime) {
        this.place = place;
        this.requestDescription = requestDescription;
        this.requestResult = requestResult;
        this.constraints = constraints;
        this.risks = risks;
        this.dueTime = dueTime;
    }

    @Override
    public void createReport() {

    }
}

package Entity;

import java.io.File;

public class FailureReport extends IReport {
    File explanationFile;

    public FailureReport(File explanationFile) {
        this.explanationFile = explanationFile;
    }

    @Override
    public void createReport() {

    }
}

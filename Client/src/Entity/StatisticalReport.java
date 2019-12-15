package Entity;
//SS
public class StatisticalReport implements IReport {
// class variables
	protected int median;
	protected int standardDeviation;
	protected int frequencyDistribution;

	@Override
	public void createReport() {
		// TODO Auto-generated method stub

	}

	// get class variables
	// if changes are to be made make sure to update the DB creation
	public int getMedian() {
		return median;
	}

	public int getStandardDeviation() {
		return standardDeviation;
	}

	public int getFrequencyDistribution() {
		return frequencyDistribution;
	}

}

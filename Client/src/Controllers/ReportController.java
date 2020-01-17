package Controllers;

import java.util.Arrays;

public class ReportController {

	void calculateTheMedian(int[] numbersArray) {
		Arrays.sort(numbersArray);
		double median;
		if (numbersArray.length % 2 == 0)
			median = ((double) numbersArray[numbersArray.length / 2]
					+ (double) numbersArray[numbersArray.length / 2 - 1]) / 2;
		else
			median = (double) numbersArray[numbersArray.length / 2];
	} // end of calculateTheMedian()

	public double calculatestandardDeviation(int[] intArray) {
		double[] doubleArray = new double[intArray.length];
		for (int i = 0; i < intArray.length; i++)
			doubleArray[i] = intArray[i];

		double sum = 0.0, standardDeviation = 0.0;
		int numAmount = doubleArray.length;
		for (double num : doubleArray)
			sum += num;
		double avrege = sum / numAmount;
		for (double num : doubleArray) {
			standardDeviation += Math.pow(num - avrege, 2);
		}
		return Math.sqrt(standardDeviation / numAmount);
	}

}// ReportController()

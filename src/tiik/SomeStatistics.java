package tiik;

import java.util.HashMap;
import java.util.Map;

public class SomeStatistics {
	
	static private final double naturalLog2 = Math.log(2);
	
	private final Map<Byte, Long> counts = new HashMap<>();
	private final Map<Byte, Double> empiricalProbabilities = new HashMap<>();
	private double binaryEntropy;
	private final Map<Byte, Double> quantitiesOfInformation = new HashMap<>();
	
	
	public SomeStatistics(byte[] bytes) {
		calculateEmpiricalProbabilities(bytes);
		calculateBinaryEntropy();
		calculateQuantitiesOfInformation();
	}

	private void calculateEmpiricalProbabilities(byte[] bytes) {
                for (byte b : bytes)
		{
			final long count = counts.containsKey(b) ? counts.get(b) + 1 : 1;
			counts.put(b, count);
		}
		final double size = bytes.length;
		for (byte b : counts.keySet())
			empiricalProbabilities.put(b, counts.get(b) / size);
	}

	private void calculateBinaryEntropy() {
		binaryEntropy = 0.0;
		for (byte b : empiricalProbabilities.keySet())
		{
			final double empiricalProbability = empiricalProbabilities.get(b);
			binaryEntropy += empiricalProbability * log2(1.0 / empiricalProbability);
		}
	}

	private void calculateQuantitiesOfInformation() {
		for (byte b : empiricalProbabilities.keySet()) {
			final double quantityOfInformation = log2(1 / empiricalProbabilities.get(b));
			quantitiesOfInformation.put(b, quantityOfInformation);
		}
	}

	static private double log2(double x) {
		return Math.log(x) / naturalLog2;
	}
	
	public Map<Byte, Long> getCounts() {
		return counts;
	}
	
	public Map<Byte, Double> getEmpiricalProbabilities() {
		return empiricalProbabilities;
	}
	
	public double getBinaryEntropy() {
		return binaryEntropy;
	}
	
	public Map<Byte, Double> getQuantitiesOfInformation() {
		return quantitiesOfInformation;
	}
	
}

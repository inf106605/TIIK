package tiik;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SomeStatistics {
	
	static private final double naturalLog2 = Math.log(2);
	
	private final String text;
	private final Map<Integer, Integer> countsOfCharacters = new HashMap<>();
	private final Map<Integer, Double> empiricalProbabilities = new HashMap<>();
	private double binaryEntropy;
	private final Map<Integer, Double> quantitiesOfInformation = new HashMap<>();
	
	
	public SomeStatistics(String text) {
		this.text = text;
		calculateEverything();
	}

	private void calculateEverything() {
		calculateEmpiricalProbabilities();
		calculateBinaryEntropy();
		calculateQuantitiesOfInformation();
	}

	private void calculateEmpiricalProbabilities() {
                for (byte b : text.getBytes(Charset.forName("windows-1252")))
		{
                        int c = b >= 0 ? b : b + 256;
			int count;
			if (countsOfCharacters.containsKey(c))
				count = countsOfCharacters.get(c) + 1;
			else
				count = 1;
			countsOfCharacters.put(c, count);
		}
		final double size = text.length();
		for (int c : countsOfCharacters.keySet())
			empiricalProbabilities.put(c, countsOfCharacters.get(c) / size);
	}

	private void calculateBinaryEntropy() {
		binaryEntropy = 0.0;
		for (int c : empiricalProbabilities.keySet())
		{
			final double empiricalProbability = empiricalProbabilities.get(c);
			binaryEntropy += empiricalProbability * log2(1.0 / empiricalProbability);
		}
	}

	private void calculateQuantitiesOfInformation() {
		for (int c : empiricalProbabilities.keySet()) {
			final double quantityOfInformation = log2(1 / empiricalProbabilities.get(c));
			quantitiesOfInformation.put(c, quantityOfInformation);
		}
	}

	static private double log2(double x) {
		return Math.log(x) / naturalLog2;
	}
	
	public Map<Integer, Double> getEmpiricalProbabilities() {
		return empiricalProbabilities;
	}
	
	public double getBinaryEntropy() {
		return binaryEntropy;
	}
	
	public Map<Integer, Double> getQuantitiesOfInformation() {
		return quantitiesOfInformation;
	}
	
}

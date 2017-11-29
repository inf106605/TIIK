package tiik;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class SomeStatistics {
	
	static private final int[] countsOfOnes = new int[256];
	static private final double naturalLog2 = Math.log(2);
	
	private final String text;
	private final Map<Integer, Integer> countsOfCharacters = new HashMap<>();
	private final Map<Integer, Double> empiricalProbability = new HashMap<>();
	private int countOfOnes = 0;
	private double probabilityOfOne;
	private double binaryEntropy;
	private final Map<Integer, Double> quantitiesOfInformation = new HashMap<>();
	
	
	static {
		for (int i = 0; i != countsOfOnes.length; ++i) {
			int count = 0;
			for (int mask = 1; mask <= i; mask <<= 1)
			{
				if (mask == 256)
					throw new RuntimeException("Oh shit, it has more than 8 bits!");
				if ((mask & i) != 0)
					++count;
			}
			countsOfOnes[i] = count;
		}
	}
	
	
	public SomeStatistics(String text) {
		this.text = text;
		calculateEverything();
	}

	private void calculateEverything() {
		calculateEmpiricalProbability();
		calculateBinaryEntropy();
		calculateQuantitiesOfInformation();
	}

	private void calculateEmpiricalProbability() {
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
			empiricalProbability.put(c, countsOfCharacters.get(c) / size);
	}

	private void calculateBinaryEntropy() {
		for (int c : countsOfCharacters.keySet())
			countOfOnes += countsOfOnes[c] * countsOfCharacters.get(c);
		probabilityOfOne = (double)countOfOnes / (text.length() * 8);
		final double p = probabilityOfOne;
		binaryEntropy = -p * log2(p)-(1-p)*log2(1-p);
	}

	private void calculateQuantitiesOfInformation() {
		for (int c : empiricalProbability.keySet()) {
			final double quantityOfInformation = log2(1 / empiricalProbability.get(c));
			quantitiesOfInformation.put(c, quantityOfInformation);
		}
	}

	static private double log2(double x) {
		return Math.log(x) / naturalLog2;
	}
	
	public Map<Integer, Double> getEmpiricalProbability() {
		return empiricalProbability;
	}
	
	public double getBinaryEntropy() {
		return binaryEntropy;
	}
	
	public Map<Integer, Double> getQuantitiesOfInformation() {
		return quantitiesOfInformation;
	}
	
}

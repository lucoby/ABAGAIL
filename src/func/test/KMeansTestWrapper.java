package func.test;

public class KMeansTestWrapper {
	public static void main(String[] args) throws Exception {
		int min = 2, max = 5;
		int[] correct = new int[max - min + 1];
		for (int k = min; k < max; k++) {
			KMeansClustererTestLetters test = new KMeansClustererTestLetters(k);
			correct[k - min] = test.test();
			
		}
		for (int k = min; k < max; k++) {
			System.out.println(correct[k - min]);
		}
	}
}

package func.test;

public class EMWrapper {
	public static void main(String[] args) throws Exception {
		testRun("src/opt/test/Letters", 16000, 4000, 16, 26, true);
//		testRun("src/opt/test/abs_", 2925, 1252, 8, 15, false);
	}
	
	public static void testRun(String testFile, int trainingLength, int testingLength, int num_attributes, int num_outputs, boolean letters) {
		int min = 26, max = 30;
		int[] trainCorrect = new int[max - min + 1];
		int[] testCorrect = new int[max - min + 1];
		
		for (int k = min; k < max; k++) {
			EMTester test = new EMTester(k, testFile, trainingLength, testingLength, num_attributes, num_outputs, letters);
			int[] correct = test.test();
			trainCorrect[k - min] = correct[0];
			testCorrect[k - min] = correct[1];
			System.out.println(trainCorrect[k - min] + " " + testCorrect[k - min]);
			
		}
	}
}

package func.test;

public class KMeansWrapper {
	public static void main(String[] args) throws Exception {
//		System.out.println("src/func/test/LettersPCA10");
//		testRun("src/func/test/LettersPCA10", 16000, 4000, 10, 26, false);
//		System.out.println("src/func/test/LettersICA2");
//		testRun("src/func/test/LettersICA2", 16000, 4000, 2, 26, false);
//		System.out.println("src/func/test/LettersICA3");
//		testRun("src/func/test/LettersICA3", 16000, 4000, 3, 26, false);
//		System.out.println("src/func/test/abs_PCA2");
//		testRun("src/func/test/abs_PCA2", 2925, 1252, 2, 15, false);
//		System.out.println("src/func/test/abs_ICA2");
//		testRun("src/func/test/abs_ICA2", 2925, 1252, 2, 15, false);
//		System.out.println("src/func/test/abs_PCA3");
//		testRun("src/func/test/abs_ICA3", 2925, 1252, 3, 15, false);
		
		
//		System.out.println("src/func/test/LettersLDA5");
//		testRun("src/func/test/LettersLDA5", 16000, 4000, 5, 26, false);
//		System.out.println("src/func/test/LettersRCA5");
//		testRun("src/func/test/LettersRCA5", 16000, 4000, 5, 26, false);
//		System.out.println("src/func/test/LettersLDA10");
//		testRun("src/func/test/LettersLDA10", 16000, 4000, 10, 26, false);
//		System.out.println("src/func/test/LettersRCA10");
//		testRun("src/func/test/LettersRCA10", 16000, 4000, 10, 26, false);
		System.out.println("src/func/test/abs_RCA2");
		testRun("src/func/test/abs_RCA2", 2925, 1252, 2, 15, false);
		System.out.println("src/func/test/abs_RCA4");
		testRun("src/func/test/abs_RCA4", 2925, 1252, 4, 15, false);
	}
	
	public static void testRun(String testFile, int trainingLength, int testingLength, int num_attributes, int num_outputs, boolean letters) {
		int min = 2, max = 100;
		int[] trainCorrect = new int[max - min + 1];
		int[] testCorrect = new int[max - min + 1];
		
		for (int k = min; k < max; k++) {
			KMeansTester test = new KMeansTester(k, testFile, trainingLength, testingLength, num_attributes, num_outputs, letters);
			int[] correct = test.test();
			trainCorrect[k - min] = correct[0];
			testCorrect[k - min] = correct[1];
			System.out.println(k + " " + trainCorrect[k - min] + " " + testCorrect[k - min]);
			
		}
		for (int k = 100; k < 301; k+=10) {
			KMeansTester test = new KMeansTester(k, testFile, trainingLength, testingLength, num_attributes, num_outputs, letters);
			int[] correct = test.test();
			System.out.println(k + " " + correct[0] + " " + correct[1]);
		}
	}
}

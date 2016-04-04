package shared.test;

public class RCAWrapper {
	public static void main(String[] args) throws Exception {
//		testRun("src/opt/test/Letters", 16000, 4000, 16, 26, true);
		testRun("src/opt/test/abs_", 2925, 1252, 8, 15, false);
	}
	
	public static void testRun(String testFile, int trainingLength, int testingLength, int num_attributes, int num_outputs, boolean letters) {
		int k = 4;
		RCATester test = new RCATester(k, testFile, trainingLength, testingLength, num_attributes, num_outputs, letters);
		test.test("4");
	}
}

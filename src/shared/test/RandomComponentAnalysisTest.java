package shared.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.filt.InsignificantComponentAnalysis;
import shared.filt.RandomizedProjectionFilter;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;

/**
 * A class for testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RandomComponentAnalysisTest {
    
    /**
     * The test main
     * @param args ignored
     */
	private static double normSub = 0, normDiv = 45;
    private static int trainingLength = 16000, testingLength = 4000, num_attributes = 16, num_outputs = 26, n=10;
    
    public static void main(String[] args) {
    	Instance[] instances = initializeTraining();
    	RandomizedProjectionFilter filter = new RandomizedProjectionFilter(num_attributes, n);
        filter.filter(set);
        System.out.println("After ICA");
        System.out.println(set);
          
    }
    
    private static Instance[] initializeTraining() {

        double[][][] attributes = new double[trainingLength][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/LettersTrain.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[num_attributes]; // 7 attributes
                attributes[i][1] = new double[1]; // 1 outputs

                for(int j = 0; j < num_attributes; j++) {
                	attributes[i][0][j] = Double.parseDouble(scan.next());
                	int k = 0;
                }

//                attributes[i][1][scan.next().charAt(0) - 'A'] = 1;
                attributes[i][1][0] = scan.next().charAt(0) - 'A';
                
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for(int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            instances[i].setLabel(new Instance(attributes[i][1]));
        }

        return instances;
    }

}

package func.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import dist.Distribution;
import dist.MultivariateGaussian;
import func.KMeansClusterer;
import shared.DataSet;
import shared.Instance;
import util.linalg.DenseVector;
import util.linalg.RectangularMatrix;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KMeansClustererTestLetters {
    /**
     * The test main
     * @param args ignored
     */
	private static double normSub = 0, normDiv = 45;
    private static int trainingLength = 16000, testingLength = 4000, num_attributes = 16, num_outputs = 26, k=26;
    
    public static void main(String[] args) throws Exception {
        Instance[] instances = initializeTraining();
        DataSet set = new DataSet(instances);
        KMeansClusterer km = new KMeansClusterer(k);
        km.estimate(set);
        int[][] clusterMatrix = new int[k][num_outputs];
        for(Instance i : instances) {
//        	clusterMatrix
        	Instance a = km.value(i);
        	Instance c = km.value(i);
        	double b = i.getLabel().getData().get(0);
        	System.out.println(km.value(i) + " " + km.closest(i) + " " + i.getLabel());
        }
        System.out.println(km);
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

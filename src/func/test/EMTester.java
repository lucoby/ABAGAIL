package func.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import dist.Distribution;
import dist.MultivariateGaussian;
import func.EMClusterer;
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
public class EMTester {
    /**
     * The test main
     * @param args ignored
     */
	private int trainingLength, testingLength, num_attributes, num_outputs, k;
	private String testFile, trainFile;
	private Instance[] trainInstances, testInstances;
	private boolean letters;

    public EMTester(int k, String file, int trainingLength, int testingLength, int num_attributes, int num_outputs, boolean letters) {
    	this.k = k;
    	this.trainFile = file + "Train.txt";
    	this.testFile = file + "Test.txt";
    	this.trainingLength = trainingLength;
    	this.testingLength = testingLength;
    	this.num_attributes = num_attributes;
    	this.num_outputs = num_outputs;
    	this.letters = letters;
    }
    
    public int[] test() {
    	trainInstances = initializeTraining(true);
        testInstances = initializeTraining(false);
        DataSet set = new DataSet(trainInstances);
        EMClusterer em = new EMClusterer(k, 1E-6, 1000);
        em.estimate(set);
        int[][] trainClusterMatrix = new int[k][num_outputs];
        for(Instance i : trainInstances) {
//        	Instance a = em.value(i);
//        	Instance c = em.value(i);
        	int a = (int) em.value(i).getData().get(0);
        	int b = (int) i.getLabel().getData().get(0);
        	trainClusterMatrix[a][b] += 1;
//        	System.out.println(a + " " + c + " " + b);
        }
        int[] correct = new int[2];
        correct[0] = 0;
        for (int[] r : trainClusterMatrix) {
        	int largest = 0;
        	for (int c : r) {
        		if (c > largest) {
        			largest = c;
        		}
        	}
        	correct[0] += largest;
        }
        
        int[][] testClusterMatrix = new int[k][num_outputs];
        for(Instance i : testInstances) {
        	int a = (int) em.value(i).getData().get(0);
        	int b = (int) i.getLabel().getData().get(0);
        	testClusterMatrix[a][b] += 1;
        }
        correct[1] = 0;
        for (int[] r : testClusterMatrix) {
        	int largest = 0;
        	for (int c : r) {
        		if (c > largest) {
        			largest = c;
        		}
        	}
        	correct[1] += largest;
        }

        return correct;
    }
    
    private Instance[] initializeTraining(boolean train) {
    	double[][][] attributes;
    	if(train) {
    		attributes = new double[this.trainingLength][][];
    	} else {
    		attributes = new double[this.testingLength][][];
    	}

        try {
        	BufferedReader br;
            if (train) {
            	br = new BufferedReader(new FileReader(new File(this.trainFile)));
            } else {
            	br = new BufferedReader(new FileReader(new File(this.testFile)));
            }
			 

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[this.num_attributes]; // 7 attributes
                attributes[i][1] = new double[1]; // 1 outputs

                for(int j = 0; j < this.num_attributes; j++) {
                	attributes[i][0][j] = Double.parseDouble(scan.next());
                }

//                attributes[i][1][scan.next().charAt(0) - 'A'] = 1;
                if(this.letters) {
                	attributes[i][1][0] = scan.next().charAt(0) - 'A';
                } else {
                	attributes[i][1][0] = Integer.parseInt(scan.next());
                }
                scan.close();
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

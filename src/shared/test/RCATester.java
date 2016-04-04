package shared.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import shared.DataSet;
import shared.Instance;
import shared.filt.IndependentComponentAnalysis;
import shared.filt.RandomizedProjectionFilter;
import util.linalg.Matrix;
import util.linalg.RectangularMatrix;

/**
 * A class for testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RCATester {
    
    /**
     * The test main
     * @param args ignored
     */
	private int trainingLength, testingLength, num_attributes, num_outputs, k;
	private String testFile, trainFile, fileName;
	private Instance[] trainInstances, testInstances;
	private boolean letters;
    
    public void test(String testCase) {
    	trainInstances = initializeTraining(true);
        testInstances = initializeTraining(false);
    	
        DataSet set = new DataSet(trainInstances);
        RandomizedProjectionFilter filter = new RandomizedProjectionFilter(k, num_attributes);
        filter.filter(set);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + "RCA" + testCase + "train.txt"), "utf-8"))) {
        	writer.write(set.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        DataSet testset = new DataSet(testInstances);
        
        filter.filter(testset);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + "RCA" + testCase + "test.txt"), "utf-8"))) {
        	writer.write(testset.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
          
    }
    
    public RCATester(int k, String file, int trainingLength, int testingLength, int num_attributes, int num_outputs, boolean letters) {
    	this.k = k;
    	this.fileName = file;
    	this.trainFile = file + "Train.txt";
    	this.testFile = file + "Test.txt";
    	this.trainingLength = trainingLength;
    	this.testingLength = testingLength;
    	this.num_attributes = num_attributes;
    	this.num_outputs = num_outputs;
    	this.letters = letters;
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
                attributes[i][0] = new double[this.num_attributes]; 
                attributes[i][1] = new double[1]; 

                for(int j = 0; j < this.num_attributes; j++) {
                	attributes[i][0][j] = Double.parseDouble(scan.next());
                }

//                attributes[i][1][scan.next().charAt(0) - 'A'] = 1;
                if(this.letters) {
                	attributes[i][1][0] = scan.next().charAt(0) - 'A';
                } else {
                	attributes[i][1][0] = Integer.parseInt(scan.next()) - 1;
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
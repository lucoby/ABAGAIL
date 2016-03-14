package opt.test;

import dist.*;
import opt.*;
import opt.example.*;
import opt.ga.*;
import shared.*;
import func.nn.backprop.*;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying abalone as having either fewer 
 * or more than 15 rings. 
 *
 * @author Hannah Lau
 * @version 1.0
 */
public class LettersTest {
	private static double normSub = 0, normDiv = 45;
    private static int trainingLength = 16000, testingLength = 4000;
    
    private static Instance[] training = initializeTraining();
    private static Instance[] testing = initializeTesting();

    private static int inputLayer = 16, hiddenLayer = 5, outputLayer = 26, trainingIterations = 10000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(training);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    
//	private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[2];
//	private static String[] oaNames = {"RHC", "SA"};
  
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");
    
    

    public static void main(String[] args) {
        for(int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                new int[] {inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);

        for(int i = 0; i < oa.length; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, train_correct = 0, train_incorrect = 0, test_correct = 0, test_incorrect = 0;
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());
            
            // Training Error
            
            double predicted, actual;
            start = System.nanoTime();
            for(int j = 0; j < training.length; j++) {
                networks[i].setInputValues(training[j].getData());
                networks[i].run();

                actual = training[j].getLabel().getData().argMax();
                predicted  = networks[i].getOutputValues().argMax();
//                predicted = Double.parseDouble(instances[j].getLabel().toString());
//                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = predicted == actual ? train_correct++ : train_incorrect++;

            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);
            
            results +=  "\nResults for " + oaNames[i] + ": \nCorrectly classified " + train_correct + " instances." +
                    "\nIncorrectly classified " + train_incorrect + " instances.\nPercent correctly classified: "
                    + df.format(train_correct/(train_correct+train_incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
            
            // Testing error
            
            start = System.nanoTime();
            for(int j = 0; j < testing.length; j++) {
                networks[i].setInputValues(testing[j].getData());
                networks[i].run();

                actual = testing[j].getLabel().getData().argMax();
                predicted = networks[i].getOutputValues().argMax();
//                predicted = Double.parseDouble(instances[j].getLabel().toString());
//                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = predicted == actual ? test_correct++ : test_incorrect++;

            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10,9);
            
            results +=  "\nResults for " + oaNames[i] + ": \nCorrectly classified " + test_correct + " instances." +
                        "\nIncorrectly classified " + test_incorrect + " instances.\nPercent correctly classified: "
                        + df.format(test_correct/(test_correct+test_incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);
    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        if  (oaName.equals("RHC")) {
        	trainingIterations = 3000;
        }
        if  (oaName.equals("SA")) {
        	trainingIterations = 1;
        }
        if  (oaName.equals("GA")) {
        	trainingIterations = 1;
        }
        
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/opt/test/LettersError " + oaName + ".txt"), "utf-8"))) {
//        	System.out.println("\nError results for " + oaName + "\n---------------------------");
        	writer.write("\nError results for " + oaName + "\n---------------------------\n");
        	
        	for(int i = 0; i < trainingIterations; i++) {
        		oa.train();
        		
        		double error = 0;
        		for(int j = 0; j < training.length; j++) {
        			network.setInputValues(training[j].getData());
        			network.run();
        			
        			Instance output = training[j].getLabel(), example = new Instance(network.getOutputValues());
        			example.setLabel(new Instance(network.getOutputValues()));
        			error += measure.value(output, example);
        		}
        		
//        		System.out.println(df.format(error));
        		writer.write(df.format(error) + "\n");
        	}
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

    private static Instance[] initializeTraining() {

        double[][][] attributes = new double[16000][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/LettersTrain.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[16]; // 7 attributes
                attributes[i][1] = new double[26]; // 26 outputs

                for(int j = 0; j < 16; j++) {
                	attributes[i][0][j] = (Double.parseDouble(scan.next()) - normSub) / normDiv;
                	int k = 0;
                }

                attributes[i][1][scan.next().charAt(0) - 'A'] = 1;
                
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
    
    private static Instance[] initializeTesting() {

        double[][][] attributes = new double[4000][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("src/opt/test/LettersTest.txt")));

            for(int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                attributes[i] = new double[2][];
                attributes[i][0] = new double[16]; // 7 attributes
                attributes[i][1] = new double[26]; // 26 outputs

                for(int j = 0; j < 16; j++)
                    attributes[i][0][j] = (Double.parseDouble(scan.next()) - normSub) / normDiv;

                attributes[i][1][scan.next().charAt(0) - 'A'] = 1;
                
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

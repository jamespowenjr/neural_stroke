package edu.jhu.nn.training;

import java.util.ArrayList;
import java.util.List;

import org.encog.ml.MLMethod;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.factory.MLTrainFactory;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;
import org.encog.util.simple.TrainingSetUtil;

public class FFRPROPTrainer {

	private static final String INPUT_DIR = "C:/Users/Jay/java_ee/nn.owen.james/src/main/resources/input/set1/";
	private static final String TRAINING_SET_FILENAME = "test-5.csv";
//	private static final String[] VALIDATION_SET_FILENAMES = new String[] {
//			"test-0.csv", 
//			"test-1.csv", 
//			"test-2.csv", 
//			"test-3.csv", 
//			"test-4.csv",
//			"test-5.csv", 
//			"test-6.csv", 
//			"test-7.csv", 
//			"test-8.csv",
//			"test-9.csv"
//			};
	private static final String[] VALIDATION_SET_FILENAMES = new String[] {"test-all.csv"};

	private static final int INPUTS = 8;
	private static final int OUTPUTS = 4;
	private static final int HIDDEN_LAYER_SIZE = 10;
	private static final double TARGET_ERROR = 0.005;
	
	private static final String NETWORK_METHOD = "?:B->SIGMOID->" + HIDDEN_LAYER_SIZE + ":B->SIGMOID->?";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final MLDataSet trainingSet = TrainingSetUtil.loadCSVTOMemory(
				CSVFormat.ENGLISH, INPUT_DIR + TRAINING_SET_FILENAME, true, 8, 4);
		
		List<MLDataSet> validationSets = new ArrayList<MLDataSet>();
		
		for(String validationFile : VALIDATION_SET_FILENAMES){
			final MLDataSet validationSet = TrainingSetUtil.loadCSVTOMemory(
					CSVFormat.ENGLISH, INPUT_DIR + validationFile, true, 8, 4);
			validationSets.add(validationSet);
		}
		
		FFRPROPTrainer trainer = new FFRPROPTrainer();
		trainer.trainNetwork(trainingSet, validationSets, MLMethodFactory.TYPE_FEEDFORWARD, NETWORK_METHOD, MLTrainFactory.TYPE_RPROP, "", INPUTS, HIDDEN_LAYER_SIZE, OUTPUTS);
	}

	public void trainNetwork(MLDataSet trainingSet, List<MLDataSet> validationSets, String methodName, String methodArchitecture, String trainerName, String trainerArgs, int inputNeurons, int hiddenNeurons, int outputNeurons){
		
		MLMethodFactory methodFactory = new MLMethodFactory();		
		MLMethod method = methodFactory.create(methodName, methodArchitecture, inputNeurons, outputNeurons);
		
		MLTrainFactory trainFactory = new MLTrainFactory();	
		MLTrain train = trainFactory.create(method, trainingSet, trainerName, trainerArgs);

		EncogUtility.trainToError(train, TARGET_ERROR);
		System.out.println("\n###################\n");
		method = train.getMethod();
		//EncogUtility.evaluate((MLRegression) method, trainingSet);
		
		//EncogUtility.evaluate((MLRegression) method, validationSet);
		double totalRegressionError = 0.0;
		for(MLDataSet validationSet : validationSets){
			double regressionError = EncogUtility.calculateRegressionError((MLRegression) method, validationSet);
			totalRegressionError += regressionError;
			System.out.println("Error for validation set: " + regressionError);
		}
		System.out.println();
		System.out.println("Average of regression error: " + (totalRegressionError/validationSets.size()));
		BasicNetwork network = (BasicNetwork) method;
		System.out.println();
		String[] weights = network.dumpWeights().split(",");
		double[] weightTotals = new double[inputNeurons];
//		System.out.println("Input > Hidden Layer weights");
//		for(int i = 0; i < inputNeurons * hiddenNeurons; i++){
//			System.out.println(weights[i]);
//			weightTotals[i % inputNeurons] += Double.parseDouble(weights[i]);
//		}
//		System.out.println("\nTotals");
//		for(int i = 0; i < weightTotals.length; i++){
//			System.out.println(weightTotals[i]);
//		}
//		System.out.println("Hidden Layer > Output weights");
//		for(int i = 0; i < hiddenNeurons * outputNeurons; i++){
//			System.out.println(weights[i + inputNeurons * hiddenNeurons]);
//		}
//		
	}
}

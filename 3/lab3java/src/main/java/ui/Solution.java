package ui;

import Algorithms.ID3;
import Structures.DataEntry;
import Structures.ID3TreeNode;
import Structures.SetOfData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Solution {

	public static void main(String ... args){

		try{
			//INITIALIZE VALUES
			Path trainingFile = null;
			Path testingFile = null;
			Integer treeDepth = null;

			//CHECK ARGUMENT VALIDITY
			if(args.length < 2)
				throw new IllegalArgumentException("Too few arguments!");

			//GET ARGUMENT PATHS
			trainingFile = Path.of(args[0]);
			testingFile = Path.of(args[1]);

			//INVALID PATHS
			if(!Files.exists(trainingFile) || !Files.isReadable(trainingFile) || !Files.exists(testingFile) || !Files.isReadable(testingFile))
				throw new IOException("Given files are not valid!");

			//IF DEPTH IS DEFINED, PARSE DEPTH
			if(args.length == 3) {
				treeDepth = Integer.parseInt(args[2]);
			}

			//READ VALUES FROM FILES
			List<String> trainingValues =  Files.readAllLines(trainingFile);
			List<String> testingValues =  Files.readAllLines(testingFile);

			//INITIALIZE ALGORITHM, PREPARE DATA AND TRAIN ALGORITHM
			ID3 algorithm = new ID3(treeDepth);
			SetOfData trainingData = loadDataSet(trainingValues, false);
			algorithm.fit(trainingData);
			ID3TreeNode.printTree(algorithm.getTrainedTree());

			//FROM TESTING SET TEST TRAINED ALGORITHM
			SetOfData testingData = loadDataSet(testingValues, true);
			algorithm.predict(testingData);

		} catch (Exception e){
			System.out.println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		}

	}

	/**
	 * Method will prepare a Set of data that argument receives from string list.
	 * @return set of data
	 */
	private static SetOfData loadDataSet(List<String> entries, boolean test) {
		//IF THERE ARE NOW ROWS (ONE STRING IS ONE ROW)
		if(entries.size() < 1) throw new IllegalArgumentException("Given file data is invalid!");

		//Helping objects, ATTRIBUTES IS A LIST OF ATTRIBUTES IN FIRST LINE
		//ATTRIBUTE MAP WILL CONTAIN ALL DIFFERENT VALUES FOR ALL ATTRIBUTES
		//DATA ENTRIES MODELS ONE ROW
		String[] attributes = entries.get(0).split(",");
		Map<String, Set<String>> attributeMap = new HashMap<>();
		List<DataEntry> dataEntries = new ArrayList<>();

		//SETS THE NAME OF LAST ATTRIBUTE, GOAL ATTRIBUTE
		SetOfData.setGoalName(attributes[attributes.length-1]);
		//WILL ORDER THEN BY THEIR NATURAL ORDER
		Set<String> goalValues = new TreeSet<>();

		//iterating trough rows
		for(int i = 1; i < entries.size(); i++){
			String[] values = entries.get(i).split(",");

			//adding new data entry
			Map<String, String> attributeValues = new HashMap<>();
			for(int j = 0; j < values.length; j++){
				attributeValues.put(attributes[j], values[j]);

				if(j == values.length-1){
					goalValues.add(values[j]);
					continue;
				}

				//add to map that contains different values of attributes
				//if map does not have it, it lazily creates a new set that will contain all values
				if(!attributeMap.containsKey(attributes[j])){
					Set<String> differentValues = new TreeSet<>();
					differentValues.add(values[j]);
					attributeMap.put(attributes[j], differentValues);
				} else {
					attributeMap.get(attributes[j]).add(values[j]);
				}

			}
			dataEntries.add(new DataEntry(attributeValues));
		}
			//SET HELPER DATA
		if(!test){
			SetOfData.setTrainingGoals(goalValues);
		} else {
			SetOfData.setTestingGoals(goalValues);
		}
			SetOfData.setAttributes(attributeMap);

		return new SetOfData(dataEntries);
	}

}

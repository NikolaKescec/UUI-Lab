package ui;

import Algorithms.ID3;
import Structures.DataEntry;
import Structures.SetOfData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Solution {

	public static void main(String ... args){

		try{
			Path trainingFile = null;
			Path testingFile = null;
			Integer treeDepth = null;

			if(args.length < 2)
				throw new IllegalArgumentException("Too few arguments!");

			trainingFile = Path.of(args[0]);
			testingFile = Path.of(args[1]);

			if(!Files.exists(trainingFile) || !Files.isReadable(trainingFile) || !Files.exists(testingFile) || !Files.isReadable(testingFile))
				throw new IOException("Given files are not valid!");

			if(args.length == 3) {
				treeDepth = Integer.parseInt(args[2]);
			}

			List<String> trainingValues =  Files.readAllLines(trainingFile);
			List<String> testingValues =  Files.readAllLines(testingFile);

			SetOfData trainingData = loadDataSet(trainingValues);
			SetOfData testingData = loadDataSet(testingValues);

			ID3 algorithm = new ID3(treeDepth);

			algorithm.fit(trainingData);

			System.out.println(algorithm.getTrainedTree());

		} catch (Exception e){
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

	/**
	 * Method will get commands from command string list.
	 * @return list of commands
	 */
	private static SetOfData loadDataSet(List<String> entries) {

		if(entries.size() < 1) throw new IllegalArgumentException("Given file data is invalid!");

		//Helping objects
		String[] attributes = entries.get(0).split(",");
		Map<String, Set<String>> attributeMap = new HashMap<>();
		List<DataEntry> dataEntries = new ArrayList<>();

		//set name of goal attribute
		SetOfData.setGoalName(attributes[attributes.length-1]);
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

		SetOfData.setGoals(goalValues);
		SetOfData.setAttributes(attributeMap);

		return new SetOfData(dataEntries);
	}

}

package ui;

import algorithms.AlgorithmType;
import algorithms.Algorithms;
import algorithms.StrategyAlgorihtms;
import structures.Clausula;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Solution {

	public static void main(String ... args) {

		AlgorithmType algorithmType = null;
		Path clausulaList = null;
		Path commandList = null;

		if(args.length < 1)
			throw new IllegalArgumentException("Too few arguments!");

		try {

			// arguments parsing
			switch (args[0]) {
				case "resolution":
					if(args.length != 2)
						throw new IllegalArgumentException("Resolution command needs only clausula list path!");
					algorithmType = AlgorithmType.RESOLUTION;
					clausulaList = Paths.get(args[1]);
					break;
				case "cooking":
					if(args.length != 3)
						throw new IllegalArgumentException("Cooking command needs clausula list path and command list path!");
					algorithmType = AlgorithmType.COOKING;
					clausulaList = Paths.get(args[1]);
					commandList = Paths.get(args[2]);
					break;
				default:
					throw new UnsupportedOperationException("No such algorithm");
			}

			// Variables declaration
			List<String> clausulaStringList;
			List<String> commandStringList;

			if(clausulaList == null || !Files.exists(clausulaList) || !Files.isReadable(clausulaList))
				throw new IllegalArgumentException("Please write correct clausula list file path.");

			clausulaStringList = Files.readAllLines(clausulaList).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

			if(algorithmType.equals(AlgorithmType.COOKING) && (commandList == null || !Files.exists(commandList) || !Files.isReadable(commandList)))
				throw new IllegalArgumentException("When using cooking algorithm correct path to command list must be given!");

			if(algorithmType.equals(AlgorithmType.COOKING))
				commandStringList =  Files.readAllLines(commandList).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

			switch (algorithmType){
				case RESOLUTION:
					LinkedHashSet<Clausula> clausulasSet = getClausulas(clausulaStringList);
					LinkedHashSet<Clausula> negatedSet = Clausula.negateClausula(clausulaStringList.get(clausulaStringList.size()-1));
					System.out.println(Algorithms.resolutionAlgorithm(negatedSet, clausulasSet, new Clausula(clausulaStringList.get(clausulaStringList.size()-1))));
			}

		} catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	private static LinkedHashSet<Clausula> getClausulas(List<String> clausulaStringList) {
		LinkedHashSet<Clausula> clausulas = new LinkedHashSet<>();
		for(int i = 0; i < clausulaStringList.size()-1; i++) {
			String clausula = clausulaStringList.get(i);
			StrategyAlgorihtms.addToClausulaSet(clausula, clausulas);
		}
		return clausulas;
	}

}

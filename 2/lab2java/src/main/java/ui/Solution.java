package ui;

import algorithms.AlgorithmType;
import algorithms.Algorithms;
import algorithms.StrategyAlgorihtms;
import structures.Clausula;
import structures.Command;
import structures.CommandType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
			List<String> commandStringList = null;

			if(clausulaList == null || !Files.exists(clausulaList) || !Files.isReadable(clausulaList))
				throw new IllegalArgumentException("Please write correct clausula list file path.");

			clausulaStringList = Files.readAllLines(clausulaList).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

			if(algorithmType.equals(AlgorithmType.COOKING) && (commandList == null || !Files.exists(commandList) || !Files.isReadable(commandList)))
				throw new IllegalArgumentException("When using cooking algorithm correct path to command list must be given!");

			if(algorithmType.equals(AlgorithmType.COOKING))
				commandStringList =  Files.readAllLines(commandList).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

			LinkedHashSet<Clausula> clausulasSet;
			switch (algorithmType){
				case RESOLUTION:
					clausulasSet = getClausulas(clausulaStringList, false);
					LinkedHashSet<Clausula> negatedSet = Clausula.negateClausula(clausulaStringList.get(clausulaStringList.size()-1));
					System.out.println(Algorithms.resolutionAlgorithm(negatedSet, clausulasSet, new Clausula(clausulaStringList.get(clausulaStringList.size()-1))));
					break;
				case COOKING:
					clausulasSet = getClausulas(clausulaStringList, true);
					List<Command> commands = getCommands(commandStringList);
					Algorithms.cookingAlgorithm(commands, clausulasSet);
					break;
				default:
					throw new IllegalArgumentException("Invalid algorithm type!");
			}

		} catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	private static List<Command> getCommands(List<String> commandStringList) {
		List<Command> commands = new ArrayList<>();
		for(String command : commandStringList) {
			CommandType commandType;
			switch(command.charAt(command.length()-1)){
				case '?':
					commandType = CommandType.TEST;
					break;
				case '-':
					commandType = CommandType.REMOVE;
					break;
				case '+':
					commandType = CommandType.ADD;
					break;
				default:
					throw new IllegalArgumentException("No such command supported!");
			}
			commands.add(new Command(command.substring(0, command.length()-2), commandType));
		}
		return commands;
	}

	private static LinkedHashSet<Clausula> getClausulas(List<String> clausulaStringList, boolean complete) {
		LinkedHashSet<Clausula> clausulas = new LinkedHashSet<>();
		int size = complete ? clausulaStringList.size() : clausulaStringList.size()-1;
		for(int i = 0; i < size; i++) {
			String clausula = clausulaStringList.get(i);
			StrategyAlgorihtms.addToClausulaSet(clausula, clausulas);
		}
		return clausulas;
	}

}

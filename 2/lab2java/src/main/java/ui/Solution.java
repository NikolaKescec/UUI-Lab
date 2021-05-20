package ui;

import algorithms.AlgorithmType;
import algorithms.Algorithms;
import algorithms.StrategyAlgorithms;
import structures.Clause;
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
		Path clauseList = null;
		Path commandList = null;

		if(args.length < 1)
			throw new IllegalArgumentException("Too few arguments!");

		try {

			// arguments parsing
			switch (args[0]) {
				case "resolution":
					if(args.length != 2)
						throw new IllegalArgumentException("Resolution command needs only clause list path!");
					algorithmType = AlgorithmType.RESOLUTION;
					clauseList = Paths.get(args[1]);
					break;
				case "cooking":
					if(args.length != 3)
						throw new IllegalArgumentException("Cooking command needs clause list path and command list path!");
					algorithmType = AlgorithmType.COOKING;
					clauseList = Paths.get(args[1]);
					commandList = Paths.get(args[2]);
					break;
				default:
					throw new UnsupportedOperationException("No such algorithm");
			}

			// Variables declaration
			List<String> clauseStringList;
			List<String> commandStringList = null;

			if(clauseList == null || !Files.exists(clauseList) || !Files.isReadable(clauseList))
				throw new IllegalArgumentException("Please write correct clause list file path.");

			clauseStringList = Files.readAllLines(clauseList).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

			if(algorithmType.equals(AlgorithmType.COOKING) && (commandList == null || !Files.exists(commandList) || !Files.isReadable(commandList)))
				throw new IllegalArgumentException("When using cooking algorithm correct path to command list must be given!");

			if(algorithmType.equals(AlgorithmType.COOKING))
				commandStringList =  Files.readAllLines(commandList).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());

			LinkedHashSet<Clause> clausesSet;
			switch (algorithmType){
				case RESOLUTION:
					//DO NOT ADD ALL CLAUSES BECAUSE LAST CLAUSE IS NEGATED!
					clausesSet = getClauses(clauseStringList, false);
					LinkedHashSet<Clause> negatedSet = Clause.negateClause(clauseStringList.get(clauseStringList.size()-1));
					System.out.println(Algorithms.resolutionAlgorithm(negatedSet, clausesSet, new Clause(clauseStringList.get(clauseStringList.size()-1))));
					break;
				case COOKING:
					//ADD ALL CLAUSES!
					clausesSet = getClauses(clauseStringList, true);
					List<Command> commands = getCommands(commandStringList);
					Algorithms.cookingAlgorithm(commands, clausesSet);
					break;
				default:
					throw new IllegalArgumentException("Invalid algorithm type!");
			}

		} catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Method will get commands from command string list.
	 * @param commandStringList command string list
	 * @return list of commands
	 */
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

	/**
	 * Method will get clauses from clause string list.
	 * @param clauseStringList clause string list
	 * @param complete argument that, if true, will add all clauses from clause string list into output linked hash set
	 * @return linked hash set
	 */
	private static LinkedHashSet<Clause> getClauses(List<String> clauseStringList, boolean complete) {
		LinkedHashSet<Clause> clauses = new LinkedHashSet<>();
		int size = complete ? clauseStringList.size() : clauseStringList.size()-1;
		for(int i = 0; i < size; i++) {
			String clause = clauseStringList.get(i);
			StrategyAlgorithms.addToClauseSet(clause, clauses, null);
		}
		return clauses;
	}

}

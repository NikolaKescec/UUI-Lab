package ui;

import algorithms.AlgorithmType;
import algorithms.Algorithms;
import algorithms.HeuristicsChecker;
import successor.SuccState;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Solution {

	public static void main(String ... args) {
		// declaring variables

		AlgorithmType algorithmType = null;
		Path stateSpace = null;
		Path heuristicsDescriptor = null;
		boolean checkOptimistic = false;
		boolean checkConsistent = false;

		try {
			// arguments parsing
			for(int i = 0; i < args.length; i++) {
				switch (args[i]){
					case "--alg" :
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						i++;
						switch (args[i]) {
							case "bfs" :
								algorithmType = AlgorithmType.BFS;
								break;
							case "ucs" :
								algorithmType= AlgorithmType.UCS;
								break;
							case "astar" :
								algorithmType = AlgorithmType.ASTAR;
								break;
							default : throw new IllegalArgumentException("Unsupported algorithm: " + args[i]);
						};
						break;
					case "--ss" :
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						i++;
						stateSpace = Path.of(args[i]);
						break;
					case "--h" :
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						i++;
						heuristicsDescriptor = Path.of(args[i]);
						break;
					case "--check-optimistic" :
						checkOptimistic = true;
						break;
					case "--check-consistent" :
						checkConsistent = true;
						break;
					default : throw new IllegalArgumentException("Unknown parameter: " + args[i]);
				}
			}

			// further initialization
			Map<String, Set<SuccState>> transitions;
			Map<String, Double> heuristics = null;
			List<String> heuristicsString;
			String startingState;
			Set<String> goalStates;

			// reading files
			if(stateSpace == null || !Files.exists(stateSpace))
				throw new IllegalArgumentException("Invalid path to state space!");

			if(heuristicsDescriptor != null && !Files.exists(heuristicsDescriptor))
				throw new IllegalArgumentException("Invalid path to heuristics descriptor!");

			// PARSING STATES GIVEN IN STATE SPACE
			List<String> spaceStrings = Files.readAllLines(stateSpace, StandardCharsets.UTF_8).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
			startingState = spaceStrings.get(0);
			goalStates = parseGoalStates(spaceStrings.get(1));
			transitions = prepareSuccessorStates(spaceStrings.subList(2, spaceStrings.size()), algorithmType != null && algorithmType.equals(AlgorithmType.BFS));

			// IF HEURISTICS DESCRIPTOR IS GIVEN AND IS VALID PARSE HEURISTICS
			if(heuristicsDescriptor != null) {
				heuristicsString = Files.readAllLines(heuristicsDescriptor, StandardCharsets.UTF_8);
				heuristics = parseHeuristics(heuristicsString);
			}

			// calling correct algorithm
			if(algorithmType != null) {
				switch (algorithmType) {
					case BFS:
						System.out.println("# BFS");
						System.out.println(Algorithms.algorithmBFS(startingState, transitions::get, goalStates));
						break;
					case UCS:
						System.out.println("# UCS");
						System.out.println(Algorithms.algorithmUCS(startingState, transitions::get, goalStates));
						break;
					case ASTAR:
						if(heuristicsDescriptor == null)
							throw new IllegalArgumentException("No heuristics given!");
						System.out.println("# A-STAR " + heuristicsDescriptor.toString());

						System.out.println(Algorithms.algorithmASTAR(startingState, transitions::get, goalStates, heuristics::get));
						break;
					default:
						throw new IllegalArgumentException("Unknown algorithm type.");
				}
			}

			// CALLING HEURISTICS CHECKING ALGHORITMS
			if(checkOptimistic) {
				if(heuristics == null)
					throw new IllegalArgumentException("Heuristics not initialized!");
				System.out.println("# HEURISTIC-OPTIMISTIC " + heuristicsDescriptor.toString());
				HeuristicsChecker.checkOptimism(heuristics, transitions::get, goalStates);
			}

			if(checkConsistent) {
				if(heuristics == null)
					throw new IllegalArgumentException("Heuristics not initialized!");
				System.out.println("# HEURISTIC-CONSISTENT " + heuristicsDescriptor.toString());
				HeuristicsChecker.checkConsistency(heuristics, transitions::get);
			}

		} catch (IOException e) {
			System.out.println("Unsuccessful reading of " + stateSpace.toString());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Method will parse given list of string into a map that contains the name of state and its heuristic value.
	 * @param heuristicsString
	 * @return heuristics map
	 */
	private static Map<String, Double> parseHeuristics(List<String> heuristicsString) {
		Map<String, Double> heuristics = new HashMap<>();
		for(String entry : heuristicsString) {
			String[] pair = entry.split(":");
			heuristics.put(pair[0].trim(), Double.parseDouble(pair[1].trim()));
		}
		return heuristics;
	}

	/**
	 * Method will parse given string into a set of goal states (separated by blank space).
	 * @param s
	 * @return
	 */
	private static Set<String> parseGoalStates(String s) {
		String[] goals = s.split(" ");
		return new HashSet<>(Arrays.asList(goals));
	}

	/**
	 * Method will parse given list of string into a map of state names and its successors (children)
	 * @param spaceStrings list of children
	 * @param sorted will create a sorted set instead of normal order
	 * @return
	 */
	private static Map<String, Set<SuccState>> prepareSuccessorStates(List<String> spaceStrings, boolean sorted) {
		Map<String, Set<SuccState>> transitions = new HashMap<>();

		// decided on sorted variable
		Supplier<Set<SuccState>> supplier;
		if(sorted) {
			supplier = () -> new TreeSet<>(SuccState.byState);
		} else {
			supplier = HashSet::new;
		}

		for(String line : spaceStrings) {
			String[] initialSplit = line.split(":");
			String key = initialSplit[0].trim();
			if(initialSplit.length == 1) {
				transitions.put(key, new HashSet<>());
				continue;
			}

			Set<SuccState> states = Arrays.stream(initialSplit[1].trim().split(" ")).map(s -> {
				String[] pairs = s.split(",");
				return new SuccState(pairs[0].trim(), Double.parseDouble(pairs[1].trim()));
			}).collect(Collectors.toCollection(supplier));
			transitions.put(key, states);
		}
		return transitions;
	}

}

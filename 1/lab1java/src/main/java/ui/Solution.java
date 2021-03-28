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
					case "--alg" -> {
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						i++;
						algorithmType = switch (args[i]) {
							case "bfs" -> AlgorithmType.BFS;
							case "ucs" -> AlgorithmType.UCS;
							case "astar" -> AlgorithmType.ASTAR;
							default -> throw new IllegalArgumentException("Unsupported algorithm: " + args[i]);
						};
					}
					case "--ss" -> {
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						i++;
						stateSpace = Path.of(args[i]);
					}
					case "--h" -> {
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						i++;
						heuristicsDescriptor = Path.of(args[i]);
					}
					case "--check-optimistic" -> {
						checkOptimistic = true;
					}
					case "--check-consistent" -> {
						checkConsistent = true;
					}
					default -> throw new IllegalArgumentException("Unknown parameter: " + args[i]);
				}
			}

			// further initialization
			Map<String, Set<SuccState>> transitions;
			String startingState;
			Set<String> goalStates;

			// reading files
			if(stateSpace == null || !Files.exists(stateSpace))
				throw new IllegalArgumentException("Invalid path to state space!");

			if(algorithmType == null || (algorithmType.equals(AlgorithmType.ASTAR) && !Files.exists(heuristicsDescriptor)))
				throw new IllegalArgumentException("Invalid path to heuristics descriptor!");

			List<String> spaceStrings = Files.readAllLines(stateSpace, StandardCharsets.UTF_8).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
			
			startingState = spaceStrings.get(0);
			goalStates = parseGoalStates(spaceStrings.get(1));

			switch(algorithmType) {
				case BFS -> {
					transitions = prepareSuccessorStates(spaceStrings.subList(2, spaceStrings.size()), true);
					System.out.println("# BFS");
					System.out.println(Algorithms.algorithmBFS(startingState, transitions::get, goalStates));
				}
				case UCS -> {
					transitions = prepareSuccessorStates(spaceStrings.subList(2, spaceStrings.size()), false);
					System.out.println("# UCS");
					System.out.println(Algorithms.algorithmUCS(startingState, transitions::get, goalStates));
				}
				case ASTAR -> {
					transitions = prepareSuccessorStates(spaceStrings.subList(2, spaceStrings.size()), false);
					System.out.println("# A-STAR " + heuristicsDescriptor.toString());
					List<String> heuristicsString = Files.readAllLines(heuristicsDescriptor, StandardCharsets.UTF_8);
					Map<String, Double> heuristics = parseHeuristics(heuristicsString);
					System.out.println(Algorithms.algorithmASTAR(startingState, transitions::get, goalStates, heuristics::get));

					if(checkOptimistic) {
						System.out.println("# HEURISTIC-OPTIMISTIC " + heuristicsDescriptor.toString());
						HeuristicsChecker.checkOptimism(heuristics, transitions::get, goalStates);
					}

					if(checkConsistent) {
						System.out.println("# HEURISTIC-CONSISTENT " + heuristicsDescriptor.toString());
						HeuristicsChecker.checkConsistency(heuristics, transitions::get);
					}
						// POZIVANJE ALGORITMA PROVJERE KONZISTENTOSTI
				}
				default -> throw new IllegalArgumentException("Unknown algorithm type.");
			}
		} catch (IOException e) {
			System.out.println("Unsuccessful reading of " + stateSpace.toString());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

	}

	private static Map<String, Double> parseHeuristics(List<String> heuristicsString) {
		Map<String, Double> heuristics = new HashMap<>();
		for(String entry : heuristicsString) {
			String[] pair = entry.split(":");
			heuristics.put(pair[0].trim(), Double.parseDouble(pair[1].trim()));
		}
		return heuristics;
	}

	private static Set<String> parseGoalStates(String s) {
		String[] goals = s.split(" ");
		return new HashSet<>(Arrays.asList(goals));
	}

	private static Map<String, Set<SuccState>> prepareSuccessorStates(List<String> spaceStrings, boolean sorted) {
		Map<String, Set<SuccState>> transitions = new HashMap<>();

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

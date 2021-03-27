package ui;

import algorithms.AlgorithmType;
import algorithms.Algorithms;
import successor.SuccState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
						algorithmType = switch (args[i + 1]) {
							case "bfs" -> AlgorithmType.BFS;
							case "ucs" -> AlgorithmType.UCS;
							case "astar" -> AlgorithmType.ASTAR;
							default -> throw new IllegalArgumentException("Unsupported algorithm: " + args[i + 1]);
						};
					}
					case "--ss" -> {
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						stateSpace = Path.of(args[i+1]);
					}
					case "--h" -> {
						if (!(i + 1 < args.length))
							throw new IllegalArgumentException("Invalid number of arguments");
						heuristicsDescriptor = Path.of(args[i+1]);
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

			List<String> spaceStrings = Files.readAllLines(stateSpace).stream().filter(s -> !s.startsWith("#")).collect(Collectors.toList());
			
			startingState = spaceStrings.get(0);
			goalStates = parseGoalStates(spaceStrings.get(1));
			transitions = prepareSuccessorStates(spaceStrings);

			switch(algorithmType) {
				case BFS -> {
					System.out.println("# BFS");
					Algorithms.algorithmBFS(startingState, transitions::get, goalStates);
				}
				case UCS -> {
					System.out.println("# UCS");
					Algorithms.algorithmUCS(startingState, transitions::get, goalStates);
				}
				case ASTAR -> {
					System.out.println("# ASTAR " + heuristicsDescriptor.toString());
					List<String> heuristicsString = Files.readAllLines(heuristicsDescriptor);
					Map<String, Double> heuristics = parseHeuristics(heuristicsString);
					// POZIVANJE ALGORITMA ASTAR

					if(checkOptimistic) {}
						// POZIVANJE ALGORITMA PROVJERE OPTIMIZMA

					if(checkConsistent) {}
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

	private static Map<String, Set<SuccState>> prepareSuccessorStates(List<String> spaceStrings) {
		Map<String, Set<SuccState>> transitions = new HashMap<>();
		for(String line : spaceStrings) {
			String[] initialSplit = line.split(":");
			String key = initialSplit[0].trim();
			Set<SuccState> states = Arrays.stream(initialSplit[1].trim().split(" ")).map(s -> {
				String[] pairs = s.split(",");
				return new SuccState(pairs[0].trim(), Double.parseDouble(pairs[1].trim()));
			}).collect(Collectors.toUnmodifiableSet());
			transitions.put(key, states);
		}
		return transitions;
	}

}

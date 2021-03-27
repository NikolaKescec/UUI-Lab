package algorithms;

import state.WeightedNode;
import successor.SuccState;

import java.util.*;
import java.util.function.Function;

public class Algorithms {

    public static void algorithmBFS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        Queue<WeightedNode> open = new PriorityQueue<>(WeightedNode.compareByState);
        open.add(new WeightedNode(startingState, 0, null));
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            if(goalStates.contains(state.getState())) {
                printOutputOfAlgorithm(true, visited.size(), WeightedNode.depth(state), state.getCost(), state);
                return;
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean inOpen = false;
                if(visited.contains(successor.getState()))
                    continue;
                for (WeightedNode currentNode : open) {
                    if (successor.getState().equals(currentNode.getState())) {
                        inOpen = true;
                        break;
                    }
                }
                if(!inOpen)
                    open.add(new WeightedNode(successor.getState(), state.getCost() + successor.getCost(), state));
            }
        }
        printOutputOfAlgorithm(false, visited.size(), 0, 0, null);
    }

    public static void algorithmUCS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        Queue<WeightedNode> open = new PriorityQueue<>(WeightedNode.compareByCost);
        open.add(new WeightedNode(startingState, 0, null));
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            if(goalStates.contains(state.getState())) {
                printOutputOfAlgorithm(true, visited.size(), WeightedNode.depth(state), state.getCost(), state);
                return;
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean addCheaper = false;
                if(visited.contains(successor.getState()))
                    continue;
                Iterator<WeightedNode> iterator = open.iterator();
                double possibleCost = successor.getCost() + state.getCost();
                while(iterator.hasNext()) {
                    WeightedNode currentNode = iterator.next();
                    if(successor.getState().equals(currentNode.getState()) && possibleCost <= currentNode.getCost()) {
                        iterator.remove();
                        addCheaper = true;
                        break;
                    }
                }
                if(addCheaper)
                    open.add(new WeightedNode(successor.getState(), possibleCost, state));
            }
        }
        printOutputOfAlgorithm(false, visited.size(), 0, 0, null);
    }

    // TODO FINISH
    public static void algorithmASTAR(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates, Function<String, Double> heuristics) {
        Set<String> visited = new HashSet<>();
        Queue<WeightedNode> open = new PriorityQueue<>(WeightedNode.compareByCost);
        open.add(new WeightedNode(startingState, 0, null));
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            if(goalStates.contains(state.getState())) {
                printOutputOfAlgorithm(true, visited.size(), WeightedNode.depth(state), state.getCost(), state);
                return;
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean addCheaper = false;
                if(visited.contains(successor.getState()))
                    continue;
                Iterator<WeightedNode> iterator = open.iterator();
                double possibleCost = successor.getCost() + state.getCost();
                while(iterator.hasNext()) {
                    WeightedNode currentNode = iterator.next();
                    if(successor.getState().equals(currentNode.getState()) && possibleCost <= currentNode.getCost()) {
                        iterator.remove();
                        addCheaper = true;
                        break;
                    }
                }
                if(addCheaper)
                    open.add(new WeightedNode(successor.getState(), possibleCost, state));
            }
        }
        printOutputOfAlgorithm(false, visited.size(), 0, 0, null);
    }

    private static void printOutputOfAlgorithm(boolean foundSolution, int visitedStates, int pathLength, double totalCost, WeightedNode finalState){
        System.out.println("[FOUND_SOLUTION]: " + (foundSolution ? "yes" : "no"));
        System.out.println("[STATES_VISITED]: " + visitedStates);
        if(foundSolution) {
            System.out.println("[PATH_LENGTH]: " + pathLength);
            System.out.println("[TOTAL_COST]: " + totalCost);
            System.out.println(finalState.printPathFromParent());
        }
    }
}

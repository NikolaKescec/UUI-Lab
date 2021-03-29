package algorithms;

import result.SearchResult;
import state.HeuristicsNode;
import state.WeightedNode;
import successor.SuccState;

import java.util.*;
import java.util.function.Function;

public class Algorithms {

    /**
     * Method will traverse the search tree and try to find the goal state using BFS algorithm.
     * @param startingState
     * @param succ
     * @param goalStates
     * @return
     */
    public static SearchResult algorithmBFS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        LinkedList<WeightedNode> open = new LinkedList<>();
        open.add(new WeightedNode(startingState, 0, null));
        int visitedStatesNumber = 0;
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            visitedStatesNumber++;
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visitedStatesNumber, WeightedNode.depth(state), state.getCost());
            }
            visited.add(state.getState());
            List<SuccState> successors = new ArrayList<>(succ.apply(state.getState()));
            for(SuccState successor : successors) {
                if(visited.contains(successor.getState()))
                    continue;
                open.addLast(new WeightedNode(successor.getState(), state.getCost() + successor.getCost(), state));
            }
        }
        return new SearchResult(false, visited.size());
    }

    /**
     * Method will traverse the search tree and try to find the goal state using UCS algorithm.
     * @param startingState
     * @param succ
     * @param goalStates
     * @return
     */
    public static SearchResult algorithmUCS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        Queue<WeightedNode> open = new PriorityQueue<>(WeightedNode.compareByCost);
        open.add(new WeightedNode(startingState, 0, null));
        int visitedStatesNumber = 0;
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            visitedStatesNumber++;
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visitedStatesNumber, WeightedNode.depth(state), state.getCost());
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean successorCheaper = true;
                if(visited.contains(successor.getState()))
                    continue;
                Iterator<WeightedNode> iterator = open.iterator();
                double possibleCost = successor.getCost() + state.getCost();
                while(iterator.hasNext()) {
                    WeightedNode currentNode = iterator.next();
                    if(!successor.getState().equals(currentNode.getState())) continue;
                    if(possibleCost > currentNode.getCost()) {
                        successorCheaper = false;
                        break;
                    }
                    iterator.remove();
                }
                if(successorCheaper)
                    open.add(new WeightedNode(successor.getState(), possibleCost, state));
            }
        }
        return new SearchResult(false, visited.size());
    }

    /**
     * Method will traverse the search tree and try to find the goal state using ASTAR algorithm.
     * @param startingState
     * @param succ
     * @param goalStates
     * @return
     */
    public static SearchResult algorithmASTAR(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates, Function<String, Double> heuristics) {
        Set<String> visited = new HashSet<>();
        Queue<HeuristicsNode> open = new PriorityQueue<>(HeuristicsNode.compareByCombinedCost);
        open.add(new HeuristicsNode(startingState, 0, null, 0));
        int visitedStatesNumber = 0;
        while(!open.isEmpty()) {
            HeuristicsNode state = open.remove();
            visitedStatesNumber++;
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visitedStatesNumber, WeightedNode.depth(state), state.getCost());
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean successorCheaper = true;
                if(visited.contains(successor.getState()))
                    continue;
                Iterator<HeuristicsNode> iterator = open.iterator();
                double possibleCost = successor.getCost() + state.getCost();
                double possibleCombinedCost = possibleCost + heuristics.apply(successor.getState());
                while(iterator.hasNext()) {
                    WeightedNode currentNode = iterator.next();
                    if(!successor.getState().equals(currentNode.getState())) continue;
                    if(possibleCost > currentNode.getCost()) {
                        successorCheaper = false;
                        break;
                    }
                    iterator.remove();
                }
                if(successorCheaper)
                    open.add(new HeuristicsNode(successor.getState(), possibleCost, state, possibleCombinedCost));
            }
        }
        return new SearchResult(false, visited.size());
    }
}

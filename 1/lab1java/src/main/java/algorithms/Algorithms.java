package algorithms;

import result.SearchResult;
import state.HeuristicsNode;
import state.WeightedNode;
import successor.SuccState;

import java.util.*;
import java.util.function.Function;

/**
 * Class contains concrete algorithm implementations: BFS, UCS, A-STAR
 */
public class Algorithms {

    /**
     * Method will traverse the search tree and try to find the goal state using BFS algorithm.
     * @param startingState state from which the search starts
     * @param succ function that, given state name will return successors for that state
     * @param goalStates states in which search ends
     * @return Search result which can be positive, meaning the path to goal state has been found or negative
     */
    public static SearchResult algorithmBFS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        LinkedList<WeightedNode> open = new LinkedList<>();
        Set<String> currentlyInOpen = new HashSet<>();
        open.add(new WeightedNode(startingState, 0, null));
        currentlyInOpen.add(startingState);
        int visitedStatesNumber = 0;
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            currentlyInOpen.remove(state.getState());
            visitedStatesNumber++;
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visitedStatesNumber, WeightedNode.depth(state), state.getCost());
            }
            visited.add(state.getState());
            List<SuccState> successors = new ArrayList<>(succ.apply(state.getState()));
            for(SuccState successor : successors) {
                if(visited.contains(successor.getState()))
                    continue;

                // check if this current tree node is already in open, if it is then don't add it in
                if(!currentlyInOpen.contains(successor.getState())) {
                    open.addLast(new WeightedNode(successor.getState(), state.getCost() + successor.getCost(), state));
                    currentlyInOpen.add(successor.getState());
                }
            }
        }
        return new SearchResult(false, visited.size());
    }

    /**
     * Method will traverse the search tree and try to find the goal state using UCS algorithm.
     * @param startingState state from which the search starts
     * @param succ function that, given state name will return successors for that state
     * @param goalStates states in which search ends
     * @return Search result which can be positive, meaning the path to goal state has been found or negative
     */
    public static SearchResult algorithmUCS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        Queue<WeightedNode> open = new PriorityQueue<>(WeightedNode.COMPARE_BY_COST.thenComparing(WeightedNode.COMPARE_BY_STATE));
        // significantly saves time when checking if state is in open
        Set<String> currentlyInOpen = new HashSet<>();
        open.add(new WeightedNode(startingState, 0, null));
        currentlyInOpen.add(startingState);
        int visitedStatesNumber = 0;
        while(!open.isEmpty()) {
            WeightedNode state = open.poll();
            currentlyInOpen.remove(state.getState());
            visitedStatesNumber++;
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visitedStatesNumber, WeightedNode.depth(state), state.getCost());
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean successorCheaper = true;
                double possibleCost = successor.getCost() + state.getCost();

                if(visited.contains(successor.getState()))
                    continue;

                // check if this current tree node is already in open, if it is only then iterate trough open list
                if(currentlyInOpen.contains(successor.getState())) {
                    Iterator<WeightedNode> iterator = open.iterator();
                    while(iterator.hasNext()) {
                        WeightedNode currentNode = iterator.next();
                        if(!successor.getState().equals(currentNode.getState())) continue;
                        if(currentNode.getCost() <= possibleCost) {
                            successorCheaper = false;
                        } else {
                            iterator.remove();
                        }
                        break;
                    }
                }
                if(successorCheaper) {
                    open.offer(new WeightedNode(successor.getState(), possibleCost, state));
                    currentlyInOpen.add(successor.getState());
                }
            }
        }
        return new SearchResult(false, visited.size());
    }

    /**
     * Method will traverse the search tree and try to find the goal state using ASTAR algorithm.
     * @param startingState state from which the search starts
     * @param succ function that, given state name will return successors for that state
     * @param goalStates states in which search ends
     * @param heuristics function that, given a state name will return its heuristics value bound to it
     * @return Search result which can be positive, meaning the path to goal state has been found or negative
     */
    public static SearchResult algorithmASTAR(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates, Function<String, Double> heuristics) {
        Set<String> visited = new HashSet<>();
        Queue<HeuristicsNode> open = new PriorityQueue<>(HeuristicsNode.compareByCombinedCost.thenComparing(HeuristicsNode.COMPARE_BY_STATE));
        Set<String> currentlyInOpen = new HashSet<>();
        open.add(new HeuristicsNode(startingState, 0, null, 0));
        currentlyInOpen.add(startingState);
        int visitedStatesNumber = 0;
        while(!open.isEmpty()) {
            HeuristicsNode state = open.remove();
            currentlyInOpen.remove(startingState);
            visitedStatesNumber++;
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visitedStatesNumber, WeightedNode.depth(state), state.getCost());
            }
            visited.add(state.getState());
            for(SuccState successor : succ.apply(state.getState())) {
                boolean successorCheaper = true;
                double possibleCost = successor.getCost() + state.getCost();
                double possibleCombinedCost = possibleCost + heuristics.apply(successor.getState());
                if(visited.contains(successor.getState()))
                    continue;

                // check if this current tree node is already in open, if it is only then iterate trough open list
                if(currentlyInOpen.contains(successor.getState())) {
                    Iterator<HeuristicsNode> iterator = open.iterator();
                    while(iterator.hasNext()) {
                        WeightedNode currentNode = iterator.next();
                        if(!successor.getState().equals(currentNode.getState())) continue;
                        if(currentNode.getCost() <= possibleCost) {
                            successorCheaper = false;
                        } else {
                            iterator.remove();
                        }
                        break;
                    }
                }
                if(successorCheaper) {
                    open.offer(new HeuristicsNode(successor.getState(), possibleCost, state, possibleCombinedCost));
                    currentlyInOpen.add(successor.getState());
                }
            }
        }
        return new SearchResult(false, visited.size());
    }
}

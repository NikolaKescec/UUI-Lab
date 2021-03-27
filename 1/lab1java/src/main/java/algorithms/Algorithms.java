package algorithms;

import result.SearchResult;
import state.HeuristicsNode;
import state.WeightedNode;
import successor.SuccState;

import java.util.*;
import java.util.function.Function;

public class Algorithms {

    public static SearchResult algorithmBFS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        LinkedList<WeightedNode> open = new LinkedList<>();
        open.add(new WeightedNode(startingState, 0, null));
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            visited.add(state.getState());
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visited.size(), WeightedNode.depth(state), state.getCost());
            }
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
                    open.addLast(new WeightedNode(successor.getState(), state.getCost() + successor.getCost(), state));
            }
        }
        return new SearchResult(false, visited.size());
    }

    public static SearchResult algorithmUCS(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        Set<String> visited = new HashSet<>();
        Queue<WeightedNode> open = new PriorityQueue<>(WeightedNode.compareByCost);
        open.add(new WeightedNode(startingState, 0, null));
        while(!open.isEmpty()) {
            WeightedNode state = open.remove();
            visited.add(state.getState());
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visited.size(), WeightedNode.depth(state), state.getCost());
            }
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

    public static SearchResult algorithmASTAR(String startingState, Function<String, Set<SuccState>> succ, Set<String> goalStates, Function<String, Double> heuristics) {
        Set<String> visited = new HashSet<>();
        Queue<HeuristicsNode> open = new PriorityQueue<>(HeuristicsNode.compareByCombinedCost);
        open.add(new HeuristicsNode(startingState, 0, null, 0));
        while(!open.isEmpty()) {
            HeuristicsNode state = open.remove();
            visited.add(state.getState());
            if(goalStates.contains(state.getState())) {
                return new SearchResult(true, state, visited.size(), WeightedNode.depth(state), state.getCost());
            }
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

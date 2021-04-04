package result;

import state.WeightedNode;

/**
 * Class represents search result. Consists of  goal state, boolean flag that describes if solution has been found,
 * number of visited states, path length and total path cost.
 */
public class SearchResult {

    private WeightedNode goal;
    private boolean solutionFound;
    private int visitedStates;
    private int pathLength;
    private double totalCost;

    public SearchResult(boolean solutionFound, WeightedNode goal, int visitedStates, int pathLength, double totalCost) {
        this.goal = goal;
        this.solutionFound = solutionFound;
        this.visitedStates = visitedStates;
        this.pathLength = pathLength;
        this.totalCost = totalCost;
    }

    public SearchResult(boolean solutionFound, int visitedStates) {
        this.solutionFound = solutionFound;
        this.visitedStates = visitedStates;
    }

    public WeightedNode getGoal() {
        return goal;
    }

    public void setGoal(WeightedNode goal) {
        this.goal = goal;
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public void setSolutionFound(boolean solutionFound) {
        this.solutionFound = solutionFound;
    }

    public int getVisitedStates() {
        return visitedStates;
    }

    public void setVisitedStates(int visitedStates) {
        this.visitedStates = visitedStates;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("[FOUND_SOLUTION]: ").append(solutionFound ? "yes" : "no").append("\n");
        resultBuilder.append("[STATES_VISITED]: ").append(visitedStates);
        if(solutionFound) {
            resultBuilder.append("\n");
            resultBuilder.append("[PATH_LENGTH]: ").append(pathLength).append("\n");
            resultBuilder.append("[TOTAL_COST]: ").append(totalCost).append("\n");
            resultBuilder.append("[PATH]: ").append(goal.printPathFromParent());
        }
        return resultBuilder.toString();
    }
}

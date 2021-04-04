package algorithms;

import result.SearchResult;
import successor.SuccState;

import java.util.Comparator;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HeuristicsChecker {

    /**
     * Method will check optimism of given heuristics by calling UCS for every state and checking the optimism condition.
     * Condition: h(testState) <= h*(testState)
     * @param heuristicsValues map of heuristics values, key is name of state and value is its heuristic value
     * @param succ function that, given state name will return successors for that state
     * @param goalStates states in which search ends
     */
    public static void checkOptimism(Map<String, Double> heuristicsValues, Function<String, Set<SuccState>> succ, Set<String> goalStates) {
        boolean optimism = true;
        for(Map.Entry<String, Double> heuristicValue : heuristicsValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList())) {
            StringBuilder heuristicOptimisticString = new StringBuilder();
            SearchResult searchResult = Algorithms.algorithmUCS(heuristicValue.getKey(), succ, goalStates);
            if(!searchResult.isSolutionFound())
                System.out.println("[ERROR]: Problem has no path to solution.");
            double realCost = searchResult.getTotalCost();
            heuristicOptimisticString.append("[CONDITION]: ");
            if(heuristicValue.getValue() <= realCost) {
                addToOptimisticBuilder(heuristicOptimisticString, false, heuristicValue.getKey(), heuristicValue.getValue(), realCost);
            } else {
                addToOptimisticBuilder(heuristicOptimisticString, true, heuristicValue.getKey(), heuristicValue.getValue(), realCost);
                optimism = false;
            }
            System.out.println(heuristicOptimisticString);
        }
        System.out.println("[CONCLUSION]: Heuristic is " + (optimism ? "" : "not ") + "optimistic.");
    }

    /**
     * Optimism result string builder method.
     * @param heuristicOptimisticString string builder that will build the report
     * @param error boolean value which describes if this condition is passed
     * @param key tested state name
     * @param value state heuristic value
     * @param realCost real cost calculated by UCS
     */
    private static void addToOptimisticBuilder(StringBuilder heuristicOptimisticString, boolean error, String key, Double value, double realCost) {
        heuristicOptimisticString.append(error ? "[ERR] " : "[OK]").append(" h(").append(key).append(") <= h*: ").append(value).append(" <= ").append(realCost);
    }

    /**
     * Method will check consistency based on consistency condition.
     * Condition: h(testState) <= h(succState) + cost.
     * @param heuristicsValues map of heuristics values, key is name of state and value is its heuristic value
     * @param succ function that, given state name will return successors for that state
     */
    public static void checkConsistency(Map<String, Double> heuristicsValues, Function<String, Set<SuccState>> succ) {
        boolean consistency = true;
        for(Map.Entry<String, Double> heuristicValue : heuristicsValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList())) {
            for(SuccState successor : succ.apply(heuristicValue.getKey()).stream().sorted(SuccState.byState).collect(Collectors.toList())) {
                StringBuilder heuristicConsistencyBuilder = new StringBuilder();
                heuristicConsistencyBuilder.append("[CONDITION]: ");
                double heuristicCondition = heuristicsValues.get(successor.getState()) + successor.getCost();
                if(heuristicValue.getValue() <= heuristicCondition) {
                    addToConsistencyBuilder(heuristicConsistencyBuilder, false, heuristicValue.getKey(), successor.getState(), heuristicValue.getValue(), heuristicsValues.get(successor.getState()), successor.getCost());
                } else {
                    addToConsistencyBuilder(heuristicConsistencyBuilder, true, heuristicValue.getKey(), successor.getState(), heuristicValue.getValue(), heuristicsValues.get(successor.getState()), successor.getCost());
                    consistency = false;
                }
                System.out.println(heuristicConsistencyBuilder);
            }
        }
        System.out.println("[CONCLUSION]: Heuristic is " + (consistency ? "" : "not ") + "consistent.");
    }

    /**
     * Consistency string builder.
     * @param builder string builder that will build the report
     * @param b boolean that will inform if condition is passed
     * @param key tested state name
     * @param state successor state
     * @param value heuristic value of tested state
     * @param futureValue successor value heuristic value
     * @param cost cost of successor state
     */
    private static void addToConsistencyBuilder(StringBuilder builder, boolean b, String key, String state, Double value, double futureValue, double cost) {
        builder.append(b ? "[ERR]" : "[OK]").append(" h(").append(key).append(") <= h(").append(state).append(") + c: ")
                .append(value)
                .append(" <= ")
                .append(futureValue)
                .append(" + ")
                .append(cost);
    }

}

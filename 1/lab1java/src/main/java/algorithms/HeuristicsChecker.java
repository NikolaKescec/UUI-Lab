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
     * Method will check optimism of given heuristics by calling UCS for every state and checking the optimism condition..
     * @param heuristicsValues
     * @param succ
     * @param goalStates
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
     * @param heuristicOptimisticString
     * @param error
     * @param key
     * @param value
     * @param realCost
     */
    private static void addToOptimisticBuilder(StringBuilder heuristicOptimisticString, boolean error, String key, Double value, double realCost) {
        heuristicOptimisticString.append(error ? "[ERR] " : "[OK]").append(" h(").append(key).append(") <= h*: ").append(value).append(" <= ").append(realCost);
    }

    /**
     * Method will check consistency based on consistency condition.
     * @param heuristicsValues
     * @param succ
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
     * @param builder
     * @param b
     * @param key
     * @param state
     * @param value
     * @param futureValue
     * @param cost
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

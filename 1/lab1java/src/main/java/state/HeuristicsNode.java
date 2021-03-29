package state;

import java.util.Comparator;

/**
 * Similar to weighted node, but with addition of combined cost (total cost + heuristic cost).
 */
public class HeuristicsNode extends WeightedNode{

    private double combinedCost;

    public HeuristicsNode(String state, double cost, WeightedNode parentNode, double combinedCost) {
        super(state, cost, parentNode);
        this.combinedCost = combinedCost;
    }

    /**
     * Compares by combined cost then by state name.
     */
    public static final Comparator<HeuristicsNode> compareByCombinedCost = Comparator.comparing(HeuristicsNode::getCombinedCost).thenComparing(HeuristicsNode.compareByState);

    public double getCombinedCost() {
        return combinedCost;
    }

    public void setCombinedCost(double combinedCost) {
        this.combinedCost = combinedCost;
    }

    @Override
    public String toString() {
        return "HeuristicsNode{}";
    }
}

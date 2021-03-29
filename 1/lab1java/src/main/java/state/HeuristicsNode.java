package state;

import java.util.Comparator;

public class HeuristicsNode extends WeightedNode{

    private double combinedCost;

    public HeuristicsNode(String state, double cost, WeightedNode parentNode, double combinedCost) {
        super(state, cost, parentNode);
        this.combinedCost = combinedCost;
    }

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

package state;

import java.util.Comparator;

public class HeuristicsNode extends WeightedNode{

    private double combinedCost;

    public HeuristicsNode(String state, double cost, WeightedNode parentNode, double combinedCost) {
        super(state, cost, parentNode);
        this.combinedCost = combinedCost;
    }

    public static final Comparator<HeuristicsNode> compareByCombinedCost = (hs1, hs2) -> {
        int result = Double.compare(hs1.getCombinedCost(), hs2.getCombinedCost());
        if(result == 0)
            return HeuristicsNode.compareByState.compare(hs1, hs2);
        return result;
    };

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

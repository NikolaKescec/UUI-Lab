package state;

import java.util.Comparator;
import java.util.Objects;

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
    public static final Comparator<HeuristicsNode> compareByCombinedCost = Comparator.comparingDouble(HeuristicsNode::getCombinedCost);

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HeuristicsNode that = (HeuristicsNode) o;
        return Double.compare(that.combinedCost, combinedCost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), combinedCost);
    }
}

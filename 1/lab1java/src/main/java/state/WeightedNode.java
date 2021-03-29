package state;

import java.util.Comparator;
import java.util.Objects;

/**
 * Weighted node contains a state name, total cost of this state and its parent which is also parent node.
 */
public class WeightedNode{

    private String state;
    private double cost;
    private WeightedNode parentNode;

    public WeightedNode(String state, double cost, WeightedNode parentNode) {
        this.state = state;
        this.cost = cost;
        this.parentNode = parentNode;
    }

    /**
     * Compares by state.
     */
    public static final Comparator<WeightedNode> compareByState = Comparator.comparing(WeightedNode::getState);

    /**
     * Compares by cost then state.
     */
    public static final Comparator<WeightedNode> compareByCost = Comparator.comparing(WeightedNode::getCost).thenComparing(compareByState);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedNode that = (WeightedNode) o;
        return cost == that.cost && Objects.equals(state, that.state) && Objects.equals(parentNode, that.parentNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, cost, parentNode);
    }

    /**
     * Prints path from parent recursively.
     * @return
     */
    public String printPathFromParent() {
        StringBuilder pathBuilder = new StringBuilder();
        recursivePath(this, pathBuilder);
        return pathBuilder.toString();
    }

    /**
     * Builds parent path string recursively.
     * @param node
     * @param builder
     */
    private void recursivePath(WeightedNode node, StringBuilder builder) {
        if(node.parentNode == null) {
            builder.append(node.getState());
            return;
        }
        recursivePath(node.getParentNode(), builder);
        builder.append(" => ").append(node.getState());
    }

    /**
     * Returns the depth of this child, counting from the root.
     * @param node
     * @return
     */
    public static int depth(WeightedNode node) {
        if(node.parentNode == null) {
            return 1;
        }
        return depth(node.getParentNode()) + 1;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public WeightedNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(WeightedNode parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public String toString() {
        return "WeightedNode{" +
                "state='" + state + '\'' +
                ", cost=" + cost +
                '}';
    }
}

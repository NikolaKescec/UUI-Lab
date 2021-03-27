package state;

import java.util.Comparator;
import java.util.Objects;

public class WeightedNode{

    private String state;
    private double cost;
    private WeightedNode parentNode;

    public WeightedNode(String state, double cost, WeightedNode parentNode) {
        this.state = state;
        this.cost = cost;
        this.parentNode = parentNode;
    }

    public static final Comparator<WeightedNode> compareByState = Comparator.comparing(WeightedNode::getState);

    public static final Comparator<WeightedNode> compareByCost = (ws1, ws2) -> {
        int result = Double.compare(ws1.getCost(), ws2.getCost());
        if(result == 0) {
            return compareByState.compare(ws1, ws2);
        }
        return result;
    };

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

    public String printPathFromParent() {
        StringBuilder pathBuilder = new StringBuilder();
        recursivePath(this, pathBuilder);
        return pathBuilder.toString();
    }

    private void recursivePath(WeightedNode node, StringBuilder builder) {
        if(node.parentNode == null) {
            builder.append(node.getState());
            return;
        }
        builder.append(" => ").append(node.getState());
        recursivePath(node.getParentNode(), builder);
    }

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
}

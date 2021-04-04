package successor;

import java.util.Comparator;

/**
 * Successor state models every successor state from some state. Contains successor state name and cost to it.
 */
public class SuccState {
    private String state;
    private double cost;

    public SuccState(String state, double cost) {
        this.state = state;
        this.cost = cost;
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

    @Override
    public String toString() {
        return "SuccState{" +
                "state='" + state + '\'' +
                ", cost=" + cost +
                '}';
    }

    /**
     * Comparator that compares by state name.
     */
    public static final Comparator<SuccState> byState = Comparator.comparing(SuccState::getState);
}

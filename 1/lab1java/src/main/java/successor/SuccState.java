package successor;

import java.util.Comparator;

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

    public static final Comparator<SuccState> byState = Comparator.comparing(SuccState::getState);
}

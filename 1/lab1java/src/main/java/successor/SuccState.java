package successor;

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
}

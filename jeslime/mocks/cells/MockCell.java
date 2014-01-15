package cells;

/**
 * Created by dbborens on 1/13/14.
 */
public class MockCell extends Cell {

    private int considerCount;
    private Cell child;
    private int state;
    private double fitness;
    private double production;

    @Override
    public int consider() {
        return considerCount;
    }

    @Override
    public void apply() {

    }

    @Override
    public Cell divide() {
        return child;
    }

    @Override
    public Cell clone(int state) {
        return child;
    }

    @Override
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setProduction(double production) {
        this.production = production;
    }

    public void setChild(Cell child) {
        this.child = child;
    }

    public void setConsiderCount(int considerCount) {
        this.considerCount = considerCount;
    }

    @Override
    public double getProduction(String solute) {
        return production;
    }

    @Override
    public void feed(double delta) {

    }
}

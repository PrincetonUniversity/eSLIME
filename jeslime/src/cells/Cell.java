package cells;

public abstract class Cell {

	protected int state;
	protected double fitness;
	protected boolean divisible;
	
	public int getState() {
		return state;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public boolean isDivisible() {
		return divisible;
	}
	
	public abstract int consider();
	
	public abstract void apply();
	
	public abstract Cell divide();
}

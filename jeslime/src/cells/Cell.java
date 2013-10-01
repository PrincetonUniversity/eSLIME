package cells;

public abstract class Cell {
	private int state;
	private double fitness;
	private boolean divisible;
	
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

	/**
	 * Informs the cell that it has been given a direct benefit.
	 * The effect of this benefit depends on the cell class. 
	 */
	public abstract void feed(double delta);
	
	protected void setState(int state) {
		if (state == 0) {
			throw new IllegalStateException("Attempted to assign special 'dead' state code 0 to an active cell.");
		}
		
		this.state = state;
	}

	protected void setFitness(double fitness) {
		this.fitness = fitness;
	}

	protected void setDivisible(boolean divisible) {
		this.divisible = divisible;
	}
}

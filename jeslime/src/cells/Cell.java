package cells;

/**
 * Parent class for cells.
 * 
 * @tested (see subclass tests)
 * @author David Bruce Borenstein
 *
 */
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
	
	/**
	 * Applies changes to the cell. DO NOT CALL THIS METHOD DIRECTLY
	 * FROM A PROCESS. Instead, use lattice.apply().
	 */
	public abstract void apply();
	
	/**
	 * Instructs the cell to divide and returns the daughter cell.
	 * This triggers any division-related behaviors.
	 * 
	 * DO NOT CALL THIS METHOD DIRECTLY
	 * FROM A PROCESS. Instead, use lattice.divide().
	 * @return
	 */
	public abstract Cell divide();

	/**
	 * Creates an exact replica of the cell, differing only by the
	 * state value. Copies all internal state variables. Does not
	 * trigger any division-related events or changes.
	 * 
	 * @param state
	 * @return
	 */
	public abstract Cell clone(int state);

    /**
     * Returns the current production of the specified solute.
     *
     * @param solute the ID of the solute layer associated with
     *               the solute whose production is to be checked.
     * @return
     */
    public abstract double getProduction(String solute);

    @Override
	public Cell clone() {
		return clone(state);
	}
	
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

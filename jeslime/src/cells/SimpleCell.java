package cells;

/**
 * class SimpleCell
 *
 * As its name suggests, a simple cell is the minimal implementation of the
 * cell class. It does not interact with any solute field. The cell state field can be used
 * to differentiate between genotypes.
 * 
 * @author David Bruce Borenstein
 * @test SimpleCellTest
 */
public class SimpleCell extends Cell {

	private int considerCount;
	
	public SimpleCell(int state) {

		setState(state);
		setFitness(0D);
		setDivisible(true);
		
		considerCount = 0;
	}
	
	@Override
	public int consider() {
		considerCount++;
		return considerCount;
	}
	
	public void apply() {
		considerCount = 0;
	}
	
	public SimpleCell divide() {
		// Make a copy of this SimpleCell
		SimpleCell child = new SimpleCell(getState());
		
		// Return it
		return child;
	}
	
	@Override
	public void feed(double delta) {
		throw new UnsupportedOperationException("SimpleCell does not use a nutrient metric.");
	}

	@Override
	public SimpleCell clone(int childState) {
		SimpleCell child = new SimpleCell(childState);
		child.considerCount = considerCount;
		return child;
	}
}

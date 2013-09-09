/**
 * class SimpleCell
 *
 * As its name suggests, a simple cell is the minimal implementation of the
 * cell class. It does not have an associated fitness value and
 * does not interact with any solute field. The cell state field can be used
 * to differentiate between genotypes.
 */

package cells;
public class SimpleCell extends Cell {

	private int considerCount;
	
	public SimpleCell(int state) {
		this.state = state;
		fitness = 0D;
		divisible = true;
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
		SimpleCell child = new SimpleCell(state);
		
		// Return it
		return child;
	}
	
}

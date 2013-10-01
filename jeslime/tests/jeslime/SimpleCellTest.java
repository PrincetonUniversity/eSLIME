package jeslime;
import cells.Cell;
import cells.SimpleCell;
import junit.framework.TestCase;

/**
 * Tests the functionality of the SimpleCell class.
 */
public class SimpleCellTest extends TestCase {
	private int state = 1;
	
	public void testConstruct() {
		Cell query = new SimpleCell(state);
		assertNotNull(query);
		assertEquals(state, query.getState());
		
		// TODO This should become machine epsilon after adding parameters
		assertEquals(0D, query.getFitness(), 1E-10);
	}
	
	public void testConsiderAndApply() {
		Cell query = new SimpleCell(state);
		
		int result = query.consider();
		assertEquals(1, result);
		
		result = query.consider();
		assertEquals(2, result);
		
		query.apply();
		
		result = query.consider();
		
		assertEquals(1, result);
	}
	
	public void testDivide() {
		Cell parent = new SimpleCell(state);
		Cell child  = parent.divide();
		
		assertEquals(state, child.getState());
	}
	
	public void testFeedThrowsException() {
		Cell c = new SimpleCell(state);
		try {
			c.feed(0.01D);
		} catch (UnsupportedOperationException e) {
			return;
		}
		
		fail();
	}
}

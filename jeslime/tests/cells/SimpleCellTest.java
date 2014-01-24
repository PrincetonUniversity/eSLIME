package cells;
import structural.identifiers.Coordinate;
import test.EslimeTestCase;

/**
 * Tests the functionality of the SimpleCell class.
 */
public class SimpleCellTest extends EslimeTestCase {
	private int state = 1;
	
	public void testConstruct() {
		Cell query = new SimpleCell(state);
		assertNotNull(query);
		assertEquals(state, query.getState());
		
		// TODO This should become machine epsilon after adding parameters
		assertEquals(0D, query.getFitness(), epsilon);
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

    public void testTriggerThrowsException() {
        boolean thrown = false;

        try {
            Cell query = new SimpleCell(state);
            query.trigger("a", new Coordinate(0, 0, 0));
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

}

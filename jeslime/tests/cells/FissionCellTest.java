package cells;

import junit.framework.TestCase;
import test.EslimeTestCase;

public class FissionCellTest extends CellTest {

	private double epsilon = calcEpsilon();
	
	private double calcEpsilon() {
        double machEps = 1.0d;
        
        do {
           machEps /= 2d;
        } while (1d + (machEps/2d) != 1d);
        
        return machEps;
	}
	
	public void testConstruct() {
		// Cell with biomass > threshold --> should be immediately divisible
		Cell query = new FissionCell(1, 1.0, 0.5);
		assertTrue(query.isDivisible());
		assertEquals(1, query.getState());

		// Cell with biomass < threshold --> not divisible
		query = new FissionCell(2, 0.5, 1.0);
		assertEquals(query.getFitness(), 0.5, epsilon);
		
		assertTrue(!query.isDivisible());
		assertEquals(2, query.getState());
	}
	
	public void testFeedConsiderApply() {
	
		Cell query = new FissionCell(1, 0.5, 1.0);
		assertEquals(0.5, query.getFitness(), epsilon);
		
		// Feed, but don't apply -- nothing should happen, except the
		// ConsiderCount goes up.
		query.feed(0.75);
		
		// consider() doesn't do anything for FissionCell
		int result = query.consider();
		
		assertEquals(1, result);
		assertEquals(0.5, query.getFitness(), epsilon);
		assertTrue(!query.isDivisible());
		
		result = query.consider();
		assertEquals(2, result);
		
		// After applying, changes should take effect.
		query.apply();
		assertEquals(1.25, query.getFitness(), epsilon);
		assertTrue(query.isDivisible());
		
		// Now feed a negative delta (i.e., a stressor) until it's too weak
		query.feed(-0.5);
		query.apply();
		assertEquals(0.75, query.getFitness(), epsilon);
		assertTrue(!query.isDivisible());
		
		result = query.consider();
		
		assertEquals(1, result);
	}
	
	public void testDivide() {
		// Start with a cell whose fitness is 3 times its division threshold
		Cell parent = new FissionCell(1, 3.0, 1.0);

		// Divide once -- each daughter should be divisible
		Cell daughter = parent.divide();
		
		assertEquals(parent.getState(), 1);
		assertEquals(daughter.getState(), 1);
		
		assertEquals(parent.getFitness(), 1.5, epsilon);
		assertEquals(daughter.getFitness(), 1.5, epsilon);
		
		assertTrue(parent.isDivisible());
		assertTrue(daughter.isDivisible());
		
		// Divide the daughter a second time -- neither cell involved should
		// be divisible
		Cell granddaughter = daughter.divide();
		
		assertEquals(parent.getState(), 1);
		assertEquals(daughter.getState(), 1);
		assertEquals(granddaughter.getState(), 1);
		
		assertEquals(daughter.getFitness(), 0.75, epsilon);
		assertEquals(granddaughter.getFitness(), 0.75, epsilon);
		assertEquals(parent.getFitness(), 1.5, epsilon);
		
		assertTrue(parent.isDivisible());
		assertTrue(!daughter.isDivisible());
		assertTrue(!granddaughter.isDivisible());
		
	}
}

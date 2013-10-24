package jeslime.processes;

import processes.gillespie.GillespieChooser;
import processes.gillespie.GillespieState;
import junit.framework.TestCase;

public class GillespieTest extends TestCase {

	/**
	 * Test the GillespieState object.
	 */
	public void testGillespieStateLifeCycle() {
		Integer[] arr = new Integer[] {100, 200};
		GillespieState gs = new GillespieState(arr);
		
		// Setters should work right now
		gs.add(100, 5, 0.7);
		gs.add(200, 3, 0.4);

		// Close it so we can use getters
		gs.close();

		assertEquals(gs.getTotalWeight(), 1.1, 1e-10);
		assertEquals(2, gs.getKeys().length);
		assertEquals(5, gs.getEventCount(100));
		assertEquals(3, gs.getEventCount(200));
		assertEquals(0.7, gs.getWeight(100), 1e-10);
		assertEquals(0.4, gs.getWeight(200), 1e-10);
	}
	
	/**
	 * Test the binary range search on the GillespieChooser.
	 */
	public void testBinaryRangeSearch() {
		
		// Failure case
		assertEquals(-1, doSearch(-0.5));

		// Standard cases
		assertEquals(0, doSearch(0.5));
		assertEquals(1, doSearch(1.5));
		assertEquals(2, doSearch(2.5));

		// Edge case -- lower is inclusive
		assertEquals(0, doSearch(0.0));
		
		// Edge case -- upper is exclusive
		assertEquals(-1, doSearch(5.0));

	}

	/**
	 * Test that the weighted target selector works.
	 */
	public void testSelectTarget() {
		Integer[] arr = new Integer[] {100, 200, 300};
		GillespieState gs = new GillespieState(arr);
		
		gs.add(100, 5, 2.0);
		gs.add(200, 3, 1.0);
		gs.add(300, 3, 7.0);
		
		gs.close();
		GillespieChooser gc = new GillespieChooser(gs);
		assertEquals(100, (int) gc.selectTarget(0.0));
		assertEquals(100, (int) gc.selectTarget(0.5));
		assertEquals(200, (int) gc.selectTarget(2.0));
		assertEquals(200, (int) gc.selectTarget(2.5));
		assertEquals(300, (int) gc.selectTarget(3.0));
		assertEquals(300, (int) gc.selectTarget(5.0));
		assertEquals(300, (int) gc.selectTarget(7.0));
		assertEquals(300, (int) gc.selectTarget(9.9));
	}
	
	
	private int doSearch(double x) {
		// These are irrelevant -- just for mock
		Integer[] arr = new Integer[0];
		GillespieState gs = new GillespieState(arr);
		
		// Set up the chooser
		GillespieChooser chooser = new GillespieChooser(gs);
		double[] bins = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
		
		int result = chooser.binaryRangeSearch(0, 4, x, bins);
		
		return result;
	}
}

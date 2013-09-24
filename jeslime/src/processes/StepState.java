package processes;

import java.util.HashSet;
import java.util.Set;

import structural.identifiers.Coordinate;

/**
 * State object for a cycle in a simulation. Each
 * process can modify this state object as necessary.
 * 
 * When the processes are done, information from this
 * state object is passed to appropriate downstream
 * elements.
 * 
 * @author dbborens
 *
 */
public class StepState {

	private Set<Coordinate> highlights = new HashSet<Coordinate>();
	private double dt = 0D;
	private boolean closed = false;
	
	public void highlight(Coordinate c) {
		if (closed)
			throw new IllegalStateException("Consistency failure in StepState object");
		
		highlights.add(c);
	}
	
	public void advanceClock(double time) {
		if (closed)
			throw new IllegalStateException("Consistency failure in StepState object");
		
		dt += time;
	}
	
	public void close() {
		closed = true;
	}
	
	public double getDt() {
		if (!closed) {
			throw new IllegalStateException("Attempted to access state before it was finalized.");
		}
		
		return dt;
	}
	
	public Coordinate[] getHighlights() {
		if (!closed) {
			throw new IllegalStateException("Attempted to access state before it was finalized.");
		}
		
		return highlights.toArray(new Coordinate[0]);
	}
}

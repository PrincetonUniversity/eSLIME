package structural.halt;


public class FixationEvent extends HaltCondition {

	private int fixationState;
	
	public FixationEvent(int fixationState, double gillespie) {
		super(gillespie);
		this.fixationState = fixationState;
	}
	
	public int getFixationState() {
		return fixationState;
	}
}

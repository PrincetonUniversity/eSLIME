package structural.halt;


public class FixationEvent extends HaltCondition {

	private int fixationState;
	
	public FixationEvent(double time, int fixationState) {
		super(time);
		this.fixationState = fixationState;
	}
	
	public int getFixationState() {
		return fixationState;
	}
}

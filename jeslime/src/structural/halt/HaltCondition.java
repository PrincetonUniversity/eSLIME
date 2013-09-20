package structural.halt;

public class HaltCondition extends Exception {

	private double gillespie;
	
	public HaltCondition(double gillespie) {
		this.gillespie = gillespie;
	}
	
	public double getGillespie() {
		return gillespie;
	}
}

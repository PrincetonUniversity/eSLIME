package control;

/**
 * Currently a hard-coded parameters class. Once the program works,
 * I will implement a more involved parameter framework.
 * 
 * @author dbborens
 *
 */
public class Parameters {

	private double epsilon;
	
	public Parameters() {
		epsilon  = calcEpsilon();
	}
	
	/**
	 * Find the machine epsilon for this computer (i.e., the value at which
	 * double-precision floating points can no longer be distinguished.)
	 * 
	 * Adapted from the Wikipedia article "Machine epsilon" (retrieved 3/18/2012)
	 * 
	 */
	private double calcEpsilon() {
        double machEps = 1.0d;
        
        do {
           machEps /= 2d;
        } while (1d + (machEps/2d) != 1d);
        
        return machEps;
	}
	
	/**
	 * Determines whether two doubles are equal to within machine epsilon.
	 * 
	 * @param p
	 * @param q
	 */
	public boolean epsilonEquals(double p, double q) {
		if (Math.abs(p - q) < epsilon)
			return true;
		
		return false;
	}
	
	public double epsilon() {
		return epsilon;
	}
	
	// System width
	public int W() {
		return 32;
	}
	
	// Output mode
	public String getOutput() {
		return "FULL";
	}
	
	public String getPath() {
		return "/Users/dbborens/state/jeSLIME/2013-09-11/";
	}
	
	public boolean isStamp() {
		return false;
	}
	
	public String getVersionIdentifier() {
		return "v0.0.1";
	}
	
	public String getLatticeShape() {
		return "HexRing";
	}
	
}

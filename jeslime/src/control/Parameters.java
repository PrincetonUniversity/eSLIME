package control;

/**
 * Currently a hard-coded parameters class. Once the program works,
 * I will implement a more involved parameter framework.
 * 
 * @author dbborens
 *
 */
public class Parameters {

	// System width
	public int W() {
		return 32;
	}
	
	// Output mode
	public String getOutput() {
		return "FULL";
	}
	
	public String getPath() {
		return "/Users/dbborens/state/jeSLIME/2013-09-09/";
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

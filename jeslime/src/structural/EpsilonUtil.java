package structural;

public abstract class EpsilonUtil {

	private final static double epsilon = calcEpsilon();
			
	private static double calcEpsilon() {
		double eps = 1.0D;
		
		while ((1.0 + (eps / 2.0)) != 1.0) {
			eps /= 2.0;
		}
		
		return eps;
	}
	
	public static double epsilon() {
		return epsilon;
	}
}

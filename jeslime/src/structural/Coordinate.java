package structural;


public class Coordinate {
	private int x, y, z;
	private int flags;
	
	public Coordinate(int x, int y, int flags) {
		this.x = x;
		this.y = y;
		
		z = 0;
		
		this.flags = flags | Flags.PLANAR;
	}
	
	public Coordinate (int x, int y, int z, int flags) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.flags = flags;	
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int z() {
		return z;
	}
	
	public int flags() {
		return flags;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + flags;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (flags != other.flags)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return stringForm();
	}
	
	public String stringForm() {
		return canonical("(", ")", true);
	}
	
	public String vectorForm() {
		return canonical("<", ">", false);
	}
	
	private String canonical(String open, String close, boolean useFlags) {
		StringBuilder ss = new StringBuilder();
		
		ss.append(open);
		ss.append(x);
		ss.append(", ");
		ss.append(y);
		
		if ((flags & Flags.PLANAR) == 0) {
			ss.append(", ");
			ss.append(z);
		}

		if (useFlags) {
			ss.append(" | ");
			ss.append(flags);
		}
		
		ss.append(close);
		
		String s = ss.toString();
		
		return s;
	}
	
	public boolean hasFlag(int flag) {
		return ((flags & flag) != 0);
	}
	
	
}

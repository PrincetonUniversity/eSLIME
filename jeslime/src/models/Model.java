package models;

public abstract class Model {

	protected static final int MAX_STEP = 100;
	
	public abstract void initialize();
	public abstract void go();
}

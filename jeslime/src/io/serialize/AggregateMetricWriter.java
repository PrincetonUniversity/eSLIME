package io.serialize;

public abstract class AggregateMetricWriter {

	public abstract void init();
	
	public abstract void push();
	
	public abstract void close();

}

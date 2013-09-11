package models;

import geometries.Geometry;

public abstract class Model {
	
	public abstract void initialize();
	public abstract void go();
	public abstract Geometry getGeometry();
	
}

package io.serialize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import geometries.Geometry;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

public class ParameterWriter extends Writer {
	private final String PROJECT_FILENAME = "project.xml";

	public ParameterWriter(GeneralParameters p, Geometry geometry) {
		super(p, geometry);
		mkDir(p.getPath(), true);
		
		try {
			String paramsFileStr = p.getPath() + '/' + PROJECT_FILENAME;			
			File paramsFile = new File(paramsFileStr);
			FileWriter fw = new FileWriter(paramsFile);
			BufferedWriter bwp = new BufferedWriter(fw);
			System.out.println("p xml is null? " + (p.getProjectXML() == null));
			
			bwp.write(p.getProjectXML());
			bwp.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void init(Lattice l) {
		// Write out an instance-specific XML file.
		if (p.getNumInstances() == 1) {
			return;
		}
		mkDir(p.getInstancePath(), true);

		try {
			String paramsFileStr = p.getInstancePath() + '/' + PROJECT_FILENAME;			
			File paramsFile = new File(paramsFileStr);
			FileWriter fw = new FileWriter(paramsFile);
			BufferedWriter bwp = new BufferedWriter(fw);
			bwp.write(p.getInstanceXML());
			bwp.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		// Doesn't do anything.

	}

	@Override
	public void dispatchHalt(HaltCondition ex) {
		// Doesn't do anything.

	}

	@Override
	public void close() {
		// Doesn't do anything.

	}

}

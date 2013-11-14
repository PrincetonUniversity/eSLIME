package io.serialize;

import geometry.Geometry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import no.uib.cipr.matrix.Vector;
import cells.Cell;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.identifiers.Extrema;
import structural.identifiers.NonZeroExtrema;

/**
 * 
 * Copyright (c) 2013, David Bruce Borenstein.
 * 
 * This work is licensed under the Creative Commons 2.0 BY-NC license.
 * 
 * Attribute (BY) -- You must attribute the work in the manner specified 
 * by the author or licensor (but not in any way that suggests that they 
 * endorse you or your use of the work).
 * 
 * Noncommercial (NC) -- You may not use this work for commercial purposes.
 * 
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc/3.0/legalcode
 * 
 * Writes the state of the system to a file. To avoid lots of opening and
 * closing of files, as well as lots of panning around the disk, a single
 * file is used for all state.
 * 
 * Since disk is much slower than compute, in the future I might move this
 * to a separate thread, with a queue of data to be appended. However, since
 * that is a somewhat delicate piece of engineering, I will defer that until
 * I see how much optimizing is needed.
 * 
 * In the meantime, this uses the default Java BufferedWriter, which will
 * block the thread every time it needs to write to disk.
 * 
 * @author dbborens@princeton.edu
 *
 */
public class BufferedStateWriter extends Writer {

	private boolean closed = true;
	
	private long prevTime;
	
	private static final double log10 = Math.log(10D);

	private double prevGillespie;
	
	// This file contains state vectors, with vector indices corresponding
	// to coordinates. The mapping between index value and coordinate is
	// specified in coordmap.txt.
	private final String STATE_FILENAME = "data.txt";
	
	private final String METADATA_FILENAME = "metadata.txt";
	
	// This file specifies the relationship between vector index and coordinate.
	private final String COORDMAP_FILENAME = "coordmap.txt";
	
	// Dictates how many data to write per line. 
	private final int DATA_PER_LINE = 20;
	
	// I/O handle for the state file
	private BufferedWriter stateWriter;
	
	
	// Timestamp for project
	private Date date = new Date();
	
	// Extrema for each field type
	private Extrema ef;				// Fitness

	// Index --> Coordinate
	private Coordinate[] coordArr;

	// Coordinate --> Index
	private HashMap<Coordinate, Integer> coordMap;
	
	//public BufferedStateWriter(String stateDir, Parameters p, int n) {
	public BufferedStateWriter(GeneralParameters p, Geometry geom) {
		super(p, geom);
	}

	public void init(CellLayer l) {
		if (!closed) {
			throw new IllegalStateException("Attempting to initialize active writer!");
		}
		
		layer = l;
		
		initStructures(p, geometry);
		
		makeFiles(p);

		initFiles(p);
		
		prevTime = System.currentTimeMillis();
		prevGillespie = 0;
	}
	
	private void initFiles(GeneralParameters p) {
		// Create the state & interval files
		String stateFileStr = p.getInstancePath() + '/' + STATE_FILENAME;
		
		try {
			
			File stateFile = new File(stateFileStr);
			FileWriter fw = new FileWriter(stateFile);
			stateWriter = new BufferedWriter(fw, 1048576);

			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
			
		// Make the coordinate map
		makeCoordinateMap();
	}

	protected void makeCoordinateMap() {
		try {
			String coordMapFileStr = p.getInstancePath() + '/' + COORDMAP_FILENAME;
			File coordMapFile = new File(coordMapFileStr);
			FileWriter fw = new FileWriter(coordMapFile);
			BufferedWriter bwp = new BufferedWriter(fw);
			for (int i = 0; i < coordArr.length; i++) {
				StringBuilder sb = new StringBuilder();
				sb.append(i);
				sb.append("\t");
				sb.append(coordArr[i].toString());
				sb.append("\n");
				bwp.append(sb.toString());
			}
			bwp.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void initStructures(GeneralParameters p, Geometry geom) {
		// Initialize extrema
		ef = new Extrema();
		
		// Initialize coordinate structures
		coordArr = geom.getCanonicalSites();
		coordMap = new HashMap<Coordinate, Integer>(coordArr.length);
		
		for (int i = 0; i < coordArr.length; i++)
			coordMap.put(coordArr[i], i);
	}

	private void makeFiles(GeneralParameters p) {
		// Create the directory for state files, if needed
		mkDir(p.getPath(), true);
		
		mkDir(p.getInstancePath(), true);
		
		System.out.println(p.getInstancePath());
	}

	public String getSimPath() {
		return p.getInstancePath();
	}
	
	private String date() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH'h'mm'm'ss's'");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	

	
	/**
	 * Appends a state to the file.
	 * 
	 * @param s Cell state array, in canonical order.
	 * @param f Cell fitness array, in canonical order.
	 * @param gillespie Interval (in simulated time) between the last time step and the current one.
	 */
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		int[] s = layer.getViewer().getStateVector();
		double[] f = layer.getViewer().getFitnessVector();
		
		if (p.isFrame(frame)) {
			writeDoubleArray(f, ef, gillespie, frame, "fitness");
			writeIntegerArray(s, gillespie, frame, "state");
			
			int[] h = coordToInt(highlights);
			writeIntegerArray(h, gillespie, frame, "highlight");
		}
		
		prevGillespie = gillespie;
	}

	protected int[] coordToInt(Coordinate[] highlights) {
		int[] hl = new int[highlights.length];
		for (int i = 0; i < highlights.length; i++) {
			hl[i] = coordMap.get(highlights[i]);
		}
		
		return hl;
	}	
	
	private static int oom(double x) {
		return oom(Math.round(x));
	}
	
	private static int oom(long x) {
		Double log = Math.log(x * 1D) / log10;
		Double floor = Math.floor(log);
		Double oom = Math.pow(10, floor);		

		// We don't want "ceiling" because log(1) = 0
		return oom.intValue();
	}
	


	/**
	 * Write out the cell types, represented by colors. 
	 * 
	 * The format for cell color is:
	 *  
	 * >color:0
	 * 0123456701234567....
	 * 7654321076543210....
	 * >color:1
	 * ...
	 * 
	 * Recall that base colors are encoded in binary:
	 *   Red   += 4
	 *   Green += 2
	 *   Blue  += 1
	 *   
	 * Hence, our visualization is limited to 8 cell
	 * types, including null cells (which are black).
	 * 
	 * @param lattice
	 */
	private void writeIntegerArray(int[] v, double gillespie, int frame, String name) {
		StringBuilder sb = new StringBuilder(">" + name + ":");
		sb.append(frame);
		sb.append(':');
		sb.append(gillespie);
		sb.append('\n');

		for (int i = 0; i < v.length; i++) {
			sb.append(v[i]);
			if (i % DATA_PER_LINE == DATA_PER_LINE - 1)
				sb.append('\n');
			else if (i == v.length - 1)
				sb.append('\n');
			else
				sb.append('\t');
		}

		try {
			stateWriter.write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Writes a vector to the file. The format is as follows:
	 * 
	 * The format is as follows:
	 * 
	 *  >first_field:0
	 *  0.012345	0.012345	0.012345....
	 *  0.012345	0.012345	0.012345....
	 *  >second_field:0
	 *  0.012345	0.012345	0.012345....
	 *  0.012345	0.012345	0.012345....
	 *  >first_field:1
	 *  ...
	 *  
	 *  The delimiters are tabs, and there are p.W() tokens per line.
	 */
	private void writeDoubleArray(double[] v, Extrema extrema, double gillespie, int frame, String title) {

		StringBuilder sb = new StringBuilder();;
		sb.append('>');
		sb.append(title);
		sb.append(':');
		sb.append(frame);
		sb.append(':');
		sb.append(gillespie);
		sb.append("\n");
		for (int i = 0; i < v.length; i++) {
			Double u = v[i];
			extrema.consider(u, coordArr[i], gillespie);
			sb.append(v[i]);
			if (i % DATA_PER_LINE == DATA_PER_LINE - 1)
				sb.append('\n');
			else if (i == v.length - 1)
				sb.append('\n');
			else
				sb.append('\t');
		}
		
		try {
			stateWriter.write(sb.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void conclude() {
		// Close the state data file.
		try {
			
			stateWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// Write the metadata file.
		try {
			File metadata = new File(p.getInstancePath() + '/' + METADATA_FILENAME);
			FileWriter mfw = new FileWriter(metadata);
			BufferedWriter mbw = new BufferedWriter(mfw);
			
			mbw.write("fitness>");
			mbw.write(ef.toString());
			mbw.write('\n');

			mbw.close();
				
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		// Doesn't do anything.
	}
	
	public void dispatchHalt(HaltCondition ex) {
		conclude();
		closed = true;
	}

}

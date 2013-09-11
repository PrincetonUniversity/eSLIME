package io;

import geometries.Geometry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import no.uib.cipr.matrix.Vector;

import control.Parameters;

import cells.Cell;
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
public class BufferedStateWriter {

	private long prevTime;
	
	private static final double log10 = Math.log(10D);

	private double prevGillespie = 0;
	
	// This file contains state vectors, with vector indices corresponding
	// to coordinates. The mapping between index value and coordinate is
	// specified in coordmap.txt.
	private final String STATE_FILENAME = "data.txt";
	
	// This file is a parsable parameters file, which can be used for subsequent
	// simulations as well as to identify the experimental condition used.
	private final String PARAMS_FILENAME = "params.txt";
	
	
	private final String METADATA_FILENAME = "metadata.txt";
	private final String INTERVAL_FILENAME = "interval.txt";
	
	// This file specifies the relationship between vector index and coordinate.
	private final String COORDMAP_FILENAME = "coordmap.txt";
	// Time step
	int n;
	
	// Parameters
	private Parameters p;
	
	// I/O handle for the state file
	private BufferedWriter stateWriter;
	
	// I/O handle for the interval file (What changed at each time step, and how long it took)
	private BufferedWriter intervalWriter;
	
	// Directory path to the state file
	private String simPath;
	
	// Timestamp for project
	private Date date = new Date();
	
	// Extrema for each field type
	private Extrema ef;				// Fitness

	// Index --> Coordinate
	private Coordinate[] coordArr;

	// Coordinate --> Index
	private HashMap<Coordinate, Integer> coordMap;
	
	//public BufferedStateWriter(String stateDir, Parameters p, int n) {
	public BufferedStateWriter(Parameters p, Geometry geom) {
		this.p = p;
		initStructures(p, geom);
		
		makeFiles(p);

		initFiles(p);
		
		prevTime = System.currentTimeMillis();
		
	}

	private void initFiles(Parameters p) {
		// Create the state & interval files
		String stateFileStr = simPath + '/' + STATE_FILENAME;
		String intervalFileStr = simPath + '/' + INTERVAL_FILENAME;
		
		try {
			
			if (p.getOutput().equalsIgnoreCase("FULL")) {
				File stateFile = new File(stateFileStr);
				FileWriter fw = new FileWriter(stateFile);
				stateWriter = new BufferedWriter(fw, 1048576);
			}
			
			File intervalFile = new File(intervalFileStr);
			FileWriter ifw = new FileWriter(intervalFile);
			intervalWriter = new BufferedWriter(ifw, 1048576);
			intervalWriter.append("Step,Gillespie,Running time\n");
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		try {
			String paramsFileStr = simPath + '/' + PARAMS_FILENAME;			
			File paramsFile = new File(paramsFileStr);
			FileWriter fw = new FileWriter(paramsFile);
			BufferedWriter bwp = new BufferedWriter(fw);
			bwp.write(p.toString());
			bwp.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		// Make the coordinate map
		makeCoordinateMap();
	}

	protected void makeCoordinateMap() {
		try {
			String coordMapFileStr = simPath + '/' + COORDMAP_FILENAME;
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

	private void initStructures(Parameters p, Geometry geom) {
		// Initialize extrema
		ef = new Extrema(p.W());
		
		// Initialize coordinate structures
		coordArr = geom.getCanonicalSites();
		coordMap = new HashMap<Coordinate, Integer>(coordArr.length);
		
		for (int i = 0; i < coordArr.length; i++)
			coordMap.put(coordArr[i], i);
	}

	private void makeFiles(Parameters p) {
		// Create the directory for state files, if needed
		mkDir(p.getPath(), true);
		
		if (p.isStamp()) {
			// Create a directory for the current time. Also, if it
			// does not exist, create a directory for the current date.
			//simPath = stateDir + date() + "/n=" + n;
			simPath = p.getPath() + date();
		} else {
			simPath = p.getPath();
		}
		
		mkDir(simPath, true);
		
		System.out.println(simPath);
	}

	public String getSimPath() {
		return simPath;
	}
	
	private String date() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH'h'mm'm'ss's'");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	private void mkDir(String pathStr, boolean recursive) {
		File path = new File(pathStr);
		if (!path.exists()) {
			try {
				if (recursive)
					path.mkdirs();
				else
					path.mkdir();
			} catch (Exception ex) {
				System.out.println("Could not create directory tree " + pathStr);
				throw new RuntimeException(ex);
			}			
		}
	}
	
	/**
	 * Appends a state to the file.
	 * 
	 * @param s Cell state array, in canonical order.
	 * @param f Cell fitness array, in canonical order.
	 * @param gillespie Interval (in simulated time) between the last time step and the current one.
	 */
	public void push(int[] s, double[] f, double gillespie) {
		long time = System.currentTimeMillis();
		long interval = time - prevTime;
		prevTime = time;
		
		if (p.getOutput().equalsIgnoreCase("FULL") && (prevGillespie == 0 || oom(gillespie) > oom(prevGillespie))) {
			System.out.println("Writing time step " + gillespie);
			writeVector(f, ef, gillespie, "fitness");
			writeState(s, gillespie);
		}
		
		interval(n, gillespie, time);
		prevGillespie = gillespie;
		n++;
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
	 * Wall clock time and simulation time for last time step.
	 * 
	 * @param simInterval
	 * @param realInterval
	 */
	private void interval(int n, double gillespie, long interval) {
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		sb.append(',');
		sb.append(gillespie);
		sb.append(',');
		sb.append(interval);
		sb.append('\n');
		try {
			intervalWriter.append(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
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
	private void writeState(int[] v, double gillespie) {
		StringBuilder sb = new StringBuilder(">state:");
		sb.append(gillespie);
		sb.append('\n');

		for (int i = 0; i < v.length; i++) {
			sb.append(v[i]);
			if (i % p.W() == p.W() - 1)
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
	private void writeVector(double[] v, Extrema extrema, double gillespie, String title) {

		StringBuilder sb = new StringBuilder();;
		sb.append('>');
		sb.append(title);
		sb.append(':');
		sb.append(gillespie);
		sb.append("\n");
		for (int i = 0; i < v.length; i++) {
			Double u = v[i];
			extrema.consider(u, coordArr[i], gillespie);
			sb.append(v[i]);
			if (i % p.W() == p.W() - 1)
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
	 * Finalizes the file. Writes a summary file.
	 */
	public void close() {
		// Close the state data file.
		System.out.println("Final Gillespie time: " + prevGillespie);
		try {
			
			if (p.getOutput().equalsIgnoreCase("FULL"))
				stateWriter.close();
			
			intervalWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if (p.getOutput().equalsIgnoreCase("FULL")) {
			// Write the metadata file.
			try {
				File metadata = new File(simPath + '/' + METADATA_FILENAME);
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
	}
}

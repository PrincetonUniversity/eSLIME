package io.deserialize;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import structural.identifiers.Coordinate;
import structural.identifiers.Extrema;
import structural.identifiers.TemporalCoordinate;
import structural.GeneralParameters;
import structural.VectorViewer;
import geometry.Geometry;

public class StateReader {
	
	private GeneralParameters p;
	private Geometry g;
	
	private static final String DATA_FILENAME = "data.txt";
	private static final String METADATA_FILENAME = "metadata.txt";
	private static final String COORDMAP_FILENAME = "coordmap.txt";
	
	private HashMap<Coordinate, Integer> coordMap;
	private Coordinate[] coordArr;
	
	private Extrema ef;
	private BufferedReader br;

	// The StateReader always reads one line past the current state, so
	// we hold onto that line for the next call to getNext().
	private String prevLine = null;
	
	private double gillespie;
	
	private int frame;
	
	// The simulation being visualized may be a single instance of a multi-
	// replicate project. The StateReader is currently agnostic to that fact,
	// so we just pass in a path.
	private String path;
	
	public StateReader(String path, GeneralParameters p, Geometry geom) {
		this.g = geom;
		this.p = p;
		this.path = path;
		
		File dataFile = new File(path + '/' + DATA_FILENAME);
		File metadataFile = new File(path + '/' + METADATA_FILENAME);
		
		try {
			// Read coordinate map
			loadCoordinates();

			// Read the metadata file to get the extrema
			extractMetadata(metadataFile);

			// Initialize read-through
			FileReader fr = new FileReader(dataFile);

			br = new BufferedReader(fr);

			prevLine = br.readLine().trim();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	
	public ConditionViewer next() {
		try {
			// If prevLine is null, we're at the end of the file
			if (prevLine == null)
				return null;
			
			if (prevLine.startsWith(">")) {
				String[] tokens = prevLine.split(":");
				frame = Integer.valueOf(tokens[1]);
				gillespie = Double.valueOf(tokens[2]);

				return readConditions();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return null;
	}
	
	private ConditionViewer readConditions() throws IOException {
		int fCurrent = frame;
		
		VectorViewer f = null;		// Fitness
		
		int[] states = new int[g.getCanonicalSites().length];
		HashSet<Coordinate> highlights = new HashSet<Coordinate>();
		while (prevLine != null) {
			// We come into this loop with a header line in prevLine
			String[] tokens = prevLine.split(">")[1].split(":");
			
			// When we reach the next time step, stop reading
			fCurrent = Integer.valueOf(tokens[1]);
			
			if (frame != fCurrent)
				break;
			
			if (tokens[0].equals("fitness"))
				f = readVector(ef);
			else if (tokens[0].equals("state"))
				states = readStates();
			else if (tokens[0].equals("highlight"))
				readHighlights(highlights);
			else
				throw new IOException("Unrecognized field " + tokens[0]);
		}
		
		return new ConditionViewer(f, states, highlights, frame, gillespie, coordMap);
	}
	
	private void readHighlights(HashSet<Coordinate> highlights) throws IOException {
		
		prevLine = br.readLine();
		
		while (prevLine != null && !(prevLine.startsWith(">"))) {
			
			String[] valueTokens = prevLine.trim().split("\t");
			
			for (int j = 0; j < valueTokens.length; j++) {
				int index = Integer.valueOf(valueTokens[j]);
				Coordinate coord = coordArr[index];
				highlights.add(coord);
			}
			
			prevLine = br.readLine();
		}		
	}

	public void close() {
		try {
			br.close();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void loadCoordinates() throws IOException {
		ArrayList<Coordinate> coordList = new ArrayList<Coordinate>();
		coordMap = new HashMap<Coordinate, Integer>();
		
		String fn = path + '/' + COORDMAP_FILENAME;
		FileReader mfr = new FileReader(fn);
		BufferedReader mbr = new BufferedReader(mfr);
		String next = mbr.readLine();
		
		while (next != null) {
			// Lines are in the form
			// 		1	(0, 1 | 1)
			// 		2	(0, 2 | 1)
			next = next.trim();
			String[] mapping = next.split("\t");
			int key = Integer.valueOf(mapping[0]);
			
			String token = mapping[1];
			Coordinate c = parseCoordinate(token);
			
			coordList.add(c);
			coordMap.put(c, key);
			next = mbr.readLine();

		}
		
		coordArr = coordList.toArray(new Coordinate[0]);
	}

	private Coordinate parseCoordinate(String token) {
		String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+)\\)");
		
		Coordinate c;
		Pattern pattern = Pattern.compile(pStr);
		Matcher matcher = pattern.matcher(token);
		matcher.find();
			
		int x = Integer.valueOf(matcher.group(1));
		int y = Integer.valueOf(matcher.group(2));
		int flags = Integer.valueOf(matcher.group(5));
			
		if (matcher.group(4) != null) {
			// (1, 2, 3 | 4)
			int z = Integer.valueOf(matcher.group(4));
			c = new Coordinate(x, y, z, flags);
		} else {
			// (1, 2 | 3)
			c = new Coordinate(x, y, flags);
		}
		
		return c;
	}
	
	private TemporalCoordinate parseTemporalCoordinate(String token) {
		String pStr = ("\\((\\d+), (\\d+)(, (\\d+))? \\| (\\d+) \\| (\\d+\\.\\d+)\\)");
		TemporalCoordinate c;
		Pattern pattern = Pattern.compile(pStr);
		Matcher matcher = pattern.matcher(token);
		matcher.find();
			
		int x = Integer.valueOf(matcher.group(1));
		int y = Integer.valueOf(matcher.group(2));
		int flags = Integer.valueOf(matcher.group(5));
		double t = Double.valueOf(matcher.group(6));
		
		if (matcher.group(4) != null) {
			// (1, 2, 3 | 4 | 5.0)
			int z = Integer.valueOf(matcher.group(4));
			c = new TemporalCoordinate(x, y, z, t, flags);
		} else {
			// (1, 2 | 3 | 4.0)
			c = new TemporalCoordinate(x, y, t, flags);
		}
		
		return c;
	}

	private void extractMetadata(File metadataFile) throws IOException {
		FileReader mfr = new FileReader(metadataFile);
		BufferedReader mbr = new BufferedReader(mfr);
		String next = mbr.readLine();

		/*if (p.getProduction() < p.epsilon())
			ec = null;
		else
			ec = new Extrema(p.W());*/
		
		ef = new Extrema();
		
		while (next != null) {
			next = next.trim();
			// So inelegant...this could be all done in one line with regex...oh well, I'm tired
			String[] mapping = next.split(">");
			String key = mapping[0];
			String value = mapping[1];
			
			if (key.equals("fitness"))
				loadExtrema(value, ef);
			else
				throw new IOException("Unrecognized metadata field " + key);
			
			next = mbr.readLine();
		}
	}
	
	private void loadExtrema(String tokenize, Extrema e) {
		String[] minMax = tokenize.split(":");
		String[] minArg = minMax[0].split("@");
		String[] maxArg = minMax[1].split("@");
		
		Double min = Double.valueOf(minArg[0]);
		Double max = Double.valueOf(maxArg[0]);
		
		String minToken = minArg[1];
		String maxToken = maxArg[1];

		TemporalCoordinate argMin = parseTemporalCoordinate(minToken);
		TemporalCoordinate argMax = parseTemporalCoordinate(maxToken);

		e.load(min, argMin, max, argMax);
		
	}
	
	private VectorViewer readVector(Extrema ex) throws IOException {
		Vector v = new DenseVector(g.getCanonicalSites().length);
		prevLine = br.readLine();
		
		// Line counter. Should reach p.H(). 
		int i = 0;
		
		// Iterate until we hit the end of the file or the start of a new data field
		while (prevLine != null && !(prevLine.startsWith(">"))) {
			String[] tokens = prevLine.trim().split("\t");

			// TODO: Replace this logic with something that goes until it gets an EOL character.
			for (int j = 0; j < tokens.length; j++) {
				double x = Double.valueOf(tokens[j]);
				v.set(i, x);
				i++;
			}
			prevLine = br.readLine();
		}

		return new VectorViewer(v, ex.min(), ex.max());
	}
	
	/**
	 * Skip ahead to the next field or end of file
	 */
	private int[] readStates() throws IOException {
		int[] states = new int[g.getCanonicalSites().length];
		
		prevLine = br.readLine();
		
		// Line counter. Should reach p.H().
		int i = 0;
		
		while (prevLine != null && !(prevLine.startsWith(">"))) {
			
			String[] valueTokens = prevLine.trim().split("\t");
			
			for (int j = 0; j < valueTokens.length; j++) {
				states[i] = Integer.valueOf(valueTokens[j]);
				i++;
			}
			
			prevLine = br.readLine();
		}
		
		return states;
	}
}

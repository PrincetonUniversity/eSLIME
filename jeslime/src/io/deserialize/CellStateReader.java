package io.deserialize;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.deserialize.ConditionViewer;
import io.deserialize.CoordinateDeindexer;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import structural.identifiers.Coordinate;
import structural.identifiers.Extrema;
import structural.identifiers.TemporalCoordinate;
import structural.GeneralParameters;
import structural.VectorViewer;
import geometry.Geometry;

// TODO This class desperately needs refactoring.
public class CellStateReader {
	
	private GeneralParameters p;
	private Geometry g;
	
	private static final String DATA_FILENAME = "data.txt";
    private static final String METADATA_FILENAME = "metadata.txt";
    
	private BufferedReader br;

	// The CellStateReader always reads one line past the current state, so
	// we hold onto that line for the next call to getNext().
	private String prevLine = null;
	
	private double gillespie;

    private CoordinateDeindexer deindexer;

	private int frame;
	
	// The simulation being visualized may be a single instance of a multi-
	// replicate project. The CellStateReader is currently agnostic to that fact,
	// so we just pass in a path.
	private String path;
	
	public CellStateReader(String path, GeneralParameters p, Geometry geom) {
		this.g = geom;
		this.p = p;
		this.path = path;
		
		File dataFile = new File(path + '/' + DATA_FILENAME);

		try {



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

        Extrema ef = getFitnessExtremes();
		int[] states = new int[g.getCanonicalSites().length];
		HashSet<Coordinate> highlights = new HashSet<Coordinate>();
		while (prevLine != null) {
			// We come into this loop with a header line in prevLine
			String[] tokens = prevLine.split(">")[1].split(":");
			
			// When we reach the next time step, stop reading
			fCurrent = Integer.valueOf(tokens[1]);
			
			if (frame != fCurrent)
				break;
			
			if (tokens[0].equals("fitness")) {
				f = readVector(ef);
            } else if (tokens[0].equals("state")) {
				states = readStates();
            } else if (tokens[0].equals("highlight"))
				readHighlights(highlights);
			else {
				throw new IOException("Unrecognized field " + tokens[0]);
            }
		}
		
		return new ConditionViewer(f, states, highlights, frame, gillespie, deindexer);
	}

    private Extrema getFitnessExtremes() {
        File metadataFile = new File(path + '/' + METADATA_FILENAME);
        ExtremaReader reader = new ExtremaReader(metadataFile);
        Extrema ret = reader.get("fitness");
        return ret;
    }

    private void readHighlights(HashSet<Coordinate> highlights) throws IOException {

		prevLine = br.readLine();
		
		while (prevLine != null && !(prevLine.startsWith(">"))) {
			
			String[] valueTokens = prevLine.trim().split("\t");
			
			for (int j = 0; j < valueTokens.length; j++) {
				int index = Integer.valueOf(valueTokens[j]);
				Coordinate coord = deindexer.getCoordinate(index);
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

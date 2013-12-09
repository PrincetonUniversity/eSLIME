package jeslime.io.serialize;

import jeslime.io.project.ProjectLoader;

import java.util.HashSet;
import java.util.Set;

import org.dom4j.Element;

import structural.GeneralParameters;
import layers.cell.CellLayer;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.postprocess.ImageSequence;
import geometry.Geometry;

public class SerializationManager {

	private Set<Writer> writers;
	private GeneralParameters p;
	private Geometry g;
	
	public SerializationManager(ProjectLoader l, GeneralParameters p, Geometry g) {
		this.p = p;
		this.g = g;
		

		writers = new HashSet<Writer>();
		
		Element we = l.getElement("writers");
		
		for (Object o : we.elements()) {
			Element e = (Element) o;
			
			String writerClass = e.getName();
			
			if (writerClass.equalsIgnoreCase("state-vector")) {
				BufferedStateWriter bsw = new BufferedStateWriter(p, g);
				writers.add(bsw);
			} else if (writerClass.equalsIgnoreCase("fixation-time")) {
				FixationTimeWriter ftw = new FixationTimeWriter(p, g);
				writers.add(ftw);
			} else if (writerClass.equalsIgnoreCase("parameter-writer")) {
				ParameterWriter pw = new ParameterWriter(p, g);
				writers.add(pw);
			} else if (writerClass.equalsIgnoreCase("progress-reporter")) {
				ProgressReporter pr = new ProgressReporter(p, g);
				writers.add(pr);
			} else if (writerClass.equalsIgnoreCase("frequency-writer")) {
				FrequencyWriter freq = new FrequencyWriter(p, g);
				writers.add(freq);
			} else if (writerClass.equalsIgnoreCase("interval-writer")) {
				IntervalWriter iw = new IntervalWriter(p, g);
				writers.add(iw);
			}
		}
	}
	
	/**
	 * Initialize all writers.
	 */
	public void init(CellLayer l) {
		for (Writer tw : writers) {
			tw.init(l);
		}
	}
	
	/**
	 * Advance current instance by one time step.
	 */
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		for (Writer tw : writers) {
			tw.step(highlights, gillespie, frame);
		}	
	}
	
	/**
	 * Opens handles / initializes data structures for a new instance.
	 * Blows up if these were left open from the previous instance.
	 */
	public void nextSimulation(CellLayer l) {
		for (Writer tw : writers) {
			tw.init(l);
		}	
	}
	
	/**
	 * Conclude the entire simulation project.
	 */
	public void close() {
		for (Writer tw : writers) {
			tw.close();
		}	
	}

	public void dispatchHalt(HaltCondition ex) {
		System.out.println("Simulation ended. Cause: " + ex.getClass().getSimpleName());

		for (Writer tw : writers) {
			tw.dispatchHalt(ex);
		}
		
		if (p.isLineageMap()) {
			ImageSequence imgSequence = new ImageSequence(p.getInstancePath(), g, p);
			imgSequence.generate();
		}		
	}
}

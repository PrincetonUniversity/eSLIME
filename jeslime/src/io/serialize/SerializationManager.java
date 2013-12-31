package io.serialize;

import io.project.GeometryManager;
import io.project.ProjectLoader;

import java.util.HashSet;
import java.util.Set;

import org.dom4j.Element;

import structural.GeneralParameters;
import layers.cell.CellLayer;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.postprocess.ImageSequence;
import geometry.Geometry;

/**
 *
 * @untested
 */
public class SerializationManager {

	private Set<AbstractCellWriter> cellLayerWriters;
	private GeneralParameters p;
	private GeometryManager gm;
	
	public SerializationManager(ProjectLoader l, GeneralParameters p, GeometryManager gm) {
		this.p = p;
		this.gm = gm;
		

		cellLayerWriters = new HashSet<AbstractCellWriter>();
		
		Element we = l.getElement("cellLayerWriters");
		
		for (Object o : we.elements()) {
			Element e = (Element) o;
			
			String writerClass = e.getName();
			
			if (writerClass.equalsIgnoreCase("cell-state-writer")) {
				CellStateWriter bsw = new CellStateWriter(p, gm);
				cellLayerWriters.add(bsw);
			} else if (writerClass.equalsIgnoreCase("fixation-time")) {
				FixationTimeWriter ftw = new FixationTimeWriter(p, gm);
				cellLayerWriters.add(ftw);
			} else if (writerClass.equalsIgnoreCase("parameter-writer")) {
				ParameterWriter pw = new ParameterWriter(p, gm);
				cellLayerWriters.add(pw);
			} else if (writerClass.equalsIgnoreCase("progress-reporter")) {
				ProgressReporter pr = new ProgressReporter(p, gm);
				cellLayerWriters.add(pr);
			} else if (writerClass.equalsIgnoreCase("frequency-writer")) {
				FrequencyWriter freq = new FrequencyWriter(p, gm);
				cellLayerWriters.add(freq);
			} else if (writerClass.equalsIgnoreCase("interval-writer")) {
				IntervalWriter iw = new IntervalWriter(p, gm);
				cellLayerWriters.add(iw);
			} else if (writerClass.equalsIgnoreCase("coordinate-indexer")) {
                CoordinateIndexer ce = new CoordinateIndexer(p, gm);
                cellLayerWriters.add(ce);
            }
		}
	}
	
	/**
	 * Initialize all cellLayerWriters.
	 */
	public void init(CellLayer l) {
		for (AbstractCellWriter tw : cellLayerWriters) {
			tw.init(l);
		}
	}
	
	/**
	 * Advance current instance by one time step.
	 */
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		for (AbstractCellWriter tw : cellLayerWriters) {
			tw.step(highlights, gillespie, frame);
		}	
	}
	
	/**
	 * Opens handles / initializes data structures for a new instance.
	 * Blows up if these were left open from the previous instance.
	 */
	public void nextSimulation(CellLayer l) {
		for (AbstractCellWriter tw : cellLayerWriters) {
			tw.init(l);
		}	
	}
	
	/**
	 * Conclude the entire simulation project.
	 */
	public void close() {
		for (Writer tw : cellLayerWriters) {
			tw.close();
		}	
	}

	public void dispatchHalt(HaltCondition ex) {
		System.out.println("Simulation ended. Cause: " + ex.getClass().getSimpleName());

		for (Writer tw : cellLayerWriters) {
			tw.dispatchHalt(ex);
		}
		
		if (p.isLineageMap()) {
			ImageSequence imgSequence = new ImageSequence(p.getInstancePath(), g, p);
			imgSequence.generate();
		}		
	}
}

package io.serialize;

import java.util.HashSet;
import java.util.Set;

import layers.LayerManager;
import org.dom4j.Element;

import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.postprocess.ImageSequence;

import javax.swing.text.AbstractWriter;

/**
 *
 * @untested
 */
public class SerializationManager {

	private Set<Serializer> writers;
	private GeneralParameters p;
	private LayerManager lm;

	public SerializationManager(Element we, GeneralParameters p, LayerManager lm) {
		this.p = p;
		this.lm = lm;
		

		writers = new HashSet<>();

		for (Object o : we.elements()) {
			Element e = (Element) o;
            Serializer w = SerializationFactory.instantiate(e, p);
            writers.add(w);
		}
	}
	
	/**
     * Opens handles / initializes data structures for a new instance.
     * Blows up if these were left open from the previous instance.
	 */
	public void init(LayerManager lm) {
		for (Serializer tw : writers) {
			tw.init(lm);
		}
	}
	
	/**
	 * Advance current instance by one time step.
	 */
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		for (Serializer tw : writers) {
			tw.step(highlights, gillespie, frame);
		}	
	}
	
	/**
	 * Conclude the entire simulation project.
	 */
	public void close() {
		for (Serializer tw : writers) {
			tw.close();
		}	
	}

	public void dispatchHalt(HaltCondition ex) {
		System.out.println("Simulation ended. Cause: " + ex.getClass().getSimpleName());

		for (Serializer tw : writers) {
			tw.dispatchHalt(ex);
		}
		
		if (p.isLineageMap()) {
			ImageSequence imgSequence = new ImageSequence(p.getInstancePath(), lm.getCellLayer().getGeometry(), p);
			imgSequence.generate();
		}		
	}
}

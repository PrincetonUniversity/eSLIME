/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0)
 * license.
 *
 * Attribute (BY) -- You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * NonCommercial (NC) -- You may not use this work for commercial purposes.
 *
 * ShareAlike (SA) -- If you remix, transform, or build upon the material,
 * you must distribute your contributions under the same license as the
 * original.
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/legalcode
 */

package io.serialize;

import java.util.HashSet;
import java.util.Set;

import layers.LayerManager;
import org.dom4j.Element;

import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.postprocess.ImageSequence;

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
		
		if (p.isStateMap()) {
			ImageSequence imgSequence = new ImageSequence(p.getInstancePath(), lm.getCellLayer().getGeometry(), p);
			imgSequence.generate();
		}		
	}
}

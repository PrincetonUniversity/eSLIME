package io.project;

import org.dom4j.Element;

import cells.Cell;
import cells.FissionCell;
import cells.SimpleCell;

import layers.cell.CellLayer; 
/**
 * Instantiates a cell based on specifications from a cell descriptor
 * XML element. The requirements for this element depend on the class
 * of cell being instantiated. If instantiate() is called numerous times,
 * multiple distinct cells of the same exact specification (include state
 * id) will be returned.
 * 
 * @untested
 * @author dbborens
 *
 */
public class CellFactory {

	private CellLayer layer;
	private Element cellDescriptor;
	private int state;
	
	/**
	 * @param lattice
	 * @param cellDescriptor
	 */
	public CellFactory(CellLayer layer, Element cellDescriptor) {
		this.layer = layer;
		this.cellDescriptor = cellDescriptor;
		
		String stateText = cellDescriptor.element("state").getTextTrim();

		// State should be consistent across all cells instantiated by
		// this factory, even if using a sequential state.
		state = getState(stateText);
	}
	
	public Cell instantiate() {
		String className = cellDescriptor.element("class").getTextTrim();
		
		if (className.equalsIgnoreCase("SimpleCell")) {
			return simpleCell(cellDescriptor, state);
			
		} else if (className.equalsIgnoreCase("FissionCell")) {
			return fissionCell(cellDescriptor, state);
			
		} else {
			
			String msg = "Unrecognized cell class '" +
					className +"'.";
			throw new IllegalArgumentException(msg);
			
		}
	}

	private Cell fissionCell(Element cellDescriptor, int state) {
		double initialFitness = Double.valueOf(cellDescriptor.element("initial-fitness").getText());
		double threshold = Double.valueOf(cellDescriptor.element("threshold").getText());

		Cell cell = new FissionCell(state, initialFitness, threshold);
		
		return cell;
	}

	private Cell simpleCell(Element cellDescriptor, int state) {
		Cell cell = new SimpleCell(state);
		return cell;
	}

	private int getState(String stateText) {
		if (stateText.equalsIgnoreCase("sequential")) {
			return getNextState();
		} else {
			return Integer.valueOf(stateText);
		}
	}

	private int getNextState() {
		Integer[] states = layer.getViewer().getStateMapViewer().getStates();
		Integer max = 0;
		for (Integer s : states) {
			if (s > max) {
				max = s;
			}
		}
		
		return max+1;
	}
}

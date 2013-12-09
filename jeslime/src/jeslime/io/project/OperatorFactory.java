package jeslime.io.project;

import org.dom4j.Element;

import continuum.operations.Advection;
import continuum.operations.CompoundOperator;
import continuum.operations.Diffusion;
import continuum.operations.Operator;
import continuum.operations.Scaling;
import structural.identifiers.Coordinate;
import geometry.Geometry;

/**
 * 
 * @test OperatorFactoryTest
 * @author dbborens
 *
 */
public class OperatorFactory {

	private Geometry geometry;
	private boolean useBoundaries;
	
	public OperatorFactory(Geometry geometry, boolean useBoundaries) {
		this.useBoundaries = useBoundaries;
		this.geometry = geometry;
	}

	public Operator instantiate(Element e) {
		
		String processClass = e.getName();
		
		if (processClass.equalsIgnoreCase("diffusion")) {
			return makeDiffusion(e);
			
		} else if (processClass.equalsIgnoreCase("advection")) {
			return makeAdvection(e);
			
		} else if (processClass.equalsIgnoreCase("scaling")) {
			return makeScaling(e);

		} else if (processClass.equalsIgnoreCase("compound")) {
			return makeCompound(e);
			
		} else {
			String msg = "Unrecognized operator '" +
					processClass + ".'";
			
			throw new IllegalArgumentException(msg);
		}		
	}

	private Operator makeCompound(Element e) {
		Element children = e.element("children");
		Operator compound = new CompoundOperator(geometry, useBoundaries, children);
		compound.init();
		return compound;
	}

	protected Operator makeScaling(Element e) {
		double lambda = Double.valueOf(get(e, "lambda"));
		Scaling scaling = new Scaling(geometry, useBoundaries, lambda);
		scaling.init();
		return scaling;
	}

	protected Operator makeAdvection(Element e) {
		Coordinate displacement = makeDisplacementVector(e);
		double r = Double.valueOf(get(e, "r"));
		Advection advection = new Advection(geometry, useBoundaries, displacement, r);
		advection.init();
		return advection;
	}

	protected Operator makeDiffusion(Element e) {
		double r = Double.valueOf(get(e, "r"));
		Diffusion diffusion = new Diffusion(geometry, useBoundaries, r);
		diffusion.init();
		return diffusion;
	}
	
	/**
	 * Construct a displacement vector from a displacement tag
	 * @param e
	 * @return
	 */
	private Coordinate makeDisplacementVector(Element e) {
		Element dispElem = e.element("displacement");
		if (dispElem == null) {
			throw new IllegalArgumentException("No displacement vector provided for advection operator.");
		}
		Coordinate displacement = CoordinateFactory.instantiate(dispElem);
		return displacement;
	}


	
	/**
	 *  Pull in a single-datum element
	 * @param g
	 * @param key
	 * @return
	 */
	protected String get(Element g, String key) {
		Element vElem = g.element(key);
		if (vElem == null) {
			throw new IllegalArgumentException("Required parameter '" + 
					key + "' not defined for operator " + 
					g.getName() + ".\nXML:\n\n" + g.asXML());
		}
		
		Object value = vElem.getData();
		
		return value.toString();
	}
}

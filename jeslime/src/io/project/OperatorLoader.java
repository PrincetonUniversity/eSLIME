package io.project;

import geometry.Geometry;

import java.util.ArrayList;

import org.dom4j.Element;

import continuum.operations.Operator;

/**
 * @test OperatorLoaderTest
 * @author dbborens
 *
 */
public class OperatorLoader {

	private OperatorFactory factory;
	
	public OperatorLoader(Geometry geometry, boolean useBoundaries) {
		factory = new OperatorFactory(geometry, useBoundaries);
	}
	
	public Operator[] getOperators(Element root) {
		ArrayList<Operator> operatorList = new ArrayList<Operator>();
		
		for (Object o : root.elements()) {
			Element e = (Element) o;
			Operator instance = factory.instantiate(e);
			operatorList.add(instance);
		}
		
		Operator[] operators = operatorList.toArray(new Operator[0]);
		
		return operators;
	}
	

}


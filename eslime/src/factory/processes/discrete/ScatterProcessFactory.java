/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes.discrete;

import control.GeneralParameters;
import control.arguments.CellDescriptor;
import factory.processes.ProcessFactory;
import layers.LayerManager;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.discrete.CellProcessArguments;
import processes.discrete.Scatter;

/**
 * Created by dbborens on 11/23/14.
 */
public abstract class ScatterProcessFactory extends ProcessFactory {

    public static Scatter instantiate(Element e, LayerManager layerManager, GeneralParameters p, int id) {
        BaseProcessArguments arguments = makeProcessArguments(e, layerManager, p, id);
        CellProcessArguments cpArguments = makeCellProcessArguments(e, layerManager, p);
        CellDescriptor cellDescriptor = makeCellDescriptor(e, "cell-descriptor", layerManager, p);
        Scatter scatter = new Scatter(arguments, cpArguments, cellDescriptor);

        return scatter;
    }
}

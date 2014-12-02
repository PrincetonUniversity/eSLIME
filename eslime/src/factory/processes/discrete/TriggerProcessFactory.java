/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package factory.processes.discrete;

import control.GeneralParameters;
import factory.processes.ProcessFactory;
import layers.LayerManager;
import org.dom4j.Element;
import processes.BaseProcessArguments;
import processes.discrete.CellProcessArguments;
import processes.discrete.TriggerProcess;
import processes.discrete.filter.Filter;
import structural.utilities.XmlUtil;

/**
 * Created by dbborens on 11/23/14.
 */
public abstract class TriggerProcessFactory extends ProcessFactory {
    public static TriggerProcess instantiate(Element e, LayerManager layerManager, GeneralParameters p, int id) {
        BaseProcessArguments arguments = makeProcessArguments(e, layerManager, p, id);
        CellProcessArguments cpArguments = makeCellProcessArguments(e, layerManager, p);
        Filter filter = loadFilters(e, layerManager, p);
        String behaviorName = XmlUtil.getString(e, "behavior", "default");
        boolean skipVacant = XmlUtil.getBoolean(e, "skip-vacant-sites");
        boolean requireNeighbors = XmlUtil.getBoolean(e, "require-neighbors");
        return new TriggerProcess(arguments, cpArguments, behaviorName, filter, skipVacant, requireNeighbors);
    }
}

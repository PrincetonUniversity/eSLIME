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
import processes.MockProcess;
import structural.utilities.XmlUtil;

/**
 * Abstract class for parsing the requirements pertaining to process
 * mocks. This class is not itself a mock, so it is not in the
 * mock/ source directory. However, it builds a mock object.
 * <p/>
 * Created by dbborens on 11/23/14.
 */
public abstract class MockProcessFactory extends ProcessFactory {

    public static MockProcess instantiate(Element e, LayerManager layerManager, GeneralParameters p, int id) {
        BaseProcessArguments arguments = makeProcessArguments(e, layerManager, p, id);
        String identifier = XmlUtil.getString(e, "identifier", "");
        double weight = XmlUtil.getDouble(e, "weight", 1.0);
        int count = XmlUtil.getInteger(e, "count", 1);
        return new MockProcess(arguments, identifier, weight, count);
    }
}

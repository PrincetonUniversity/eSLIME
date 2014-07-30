/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import io.loader.ProcessLoader;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by David B Borenstein on 1/11/14.
 */
public class MockProcessLoader extends ProcessLoader {

    public HashMap<Integer, Element> elements;

    public MockProcessLoader() {
        super();
        elements = new HashMap<>();
    }

    public void setElement(Integer key, Element value) {
        elements.put(key, value);
    }


    @Override
    public Element getProcess(int key) {
        return elements.get(key);
    }

    private Integer[] processes;

    public void setProcesses(Integer[] processes) {
        this.processes = processes;
    }

    @Override
    public Integer[] getProcesses() {
        return processes;
    }

}

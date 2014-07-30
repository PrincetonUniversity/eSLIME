/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
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

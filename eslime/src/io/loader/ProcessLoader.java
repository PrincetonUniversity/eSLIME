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

package io.loader;

import org.dom4j.Element;

import java.util.ArrayList;

/**
 * Reads the specifications of individual processes from the project
 * loader and provides them to the process factory for construction.
 * <p/>
 * As of eSLIME 0.4.0, the user is no longer required to provide
 * process IDs in the project file. This is because dom4j returns
 * elements in the order that they are loaded, which is verified
 * in io.project.XmlTest.
 * <p/>
 * As a result, this class may look a little unnecessary! In fact, it is:
 * it was retained to preserve the API.
 *
 * @author dbborens
 */
public class ProcessLoader {

    private ArrayList<Element> elems;

    public ProcessLoader(Element root) {
        elems = new ArrayList<>();

        for (Object o : root.elements()) {
            Element e = (Element) o;
            elems.add(e);
        }
    }

    /**
     * Default constructor is included for testing purposes.
     */
    public ProcessLoader() {
    }

    /**
     * Retrieves a process element from the processes sub-tree, or throws
     * an exception if the element is not found.
     *
     * @param id
     * @return
     */
    public Element getProcess(int id) {
        return elems.get(id);
    }

    public Integer[] getProcesses() {
        Integer[] processes = new Integer[elems.size()];
        for (int i = 0; i < elems.size(); i++) {
            processes[i] = i;
        }

        // Return the array.
        return processes;
    }

    // Pull in a single-datum element
    protected String get(Element g, String key) {
        Element vElem = g.element(key);
        if (vElem == null) {
            throw new IllegalArgumentException("Process " +
                    key + " missing. XML as follows: \n" + g.asXML());
        }

        Object value = vElem.getData();

        return value.toString();
    }
}


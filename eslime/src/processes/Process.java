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

package processes;

import control.GeneralParameters;
import control.halt.HaltCondition;
import factory.cell.CellFactory;
import io.loader.ProcessLoader;
import layers.LayerManager;
import org.dom4j.Element;
import processes.gillespie.GillespieState;

public abstract class Process {

    // XML element associated with this process
    protected Element e;
    protected LayerManager layerManager;
    protected GeneralParameters p;
    private int id;
    private int period;
    private int start;

	/* Constructors */

    public Process(ProcessLoader loader, LayerManager layerManager, GeneralParameters p, int id) {
        this.layerManager = layerManager;
        this.p = p;

        if (loader == null) {
            //System.err.println("WARNING: Mock behavior for process loader invoked. Use only for testing!");
            id = 0;
            period = 1;
            return;
        }
        this.id = id;
        e = loader.getProcess(id);
        period = Integer.valueOf(get("period", "1"));
        start = Integer.valueOf(get("start", "0"));

    }

	/* Process identifiers */

    protected abstract String getProcessClass();

    public int getID() {
        return id;
    }

	/* Process events */

    /**
     * Identifies possible update targets in the event of an iteration. Should
     * accept a null GillespieState for non-Gillespie events.
     *
     * @throws HaltCondition
     */
    public abstract void target(GillespieState gs) throws HaltCondition;

    /**
     * Convenience interface for target(...) -- calls it with a null GillespieState
     * argument.
     *
     * @throws HaltCondition
     */
    public void target() throws HaltCondition {
        target(null);
    }

    /**
     * Chooses one of its available targets and executes the update.
     *
     * @param state
     * @throws HaltCondition
     */
    public abstract void fire(StepState state) throws HaltCondition;

    public void iterate() throws HaltCondition {
        target();
        StepState stepState = layerManager.getStepState();
        fire(stepState);
    }

    /**
     * Pull in a single-datum element
     *
     * @param g
     * @param key
     * @return
     */
    private String get(Element g, String key) {
        Element vElem = g.element(key);
        if (vElem == null) {
            throw new IllegalArgumentException("Required parameter '" +
                    key + "' not defined for process " +
                    getProcessClass() + " (id=" + getID() + ").");
        }

        Object value = vElem.getData();

        return value.toString();
    }

    /**
     * Parameter getter with no default value. If the
     * parameter is omitted, an IllegalArgumentException
     * will be thrown. Use for required parameters.
     *
     * @param key
     * @return
     */
    protected String get(String key) {
        return get(e, key);
    }

    /**
     * Parameter getter with a default value argument.
     * Use for optional parameters.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    protected String get(String key, String defaultValue) {
        Element vElem = e.element(key);
        if (vElem == null) {
            return defaultValue;
        }

        Object value = vElem.getData();
        return value.toString();
    }

    public int getPeriod() {
        return period;
    }

    public int getStart() {
        return start;
    }

    protected CellFactory getCellFactory(LayerManager layerManager) {
        return new CellFactory(layerManager, e.element("cell-descriptor"), p);
    }
}
